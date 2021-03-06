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
package org.fenixedu.treasury.ui.administration.managefinantialinstitution;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.document.manageDocumentNumberSeries") <-- Use for duplicate controller name disambiguation
@BennuSpringController(value = FinantialInstitutionController.class)
@RequestMapping(DocumentNumberSeriesController.CONTROLLER_URL)
public class DocumentNumberSeriesController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/administration/managefinantialinstitution/documentnumberseries";
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
        return "forward:" + SEARCH_URL;
    }

    private DocumentNumberSeries getDocumentNumberSeries(Model model) {
        return (DocumentNumberSeries) model.asMap().get("documentNumberSeries");
    }

    private void setDocumentNumberSeries(DocumentNumberSeries documentNumberSeries, Model model) {
        model.addAttribute("documentNumberSeries", documentNumberSeries);
    }

    @Atomic
    public void deleteDocumentNumberSeries(DocumentNumberSeries documentNumberSeries) {
        documentNumberSeries.delete();
    }

    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "series", required = false) Series series, Model model) {
        List<DocumentNumberSeries> searchdocumentnumberseriesResultsDataSet = filterSearchDocumentNumberSeries(series);
        model.addAttribute("searchdocumentnumberseriesResultsDataSet", searchdocumentnumberseriesResultsDataSet);
        return "treasury/administration/managefinantialinstitution/documentnumberseries/search";
    }

    private List<DocumentNumberSeries> getSearchUniverseSearchDocumentNumberSeriesDataSet() {
        return DocumentNumberSeries.findAll().collect(Collectors.toList());
    }

    private List<DocumentNumberSeries> filterSearchDocumentNumberSeries(Series series) {
        Stream<DocumentNumberSeries> result = getSearchUniverseSearchDocumentNumberSeriesDataSet().stream();
        if (series != null) {
            result = result.filter(documentNumberSeries -> documentNumberSeries.getSeries() == series);
        }
        return result.collect(Collectors.toList());
    }

    private static final String SEARCH_VIEW_URI = "/search/view/";
    public static final String SEARCH_VIEW_URL = CONTROLLER_URL + SEARCH_VIEW_URI;

    @RequestMapping(value = SEARCH_VIEW_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(READ_URL + documentNumberSeries.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model) {
        setDocumentNumberSeries(documentNumberSeries, model);
        return "treasury/administration/managefinantialinstitution/documentnumberseries/read";
    }

    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model,
            RedirectAttributes redirectAttributes) {
        setDocumentNumberSeries(documentNumberSeries, model);
        try {
            String seriesExternalId = documentNumberSeries.getSeries().getExternalId();
            deleteDocumentNumberSeries(documentNumberSeries);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect(SeriesController.READ_URL + seriesExternalId, model, redirectAttributes);
        } catch (TreasuryDomainException ex) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        } catch (Exception ex) {
            //TODOJN - how to handle generic exception
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }
        return redirect(READ_URL + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = CREATE_URI + "/series{oid}", method = RequestMethod.GET)
    public String create(@PathVariable("oid") Series series, Model model) {
        model.addAttribute("series", series);
        model.addAttribute("DocumentNumberSeries_finantialDocumentType_options",
                FinantialDocumentType.findAll().collect(Collectors.toList()));
        return "treasury/administration/managefinantialinstitution/documentnumberseries/create";
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "series", required = false) Series series, @RequestParam(
            value = "finantialdocumenttype", required = false) FinantialDocumentType finantialDocumentType, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            DocumentNumberSeries documentNumberSeries = createDocumentNumberSeries(series, finantialDocumentType);

            model.addAttribute("documentNumberSeries", documentNumberSeries);
            return redirect(READ_URL + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + tde.getLocalizedMessage(), model);
            return create(series, model);
        } catch (Exception de) {
            //TODOJN - how to handle generic exception
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(series, model);
        }
    }

    @Atomic
    public DocumentNumberSeries createDocumentNumberSeries(Series series, FinantialDocumentType finantialDocumentType) {
        DocumentNumberSeries documentNumberSeries = DocumentNumberSeries.create(finantialDocumentType, series);
        return documentNumberSeries;
    }
}
