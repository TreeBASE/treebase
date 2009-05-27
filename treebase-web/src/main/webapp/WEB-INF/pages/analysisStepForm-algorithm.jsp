<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.step.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.title"/></content>
<body id="submissions"/>

<p>Please complete the following algorithm information used in the analysis step</p>

<spring:bind path="step.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post" onsubmit="return validateCitation(this)">
<fieldset>
<legend>Analysis Step - Algorithm Information</legend>
<input type="hidden" name="_page" value="2"/>

<!--  pick "Likelihood" to work with, so any of the object inherits from Algorithm should work -->
<c:choose>
<c:when test="${empty step.algorithmType }">
	<c:set var="algorithmType" value="Likelihood"/>
</c:when>                             
<c:otherwise>
	<c:set var="algorithmType" value="${step.algorithmType}"/>
</c:otherwise>
</c:choose>


<table>
    <tr>
        <th><fmt:message key="analysis.step.algorithm.type"/>:</th>
        <td>
        	<spring:bind path="step.algorithmType">
        	<select name="${status.expression}" style="width:150px" onchange="this.form.submit()">
        		<option value="">--- Please Select ---
        			<c:forEach var="type" items="${algorithmtypes}">
        				<option value="${type}" <c:if test="${type == step.algorithmType}">selected="true"</c:if> >
        					<c:out value="${type}"/>
        				</option>
        			</c:forEach>
        		</option>
        	</select>
        	</spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="analysis.step.algorithm.propertyName"/>:</th>
        <td>
            <spring:bind path="step.algorithmMap[${algorithmType}].propertyName">
            <input size="50" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="analysis.step.algorithm.propertyValue"/>:</th>
        <td>
            <spring:bind path="step.algorithmMap[${algorithmType}].propertyValue">
            <input size="30" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
     <c:choose>
    	<c:when test="${step.algorithmType == 'Likelihood'}">
    		<jsp:include page="analysisStepForm-algorithm-likelihood.jsp"/>
    	</c:when>
    	<c:when test="${step.algorithmType == 'Parsimony'}">
    		<jsp:include page="analysisStepForm-algorithm-parsimony.jsp"/>
    	</c:when>
    	<c:when test="${step.algorithmType == 'Distance'}">
    		<jsp:include page="analysisStepForm-algorithm-distance.jsp"/>
    	</c:when>
    	<c:when test="${step.algorithmType == 'Other Algorithm'}">
    		<jsp:include page="analysisStepForm-algorithm-other.jsp"/>
    	</c:when>
    </c:choose>
    
  	<tr>
    	<th></th>
    	<td >
    		<input type="submit" name="_target1" value="<fmt:message key="button.previous"/>" />
	        <input type="submit" name="_finish" value="<fmt:message key="button.finish"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</table>
</fieldset>
</form>

<script type="text/javascript">
</script>

