<%@ include file="/common/taglibs.jsp"%>
<%
	java.util.LinkedHashMap searchOptions = new java.util.LinkedHashMap();
	searchOptions.put("treeID", "Tree ID");
	searchOptions.put("treeTitle", "Title");
	searchOptions.put("treeType", "Type");
	searchOptions.put("treeKind", "Kind");
	searchOptions.put("treeQuality", "Quality");
	searchOptions.put("treeNTAX", "NTAX");
	
	pageContext.setAttribute("searchOptions", searchOptions);
%> 
  <form id="searchSimple" method="post">
  <fieldset>
  Search: <input type="hidden" name="formName" value="treeSimple"/> 
    <input type="text" class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
       <select name="searchButton">
  			<c:forEach var="options" items="${searchOptions}">
  				<option value="${options.key}" <c:if test="${options.key == searchButton}">selected="selected"</c:if> />
    				${options.value}
				</option>
			</c:forEach>
  		</select>
  		<button type="submit" name="submit" >Search</button>
	  		<a href="#" class="openHelp" onclick="openHelp('treeSimpleSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>   
    </fieldset>
    <jsp:include page="querySearchBox.jsp"/>
  </form>
