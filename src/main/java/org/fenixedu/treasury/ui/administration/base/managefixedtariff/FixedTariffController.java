/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com
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
package org.fenixedu.treasury.ui.administration.base.managefixedtariff;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.joda.time.DateTime;

import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Component;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.administration.base.manageproduct.ProductController;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.tariff.DueDateCalculationType;
import org.fenixedu.treasury.domain.tariff.FixedTariff;
import org.fenixedu.treasury.domain.tariff.InterestRate;
import org.fenixedu.treasury.dto.FixedTariffBean;
import org.fenixedu.treasury.dto.FixedTariffInterestRateBean;
import org.fenixedu.treasury.dto.InterestRateBean;

//@Component("org.fenixedu.treasury.ui.administration.base.manageFixedTariff") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageFixedTariff",accessGroup = "logged")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = ProductController.class)
@RequestMapping(FixedTariffController.CONTROLLER_URL)
public class FixedTariffController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/base/managefixedtariff/fixedtariff";
    protected static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

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

    private FixedTariffBean getFixedTariffBean(Model model) {
        return (FixedTariffBean) model.asMap().get("fixedTariffBean");
    }

    private void setFixedTariffBean(FixedTariffBean bean, Model model) {
        model.addAttribute("fixedTariffBeanJson", getBeanJson(bean));
        model.addAttribute("fixedTariffBean", bean);
    }

    // @formatter: on

    private FixedTariff getFixedTariff(Model model) {
        return (FixedTariff) model.asMap().get("fixedTariff");
    }

    private void setFixedTariff(FixedTariff fixedTariff, Model model) {
        model.addAttribute("fixedTariff", fixedTariff);
    }

    @Atomic
    public void deleteFixedTariff(FixedTariff fixedTariff) {

        fixedTariff.delete();
    }

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") FixedTariff fixedTariff, Model model, RedirectAttributes redirectAttributes) {

        String productId = fixedTariff.getProduct().getExternalId();
        setFixedTariff(fixedTariff, model);
        try {
            //call the Atomic delete function
            deleteFixedTariff(fixedTariff);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect(ProductController.READ_URL + productId, model, redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }
        //The default mapping is the same Read View
        return redirect(ProductController.READ_URL + productId, model, redirectAttributes);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") FixedTariff fixedTariff, Model model) {
        setFixedTariff(fixedTariff, model);
        return "treasury/administration/base/managefixedtariff/fixedtariff/read";
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(@RequestParam(value = "product", required = true) Product product, @RequestParam(
            value = "finantialInstitution", required = true) FinantialInstitution finantialInstitution, Model model) {

        FixedTariffBean bean = new FixedTariffBean();
        bean.setProduct(product);
        bean.setFinantialEntityDataSource(finantialInstitution.getFinantialEntitiesSet().stream().collect(Collectors.toList()));
        bean.setFinantialInstitution(finantialInstitution);
        this.setFixedTariffBean(bean, model);
        return "treasury/administration/base/managefixedtariff/fixedtariff/create";
    }

//						// @formatter: off
//			
    private static final String _CREATEPOSTBACK_URI = "/createpostback";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) FixedTariffBean bean, Model model) {

        // Do validation logic ?!?!
        this.setFixedTariffBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) FixedTariffBean bean, Model model,
            RedirectAttributes redirectAttributes) {

        /*
        *  Creation Logic
        */

        try {

            FixedTariff fixedTariff =
                    createFixedTariff(bean.getAmount(), bean.getApplyInterests(), bean.getBeginDate(),
                            bean.getDueDateCalculationType(), bean.getEndDate(), bean.getFinantialEntity(),
                            bean.getFixedDueDate(), bean.getNumberOfDaysAfterCreationForDueDate(), bean.getInterestRate(),
                            bean.getProduct());

            //Success Validation
            //Add the bean to be used in the View
            setFixedTariff(fixedTariff, model);
            return redirect(FixedTariffController.READ_URL + getFixedTariff(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            this.setFixedTariffBean(bean, model);
            return "treasury/administration/base/managefixedtariff/fixedtariff/create";
        }
    }

//						// @formatter: on

//				

    @Atomic
    public FixedTariff createFixedTariff(java.math.BigDecimal amount, boolean applyInterests, org.joda.time.LocalDate beginDate,
            org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType, org.joda.time.LocalDate endDate,
            org.fenixedu.treasury.domain.FinantialEntity finantialEntity, org.joda.time.LocalDate fixedDueDate,
            int numberOfDaysAfterCreationForDueDate, FixedTariffInterestRateBean interestRateBean, Product product) {

        InterestRate interestRate = null;

        FixedTariff fixedTariff =
                FixedTariff.create(product, interestRate, finantialEntity, amount, beginDate.toDateTimeAtStartOfDay(),
                        endDate.toDateTimeAtStartOfDay(), dueDateCalculationType, fixedDueDate,
                        numberOfDaysAfterCreationForDueDate, applyInterests);
        fixedTariff.setAmount(amount);
        fixedTariff.setApplyInterests(applyInterests);
        fixedTariff.setDueDateCalculationType(dueDateCalculationType);
        fixedTariff.setFinantialEntity(finantialEntity);
        fixedTariff.setFixedDueDate(fixedDueDate);
        fixedTariff.setNumberOfDaysAfterCreationForDueDate(numberOfDaysAfterCreationForDueDate);

        if (applyInterests) {
            interestRate =
                    InterestRate.create(fixedTariff, interestRateBean.getInterestType(),
                            interestRateBean.getNumberOfDaysAfterDueDate(), interestRateBean.getApplyInFirstWorkday(),
                            interestRateBean.getMaximumDaysToApplyPenalty(), interestRateBean.getMaximumMonthsToApplyPenalty(),
                            interestRateBean.getInterestFixedAmount(), interestRateBean.getRate());
            fixedTariff.setInterestRate(interestRate);
        }

        fixedTariff.checkRules();
        return fixedTariff;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FixedTariff fixedTariff, Model model) {
        setFixedTariff(fixedTariff, model);

        FixedTariffBean bean = new FixedTariffBean(fixedTariff);

        setFixedTariffBean(bean, model);
        return "treasury/administration/base/managefixedtariff/fixedtariff/update";
    }

//

//
//						// @formatter: off
//			
    private static final String _UPDATEPOSTBACK_URI = "/updatepostback/";
    public static final String UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody String updatepostback(@PathVariable("oid") FixedTariff fixedTariff, @RequestParam(value = "bean",
            required = false) FixedTariffBean bean, Model model) {

        // Do validation logic ?!?!
        this.setFixedTariffBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FixedTariff fixedTariff,
            @RequestParam(value = "bean", required = false) FixedTariffBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        setFixedTariff(fixedTariff, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateFixedTariff(bean.getAmount(), bean.getApplyInterests(), bean.getBeginDate().toDateTimeAtStartOfDay(),
                    bean.getDueDateCalculationType(), bean.getEndDate().toDateTimeAtStartOfDay(), bean.getFinantialEntity(),
                    bean.getFixedDueDate(), bean.getNumberOfDaysAfterCreationForDueDate(), bean.getInterestRate(),
                    bean.getProduct(), model);

            /*Succes Update */

            return redirect(FixedTariffController.READ_URL + getFixedTariff(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            setFixedTariffBean(bean, model);
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return "treasury/administration/base/managefixedtariff/fixedtariff/update";

        }
    }

//						// @formatter: on    			
//				

    @Atomic
    public void updateFixedTariff(java.math.BigDecimal amount, boolean applyInterests, org.joda.time.DateTime beginDate,
            org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType, org.joda.time.DateTime endDate,
            org.fenixedu.treasury.domain.FinantialEntity finantialEntity, org.joda.time.LocalDate fixedDueDate,
            int numberOfDaysAfterCreationForDueDate, FixedTariffInterestRateBean rateBean, Product product, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getFixedTariff(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        FixedTariff fixedTariff = getFixedTariff(model);
        fixedTariff.setAmount(amount);
        fixedTariff.setApplyInterests(applyInterests);
        fixedTariff.setBeginDate(beginDate);
        fixedTariff.setDueDateCalculationType(dueDateCalculationType);
        fixedTariff.setEndDate(endDate);
        fixedTariff.setFinantialEntity(finantialEntity);
        fixedTariff.setFixedDueDate(fixedDueDate);
        if (applyInterests) {
            if (fixedTariff.getInterestRate() == null) {
                InterestRate rate =
                        InterestRate.create(fixedTariff, rateBean.getInterestType(), rateBean.getNumberOfDaysAfterDueDate(),
                                rateBean.getApplyInFirstWorkday(), rateBean.getMaximumDaysToApplyPenalty(),
                                rateBean.getMaximumMonthsToApplyPenalty(), rateBean.getInterestFixedAmount(), rateBean.getRate());
                fixedTariff.setInterestRate(rate);
            } else {
                InterestRate rate = fixedTariff.getInterestRate();
                rate.setApplyInFirstWorkday(rateBean.getApplyInFirstWorkday());
                rate.setInterestFixedAmount(rateBean.getInterestFixedAmount());
                rate.setInterestType(rateBean.getInterestType());
                rate.setMaximumDaysToApplyPenalty(rateBean.getMaximumDaysToApplyPenalty());
                rate.setMaximumMonthsToApplyPenalty(rateBean.getMaximumDaysToApplyPenalty());
                rate.setNumberOfDaysAfterDueDate(rateBean.getNumberOfDaysAfterDueDate());
                rate.setRate(rateBean.getRate());
            }
        } else {
            fixedTariff.setInterestRate(null);
        }
        fixedTariff.setNumberOfDaysAfterCreationForDueDate(numberOfDaysAfterCreationForDueDate);

        fixedTariff.checkRules();
    }
}