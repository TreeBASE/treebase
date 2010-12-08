<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>

<div class="gutter">
	<h1>Data Access</h1>
	<h2>Web Browser User Interface</h2>
	<p>
		Primary access to the database is through its web interface, where 
		users most commonly search on authors, citations, and taxa. Datasets 
		are downloadable in NEXUS and NeXML formats for further analysis.
	</p>
	<h2>Programmatic Data Access</h2>
	<p>
		In addition to the web interface, TreeBASE can be accessed programmatically 
		through a stateless web service interface and URL architecture. This 
		interface can deliver data in several different formats, including NEWICK, 
		NEXUS, JSON, NeXML.
	</p>
	<ul>
		<li>
			a <a href="http://evoinfo.nescent.org/PhyloWS"
			title="PhyloWS">PhyloWS</a> RESTful API. A detailed description
			of <a href="https://sourceforge.net/apps/mediawiki/treebase/index.php?title=API"
			title="TreeBASE's PhyloWS implementation">TreeBASE's PhyloWS
			implementation</a> is on the TreeBASE wiki.
		</li>
		<li>
			<a href="http://www.openarchives.org/pmh/" title="OAI-PMH">OAI-PMH</a>
			metadata harvesting interface is available, though under development. 
			A detailed description is on the <a href="http://sourceforge.net/apps/mediawiki/treebase/index.php?title=OAI-PMH" title="OAI-PMH">TreeBASE wiki</a>.
		</li>
		<li>SQL data dumps, coming soon.</li>
	</ul>
	<h2>Links to external objects</h2>
	<p>
		TreeBASE stores references to and provides outlinks to external objects including 
		DOIs (for publications), NameBankIDs and NCBI taxids (for taxa), and Genbank 
		accession numbers (for sequences).
	</p>
	<h2>Links to objects within TreeBASE</h2>
	<p>
		TreeBASE issues persistent and resolvable uniform resource identifiers (URIs)
		to studies, matrices, trees, and nodes in trees. These allow external data 
		services to link directly into TreeBASE resources. For example:
	</p>
	<dl>
		<dt>URI to a study</dt>
		<dd><%=purlBase%>study/TB2:S1925</dd>
		<dt>URI to a matrix</dt>
		<dd><%=purlBase%>matrix/TB2:M2610</dd>
		<dt>URI to a tree</dt>
		<dd><%=purlBase%>tree/TB2:Tr2026</dd>		
	</dl>
	<h2>RSS Feeds</h2>
	<p>
		When TreeBASE's API delivers a list of results in RDF/XML, it expresses 
		this in RSS 1.0. This delivery allows programmatic capture of these results as well 
		as human perusal using a RSS client reader or browser. For example, a user interested 
		in a certain taxon could express a query as a PhyloWS URL and enter it into his or her 
		favorite RSS reader. As a result, any new submissions to TreeBASE that includes this 
		taxon of interest will alert the user with a new RSS article.
	</p>
	<h2>Data Dumps</h2>
	<p>
		In the future, users who wish to perform queries and analyses beyond the scope and 
		performance of TreeBASE will be able to acquire entire periodic data dumps that 
		comply with a standard data model, such as BioSQL.
	</p>
</div>