<%@ include file="/common/taglibs.jsp"%>
<%
	java.util.LinkedHashMap searchOptions = new java.util.LinkedHashMap();
	searchOptions.put("studyID", "Study ID");
	searchOptions.put("legacyStudyID", "Legacy Study ID");
	searchOptions.put("authorKeyword", "Author");
	searchOptions.put("titleKeyword", "Title");
	searchOptions.put("abstractKeyword", "Abstract");
	searchOptions.put("citationKeyword", "Entire citation");
	searchOptions.put("textKeyword", "All text");
	searchOptions.put("doiKeyword", "DOI");
	
	pageContext.setAttribute("searchOptions", searchOptions);

%> 
  <form id="searchSimple" method="post">
  <fieldset>
  For study IDs with values less than 3000, please search using the &quot;Legacy Study ID&quot; button.<br>
  Search: <input type="hidden" name="formName" value="searchKeyword"/>
    <input type="text" class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
	<select name="searchButton">
  		<c:forEach var="options" items="${searchOptions}">
  			<option value="${options.key}" <c:if test="${options.key == searchButton}">selected="selected"</c:if> />
    			${options.value}
			</option>
		</c:forEach>
	</select>	
	<button type="submit" name="submit" >Search</button>
	  		<a href="#" class="openHelp" onclick="openHelp('studyKeywordSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>    
    </fieldset>
    <jsp:include page="querySearchBox.jsp"/>  
  </form>
