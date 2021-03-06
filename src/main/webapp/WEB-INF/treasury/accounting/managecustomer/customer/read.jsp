<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>

<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />
<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message code="label.accounting.manageCustomer.readCustomer" />
    </h1>
    <small></small>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/delete/${customer.externalId}" method="POST">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <spring:message code="label.confirmation" />
                    </h4>
                </div>
                <div class="modal-body">
                    <p>
                        <spring:message code="label.accounting.manageCustomer.readCustomer.confirmDelete" />
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="label.close" />
                    </button>
                    <button id="deleteButton" class="btn btn-danger" type="submit">
                        <spring:message code="label.delete" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/"><spring:message
            code="label.event.back" /></a> &nbsp;|&nbsp; 
            <c:if test='${customer.isAdhocCustomer() }'>
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/adhoccustomer/update/${customer.externalId}"><spring:message code="label.event.update" /></a>
        </c:if>
</div>
<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">

        <c:forEach items="${infoMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">

        <c:forEach items="${warningMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">

        <c:forEach items="${errorMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.details" />
        </h3>
    </div>
    <div class="panel-body">
        <form method="post" class="form-horizontal">
            <table class="table">
                <tbody>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.Customer.code" /></th>
                        <td><c:out value='${customer.code}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.Customer.name" /></th>
                        <td><c:out value='${customer.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.Customer.fiscalNumber" /></th>
                        <td><c:out value='${customer.fiscalNumber}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.Customer.identificationNumber" /></th>
                        <td><c:out value='${customer.identificationNumber}' /></td>
                    </tr>
                </tbody>
            </table>

        </form>
    </div>
</div>

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.Customer.debtAccountsBalances" />
        </h3>
    </div>
    <div class="panel-body">
        <form method="post" class="form-horizontal">
            <table class="table">
                <tbody>
                    <c:forEach var="debtAccount" items='${customer.debtAccountsSet}'>
                        <tr>
                            <th scope="row" class="col-xs-3"><c:out value="${debtAccount.finantialInstitution.name}" /></th>
                            <td style="vertical-align: middle">
                                <div class="col-xs-3">
                                    <c:out value="${debtAccount.finantialInstitution.currency.getValueFor(debtAccount.totalInDebt + debtAccount.calculatePendingInterestAmount())}" />
                                </div> &nbsp;&nbsp;<a class="btn btn-default btn-xs"
                                href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}"><spring:message
                                        code="label.customer.read.showdebtaccount"></spring:message></a> <c:if
                                    test="${debtAccount.totalInDebt < 0 }">
                                    <span class="label label-warning"> <spring:message code="label.DebtAccount.customerHasAmountToRehimburse" />
                                    </span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </form>
    </div>
</div>
</br>
<h2>Documentos Pendentes</h2>
<div id="content">
    <!--             <h3>Docs. Pendentes</h3> -->
    <p></p>
    <c:choose>
        <c:when test="${not empty pendingDocumentsDataSet}">
            <datatables:table id="pendingDocuments" row="pendingEntry" data="${pendingDocumentsDataSet}" cssClass="table table-bordered table-hover" cdn="false" cellspacing="2">
                <datatables:column cssStyle="width:10%;align:right">
                    <datatables:columnHead>
                        <spring:message code="label.DebtAccount.finantialInstitution" />
                    </datatables:columnHead>
                    <c:out value="${pendingEntry.debtAccount.finantialInstitution.name}" />
                </datatables:column>
                <datatables:column>
                    <datatables:columnHead>
                        <spring:message code="label.InvoiceEntry.finantialDocument" />
                    </datatables:columnHead>
                    <c:if test="${pendingEntry.isDebitNoteEntry() }">
                        <a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${pendingEntry.finantialDocument.externalId}"> <c:out
                                value="${pendingEntry.finantialDocument.uiDocumentNumber}" /></a>
                    </c:if>
                    <c:if test="${pendingEntry.isCreditNoteEntry() }">
                        <a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${pendingEntry.finantialDocument.externalId}"> <c:out
                                value="${pendingEntry.finantialDocument.uiDocumentNumber}" /></a>
                    </c:if>
                </datatables:column>
                <datatables:column cssStyle="width:10%;align:right">
                    <datatables:columnHead>
                        <spring:message code="label.InvoiceEntry.description" />
                    </datatables:columnHead>
                    <c:out value="${pendingEntry.description}" />
                </datatables:column>
                <datatables:column>
                    <datatables:columnHead>
                        <spring:message code="label.InvoiceEntry.date" />
                    </datatables:columnHead>
                    <c:out value="${pendingEntry.entryDateTime}" />

                </datatables:column>
                <%-- 						<datatables:column> --%>
                <%-- 							<datatables:columnHead> --%>
                <%-- 								<spring:message code="label.Invoice.debitAmount" /> --%>
                <%-- 							</datatables:columnHead> --%>
                <!-- 							<div align=right> -->
                <%-- 								<c:out value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.debitAmount)}" /> --%>
                <!-- 							</div> -->
                <%-- 						</datatables:column> --%>
                <datatables:column>
                    <datatables:columnHead>
                        <spring:message code="label.InvoiceEntry.totalAmount" />
                    </datatables:columnHead>
                    <div align=right>
                        <c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
                        <c:out value="${pendingEntry.currency.getValueFor(pendingEntry.totalAmount)}" />
                    </div>
                </datatables:column>
                <datatables:column>
                    <datatables:columnHead>
                        <spring:message code="label.InvoiceEntry.openAmount" />
                    </datatables:columnHead>
                    <div align=right>
                        <c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
                        <c:out value="${pendingEntry.currency.getValueFor(pendingEntry.openAmount)}" />
                    </div>
                </datatables:column>
                <datatables:column>
                            <c:if test="${not empty pendingEntry.finantialDocument }">
                                <c:if test="${pendingEntry.isDebitNoteEntry() }">
                                    <a class="btn btn-default btn-xs" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${pendingEntry.finantialDocument.externalId}">
                                    	<spring:message code="label.view" />
                                    </a>
                                </c:if>
                                <c:if test="${pendingEntry.isCreditNoteEntry() }">
                                    <a class="btn btn-default btn-xs" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${pendingEntry.finantialDocument.externalId}">
                                    	<spring:message code="label.view" />
                                    </a>
                                </c:if>
                            </c:if>
                
                    <%-- 				<form method="post" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/deleteentry/${debitEntry.externalId}"> --%>
                    <!-- 					<button type="submit" class="btn btn-default btn-xs"> -->
                    <!-- 						<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; -->
                    <%-- 						<spring:message code="label.event.document.manageInvoice.deleteEntry" /> --%>
                    <!-- 					</button> -->
                    <!-- 				</form> -->
                </datatables:column>
            </datatables:table>
            <script>
													createDataTables(
															'pendingDocuments',
															false,
															false,
															false,
															"${pageContext.request.contextPath}",
															"${datatablesI18NUrl}");
												</script>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning" role="alert">

                <p>
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
                    <spring:message code="label.noResultsFound" />
                </p>

            </div>

        </c:otherwise>
    </c:choose>
</div>

<script>
	$(document).ready(function() {

		//Enable Bootstrap Tabs
		$('#tabs').tab();

	});
</script>
