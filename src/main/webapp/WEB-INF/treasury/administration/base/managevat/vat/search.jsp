<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>
<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css" rel="stylesheet" />
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.administration.base.manageVat.searchVat" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/base/managevat/vat/create"   ><spring:message code="label.event.create" /></a>
|&nbsp;&nbsp;</div>
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


<%--
<div class="panel panel-default">
<form method="get" class="form-horizontal">
<div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.Vat.taxRate"/></div> 

<div class="col-sm-10">
	<input id="vat_taxRate" class="form-control" type="text" name="taxrate"  value='<c:out value='${not empty param.taxrate ? param.taxrate : vat.taxRate }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.Vat.beginDate"/></div> 

<div class="col-sm-4">
	<input id="vat_beginDate" class="form-control" type="text" name="begindate"  bennu-datetime 
	value = '<c:out value='${not empty param.begindate ? param.begindate : vat.beginDate }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.Vat.endDate"/></div> 

<div class="col-sm-4">
	<input id="vat_endDate" class="form-control" type="text" name="enddate"  bennu-datetime 
	value = '<c:out value='${not empty param.enddate ? param.enddate : vat.endDate }'/>' />
</div>
</div>		
</div>
<div class="panel-footer">
	<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/>
</div>
</form>
</div>
 --%>

<c:choose>
	<c:when test="${not empty searchvatResultsDataSet}">
		<table id="searchvatTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.Vat.taxRate"/></th>
<th><spring:message code="label.Vat.beginDate"/></th>
<th><spring:message code="label.Vat.endDate"/></th>
<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
				<div class="alert alert-warning" role="alert">
					
					<spring:message code="label.noResultsFound"/>
					
				</div>	
		
	</c:otherwise>
</c:choose>

<script>
	var searchvatDataSet = [
			<c:forEach items="${searchvatResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"taxrate" : "<c:out value='${searchResult.taxRate}'/>",
"begindate" : "<c:out value='${searchResult.beginDate}'/>",
"enddate" : "<c:out value='${searchResult.endDate}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/base/managevat/vat/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchvatTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'taxrate' },
			{ data: 'begindate' },
			{ data: 'enddate' },
			{ data: 'actions' }
			
		],
		"columnDefs": [
			       		//54
			       		//128
			       		               { "width": "54px", "targets": 3 } 
			       		             ],
		"data" : searchvatDataSet,
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
		
		  $('#searchvatTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>
