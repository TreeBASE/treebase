<%@ include file="/common/taglibs.jsp" %>
<div id="contentRight"><div id="contentRight">
	<div class="gutter">
		<h1>Welcome to TreeBASE</h1>
		<p>TreeBASE is a repository of phylogenetic information, specifically user-submitted phylogenetic 
		trees and the data used to generate them. TreeBASE accepts all kinds of phylogenetic data (e.g., 
		trees of species, trees of populations, trees of genes) representing all biotic taxa. Data in 
		TreeBASE are exposed to the public if they are used in a publication that is in press or published 
		in a peer-reviewed scientific journal, book, conference proceedings, or thesis. Data used in 
		publications that are in preparation or in review can be submitted to TreeBASE but will not be 
		available to the public until they have passed peer review. Aside from the submitter, such data 
		are only available to the publication editors or reviewers using a special access URL. TreeBASE 
		is produced and governed by The Phyloinformatics Research Foundation, Inc.</p>
		<p>As of April 2014, TreeBASE contains data for 4,076 publications written by 8,777 different authors. 
		These studies analyzed 8,233 matrices and resulted in 12,817 trees with 761,460 taxon labels that mapped 
		to 104,593 distinct taxa.</p>
		<p>Some recent additions:</p>
		<c:choose>
			<c:when test="${not empty recentStudies}">
				<ul class="recent-studies">
				<c:forEach var="study" items="${recentStudies}">
					<li>
						<a href="<c:url value='/search/study/summary.html'><c:param name='id' value='${study.id}'/></c:url>">
							<c:choose>
								<c:when test="${not empty study.citation and not empty study.citation.title}">
									<c:out value="${study.citation.title}"/>
								</c:when>
								<c:when test="${not empty study.name}">
									<c:out value="${study.name}"/>
								</c:when>
								<c:otherwise>
									Study S<c:out value="${study.id}"/>
								</c:otherwise>
							</c:choose>
						</a>
						<c:if test="${not empty study.lastModifiedDate}">
							<span class="study-date">
								<fmt:formatDate value="${study.lastModifiedDate}" pattern="yyyy-MM-dd"/>
							</span>
						</c:if>
					</li>
				</c:forEach>
				</ul>
				<p><a href="<c:url value='/rss.xml'/>">Subscribe to RSS feed</a></p>
			</c:when>
			<c:otherwise>
				<p><em>No recent studies available.</em></p>
			</c:otherwise>
		</c:choose>
		<p>The current release includes a host of new features and improvements over the previous TreeBASE prototype. New features include:</p>
		<ul>
		  <li>Richer annotation of metadata (journal DOIs, specimen georeferences, Genbank accession numbers, etc) </li>
		  <li>A mapping between taxon labels and taxonomic names in uBio and NCBI for improved normalization of names</li>
		  <li>The ability to visualize trees using phylotree.js</li>
		  <li>The ability to search on tree topology</li>
		  <li>Persistent and resolvable URIs for data objects in TreeBASE (i.e. studies, trees, matrices)  serve as both globally unique identification numbers and 
		  resource locators.  These  can be included in articles and on researcher's websites, making access to TreeBASE data only a click away</li>
		  <li>Data are delivered in several  serializations, including <a href="http://informatics.nescent.org/wiki/Supporting_NEXUS" target="_blank">NEXUS</a> 
		  and <a href="http://www.nexml.org" target="_blank">NeXML</a></li>
		  <li> A special URL  gives journal editors and reviewers anonymous advanced access to data</li>
		  <li>Programmatic access to the data using the <a href="http://evoinfo.nescent.org/PhyloWS" target="_blank">PhyloWS API</a>.  Queries are expressed in  
		  URLs using PhyloWS syntax and can return results in RDF as RSS 1.0 feeds, which means that users can set their favorite 
		  RSS Reader to fetch all new TreeBASE studies that satisfy a particular query (e.g. return all studies published in &quot;Systematic Biology,&quot; 
		  or return all trees that include &quot;Homo sapiens,&quot; etc)</li>
		</ul>
	</div>
</div>
