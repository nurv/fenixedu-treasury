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
<script
	src="${pageContext.request.contextPath}/static/treasury/js/bootbox.min.js"></script>
<script
	src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message
			code="label.administration.document.manageSeries.updateSeries" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/administration/document/manageseries/series/read/${series.externalId}"><spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
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

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.code" />
				</div>

				<div class="col-sm-10">
					<input id="series_code" class="form-control" type="text"
						name="code"
						value='<c:out value='${not empty param.code ? param.code : series.code }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.name" />
				</div>

				<div class="col-sm-10">
					<input id="series_name" class="form-control" type="text"
						name="name" bennu-localized-string
						value='${not empty param.name ? param.name : series.name.json() } ' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.externSeries" />
				</div>

				<div class="col-sm-2">
					<select id="series_externSeries" name="externseries"
						class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
		$("#series_externSeries").val('<c:out value='${not empty param.externseries ? param.externseries : series.externSeries }'/>');
	</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.certificated" />
				</div>

				<div class="col-sm-2">
					<select id="series_certificated" name="certificated"
						class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
		$("#series_certificated").val('<c:out value='${not empty param.certificated ? param.certificated : series.certificated }'/>');
	</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.legacy" />
				</div>

				<div class="col-sm-2">
					<select id="series_legacy" name="legacy" class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
		$("#series_legacy").val('<c:out value='${not empty param.legacy ? param.legacy : series.legacy }'/>');
	</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Series.finantialInstitution" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="series_finantialInstitution"
						class="js-example-basic-single" name="finantialinstitution">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
				</div>
			</div>
			<%--	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.Series.bennu"/></div> 

<div class="col-sm-4">

			<select id="series_bennu" class="js-example-basic-single"
				name="bennu">
				<option value=""></option>
				
			</select>
		</div>
	</div>
	--%>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {

	<%-- Block for providing finantialInstitution options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	finantialInstitution_options = [
		<c:forEach items="${finantialInstitutionList}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#series_finantialInstitution").select2(
		{
			data : finantialInstitution_options,
		}	  
		    );
		    
	$("#series_finantialInstitution").select2().select2('val','${param.finantialinstitution}');
		    // ACFSILVA $("#series_finantialInstitution").select2().select2('val', '<c:out value='${not empty param.finantialinstitution ? param.finantialinstitution : series.finantialInstitution.externalId }'/>');
		    <%-- End block for providing finantialInstitution options --%>
	<%-- Block for providing bennu options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	bennu_options = [
		<c:forEach items="${Series_bennu_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#series_bennu").select2(
		{
			data : bennu_options,
		}	  
		    );
		    
		    
		    $("#series_bennu").select2().select2('val', '<c:out value='${not empty param.bennu ? param.bennu : series.bennu.externalId }'/>');
		    <%-- End block for providing bennu options --%>
	
	
	});
</script>