<%@ include file="/common/taglibs.jsp"%>
<%
	java.util.LinkedHashMap searchOptions = new java.util.LinkedHashMap();
	searchOptions.put("matrixID", "Matrix ID");
	searchOptions.put("matrixTitle", "Title");
	searchOptions.put("matrixType", "Type");
	searchOptions.put("matrixNTAX", "NTAX");
	searchOptions.put("matrixNCHAR", "NCHAR");
	
	pageContext.setAttribute("searchOptions", searchOptions);
%> 
  <form id="searchSimple" method="post">
  <fieldset>
  Search: <input type="hidden" name="formName" value="matrixSimple"/>
    <input type=text class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
    <select name="searchButton">
		<c:forEach var="options" items="${searchOptions}">
  			<option value="${options.key}" <c:if test="${options.key == searchButton}">selected="selected"</c:if> />
    			${options.value}
			</option>
		</c:forEach>
	</select>
	<button type="submit" name="submit" >Search</button>
	  		<a href="#" class="openHelp" onclick="openHelp('matrixSimpleSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>    
    </fieldset>
    <jsp:include page="querySearchBox.jsp"/>   
  </form>
