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
    <input type="hidden" name="formName" value="matrixSimple"/>
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
    <jsp:include page="querySearchBox.jsp"/>
</form>
