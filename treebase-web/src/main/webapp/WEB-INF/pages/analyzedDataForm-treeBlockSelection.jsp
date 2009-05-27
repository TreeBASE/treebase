<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.list.title"/></title>
<content tag="heading">List of uploaded <b>${data.dataType} data</b></content>
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

<form method="post" name="dataform">
<fieldset>
<legend>Analyzed tree block selection
<a href="#" class="openHelp" onclick="openHelp('analyzedTreeBlockSelection')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
Check the list of ${data.dataType} (TreeBlock) data that will be used for analysis step
<input type="hidden" name="_page" value="2"/>
<c:set var="counter"   value="0"/>

<display:table name="${data.treeBlockList}"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="matrix"
			   cellspacing="3"
			   cellpadding="3">

	<display:column class="checkBoxColumn">
			<spring:bind path="data.treeBlockList[${counter}].checked">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<!--  do not allow user select the matrix if it's been used either as input or output -->
				<!--  or you can select data.dataType == selected if only specific the type input or output type -->
				<input type="checkbox" name="${status.expression}" value="true" 
				<c:if test="${not empty matrix.selected}">checked disabled</c:if>/>
            </spring:bind>            
	</display:column>
			
	<display:column property="treeBlock.title" title="Tree block title" 
				sortable="true" style="width:100%">
	</display:column>	
	
	<display:column paramProperty="treeBlock.id"
			url="/user/treeList.html" 
			paramId="treeblockid" 
			sortable="false"
			class="iconColumn" 
			headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="tree.list"/>" 
				alt="<fmt:message key="tree.list"/>"/>
	</display:column>	
	
	<c:set var="counter" value="${counter+1}"/>
	
	<display:footer>
	<tr>
    	<td colspan="3" id="buttonContainer" align="center">
	        <input type="submit" class="button" name="_target0" value="<fmt:message key="button.previous"/>" />
	        <input type="submit" class="button" name="_finish" value="<fmt:message key="button.finish"/>" />
	        <input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
	
	</display:footer>
	
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</form>
