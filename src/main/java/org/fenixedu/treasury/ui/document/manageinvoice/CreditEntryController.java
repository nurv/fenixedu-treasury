/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.ui.document.manageinvoice;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditEntry;
import org.fenixedu.treasury.domain.document.CreditNote;
import org.fenixedu.treasury.domain.tariff.FixedTariff;
import org.fenixedu.treasury.domain.tariff.Tariff;
import org.fenixedu.treasury.dto.CreditEntryBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.accounting.managecustomer.DebtAccountController;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.document.manageInvoice") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageInvoice", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = CreditNoteController.class)
@RequestMapping(CreditEntryController.CONTROLLER_URL)
public class CreditEntryController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/document/manageinvoice/creditentry";

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + "/";
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    private CreditEntry getCreditEntryBean(Model model) {
        return (CreditEntry) model.asMap().get("creditEntryBean");
    }

    private void setCreditEntryBean(CreditEntryBean bean, Model model) {
        model.addAttribute("creditEntryBeanJson", getBeanJson(bean));
        model.addAttribute("creditEntryBean", bean);
    }

    // @formatter: on

    private CreditEntry getCreditEntry(Model model) {
        return (CreditEntry) model.asMap().get("creditEntry");
    }

    private void setCreditEntry(CreditEntry creditEntry, Model model) {
        model.addAttribute("creditEntry", creditEntry);
    }

    @Atomic
    public void deleteCreditEntry(CreditEntry creditEntry) {
        // CHANGE_ME: Do the processing for deleting the creditEntry
        // Do not catch any exception here

        creditEntry.delete();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

//  
    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(@RequestParam(value = "creditnote") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {

        DebtAccount debtAccount = creditNote.getDebtAccount();
        if (creditNote != null && !creditNote.isPreparing()) {
            addWarningMessage(BundleUtil.getString(Constants.BUNDLE,
                    "label.error.document.manageinvoice.creditentry.invalid.state.add.creditentry"), model);
            redirect(CreditNoteController.READ_URL + creditNote.getExternalId(), model, redirectAttributes);
        }

        CreditEntryBean bean = new CreditEntryBean();

        bean.setProductDataSource(Product.findAll().collect(Collectors.toList()));
        bean.setDebtAccount(debtAccount);
        bean.setFinantialDocument(creditNote);
        bean.setCurrency(debtAccount.getFinantialInstitution().getCurrency());
        this.setCreditEntryBean(bean, model);

        return "treasury/document/manageinvoice/creditentry/create";
    }

// @formatter: off

    @RequestMapping(value = "/createpostback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createpostback(
            @RequestParam(value = "bean", required = true) CreditEntryBean bean, Model model) {

        Product product = bean.getProduct();
        if (product != null) {
            bean.setVat(bean.getDebtAccount().getFinantialInstitution().getActiveVat(product.getVatType(), new DateTime()));
            Tariff tariff =
                    product.getActiveTariffs(bean.getDebtAccount().getFinantialInstitution(), new DateTime()).findFirst()
                            .orElse(null);

            if (tariff != null) {
                bean.setTariff(tariff);
                if (tariff instanceof FixedTariff) {
                    bean.setAmount(bean.getDebtAccount().getFinantialInstitution().getCurrency()
                            .getValueWithScale(((FixedTariff) tariff).getAmount()));
                } else {
                    bean.setAmount(bean.getDebtAccount().getFinantialInstitution().getCurrency()
                            .getValueWithScale(BigDecimal.ZERO));

                }
            } else {
                return new ResponseEntity<String>(BundleUtil.getString(Constants.BUNDLE, "label.Tariff.no.valid.fixed"),
                        HttpStatus.BAD_REQUEST);
            }
            bean.setDescription(product.getName().getContent());
        }
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATE_URI + "{oid}", method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) CreditEntryBean bean,
            @PathVariable("oid") DebtAccount debtAccount, Model model, RedirectAttributes redirectAttributes) {

/*
*  Creation Logic
*/

        try {
            if (bean.getFinantialDocument() != null && !bean.getFinantialDocument().isPreparing()) {
                addWarningMessage(BundleUtil.getString(Constants.BUNDLE,
                        "label.error.document.manageinvoice.creditentry.invalid.state.add.creditentry"), model);
                redirect(CreditNoteController.READ_URL + bean.getFinantialDocument().getExternalId(), model, redirectAttributes);
            }

            CreditEntry creditEntry =
                    createCreditEntry(bean.getFinantialDocument(), bean.getDebtAccount(), bean.getDescription(),
                            bean.getProduct(), bean.getAmount(), bean.getQuantity(), bean.getFinantialDocument()
                                    .getDocumentDueDate());

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.create"), model);

//Success Validation
//Add the bean to be used in the View
            setCreditEntry(creditEntry, model);

            if (getCreditEntry(model).getFinantialDocument() != null) {
                return redirect(CreditNoteController.READ_URL + getCreditEntry(model).getFinantialDocument().getExternalId(),
                        model, redirectAttributes);
            } else {
                return redirect(DebtAccountController.READ_URL + getCreditEntry(model).getDebtAccount().getExternalId(), model,
                        redirectAttributes);
            }
        } catch (Exception de) {

/*
 * If there is any error in validation 
 *
 * Add a error / warning message
 * 
 * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
 * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            this.setCreditEntryBean(bean, model);
            return "treasury/document/manageinvoice/creditentry/create";
        }
    }

// @formatter: on

//  

    @Atomic
    public CreditEntry createCreditEntry(CreditNote creditNote, DebtAccount debtAccount, java.lang.String description,
            org.fenixedu.treasury.domain.Product product, java.math.BigDecimal amount, java.math.BigDecimal quantity,
            LocalDate dueDate) {

// @formatter: off

/*
* Modify the creation code here if you do not want to create
* the object with the default constructor and use the setter
* for each field
* 
*/

// CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
//CreditEntry creditEntry = creditEntry.create(fields_to_create);

//Instead, use individual SETTERS and validate "CheckRules" in the end
// @formatter: on

        Optional<Tariff> tariff = product.getActiveTariffs(debtAccount.getFinantialInstitution(), new DateTime()).findFirst();

        Optional<Vat> activeVat =
                Vat.findActiveUnique(product.getVatType(), debtAccount.getFinantialInstitution(), new DateTime());

//        CreditEntry.create(creditNote, debtAccount, null, activeVat.orElse(null), amount, dueDate, null, product,
//                description, quantity, tariff.orElse(null), new DateTime());

        CreditEntry creditEntry =
                CreditEntry.create(creditNote, description, product, activeVat.orElse(null), amount, new DateTime(), null,
                        quantity);
        return creditEntry;
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") CreditEntry creditEntry, Model model) {
        setCreditEntry(creditEntry, model);
        return "treasury/document/manageinvoice/creditentry/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") CreditEntry creditEntry, Model model, RedirectAttributes redirectAttributes) {

        setCreditEntry(creditEntry, model);
        try {
            CreditNote note = (CreditNote) creditEntry.getFinantialDocument();
            //call the Atomic delete function
            deleteCreditEntry(creditEntry);

            addInfoMessage("Sucess deleting CreditEntry ...", model);
            return redirect("/treasury/document/manageinvoice/creditnote/read/" + note.getExternalId(), model, redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/document/manageinvoice/creditentry/read/" + getCreditEntry(model).getExternalId();
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") CreditEntry creditEntry, Model model) {
        setCreditEntry(creditEntry, model);
        return "treasury/document/manageinvoice/creditentry/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") CreditEntry creditEntry, @RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setCreditEntryBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") CreditEntry creditEntry, @RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setCreditEntry(creditEntry,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateCreditEntry( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditEntry(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de) 
//					{
//				
//						/*
//					 	* If there is any error in validation 
//				     	*
//				     	* Add a error / warning message
//				     	* 
//				     	* addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//				     	*/
//										     
//				     	addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	return update(creditEntry,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") CreditEntry creditEntry,
            @RequestParam(value = "description", required = true) java.lang.String description, @RequestParam(value = "amount",
                    required = true) java.math.BigDecimal amount,
            @RequestParam(value = "quantity", required = true) java.math.BigDecimal quantity, Model model,
            RedirectAttributes redirectAttributes) {

        setCreditEntry(creditEntry, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateCreditEntry(description, amount, quantity, model);

            /*Succes Update */

            return redirect("/treasury/document/manageinvoice/creditnote/read/"
                    + getCreditEntry(model).getFinantialDocument().getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {
            // @formatter: off

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(creditEntry, model);

        }
    }

    @Atomic
    public void updateCreditEntry(java.lang.String description, java.math.BigDecimal amount, BigDecimal quantity, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getCreditEntry(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        CreditEntry creditEntry = getCreditEntry(model);
        creditEntry.setDescription(description);
        creditEntry.setAmount(amount);
        creditEntry.edit(description, amount, quantity);
    }

}
