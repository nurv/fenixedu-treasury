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
package org.fenixedu.treasury.ui.accounting.managecustomer;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.AdhocCustomer;
import org.fenixedu.treasury.dto.AdhocCustomerBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.accounting.manageCustomer") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.accounting.manageCustomer",accessGroup = "#managers")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = CustomerController.class)
@RequestMapping(AdhocCustomerController.CONTROLLER_URL)
public class AdhocCustomerController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/adhoccustomer";
    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;
    private static final String CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;
    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;
    private static final String DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + DELETE_URI;

    //

    private AdhocCustomer getAdhocCustomer(Model model) {
        return (AdhocCustomer) model.asMap().get("adhocCustomer");
    }

    private void setAdhocCustomer(AdhocCustomer adhocCustomer, Model model) {
        model.addAttribute("adhocCustomer", adhocCustomer);
    }

    @Atomic
    public void deleteAdhocCustomer(AdhocCustomer adhocCustomer) {
        // CHANGE_ME: Do the processing for deleting the adhocCustomer
        // Do not catch any exception here

        // adhocCustomer.delete();
    }

    private AdhocCustomer getAdhocCustomerBean(Model model) {
        return (AdhocCustomer) model.asMap().get("adhocCustomerBean");
    }

    private void setAdhocCustomerBean(AdhocCustomerBean bean, Model model) {
        model.addAttribute("adhocCustomerBeanJson", getBeanJson(bean));
        model.addAttribute("adhocCustomerBean", bean);
    }

    //
    @RequestMapping(value = SEARCH_URI)
    public String search(Model model) {
        List<AdhocCustomer> searchadhoccustomerResultsDataSet = filterSearchAdhocCustomer();

        // add the results dataSet to the model
        model.addAttribute("searchadhoccustomerResultsDataSet", searchadhoccustomerResultsDataSet);
        return "treasury/accounting/managecustomer/adhoccustomer/search";
    }

    private List<AdhocCustomer> getSearchUniverseSearchAdhocCustomerDataSet() {
        //
        // The initialization of the result list must be done here
        //
        //
        return AdhocCustomer.findAll().collect(Collectors.toList());
    }

    private List<AdhocCustomer> filterSearchAdhocCustomer() {

        return getSearchUniverseSearchAdhocCustomerDataSet().stream().collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") AdhocCustomer adhocCustomer, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below
        return redirect("/treasury/accounting/managecustomer/customer/read" + "/" + adhocCustomer.getExternalId(), model,
                redirectAttributes);
    }

    //
    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        this.setAdhocCustomerBean(new AdhocCustomerBean(), model);
        return "treasury/accounting/managecustomer/adhoccustomer/create";
    }

    @RequestMapping(value = "/createpostback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) AdhocCustomerBean bean, Model model) {

        // Do validation logic ?!?!
        this.setAdhocCustomerBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) AdhocCustomerBean bean, Model model,
            RedirectAttributes redirectAttributes) {

        /*
        *  Creation Logic
        */

        try {

            AdhocCustomer adhocCustomer =
                    createAdhocCustomer(bean.getCode(), bean.getName(), bean.getFiscalNumber(), bean.getIdentificationNumber());
            adhocCustomer.registerFinantialInstitutions(bean.getFinantialInstitutions());
            //Success Validation
            //Add the bean to be used in the View
            setAdhocCustomer(adhocCustomer, model);

            return redirect(CustomerController.READ_URL + getAdhocCustomer(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public AdhocCustomer createAdhocCustomer(java.lang.String code, java.lang.String name, java.lang.String fiscalNumber,
            java.lang.String identificationNumber) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create the object
         * with the default constructor and use the setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        // AdhocCustomer adhocCustomer = adhocCustomer.create(fields_to_create);

        // Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        AdhocCustomer adhocCustomer = AdhocCustomer.create(code, fiscalNumber, name, "", "", "", "", identificationNumber);
        return adhocCustomer;
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") AdhocCustomer adhocCustomer, Model model) {
        setAdhocCustomer(adhocCustomer, model);
        setAdhocCustomerBean(new AdhocCustomerBean(adhocCustomer), model);
        return "treasury/accounting/managecustomer/adhoccustomer/update";
    }

    @RequestMapping(value = "/updatepostback/{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String updatepostback(@PathVariable("oid") AdhocCustomer adhocCustomer, @RequestParam(value = "bean",
            required = false) AdhocCustomerBean bean, Model model) {

        // Do validation logic ?!?!
        this.setAdhocCustomerBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") AdhocCustomer adhocCustomer,
            @RequestParam(value = "bean", required = false) AdhocCustomerBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        setAdhocCustomer(adhocCustomer, model);

        try {
            /*
            *  UpdateLogic here
            */

            adhocCustomer.registerFinantialInstitutions(bean.getFinantialInstitutions());
            updateAdhocCustomer(bean.getCode(), bean.getName(), bean.getFiscalNumber(), bean.getIdentificationNumber(), model);

            /*Succes Update */

            return redirect("/treasury/accounting/managecustomer/customer/read/" + getAdhocCustomer(model).getExternalId(),
                    model, redirectAttributes);
        } catch (DomainException de) {

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(adhocCustomer, model);

        }
    }

    @Atomic
    public void updateAdhocCustomer(java.lang.String code, java.lang.String name, java.lang.String fiscalNumber,
            java.lang.String identificationNumber, Model model) {

        // @formatter: off
        /*
         * Modify the update code here if you do not want to update the object
         * with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        // getAdhocCustomer(model).edit(fields_to_edit);

        // Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getAdhocCustomer(model).edit(code, fiscalNumber, name, "", "", "", "", identificationNumber);
    }

}
