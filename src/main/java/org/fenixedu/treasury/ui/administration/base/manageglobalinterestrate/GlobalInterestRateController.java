/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.base.manageglobalinterestrate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.tariff.GlobalInterestRate;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.manageGlobalInterestRate") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageGlobalInterestRate",
        accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(GlobalInterestRateController.CONTROLLER_URL)
public class GlobalInterestRateController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/base/manageglobalinterestrate/globalinterestrate";

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

    //private GlobalInterestRate getGlobalInterestRateBean(Model model)
    //{
    //	return (GlobalInterestRate)model.asMap().get("globalInterestRateBean");
    //}
    //				
    //private void setGlobalInterestRateBean (GlobalInterestRateBean bean, Model model)
    //{
    //	model.addAttribute("globalInterestRateBeanJson", getBeanJson(bean));
    //	model.addAttribute("globalInterestRateBean", bean);
    //}

    // @formatter: on

    private GlobalInterestRate getGlobalInterestRate(Model model) {
        return (GlobalInterestRate) model.asMap().get("globalInterestRate");
    }

    private void setGlobalInterestRate(GlobalInterestRate globalInterestRate, Model model) {
        model.addAttribute("globalInterestRate", globalInterestRate);
    }

    @Atomic
    public void deleteGlobalInterestRate(GlobalInterestRate globalInterestRate) {
        // CHANGE_ME: Do the processing for deleting the globalInterestRate
        // Do not catch any exception here

        globalInterestRate.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "description", required = false) org.fenixedu.commons.i18n.LocalizedString description,
            @RequestParam(value = "rate", required = false) java.math.BigDecimal rate, Model model) {
        List<GlobalInterestRate> searchglobalinterestrateResultsDataSet = filterSearchGlobalInterestRate(description, rate);

        //add the results dataSet to the model
        model.addAttribute("searchglobalinterestrateResultsDataSet", searchglobalinterestrateResultsDataSet);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/search";
    }

    private Stream<GlobalInterestRate> getSearchUniverseSearchGlobalInterestRateDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return GlobalInterestRate.findAll();
        //return new ArrayList<GlobalInterestRate>().stream();
    }

    private List<GlobalInterestRate> filterSearchGlobalInterestRate(org.fenixedu.commons.i18n.LocalizedString description,
            java.math.BigDecimal rate) {

        return getSearchUniverseSearchGlobalInterestRateDataSet()
//                .filter(globalInterestRate -> globalInterestRate.getYear() == year)
                .filter(globalInterestRate -> description == null
                        || description.isEmpty()
                        || description
                                .getLocales()
                                .stream()
                                .allMatch(
                                        locale -> globalInterestRate.getDescription().getContent(locale) != null
                                                && globalInterestRate.getDescription().getContent(locale).toLowerCase()
                                                        .contains(description.getContent(locale).toLowerCase())))
                .filter(globalInterestRate -> rate == null || rate.equals(globalInterestRate.getRate()))
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") GlobalInterestRate globalInterestRate, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/read" + "/"
                + globalInterestRate.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") GlobalInterestRate globalInterestRate, Model model) {
        setGlobalInterestRate(globalInterestRate, model);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") GlobalInterestRate globalInterestRate, Model model,
            RedirectAttributes redirectAttributes) {

        setGlobalInterestRate(globalInterestRate, model);
        try {
            //call the Atomic delete function
            deleteGlobalInterestRate(globalInterestRate);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/", model,
                    redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/read/"
                + getGlobalInterestRate(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {

        //IF ANGULAR, initialize the Bean
        //GlobalInterestRateBean bean = new GlobalInterestRateBean();
        //this.setGlobalInterestRateBean(bean, model);

        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) GlobalInterestRateBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setGlobalInterestRateBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) GlobalInterestRateBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	GlobalInterestRate globalInterestRate = createGlobalInterestRate(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("globalInterestRate",globalInterestRate);
//				    return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/read/" + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de)
//					{
//
//						/*
//						 * If there is any error in validation 
//					     *
//					     * Add a error / warning message
//					     * 
//					     * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//						
//						addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//				     	return create(model);
//					}
//    			}
//						// @formatter: on

//				
    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "year", required = false) int year, @RequestParam(value = "description",
            required = false) org.fenixedu.commons.i18n.LocalizedString description, @RequestParam(value = "rate",
            required = false) java.math.BigDecimal rate, Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            GlobalInterestRate globalInterestRate = createGlobalInterestRate(year, description, rate);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("globalInterestRate", globalInterestRate);
            return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/read/"
                    + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public GlobalInterestRate createGlobalInterestRate(int year, org.fenixedu.commons.i18n.LocalizedString description,
            java.math.BigDecimal rate) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //GlobalInterestRate globalInterestRate = globalInterestRate.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        GlobalInterestRate globalInterestRate = GlobalInterestRate.create(year, description, rate);
        return globalInterestRate;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") GlobalInterestRate globalInterestRate, Model model) {
        setGlobalInterestRate(globalInterestRate, model);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") GlobalInterestRate globalInterestRate, @RequestParam(value = "bean", required = false) GlobalInterestRateBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setGlobalInterestRateBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") GlobalInterestRate globalInterestRate, @RequestParam(value = "bean", required = false) GlobalInterestRateBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setGlobalInterestRate(globalInterestRate,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateGlobalInterestRate( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/read/" + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
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
//				     	return update(globalInterestRate,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") GlobalInterestRate globalInterestRate, @RequestParam(value = "year",
            required = false) int year,
            @RequestParam(value = "description", required = false) org.fenixedu.commons.i18n.LocalizedString description,
            @RequestParam(value = "rate", required = false) java.math.BigDecimal rate, Model model,
            RedirectAttributes redirectAttributes) {

        setGlobalInterestRate(globalInterestRate, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateGlobalInterestRate(year, description, rate, model);

            /*Succes Update */

            return redirect("/treasury/administration/base/manageglobalinterestrate/globalinterestrate/read/"
                    + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
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
            return update(globalInterestRate, model);

        }
    }

    @Atomic
    public void updateGlobalInterestRate(int year, org.fenixedu.commons.i18n.LocalizedString description,
            java.math.BigDecimal rate, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getGlobalInterestRate(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getGlobalInterestRate(model).setYear(year);
        getGlobalInterestRate(model).setDescription(description);
        getGlobalInterestRate(model).setRate(rate);
    }

}
