<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.type.title"/></title>
<content tag="heading"><fmt:message key="data.type.title"/></content>
<body id="submissions"/>

<spring:bind path="data.*">
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

<form method="post"  id="dataForm">
<input type="hidden" name="_page" value="0"/>

<fieldset>
<legend>Data Type Selection
<a href="#" class="openHelp" onclick="openHelp('analyzedDataTypeSelection')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>	
</legend>
<p>Please select the data you are entering for a analysis step</p>
<table border="0" cellpadding="3" cellspacing="3">
	<tr>
        <th><fmt:message key="data.analysis.step"/>:</th>
        <td>
        	<spring:bind path="data.step">
          	  <c:out value="${steps[0].label}"/>
          	</spring:bind>
        </td>
    </tr>    
    <tr>
        <th><fmt:message key="data.inputoutput.type"/>:</th>
        <td>
        	<spring:bind path="data.inputOutputType">
        	<select name="${status.expression}" style="width:150px"> 
        		<option value="">--- Select ---
        			<c:forEach var="type" items="${inputOutputTypes}">
        				<option value="${type.value}" <c:if test="${type.value == data.inputOutputType}">selected="selected"</c:if>> 
        					<c:out value="${type.label}"/>
        				</option>
        			</c:forEach>
        		</option>
        	</select>
        	</spring:bind>
        </td>
    </tr>    
    <tr>
        <th><fmt:message key="data.type"/>:</th>
        <td>
        	<spring:bind path="data.dataType">
        	<select name="${status.expression}" style="width:150px">
        		<option value="">--- Select ---
        			<c:forEach var="type" items="${dataTypes}">
        				<option value="${type.value}" <c:if test="${type.value == data.dataType}">selected="selected"</c:if>> 
        					<c:out value="${type.label}"/>
        				</option>
        			</c:forEach>
        		</option>
        	</select>
        	</spring:bind>
        </td>
    </tr>    
  	<tr>
    	<th></th>
    	<td>
	        <input type="submit" name="_target1" value="<fmt:message key="button.next"/>" />
	        <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>  	
</table>
</fieldset>
</form>

<script type="text/javascript">
</script>

<v:javascript formName="data" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
