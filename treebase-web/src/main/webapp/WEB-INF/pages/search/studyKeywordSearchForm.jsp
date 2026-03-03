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

<form id="searchSimple" method="post" class="mb-3">
	<fieldset class="border rounded p-3 mb-2">
		<div class="mb-2">
			<small class="text-muted">For study IDs with values less than 3000, please search using the "Legacy Study ID" button.</small>
		</div>
		<div class="row g-2 align-items-center">
			<div class="col-auto">
				<label for="keyword" class="form-label fw-semibold mb-0">Search:</label>
				<input type="hidden" name="formName" value="searchKeyword"/>
			</div>
			<div class="col-auto">
				<input type="text" class="form-control" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
			</div>
			<div class="col-auto">
				<select name="searchButton" class="form-select">
					<c:forEach var="options" items="${searchOptions}">
						<option value="${options.key}" <c:if test="${options.key == searchButton}">selected="selected"</c:if>>
							${options.value}
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-auto">
				<button type="submit" name="submit" class="btn btn-primary">Search</button>
			</div>
			<div class="col-auto">
				<a href="#" class="openHelp ms-2" onclick="openHelp('studyKeywordSearchForm')">
					<img class="iconButton" src="<fmt:message key="icons.help"/>" />
				</a>
			</div>
		</div>
	</fieldset>
	<jsp:include page="querySearchBox.jsp"/>
</form>
