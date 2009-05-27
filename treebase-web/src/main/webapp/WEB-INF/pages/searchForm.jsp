<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.title"/></title>
<content tag="heading"><fmt:message key="search.title"/></content>
<body id="search"/>

<p>Select list of search criteria</p>

<spring:bind path="search.*">
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

<form method="post" id="searchForm">
<!--  input type="hidden" name="id" value="${status.value}"/ -->
<fieldset>
	<legend>Type of Search</legend>
	<table border="0" cellpadding="3" align="center">
		<tr>
    	    <th><fmt:message key="search.type"/>:</th>
        
			<td align="center">
        		<spring:bind path="search.category">
        		<select name="${status.expression}" style="width:150px" onchange="formSubmit(form)">
        			<option value="">--- Please Select ---
        				<c:forEach var="category" items="${categories}">
        					<option value="${category.value}" <c:if test="${category.value == search.category}">selected="true"</c:if> >
        						<c:out value="${category.label}"/>
        					</option>
        				</c:forEach>
        			</option>
        		</select>
        		</spring:bind>
        	</td>
        </tr>
	</table>
</fieldset>


<c:choose>
	<c:when test="${search.category == 'Study' }">
    	<jsp:include page="searchForm-study.jsp"/>
    </c:when>
    <c:when test="${search.category == 'Matrix' }">
    	<jsp:include page="searchForm-matrix.jsp"/>
    </c:when>
    <c:when test="${search.category == 'Tree' }">
    	<jsp:include page="searchForm-tree.jsp"/>
    </c:when>
</c:choose>
    	
</form>
	
<script type="text/javascript">
function formSubmit(form) {
	form.submit();
}
</script>

