<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message code="label.document.manageInvoice.createCreditNote" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <c:if test="${not empty debitNote }">
        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
            href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}"><spring:message code="label.event.back" /></a> &nbsp;|&nbsp;
	</c:if>
    <c:if test="${empty debitNote }">
        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
            href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/"><spring:message code="label.event.back" /></a> &nbsp;|&nbsp;
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


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.details" />
        </h3>
    </div>
    <div class="panel-body">

        <div class="form-group row">
            <div class="col-sm-2 control-label">
                <spring:message code="label.DebtAccount.finantialInstitution" />
            </div>

            <div class="col-sm-10">
                <div class="form-control">
                    <c:out value="${debtAccount.finantialInstitution.name}" />
                </div>
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-2 control-label">
                <spring:message code="label.CreditNote.debtAccount" />
            </div>

            <div class="col-sm-10">
                <div class="form-control">
                    <c:out value="${debtAccount.customer.code} - ${debtAccount.customer.name}" />
                </div>
            </div>
        </div>
        <c:if test="${not empty debitNote }">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.CreditNote.debitNote" />
                </div>

                <div class="col-sm-10">
                    <div class="form-control">
                        <c:out value='${debitNote.uiDocumentNumber}' />
                    </div>
                </div>
            </div>
        </c:if>

    </div>
</div>


<form method="post" class="form-horizontal">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.CreditNote.documentNumberSeries" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="creditNote_documentNumberSeries" class="js-example-basic-single" name="documentnumberseries">
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.CreditNote.documentDate" />
                </div>

                <div class="col-sm-4">
                    <input id="creditNote_documentDate" class="form-control" type="text" name="documentdate" bennu-date required
                        value='<c:out value='${not empty param.documentdate ? param.documentdate : creditNote.documentDate }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.CreditNote.originDocumentNumber" />
                </div>

                <div class="col-sm-10">
                    <input id="creditNote_originDocumentNumber" class="form-control" type="text" name="origindocumentnumber"
                        value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : creditNote.originDocumentNumber }'/>' />
                </div>
            </div>

        </div>

        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
        </div>
    </div>
</form>

<!-- <form method="post" class="form-horizontal"> -->
<!-- 	<div class="panel panel-default"> -->
<!-- 		<div class="panel-body"> -->
<!-- 			<div class="form-group row"> -->
<!-- 				<div class="col-sm-2 control-label"> -->
<%-- 					<spring:message code="label.CreditNote.documentDate" /> --%>
<!-- 				</div> -->

<!-- 				<div class="col-sm-4"> -->
<!-- 					<input id="creditNote_documentDate" class="form-control" type="text" name="documentdate" bennu-date -->
<%-- 						value='<c:out value='${not empty param.documentdate ? param.documentdate : creditNote.documentDate }'/>' /> --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="form-group row"> -->
<!-- 				<div class="col-sm-2 control-label"> -->
<%-- 					<spring:message code="label.CreditNote.originDocumentNumber" /> --%>
<!-- 				</div> -->

<!-- 				<div class="col-sm-10"> -->
<!-- 					<input id="creditNote_originDocumentNumber" class="form-control" type="text" name="origindocumentnumber" -->
<%-- 						value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : creditNote.originDocumentNumber }'/>' /> --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="panel-footer"> -->
<%-- 			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" /> --%>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </form> -->

<script>
	$(document).ready(function() {
        <%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
        documentNumberSeries_options = [
            <c:forEach items="${CreditNote_documentNumberSeries_options}" var="element"> 
                {
                    text : "<c:out value='${element.series.code} - ${element.series.name.content}'/>",  
                    id : "<c:out value='${element.externalId}'/>"
                },
            </c:forEach>
        ];
        
        $("#creditNote_documentNumberSeries").select2(
            {
                data : documentNumberSeries_options,
            }     
        );
        
        
        
        $("#creditNote_documentNumberSeries").select2().select2('val', '<c:out value='${param.documentnumberseries}'/>');
    
        <%-- End block for providing documentNumberSeries options --%>
	});
</script>
