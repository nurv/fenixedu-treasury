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
package org.fenixedu.treasury.services.integration.erp;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.lang.ArrayUtils;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.domain.integration.ERPImportOperation;
import org.fenixedu.treasury.domain.integration.OperationFile;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.FinantialDocumentEntry;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueInput;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueOuptut;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;
import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

    public static boolean validate(String username, String password) {
        Optional<FinantialInstitution> findUniqueByFiscalCode = FinantialInstitution.findUniqueByFiscalCode(username);
        if (findUniqueByFiscalCode.isPresent()) {

            ERPConfiguration configuration = findUniqueByFiscalCode.get().getErpIntegrationConfiguration();
            if (configuration != null) {
                return password.compareToIgnoreCase(configuration.getPassword()) == 0;
            }
        }
        return false;
    }

    @WebMethod
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());

        FinantialInstitution finantialInstitution = validateFinantialInstitution(documentsInformation);
        //Integrate the information from XML SAFT
        DateTime now = new DateTime();
        String filename = finantialInstitution.getFiscalNumber() + "_" + now.toString() + ".xml";
        OperationFile file = OperationFile.create(filename, ArrayUtils.toPrimitive(documentsInformation.getData()));
        ERPImportOperation operation = ERPImportOperation.create(file, finantialInstitution, now, false, false, false, null);

        ERPImporter importer = new ERPImporter(file.getStream());
        importer.processAuditFile(operation);
        return operation.getExternalId();
    }

    private FinantialInstitution validateFinantialInstitution(DocumentsInformationInput documentsInformation) {
        FinantialInstitution finantialInstitution =
                FinantialInstitution.findUniqueByFiscalCode(documentsInformation.getFinantialInstitution()).orElse(null);

        if (finantialInstitution == null) {
            throw new TreasuryDomainException("error.integration.erp.invalid.fiscalinstitution");
        }
        return finantialInstitution;
    }

    @WebMethod
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());

        FinantialInstitution finantialInstitution = validateFinantialInstitution(documentsInformation);
        //Integrate the information from XML SAFT
        DateTime now = new DateTime();
        String filename = finantialInstitution.getFiscalNumber() + "_" + now.toString() + ".xml";
        ERPImportOperation operation = ERPImportOperation.create(null, finantialInstitution, now, false, false, false, null);
        try {
            File externalFile = new File(documentsInformation.getDataURI());
            byte[] bytes = Files.toByteArray(externalFile);
            OperationFile operationFile = OperationFile.create(filename, bytes);
            operation.setFile(operationFile);
            ERPImporter importer = new ERPImporter(operationFile.getStream());
            importer.processAuditFile(operation);
        } catch (Exception e) {
            operation.setErrorLog(e.getLocalizedMessage());
        }
        return operation.getExternalId();
    }

    @WebMethod
    public IntegrationStatusOutput getIntegrationStatusFor(String requestIdentification) {

        ERPImportOperation importOperation = FenixFramework.getDomainObject(requestIdentification);
        validateRequestHeader(importOperation.getFinantialInstitution().getFiscalNumber());
        IntegrationStatusOutput status = new IntegrationStatusOutput(requestIdentification);
        ERPImporter importer = new ERPImporter(importOperation.getFile().getStream());
        Set<String> documentNumbers = importer.getRelatedDocumentsNumber();
        for (String docNumber : documentNumbers) {
            DocumentStatusWS docStatus = new DocumentStatusWS();
            FinantialDocument document =
                    FinantialDocument.findByUiDocumentNumber(importOperation.getFinantialInstitution(), docNumber);
            if (document == null) {
                docStatus.setIntegrationStatus(StatusType.ERROR);
            } else {
                docStatus.setIntegrationStatus(StatusType.SUCCESS);
            }
            status.getDocumentStatus().add(docStatus);
        }
        return status;
    }

    @WebMethod
    public InterestRequestValueOuptut getInterestValueFor(InterestRequestValueInput interestRequest) {
        final InterestRequestValueOuptut bean = new InterestRequestValueOuptut();
        validateRequestHeader(interestRequest.getFinantialInstitutionFiscalNumber());

        //1. Check if the the lineNumber+DebitNoteNumber is for the Customer of the FinantialInstitution
        final Optional<? extends FinantialDocument> optionalFinantialDocument =
                FinantialDocument.findUniqueByDocumentNumber(interestRequest.getDebitNoteNumber());

        if (!optionalFinantialDocument.isPresent()) {
            throw new RuntimeException("Debit note not found");
        }

        final FinantialDocument finantialDocument = optionalFinantialDocument.get();

        if (!finantialDocument.isDebitNote()) {
            throw new RuntimeException("Finantial document was not debit note");
        }

        if (finantialDocument.getDebtAccount().getFinantialInstitution().getFiscalNumber()
                .equals(interestRequest.getFinantialInstitutionFiscalNumber())) {
            throw new RuntimeException("Finantial institution fiscal number invalid");
        }

        if (!finantialDocument.getDebtAccount().getCustomer().getCode().equals(interestRequest.getCustomerCode())) {
            throw new RuntimeException("Customer code invalid");
        }

        //2. Check if the lineNumber+DebitNoteNumber Amount is correct
        final Optional<? extends FinantialDocumentEntry> optionalDebitEntry =
                FinantialDocumentEntry.findUniqueByEntryOrder(finantialDocument, interestRequest.getLineNumber());

        if (!optionalDebitEntry.isPresent()) {
            throw new RuntimeException("Debit entry not found");
        }

        FinantialDocumentEntry finantialDocumentEntry = optionalDebitEntry.get();

        if (!(finantialDocumentEntry instanceof DebitEntry)) {
            throw new RuntimeException("Finantial document entry not debit entry");
        }

        final DebitEntry debitEntry = (DebitEntry) finantialDocumentEntry;

        final BigDecimal amountInDebt = debitEntry.amountInDebt(interestRequest.getPaymentDate());

        if (!Constants.isPositive(amountInDebt)) {
            throw new RuntimeException("Debit entry has no debt");
        }

        if (!Constants.isEqual(amountInDebt, interestRequest.getAmount())) {
            throw new RuntimeException("Amount in debt not equal");
        }

        //3 . calculate the amount of interest
        final InterestRateBean interestRateBean = debitEntry.calculateUndebitedInterestValue(interestRequest.getPaymentDate());

        bean.setInterestAmount(interestRateBean.getInterestAmount());
        bean.setDescription(interestRateBean.getDescription());

        if (Constants.isGreaterThan(interestRateBean.getInterestAmount(), BigDecimal.ZERO)
                && interestRequest.getGenerateInterestDebitNote()) {
            processInterestEntries(debitEntry, interestRateBean, interestRequest.getPaymentDate());
        }

        final Set<FinantialDocument> interestFinantialDocumentsSet =
                debitEntry.getInterestDebitEntriesSet().stream().filter(l -> l.isProcessedInClosedDebitNote())
                        .map(l -> l.getFinantialDocument()).collect(Collectors.toSet());

        final String saftResult =
                ERPExporter.exportFinantialDocumentToXML(debitEntry.getDebtAccount().getFinantialInstitution(),
                        interestFinantialDocumentsSet);

        try {
            bean.setInterestDocumentsContent(saftResult.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return bean;
    }

    @Atomic
    private void processInterestEntries(final DebitEntry debitEntry, final InterestRateBean interestRateBean,
            final LocalDate paymentDate) {

        DocumentNumberSeries debitNoteSeries =
                DocumentNumberSeries
                        .find(FinantialDocumentType.findForDebitNote(), debitEntry.getDebtAccount().getFinantialInstitution())
                        .filter(x -> Boolean.TRUE.equals(x.getSeries().getDefaultSeries())).findFirst().orElse(null);

        final DebitNote interestDebitNote =
                DebitNote.create(debitEntry.getDebtAccount(), debitNoteSeries, paymentDate.toDateTimeAtStartOfDay());

        debitEntry.generateInterestRateDebitEntry(interestRateBean, paymentDate.toDateTimeAtStartOfDay(), interestDebitNote);
        interestDebitNote.closeDocument();
    }

    private void validateRequestHeader(String finantialInstitution) {
        if (finantialInstitution == null || getSecurityHeader() == null
                || !finantialInstitution.equalsIgnoreCase(getSecurityHeader().getUsername())) {
            throw new SecurityException("invalid request permission");
        }
    }
}
