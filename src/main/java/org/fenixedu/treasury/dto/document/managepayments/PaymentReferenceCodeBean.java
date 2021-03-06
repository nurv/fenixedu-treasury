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

package org.fenixedu.treasury.dto.document.managepayments;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;

public class PaymentReferenceCodeBean implements IBean {

    private DebitNote debitNote;
    private PaymentCodePool paymentCodePool;
    private List<TupleDataSourceBean> paymentCodePoolDataSource;
    private java.lang.String referenceCode;
    private org.joda.time.LocalDate beginDate;
    private org.joda.time.LocalDate endDate;
    private java.math.BigDecimal maxAmount;
    private java.math.BigDecimal minAmount;

    public PaymentCodePool getPaymentCodePool() {
        return paymentCodePool;
    }

    public void setPaymentCodePool(PaymentCodePool value) {
        paymentCodePool = value;
    }

    public List<TupleDataSourceBean> getPaymentCodePoolDataSource() {
        return paymentCodePoolDataSource;
    }

    public void setPaymentCodePoolDataSource(List<PaymentCodePool> value) {
        this.paymentCodePoolDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText("[" + x.getEntityReferenceCode() + "] - " + x.getName());
            return tuple;
        }).collect(Collectors.toList());
    }

    public java.lang.String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(java.lang.String value) {
        referenceCode = value;
    }

    public org.joda.time.LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(org.joda.time.LocalDate value) {
        beginDate = value;
    }

    public org.joda.time.LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(org.joda.time.LocalDate value) {
        endDate = value;
    }

    public java.math.BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(java.math.BigDecimal value) {
        maxAmount = value;
    }

    public java.math.BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(java.math.BigDecimal value) {
        minAmount = value;
    }

    public PaymentReferenceCodeBean() {

    }

    public DebitNote getDebitNote() {
        return debitNote;
    }

    public void setDebitNote(DebitNote debitNote) {
        this.debitNote = debitNote;
    }

//    public PaymentReferenceCodeBean(PaymentReferenceCode paymentReferenceCode) {
//        this.setTargetPayment(paymentReferenceCode.getTargetPayment());
//        this.setPaymentCodePool(paymentReferenceCode.getPaymentCodePool());
//        this.setReferenceCode(paymentReferenceCode.getReferenceCode());
//        this.setBeginDate(paymentReferenceCode.getBeginDate());
//        this.setEndDate(paymentReferenceCode.getEndDate());
//        this.setState(paymentReferenceCode.getState());
//        this.setMaxAmount(paymentReferenceCode.getMaxAmount());
//        this.setMinAmount(paymentReferenceCode.getMinAmount());
//        this.setReportedInFiles(paymentReferenceCode.getReportedInFiles());
//        this.setReferenceCode(paymentReferenceCode.getReferenceCode());
//        this.setBeginDate(paymentReferenceCode.getBeginDate());
//        this.setEndDate(paymentReferenceCode.getEndDate());
//        this.setState(paymentReferenceCode.getState());
//        this.setMaxAmount(paymentReferenceCode.getMaxAmount());
//        this.setMinAmount(paymentReferenceCode.getMinAmount());
//    }

}
