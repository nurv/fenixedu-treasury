<%@page import="org.fenixedu.treasury.domain.FinantialInstitution"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="pt.ist.standards.geographic.Municipality"%>
<%@page import="pt.ist.standards.geographic.District"%>
<%@page import="pt.ist.standards.geographic.Country"%>
<%@page import="java.util.Collection"%>
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

<link
    href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css"
    rel="stylesheet" />
<script
    src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link
    href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css"
    rel="stylesheet" />
<script
    src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link
    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css"
    rel="stylesheet" />
<script
    src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>
<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%
    FinantialInstitution finantialInstitution = (FinantialInstitution) request
					.getAttribute("finantialInstitution");
%>

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.manageFinantialInstitution.searchFinantialInstitution" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution2/create"><spring:message
            code="label.event.create" /></a> |&nbsp;&nbsp;
</div>
<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">

        <c:forEach items="${infoMessages}" var="message">
            <p>${message}</p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">

        <c:forEach items="${warningMessages}" var="message">
            <p>${message}</p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">

        <c:forEach items="${errorMessages}" var="message">
            <p>${message}</p>
        </c:forEach>

    </div>
</c:if>

<c:choose>
    <c:when test="${not empty searchfinantialinstitutionResultsDataSet}">
        <table id="searchfinantialinstitutionTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.FinantialInstitution.code" /></th>
                    <th><spring:message
                            code="label.FinantialInstitution.fiscalNumber" /></th>
                    <th><spring:message
                            code="label.FinantialInstitution.name" /></th>                    
                    <th><%-- Operations Column --%></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info" role="alert">
            <spring:message code="label.noResultsFound" />
        </div>
    </c:otherwise>
</c:choose>

<script>
	var searchfinantialinstitutionDataSet = [
		<c:forEach items="${searchfinantialinstitutionResultsDataSet}" var="searchResult">
			{
				"code" : "<c:out value='${searchResult.code}'/>",
				"fiscalnumber" : "<c:out value='${searchResult.fiscalNumber}'/>",
				"name" : "<c:out value='${searchResult.name}'/>",
				"actions" : "<a  class=\"btn btn-default btn-xs\" "
				              +" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution2/search/view/${searchResult.externalId}\">"
				              +" <spring:message code='label.view'/></a>" 
			},
		</c:forEach>
    ];
	
    $(document).ready(function() {
        var table = $('#searchfinantialinstitutionTable').DataTable(
        	    {
        	        language : {
        	            url : "${datatablesI18NUrl}",			
        	        },
        	        "columns": [
        	            { data: 'code' },
        	            { data: 'fiscalnumber' },
        	            { data: 'name' },
        	            { data: 'actions' }			
        	        ],
        	        "data" : searchfinantialinstitutionDataSet,
            		//Documentation: https://datatables.net/reference/option/dom
            		"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
            		//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
            		//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
            		//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
                    "tableTools": {
                        "sSwfPath": "//cdn.datatables.net/tabletools/2.2.3/swf/copy_csv_xls_pdf.swf"
                    }
		        });
		table.columns.adjust().draw();
		$('#searchfinantialinstitutionTable tbody').on( 'click', 'tr', function () {
	        $(this).toggleClass('selected');
	    } );  
	}); 
</script>
