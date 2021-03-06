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
package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreditNote extends CreditNote_Base {

    public CreditNote() {
        super();
    }

    protected CreditNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate, DebitNote debitNote) {
        super();

        init(debtAccount, documentNumberSeries, documentDate, debitNote);
        checkRules();
    }

    protected void init(DebtAccount debtAccount, DocumentNumberSeries documentNumberSeries, DateTime documentDate,
            DebitNote debitNote) {
        super.init(debtAccount, documentNumberSeries, documentDate);

        this.setDebitNote(debitNote);

    }

    protected CreditNote(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();

        init(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
        checkRules();
    }

    @Override
    public boolean isCreditNote() {
        return true;
    }

    @Override
    protected void checkRules() {
        if (!getDocumentNumberSeries().getFinantialDocumentType().getType().equals(FinantialDocumentTypeEnum.CREDIT_NOTE)) {
            throw new TreasuryDomainException("error.CreditNote.finantialDocumentType.invalid");
        }

        if (getDebitNote() != null && !getDebitNote().getDebtAccount().equals(getDebtAccount())) {
            throw new TreasuryDomainException("error.CreditNote.invalid.debtaccount.with.debitnote");
        }
        super.checkRules();
    }

    @Override
    public boolean isDeletable() {
        return true;
    }

    @Override
    @Atomic
    public void delete(boolean deleteEntries) {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.CreditNote.cannot.delete");
        }

        setDebitNote(null);
        super.delete(deleteEntries);
    }

    public Stream<? extends CreditEntry> getCreditEntries() {
        return CreditEntry.find(this);
    }

    public Set<? extends CreditEntry> getCreditEntriesSet() {
        return this.getCreditEntries().collect(Collectors.<CreditEntry> toSet());
    }

    public BigDecimal getDebitAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getCreditAmount() {
        return this.getTotalAmount();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends CreditNote> findAll() {
        return Invoice.findAll().filter(i -> i instanceof CreditNote).map(CreditNote.class::cast);
    }

    @Atomic
    public static CreditNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate, DebitNote debitNote, String originNumber) {
        CreditNote note = new CreditNote(debtAccount, documentNumberSeries, documentDate, debitNote);
        note.setOriginDocumentNumber(originNumber);
        note.checkRules();
        return note;
    }

//    @Atomic
//    public static CreditNote create(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
//            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
//        return new CreditNote(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
//    }

    @Atomic
    public void edit(final DebitNote debitNote, final DebtAccount payorDebtAccount,
            final FinantialDocumentType finantialDocumentType, final DebtAccount debtAccount,
            final DocumentNumberSeries documentNumberSeries, final Currency currency, final java.lang.String documentNumber,
            final org.joda.time.DateTime documentDate, final org.joda.time.LocalDate documentDueDate,
            final java.lang.String originDocumentNumber,
            final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
        setDebitNote(debitNote);
        setPayorDebtAccount(payorDebtAccount);
        setFinantialDocumentType(finantialDocumentType);
        setDebtAccount(debtAccount);
        setDocumentNumberSeries(documentNumberSeries);
        setCurrency(currency);
        setDocumentNumber(documentNumber);
        setDocumentDate(documentDate);
        setDocumentDueDate(documentDueDate);
        setOriginDocumentNumber(originDocumentNumber);
        setState(state);
        checkRules();
    }

    @Override
    public BigDecimal getOpenAmount() {
        if (this.isAnnulled()) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimal.ZERO;
        for (CreditEntry entry : getCreditEntriesSet()) {
            amount = amount.add(entry.getOpenAmount());
        }
        return getDebtAccount().getFinantialInstitution().getCurrency().getValueWithScale(amount);
    }

    @Override
    public Set<FinantialDocument> findRelatedDocuments(Set<FinantialDocument> documentsBaseList, Boolean includeAnulledDocuments) {
        documentsBaseList.add(this);

        for (CreditEntry entry : getCreditEntriesSet()) {
            if (entry.getDebitEntry() != null && entry.getDebitEntry().getFinantialDocument() != null
                    && !entry.getDebitEntry().getFinantialDocument().isPreparing()) {
                if (includeAnulledDocuments == true || this.isAnnulled() == false) {
                    if (documentsBaseList.contains(entry.getDebitEntry().getFinantialDocument()) == false) {
                        documentsBaseList.addAll(entry.getDebitEntry().getFinantialDocument()
                                .findRelatedDocuments(documentsBaseList, includeAnulledDocuments));
                    }
                }
            }
        }

        for (CreditEntry entry : getCreditEntriesSet()) {
            if (entry.getSettlementEntriesSet().size() > 0) {
                for (SettlementEntry settlementEntry : entry.getSettlementEntriesSet()) {
                    if (settlementEntry.getFinantialDocument() != null && !settlementEntry.getFinantialDocument().isPreparing()) {
                        if (includeAnulledDocuments == true || settlementEntry.getFinantialDocument().isAnnulled() == false) {
                            if (documentsBaseList.contains(settlementEntry.getFinantialDocument()) == false) {
                                documentsBaseList.addAll(settlementEntry.getFinantialDocument().findRelatedDocuments(
                                        documentsBaseList, includeAnulledDocuments));
                            }
                        }
                    }
                }
            }
        }

        return documentsBaseList;
    }

    @Override
    public void anullDocument(boolean freeEntries, String reason) {
        // The Credit Note can neve free entries
        super.anullDocument(false, reason);
    }

}
