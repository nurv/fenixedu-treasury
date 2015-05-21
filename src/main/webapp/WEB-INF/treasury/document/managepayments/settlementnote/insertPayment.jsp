<%@page import="org.fenixedu.treasury.ui.document.managepayments.SettlementNoteController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl"
    value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl"
    value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl"
    value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl"
    value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%-- ${portal.toolkit()} --%>
${portal.angularToolkit()}


<link
    href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript"
    src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script
    src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<script
    src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script
    src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.manageCustomer.createSettlementNote.chooseInvoiceEntries" />
        <small></small>
    </h1>
    <div>
        <p>
            ${ settlementNoteBean.debtAccount.customer.name } (NIF ${ settlementNoteBean.debtAccount.customer.fiscalNumber })
        </p>
    </div>
</div>

<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">
        <c:forEach items="${infoMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon glyphicon-ok-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">
        <c:forEach items="${warningMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">
        <c:forEach items="${errorMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>

<div>
    <p>
        1. <spring:message code="label.administration.manageCustomer.createSettlementNote.chooseInvoiceEntries" />
        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
        2. <spring:message code="label.administration.manageCustomer.createSettlementNote.calculateInterest" />
        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
        3. <spring:message code="label.administration.manageCustomer.createSettlementNote.createDebitNote" />
        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
        <b>4. <spring:message code="label.administration.manageCustomer.createSettlementNote.insertpayment" /></b>
        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
        5. <spring:message code="label.administration.manageCustomer.createSettlementNote.summary" />
    </p>
</div>

<script>
   angular.isUndefinedOrNull = function(val) {
        return angular.isUndefined(val) || val === null };
   angular
        .module('angularAppSettlementNote', [ 'ngSanitize', 'ui.select' ])
        .controller(
                  'SettlementNoteController',
                  [
                    '$scope',
                    function($scope) {
                        $scope.object = angular.fromJson('${settlementNoteBeanJson}');
                        $scope.getTotal = function() {
                            var total = 0;
                    	    for(var i = 0; i < $scope.object.paymentEntries.length; i++){
                    	        total += parseFloat($scope.object.paymentEntries[i].payedAmount);
                    	    }
                    	    return total.toFixed(2);
                	    }
                        $scope.addPaymentMethod = function() {
                        	$scope.object.paymentEntries.push({payedAmount : $scope.paymentAmount , paymentMethod : $scope.paymentMethod.id });
                        	$scope.paymentAmount='';
                        }
                        $scope.getPaymentName = function(id) {
                        	var name;
                        	angular.forEach($scope.object.paymentMethods, function(paymentMethod) {
                        		if(paymentMethod.id == id) {
                        			name = paymentMethod.text;
                        		}
                        	}, id, name)
                        	return name;
                        }
                    } 
                  ]
         );
</script>

<script type="text/javascript">
    function processSubmit(url) {
        $("#insertPaymentForm").attr("action", url);
        $("#insertPaymentForm").submit();
    }
</script>

