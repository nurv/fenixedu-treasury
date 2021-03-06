package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class FinantialDocumentPaymentCode extends FinantialDocumentPaymentCode_Base {

    @Override
    @Atomic
    public SettlementNote processPayment(User person, BigDecimal amountToPay, DateTime whenRegistered, String sibsTransactionId,
            String comments) {

        Set<InvoiceEntry> invoiceEntriesToPay =
                this.getFinantialDocument().getFinantialDocumentEntriesSet().stream().filter(x -> x instanceof InvoiceEntry)
                        .map(InvoiceEntry.class::cast).sorted((x, y) -> y.getOpenAmount().compareTo(x.getOpenAmount()))
                        .collect(Collectors.toSet());
        return internalProcessPayment(person, amountToPay, whenRegistered, sibsTransactionId, comments, invoiceEntriesToPay);
    }

    @Override
    public boolean isFinantialDocumentPaymentCode() {
        return true;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPaymentCodeFor(final TreasuryEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    protected FinantialDocumentPaymentCode() {
        super();
    }

    protected void init(final FinantialDocument finantialDocument, final PaymentReferenceCode paymentReferenceCode,
            final java.lang.Boolean valid) {
        setFinantialDocument(finantialDocument);
        setPaymentReferenceCode(paymentReferenceCode);
        setValid(valid);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getFinantialDocument() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.finantialDocument.required");
        }

        if (getPaymentReferenceCode() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.paymentReferenceCode.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByFinantialDocument(getFinantialDocument().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.finantialDocument.duplicated");
        //} 
        //if (findByPaymentReferenceCode(getPaymentReferenceCode().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.paymentReferenceCode.duplicated");
        //} 
        //if (findByValid(getValid().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.valid.duplicated");
        //} 
    }

    @Atomic
    public void edit(final FinantialDocument finantialDocument, final PaymentReferenceCode paymentReferenceCode,
            final java.lang.Boolean valid) {
        setFinantialDocument(finantialDocument);
        setPaymentReferenceCode(paymentReferenceCode);
        setValid(valid);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.FinantialDocumentPaymentCode.cannot.be.deleted"));
        //}
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FinantialDocumentPaymentCode.cannot.delete");
        }
        deleteDomainObject();
    }

    private boolean isDeletable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Atomic
    public static FinantialDocumentPaymentCode create(final FinantialDocument finantialDocument,
            final PaymentReferenceCode paymentReferenceCode, final java.lang.Boolean valid) {
        FinantialDocumentPaymentCode finantialDocumentPaymentCode = new FinantialDocumentPaymentCode();
        finantialDocumentPaymentCode.init(finantialDocument, paymentReferenceCode, valid);
        return finantialDocumentPaymentCode;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<FinantialDocumentPaymentCode> findAll(final FinantialInstitution finantialInstitution) {
        Set<FinantialDocumentPaymentCode> entries = new HashSet<FinantialDocumentPaymentCode>();
        for (PaymentCodePool pool : finantialInstitution.getPaymentCodePoolsSet()) {
            for (PaymentReferenceCode code : pool.getPaymentReferenceCodesSet()) {
                if (code.getTargetPayment() != null && code.getTargetPayment() instanceof FinantialDocumentPaymentCode) {
                    entries.add((FinantialDocumentPaymentCode) code.getTargetPayment());
                }
            }
        }
        return entries.stream();
    }

    public static Stream<FinantialDocumentPaymentCode> findByFinantialDocument(final FinantialInstitution finantialInstitution,
            final FinantialDocument finantialDocument) {
        return findAll(finantialInstitution).filter(i -> finantialDocument.equals(i.getFinantialDocument()));
    }

    public static Stream<FinantialDocumentPaymentCode> findNewByFinantialDocument(final FinantialDocument finantialDocument) {
        return findByFinantialDocument(finantialDocument.getDebtAccount().getFinantialInstitution(), finantialDocument).filter(
                p -> p.getPaymentReferenceCode().isNew());
    }

    public static Stream<FinantialDocumentPaymentCode> findByValid(final FinantialInstitution finantialInstitution,
            final java.lang.Boolean valid) {
        return findAll(finantialInstitution).filter(i -> valid.equals(i.getValid()));
    }

    @Override
    protected DocumentNumberSeries getDocumentSeriesForPayments() {
        return this.getPaymentReferenceCode().getPaymentCodePool().getDocumentSeriesForPayments();
    }

    @Override
    protected DebtAccount getReferenceDebtAccount() {
        return this.getFinantialDocument().getDebtAccount();
    }

}
