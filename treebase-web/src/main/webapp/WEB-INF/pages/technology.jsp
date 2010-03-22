<div class="gutter">
	<h1>Technology</h1>
	<h2>Data Content</h2>
	<p>
		The primary data objects in TreeBASE are bibliographic references to published 
		phylogenetic studies, taxon by character data matrices, and phylogenetic trees 
		resulting from the analysis of such data matrices. Information is also 
		available that links data matrices and trees, including types of analyses 
		performed, software used, etc. Wherever possible, taxon labels are mapped to 
		uBio's name services and NCBI's taxonomy. 
	</p>
	<h2>Implementation Technologies</h2>
	<ul>
		<li>Database: <a href="http://www.postgresql.org/">PostgreSQL 8.4.2</a></li>
		<li>Programming language: Java (web-application), Perl (data migration and 
		maintenance)</li>
		<li>Database ORM: <a href="http://www.hibernate.org/">Hibernate</a></li>
		<li>Web-application framework: <a href="http://www.springsource.org/">
		Spring</a></li>
		<li>Submission file parsing: <a href="http://mesquiteproject.org/">
		Mesquite</a></li>
		<li>Tree Visualization: <a href="http://www.phylowidget.org/">
		PhyloWidget</a></li>	
	</ul>
	<h2>Source Code</h2>
	<p>
		Development of <a href="https://sourceforge.net/projects/treebase/">TreeBASE 
		is hosted at SourceForge</a>. All source code can be downloaded from the 
		<a href="https://sourceforge.net/projects/treebase/develop">TreeBASE 
		subversion repository</a> at SourceForge under a BSD license. 
		Documentation on installing and running TreeBASE can be 
		found at the <a href="https://sourceforge.net/apps/mediawiki/treebase/index.php?title=Main_Page"> 
		TreeBASE wiki</a>.	
	</p>
	<h2>Architecture</h2>
	<p>
		TreeBASE has a tiered Java-based architecture using the Hibernate and Spring 
		frameworks built on a PostgreSQL database. The following schematic illustrates 
		the content and features of this software stack:
	</p>
	<div style="width:100%;text-align:center">
		<img src="images/architecture.jpeg" alt="TreeBASE architecture" />
	</div>
	<ol>
		<li>
			TreeBASE stores alignments, character matrices, and trees used for research 
			that is published in peer-reviewed journals and books.
		</li>
		<li>
			Each study has one or more analyses; each analysis has one or more steps; 
			each step associates matrices and trees with algorithms and software. 
		</li>
		<li>
			Each row of sequence alignments or coded characters has a taxon label that 
			maps to leaf nodes on associated trees. Trees are hashed to allow topological 
			querying.
		</li>
		<li>
			Each row in a character matrix can be subdivided into one or more row segments; 
			each row segment can have associated specimen, tissue, or gene sequence metadata.
		</li> 
		<li>
			Names in the rows of a matrix and in row segment metadata can independently 
			map to a dictionary of name variants, which maps to a taxonomy. This taxonomy will 
			be mapped to a classification tree in the next release.
		</li> 	
	</ol>
</div>