<form id='insertPaymentForm' name='form' method="post" class="form-horizontal"
    ng-app="angularAppSettlementNote"
    ng-controller="SettlementNoteController"
    action='${pageContext.request.contextPath}<%= SettlementNoteController.CREATE_DEBIT_NOTE_URL %>'>

    <input name="bean" type="hidden" value="{{ object }}" />

    <div class="panel panel-primary">    
        <div class="panel-heading">
            <h3 class="panel-title">
                <spring:message code="label.DebitEntry" />
            </h3>
        </div>
        <div class="panel-body">
            <table id="debitNoteTable"
                class="table responsive table-bordered table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="label.DebitEntry.documentNumber" /></th>
                        <th><spring:message code="label.DebitEntry.description" /></th>
                        <th><spring:message code="label.DebitEntry.dueDate" /></th>
                        <th><spring:message code="label.DebitEntry.vat" /></th>
                        <th><spring:message code="label.DebitEntry.amountWithVat" /></th> 
                    </tr>
                </thead>
                <tbody>
                    <c:set var="debitNoteDate" value='${settlementNoteBean.debitNoteDate.toString("yyyy-MM-dd")}' />
                    <c:forEach items="${ settlementNoteBean.debitEntries }" var="debitEntryBean">
                        <c:if test="${ debitEntryBean.included && empty debitEntryBean.debitEntry.finantialDocument  }">
                            <tr>
                                <td>
                                    ---
                                </td>
                                <td>
                                    <c:out value="${ debitEntryBean.debitEntry.description }" />
                                </td>
                                <td>
                                    <c:out value='${ debitNoteDate }' />
                                </td>                                
                                <td>
                                    <c:out value="${ debitEntryBean.debitEntry.vat.taxRate}"/>
                                </td>
                                <td>
                                    <c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.symbol }" />
                                    <c:out value="${ debitEntryBean.paymentAmountWithVat }" />
                                </td>
                            </tr>
                        </c:if>                    
                    </c:forEach>
                    <c:forEach items="${ settlementNoteBean.interestEntries }" var="interestEntryBean">
                        <c:if test="${ interestEntryBean.included  }">
                            <tr>
                                <td>
                                    ---
                                </td>
                                <td>
                                    <c:out value="${ interestEntryBean.debitEntry.description }" />
                                </td>
                                <td>
                                    <c:out value='${ debitNoteDate }' />
                                </td>                                
                                <td>
                                    0.00
                                </td>
                                <td>
                                    <c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.symbol }" />
                                    <c:out value="${ interestEntryBean.interest }" />
                                </td>
                            </tr>
                        </c:if>                    
                    </c:forEach>                 
                </tbody>
            </table>
            <table id="debitEntriesTable"
                class="table responsive table-bordered table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="label.DebitEntry.documentNumber" /></th>
                        <th><spring:message code="label.DebitEntry.description" /></th>
                        <th><spring:message code="label.DebitEntry.dueDate" /></th>
                        <th><spring:message code="label.DebitEntry.vat" /></th>
                        <th><spring:message code="label.DebitEntry.amountWithVat" /></th>                    
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${ settlementNoteBean.debitEntries }" var="debitEntryBean">
                        <c:if test="${ debitEntryBean.included && not empty debitEntryBean.debitEntry.finantialDocument  }">
                            <tr>
                                <td>
                                    <c:out value="${ debitEntryBean.debitEntry.finantialDocument.documentNumber }" />
                                </td>
                                <td>
                                    <c:out value="${ debitEntryBean.debitEntry.description }" />
                                </td>
                                <td>
                                    <c:out value="${ debitEntryBean.documentDate.toString('yyyy-MM-dd')}"/>
                                </td>
                                <td>
                                    <c:out value="${ debitEntryBean.debitEntry.vat.taxRate}"/>
                                </td>
                                <td>
                                    <c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.symbol }" />
                                    <c:out value="${ debitEntryBean.paymentAmountWithVat }" />
                                </td>
                            </tr>
                        </c:if>                    
                    </c:forEach>
                    <c:forEach items="${ settlementNoteBean.creditEntries}" var="creditEntryBean" varStatus="loop">
                        <tr>
                            <td>
                                <c:out value="${ creditEntryBean.creditEntry.finantialDocument.documentNumber }"/>
                            </td>
                            <td>
                                <c:out value="${ creditEntryBean.creditEntry.description }"/>
                            </td>
                            <td>
                                <c:out value="${ creditEntryBean.documentDate.toString('yyyy-MM-dd')}"/>
                            </td>
                            <td>
                                <c:out value="${ creditEntryBean.creditEntry.vat.taxRate}"/>
                            </td>
                            <td>
                                <c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.symbol }" />
                                -
                                <c:out value="${ creditEntryBean.creditEntry.amount }"/>
                            </td>
                        </tr>
                    </c:forEach>                   
                </tbody>
            </table>
            <div class="panel-footer">
                <p align="right"><b><spring:message code="label.total" /></b>: ${ settlementNoteBean.paymentAmountWithVat }</p>
            </div>            
        </div>
    </div>
    <div class="panel panel-primary">    
        <div class="panel-heading">
            <h3 class="panel-title">
                <spring:message code="label.PaymentMethod" />
            </h3>
        </div>
        <div class="panel-body">            
            <table id="paymentTableTable"
                class="table responsive table-bordered table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="label.PaymentMethod" /></th>
                        <th><spring:message code="label.PaymentMethod.value" /></th>
                        <!-- operation column -->
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="paymentEntryBean in object.paymentEntries">
                        <td>
                            {{ getPaymentName( paymentEntryBean.paymentMethod ) }}
                        </td>
                        <td>
                            {{ paymentEntryBean.payedAmount }}
                        </td>
                        <td>
                            <button type="button" class="btn btn-default" ng-click="object.paymentEntries.splice($index,1);"><spring:message code="label.event.delete" /></button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="panel-footer">
                <p align="right"><b><spring:message code="label.total" /></b>: {{ getTotal() }}</p>
            </div>
        </div>
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentMethod" />
                </div>
                <div class="col-sm-4">
                    <ui-select id="settlementNote_paymentMethod" ng-model="$parent.paymentMethod" theme="bootstrap" ng-disabled="disableddew-"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="paymentMethod as paymentMethod in object.paymentMethods | filter: $select.search">
                            <span ng-bind-html="paymentMethod.text | highlight: $select.search"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentMethod.base.value" />
                </div>
                <div class="col-sm-4">
                    <input id="settlementNote_paymentAmount" class="form-control" type="text" name="paymentAmount" ng-model="paymentAmount" ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/"/>
                    <p class="alert alert-danger" ng-show="form.paymentAmount.$error.pattern"><spring:message code="error.invalid.format.input" /></p>
                </div>
            </div> 
            <div class="panel-footer">
                <button type="button" class="btn btn-default" ng-click="addPaymentMethod()" ng-disabled="form.paymentAmount.$error.pattern" ><spring:message code="label.event.add" /></button>
            </div>  
        </div>
    </div>    
    <div class="panel-footer">
        <button type="button" class="btn btn-default" onClick="javascript:processSubmit('${pageContext.request.contextPath}<%= SettlementNoteController.CALCULATE_INTEREST_URL %>')"><spring:message code="label.event.back" /></button>
        <button type="button" class="btn btn-primary" onClick="javascript:processSubmit('${pageContext.request.contextPath}<%= SettlementNoteController.INSERT_PAYMENT_URL %>')"><spring:message code="label.continue" /></button>
    </div>
</form>

<script>
	$(document).ready(function() {

	});
</script>