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
    <input type="hidden" name="formName" value="searchKeyword"/>
    <div class="row g-3 align-items-center">
        <div class="col-auto">
            <label for="keyword" class="col-form-label fw-semibold">Search:</label>
        </div>
        <div class="col-md-4">
            <input type="text" class="form-control" name="searchTerm" id="keyword" value="${searchTerm}" placeholder="Enter search term..."/>
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
            <button type="submit" name="submit" class="btn btn-primary">
                <i class="fa fa-search me-1"></i>Search
            </button>
        </div>

    </div>
    <p class="text-muted small mb-3">For study IDs with values less than 3000, please search using the "Legacy Study ID" button.</p>
    <jsp:include page="querySearchBox.jsp"/>
</form>
