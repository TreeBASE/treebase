<div id="contentRight">
	<div class="gutter">
		<h1>Data Access</h1>

<p><b>Web Browser User Interface</b></p>

<p>Primary access to the database is through its web interface, where users most commonly search on authors, citations, and taxa. Datasets are downloadable in NEXUS and NeXML formats for further analysis.</p>

<p><b>Programmatic Data Access</b></p>

<p>In addition to the web interface, TreeBASE can be accessed programmatically through a stateless web service interface and URL architecture. This interface can deliver data in several different formats, including NEWICK, NEXUS, JSON, NeXML.</p>
<ul type=disc>
 <li>a&nbsp;<a href="http://evoinfo.nescent.org/PhyloWS"
     title=PhyloWS id=fp8u>PhyloWS</a>&nbsp;RESTful API. A detailed description
     of <a
     href="https://sourceforge.net/apps/mediawiki/treebase/index.php?title=API"
     title="TreeBASE's PhyloWS implementation" id=sb5r>TreeBASE's PhyloWS
     implementation</a> is at the TreeBASE wiki.&nbsp;</li>
 <li><a href="http://www.openarchives.org/pmh/" title=OAI-PMH
     id=basz>OAI-PMH</a>&nbsp;harvesting interface, coming soon.</li>
 <li>SQL data dumps, coming soon.</li>
</ul>

<p><i><u>Links to external objects</u></i></p>

<p>TreeBASE stores references to and provides
outlinks to external objects including DOIs (for publications), NameBankIDs and
NCBI taxids (for taxa), and Genbank accession numbers (for sequences).&nbsp;</p>

<p><i><u>Links to objects within TreeBASE</u></i></p>

<p>TreeBASE issues persistent and resolvable uniform resource identifiers (URIs)
to studies, matrices, trees, and nodes in trees.&nbsp; These allow external
data services to link directly into TreeBASE resources. For example:&nbsp;</p>

<table border=0 cellspacing=0 cellpadding=0 width=600
 style='width:5.0in' id=ua52>
 <tr>
  <td width=100 style='width:60.0pt;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  URI to a study:
  </td>
  <td width="50%" style='width:50.0%;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  http://purl.org/phylo/treebase/phylows/study/TB2:S1925
  </td>
 </tr>
 <tr>
  <td width=100 style='width:60.0pt;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  URI to a matrix:
  </td>
  <td width="50%" style='width:50.0%;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  http://purl.org/phylo/treebase/phylows/matrix/TB2:M2610
  </td>
 </tr>
 <tr>
  <td width=100 style='width:60.0pt;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  URI to a tree:
  </td>
  <td width="50%" style='width:50.0%;padding:1.8pt 1.8pt 1.8pt 1.8pt'>
  http://purl.org/phylo/treebase/phylows/tree/TB2:Tr2026
  </td>
 </tr>
</table>

<p><b>RSS Feeds</b></p>

<p>When TreeBASE's API delivers a list of results
in RDF/XML, it typically expresses this in RSS 1.0. This delivery allows
programmatic capture of these results as well as human perusal using a RSS
client reader or browser. For example, a user interested in a certain taxon
could express a query as a PhyloWS URL and enter it into his or her favorite
RSS reader. As a result, any new submissions to TreeBASE that includes this
taxon of interest will alert the user with a new RSS article.&nbsp;</p>

<p><b>Data Dumps</b></p>

<p>In the future, users who wish to perform queries and
analyses beyond the scope and performance of TreeBASE will be able to acquire
entire periodic data dumps that comply with a
standard data model, such as BioSQL.</p>

</div></div>