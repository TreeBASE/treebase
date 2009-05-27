<%@ include file="/common/taglibs.jsp"%>

<c:set var="DOIResolver" value="http://dx.doi.org/" />


<display:table name="${resultSet.results}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="study"
			   cellspacing="3"
			   cellpadding="3">

	<display:column title="" sortable="true" class="checkBoxColumn noBreak">		
		<c:url var="url" value="study/summary.html">
			<c:param name="id" value="${study.id}" />
			<%-- c:param name="id" value="${taxon.study.id}" / --%>
		</c:url>
		<input type="checkbox" name="selection" id="s-${study.id}" value="${study.id }" />
		<a href="${url}">S${study.id }</a>				
	</display:column>

	<display:column  title="Authors" sortable="true">
		<c:forEach var="author" items="${study.citation.authors}" varStatus="status" end="2">
			<c:out value="${ author.fullNameCitationStyle }"/><c:if test="${! status.last}">, </c:if>
		</c:forEach>
		<c:if test="${fn:length(study.citation.authors) > 3}">, et al.</c:if> 
	</display:column>

	<display:column property="citation.publishYear" title="Year" sortable="true"/>

	<display:column property="citation.title" title="Title" sortable="true" style="width:50%"/>

	<display:column title="Journal/Publisher" sortable="true">
		<c:choose>
			<c:when test="${study.citation.citationType eq 'Article'}">
				<!-- ArticleCitation -->
				<c:if test="${not empty(study.citation.journal)}">${study.citation.journal }</c:if>
			</c:when>
			<c:when test="${study.citation.citationType eq 'Book' || study.citation.citationType eq 'InBook'}">
				<!-- BookCitation -->
				<c:if test="${not empty(study.citation.publisher)}">${study.citation.publisher }</c:if>
			</c:when>
		</c:choose>
	</display:column>
	
	<display:column class="iconColumn noBreak" headerClass="iconColumn" sortable="false">
		<c:if test="${study.citation.doi != null}">
			<c:set var="DOIResource" value="${DOIResolver}${study.citation.doi}"/>
			<a href="${DOIResource}" target="_blank">
			<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
			${study.citation.doi}</a>			
		</c:if>
	</display:column>

	<display:column 
		sortable="false"
		url="/search/study/summary.html" 
		paramId="id" 
		paramProperty="id" 
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="study.title"/>" 
				alt="<fmt:message key="study.title"/>"/>				
	</display:column>		
			 
	<display:footer>
		<tr>
			<!--  this id is important, because we might add additional buttons here -->
    		<td colspan="7" align="center" id="buttonContainer">
				<!--  insert additional controls here -->
        	</td>
    	</tr>
    </display:footer>
    <display:setProperty name="basic.empty.showtable" value="true"/>
    
</display:table>




