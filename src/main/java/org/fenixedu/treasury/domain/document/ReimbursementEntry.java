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

package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ReimbursementEntry extends ReimbursementEntry_Base {

    protected ReimbursementEntry() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected ReimbursementEntry(final SettlementNote settlementNote, final PaymentMethod paymentMethod,
            final BigDecimal reimbursedAmount) {
        this();
        init(settlementNote, paymentMethod, reimbursedAmount);
    }

    protected void init(final SettlementNote settlementNote, final PaymentMethod paymentMethod, final BigDecimal reimbursedAmount) {
        setSettlementNote(settlementNote);
        setPaymentMethod(paymentMethod);
        setReimbursedAmount(reimbursedAmount);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getSettlementNote() == null) {
            throw new TreasuryDomainException("error.ReimbursementEntry.settlementNote.required");
        }

        if (getPaymentMethod() == null) {
            throw new TreasuryDomainException("error.ReimbursementEntry.paymentMethod.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findBySettlementNote(getSettlementNote().count()>1)
        //{
        //	throw new TreasuryDomainException("error.ReimbursementEntry.settlementNote.duplicated");
        //}	
        //if (findByPaymentMethod(getPaymentMethod().count()>1)
        //{
        //	throw new TreasuryDomainException("error.ReimbursementEntry.paymentMethod.duplicated");
        //}	
        //if (findByReimbursedAmount(getReimbursedAmount().count()>1)
        //{
        //	throw new TreasuryDomainException("error.ReimbursementEntry.reimbursedAmount.duplicated");
        //}	
    }

    @Atomic
    public void edit(final SettlementNote settlementNote, final PaymentMethod paymentMethod,
            final java.math.BigDecimal reimbursedAmount) {
        setSettlementNote(settlementNote);
        setPaymentMethod(paymentMethod);
        setReimbursedAmount(reimbursedAmount);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ReimbursementEntry.cannot.be.deleted"));
        //}
    }

    public boolean isDeletable() {
        //TODOJN
        return false;
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.ReimbursementEntry.cannot.delete");
        }
        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static ReimbursementEntry create(final SettlementNote settlementNote, final PaymentMethod paymentMethod,
            final java.math.BigDecimal reimbursedAmount) {
        return new ReimbursementEntry(settlementNote, paymentMethod, reimbursedAmount);
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<ReimbursementEntry> findAll() {
        return Bennu.getInstance().getReimbursementEntriesSet().stream();
    }

    public static Stream<ReimbursementEntry> findBySettlementNote(final SettlementNote settlementNote) {
        return findAll().filter(i -> settlementNote.equals(i.getSettlementNote()));
    }

    public static Stream<ReimbursementEntry> findByPaymentMethod(final PaymentMethod paymentMethod) {
        return findAll().filter(i -> paymentMethod.equals(i.getPaymentMethod()));
    }

    public static Stream<ReimbursementEntry> findByReimbursedAmount(final java.math.BigDecimal reimbursedAmount) {
        return findAll().filter(i -> reimbursedAmount.equals(i.getReimbursedAmount()));
    }

}
