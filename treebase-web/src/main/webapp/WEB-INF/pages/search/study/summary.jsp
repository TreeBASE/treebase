<%@ include file="/common/taglibs.jsp"%>

<c:set var="cit" value="${study.citation}"/>
<c:set var="sub" value="${study.submission}"/>

<head>
<title>Citation for study ${study.id}</title>
</head>

<body id="s-study"/>
<c:set var="baseLink" scope="request" value='${study.phyloWSPath.purl}'/>
<c:set var="currentSection" scope="request" value="Citation"/>
<c:set var="headerPrefix" scope="request" value="${currentSection} for"/>
<jsp:include page="nav.jsp"/>
<c:if test="${not empty cit.authors || true}">
	<p><c:out value="${cit.authorsCitationStyleWithoutHtml}"/></p>
	<c:if test="${not empty cit.authors}">
		<h3>Authors</h3>
		<ul>
			<c:forEach var="author" items="${cit.authors}" varStatus="status_author">
				<li>
					<c:out value="${author.fullNameCitationStyle}"/>
					<c:if test="${author.id == sub.submitter.person.id}">(submitter)</c:if>
					<c:if test="${not empty author.emailAddressString}">	
						<script type="text/javascript">
document.write(makeLink([<c:forEach var="code" items="${author.emailAddressCodePoints}"><c:out value="${code}"/>,</c:forEach>]));
						</script>
					</c:if>	
					<c:if test="${not empty author.phoneNumber}"> 
						<img class="iconButton" src="<fmt:message key="icons.phone"/>" alt="Phone"/>		
						<c:out value="${author.phoneNumber}"/>		
					</c:if>			
				</li>
			</c:forEach>
		</ul>
	</c:if>
</c:if>

<c:if test="${not empty cit.abstract}">
	<h3>Abstract</h3>
	<p style="abstract"><c:out value="${cit.abstract}"/></p>
</c:if>

<c:if test="${not empty cit.keywords}">
	<h3>Keywords</h3>
	<p><c:out value="${cit.keywords}"/></p>
</c:if>

<c:if test="${not empty cit.doi || not empty cit.PMID || not empty cit.URL}">
	<h3>External links</h3>
	<ul>
	<c:if test="${not empty cit.doi}">
		<li>
			DOI: 
			<a href="http://dx.doi.org/<c:out value="${cit.doi}"/>">
				<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
				<c:out value="${cit.doi}"/>
			</a>
		</li>
	</c:if>
	<c:if test="${not empty cit.PMID}">
		<li>
			PMID:
			<a href="http://pmid.us/<c:out value="${cit.PMID}"/>">
				<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
				<c:out value="${cit.PMID}"/>
			</a>
		</li>
	</c:if>
	<c:if test="${not empty cit.URL && cit.URL != 'http://' }">
		<li>
			Other URL: 
			<a href="<c:out value="${cit.URL}"/>">
				<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
				<c:out value="${cit.URL}"/>
			</a>			
		</li>
	</c:if>
	</ul>
</c:if>
<h3>About this resource</h3>
<ul>
	<li>Canonical resource URI: 
		<a href="${baseLink}">
			<img class="iconButton" src="<fmt:message key="icons.weblink"/>"/>	
			${baseLink}
		</a>
	</li>
	<li>Other versions:
	<%
	pageContext.setAttribute("accesscode",request.getSession().getAttribute("x-access-code"));
	%>
		<a href="${baseLink}?format=nexus<c:if test="${!empty accesscode}">&x-access-code=<c:out value='${accesscode}' /></c:if>">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>	
			Nexus			
		</a>
		<a href="${baseLink}?format=nexml<c:if test="${!empty accesscode}">&x-access-code=<c:out value='${accesscode}' /></c:if>">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.xml"/>" 
				title="<fmt:message key="download.nexml"/>" 
				alt="<fmt:message key="download.nexml"/>"/>	
			NeXML			
		</a>	
		<%
		/* <a href="${baseLink}?format=rdf">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.rdf"/>" 
				title="<fmt:message key="download.rdf"/>" 
				alt="<fmt:message key="download.rdf"/>"/>	
			RDF			
		</a> */ %>
	</li>

	<li><a onclick="toggle_visibility('bibtexfoo');">Show BibTeX reference</a>
		<pre id="bibtexfoo" style="display:none;width:100%;overflow:scroll">
		<c:out value="${cit.bibtexReference}"/>
		</pre>
	</li>
	
	<li><a onclick="toggle_visibility('risfoo');">Show RIS reference</a>
		<pre id="risfoo" style="display:none;width:100%;overflow:scroll">
		<c:out value="${cit.risReference}"/>
		</pre>
	</li>
</ul>


</body>
 		