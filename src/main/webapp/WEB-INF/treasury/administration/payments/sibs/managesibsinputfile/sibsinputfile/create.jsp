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
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

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



<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.payments.sibs.manageSibsInputFile.createSibsInputFile" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/"><spring:message
            code="label.event.back" /></a> &nbsp;|&nbsp;
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

<form method="post" class="form-horizontal" enctype="multipart/form-data">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SibsInputFile.whenProcessedBySibs" />
                </div>

                <div class="col-sm-4">
                    <input id="sibsInputFile_whenProcessedBySibs"
                        class="form-control" type="text"
                        name="whenprocessedbysibs" bennu-datetime
                        value='<c:out value='${not empty param.whenprocessedbysibs ? param.whenprocessedbysibs : sibsInputFile.whenProcessedBySibs }'/>' />
                </div>
            </div>
            <!-- <div class="form-group row"> -->
            <%-- <div class="col-sm-2 control-label"><spring:message code="label.SibsInputFile.transactionsTotalAmount"/></div>  --%>
            <!-- <div class="col-sm-10"> -->
            <%--    <input id="sibsInputFile_transactionsTotalAmount" class="form-control" type="text" name="transactionstotalamount"  value='<c:out value='${not empty param.transactionstotalamount ? param.transactionstotalamount : sibsInputFile.transactionsTotalAmount }'/>' /> --%>
            <!-- </div>  -->
            <!-- </div>      -->
            <!-- <div class="form-group row"> -->
            <%-- <div class="col-sm-2 control-label"><spring:message code="label.SibsInputFile.totalCost"/></div>  --%>

            <!-- <div class="col-sm-10"> -->
            <%--    <input id="sibsInputFile_totalCost" class="form-control" type="text" name="totalcost"  value='<c:out value='${not empty param.totalcost ? param.totalcost : sibsInputFile.totalCost }'/>' /> --%>
            <!-- </div>  -->
            <!-- </div>      -->
            <div class="form-group row">
            <div class="col-sm-2 control-label"><spring:message code="label.SibsInputFile.uploadFile"/></div> 
            <div class="col-sm-10">
               <input type="file" name="documentSibsInputFile" accept="${ allowedFileType}" required />
            </div> 
            </div>     
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.submit" />" />
        </div>
    </div>
</form>

<script>
    $(document).ready(function() {

    });
</script>
