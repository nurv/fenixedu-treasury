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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.administration.payments.sibs.managePaymentCodePool.createPaymentCodePool" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/"  ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;</div>
	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>

<form method="post" class="form-horizontal">
<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.name"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_name" class="form-control" type="text" name="name"  value='<c:out value='${not empty param.name ? param.name : paymentCodePool.name }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.minPaymentCodes"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_minPaymentCodes" class="form-control" type="text" name="minpaymentcodes"  value='<c:out value='${not empty param.minpaymentcodes ? param.minpaymentcodes : paymentCodePool.minPaymentCodes }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.maxPaymentCodes"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_maxPaymentCodes" class="form-control" type="text" name="maxpaymentcodes"  value='<c:out value='${not empty param.maxpaymentcodes ? param.maxpaymentcodes : paymentCodePool.maxPaymentCodes }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.minAmount"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_minAmount" class="form-control" type="text" name="minamount"  value='<c:out value='${not empty param.minamount ? param.minamount : paymentCodePool.minAmount }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.maxAmount"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_maxAmount" class="form-control" type="text" name="maxamount"  value='<c:out value='${not empty param.maxamount ? param.maxamount : paymentCodePool.maxAmount }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.active"/></div> 

<div class="col-sm-2">
<select id="paymentCodePool_active" name="active" class="form-control">
<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#paymentCodePool_active").val('<c:out value='${not empty param.active ? param.active : paymentCodePool.active }'/>');
	</script>	
</div>
</div>		
  </div>
  <div class="panel-footer">
		<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
	</div>
</div>
</form>

<script>
$(document).ready(function() {


	});
</script>
