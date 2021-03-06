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
package org.fenixedu.treasury.ui.document.manageexemption;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exemption.TreasuryExemptionType;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@Component("org.fenixedu.treasury.ui.document.manageExemption") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageExemptionType", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
// @BennuSpringController(value=TreasuryController.class)
@RequestMapping(TreasuryExemptionTypeController.CONTROLLER_URL)
public class TreasuryExemptionTypeController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/document/manageexemption/treasuryexemptiontype";
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


    @RequestMapping
    public String home(Model model) {
        // this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/document/manageexemption/treasuryexemptiontype/";
    }

    private TreasuryExemptionType getTreasuryExemptionType(final Model model) {
        return (TreasuryExemptionType) model.asMap().get("treasuryExemptionType");
    }

    private void setTreasuryExemptionType(final TreasuryExemptionType treasuryExemptionType, final Model model) {
        model.addAttribute("treasuryExemptionType", treasuryExemptionType);
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/document/manageexemption/treasuryexemptiontype/create";
    }

    //
    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "name",
            required = false) LocalizedString name,
            @RequestParam(value = "defaultexemptionpercentage", required = false) BigDecimal defaultExemptionPercentage,
            Model model, RedirectAttributes redirectAttributes) {
        /*
         * Creation Logic
         */

        try {

            TreasuryExemptionType treasuryExemptionType =
                    TreasuryExemptionType.create(code, name, defaultExemptionPercentage, true);

            // Success Validation
            // Add the bean to be used in the View
            model.addAttribute("treasuryExemptionType", treasuryExemptionType);

            return redirect("/treasury/document/manageexemption/treasuryexemptiontype/read/"
                    + getTreasuryExemptionType(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation
             * 
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+
             * ex.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "name",
            required = false) LocalizedString name,
            @RequestParam(value = "discountrate", required = false) BigDecimal discountRate, Model model) {
        List<TreasuryExemptionType> searchtreasuryexemptiontypeResultsDataSet =
                filterSearchTreasuryExemptionType(code, name, discountRate);

        // add the results dataSet to the model
        model.addAttribute("searchtreasuryexemptiontypeResultsDataSet", searchtreasuryexemptiontypeResultsDataSet);

        return "treasury/document/manageexemption/treasuryexemptiontype/search";
    }

    private List<TreasuryExemptionType> getSearchUniverseSearchtreasuryExemptionTypeDataSet() {
        //
        // The initialization of the result list must be done here
        //
        //
        return TreasuryExemptionType.findAll().collect(Collectors.toList());
    }

    private List<TreasuryExemptionType> filterSearchTreasuryExemptionType(String code, LocalizedString name, BigDecimal discountRate) {

        return getSearchUniverseSearchtreasuryExemptionTypeDataSet()
                .stream()
                .filter(treasuryExemptionType -> code == null || code.length() == 0 || treasuryExemptionType.getCode() != null
                        && treasuryExemptionType.getCode().length() > 0
                        && treasuryExemptionType.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(treasuryExemptionType -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> treasuryExemptionType.getName().getContent(locale) != null
                                                && treasuryExemptionType.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(treasuryExemptionType -> discountRate == null
                        || discountRate.equals(treasuryExemptionType.getDefaultExemptionPercentage()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") TreasuryExemptionType treasuryExemptionType, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below
        return redirect(
                "/treasury/document/manageexemption/treasuryexemptiontype/read" + "/" + treasuryExemptionType.getExternalId(),
                model, redirectAttributes);
    }

    //
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") TreasuryExemptionType treasuryExemptionType, Model model) {
        setTreasuryExemptionType(treasuryExemptionType, model);
        return "treasury/document/manageexemption/treasuryexemptiontype/read";
    }

    //
    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") TreasuryExemptionType treasuryExemptionType, Model model,
            RedirectAttributes redirectAttributes) {

        setTreasuryExemptionType(treasuryExemptionType, model);
        try {
            // call the Atomic delete function
            treasuryExemptionType.delete();

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/document/manageexemption/treasuryexemptiontype/", model, redirectAttributes);
        } catch (DomainException ex) {
            // Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        // The default mapping is the same Read View
        return "treasury/document/manageexemption/treasuryexemptiontype/read/" + getTreasuryExemptionType(model).getExternalId();
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") TreasuryExemptionType treasuryExemptionType, Model model) {
        setTreasuryExemptionType(treasuryExemptionType, model);
        return "treasury/document/manageexemption/treasuryexemptiontype/update";
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") TreasuryExemptionType treasuryExemptionType, @RequestParam(value = "code",
            required = false) String code, @RequestParam(value = "name", required = false) LocalizedString name, @RequestParam(
            value = "defaultexemptionpercentage", required = false) BigDecimal defaultExemptionPercentage, Model model,
            RedirectAttributes redirectAttributes) {

        setTreasuryExemptionType(treasuryExemptionType, model);

        try {
            /*
             * UpdateLogic here
             */

            getTreasuryExemptionType(model).edit(code, name, defaultExemptionPercentage, true);

            /* Succes Update */

            return redirect("/treasury/document/manageexemption/treasuryexemptiontype/read/"
                    + getTreasuryExemptionType(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(treasuryExemptionType, model);

        }
    }

}
