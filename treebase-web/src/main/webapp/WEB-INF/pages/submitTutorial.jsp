<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>

<div class="gutter">

<h2>The Submission Process</h2>

<p>Typically an author submits a paper to a journal for review 
	and initiates a TreeBASE submission. At this stage, the 
	submission is classified as &quot;in progress.&quot; A submission 
	number is issued and the submitter is given a special URL 
	that can be mailed to the journal editor so that referees 
	or reviewers can inspect (but not change) the uploaded data.
</p>

<p>When the journal editor says that the paper is &quot;accepted&quot; or 
	&quot;accepted with minor revision,&quot; the submitter can return to the 
	submission and change its status from &quot;in progress&quot; to &quot;ready.&quot; 
	At that point, the submitter can cite a URI in his or her paper 
	that serves as an accession number and as a permanent, resolvable 
	URL to the data. TreeBASE staff will verify that the submission 
	is complete, and if so, the data are made public. 
</p>

<h2>General Requirements</h2>

<ul>
    <li>The paper must be published in a peer-reviewed journal or book. Data for manuscripts that are &quot;accepted with minor revision,&quot; &quot;accepted,&quot; or &quot;in press&quot; can be published in TreeBASE. Data for manuscripts that are &quot;submitted&quot; or &quot;in preparation&quot; must not move beyond the initial &quot;in progress&quot; stage of TreeBASE submission.</li>
    
    <li>The names of all authors listed with the paper must be included.</li>
    
    <li>At least one data matrix must be included.</li>
    
    <li>At least one tree must result from an analysis of one or more data matrices. TreeBASE does not accept matrices that were not analyzed to produce a tree or trees for which the matrices used to produce them are not available.</li>
    
    <li>Normally the trees should be limited to those that were published as figures in the manuscript. However, a set of trees that were used to produce a published consensus tree is also acceptable.</li>
    
    <li>Only matrices and trees listed with an analysis will ultimately become available to the public. In other words, matrices or trees must not be orphaned but instead must be either the inputs or outputs of explicit analyses.</li>
</ul>

<h2>Preparation of NEXUS files</h2>

<p>Data are uploaded to TreeBASE in the NEXUS format, which is used by many popular phylogenetics software packages (e.g., MacClade and Mesquite: Maddison and Maddison, 1992; PAUP: Swofford, 1993). These programs allow other file formats (e.g. Hennig86, PHYLIP, etc.) to be converted into NEXUS.  TreeBASE uses <a href="http://mesquiteproject.org/" title="Mesquite" target="_blank">Mesquite</a> to parse incoming data: <strong>if your data cannot be parsed by Mesquite then they will not be parsed by TreeBASE</strong>, so it's always a good idea to save your data in Mesquite prior to uploading. For example: </p>
<ul>
<li> Launch <a href="http://mesquiteproject.org/" title="Mesquite" target="_blank">Mesquite</a> </li>
<li>Under the <em>File Menu</em> select <em>Open File</em> and choose your character matrix</li>
<li>If your tree(s) are stored in a separate file, under the <em>File Menu</em> select
  <em>Include File...</em> and choose your tree file  </li>
<li>By clicking on &quot;Taxa,&quot; &quot;Character Matrix,&quot; or &quot;Trees from treefile.tre&quot; from the project margin on the left, you can rename 
  them: these names will be carried through into TreeBASE, so it's useful to give them informative names. For example, you could name your matrix &quot;Hymenopteran COI Alignment&quot;
or your tree block &quot;Set of 5 MP Trees from COI Analysis&quot;</li>
<li>By clicking on the <em>Tree</em> icon from the project margin on the left, you can examine your trees.
  <ul>
    <li>Use the <em>Reroot at Branch</em> tool to reorient the tree the way it appears in the figures of your paper</li>
    <li>Under the<em> Drawing Menu</em>, 
      you can view the relative branch lengths in your tree by selecting <em>Branches Proportional to Lengths</em></li>
    </ul>
</li>

  <li>To rename your trees, click the <em>List &amp; Manage Trees</em> icon from the project margin on the left. Renaming your trees makes it easy to identify them after uploading to TreeBASE.</li>
  <li>To view or edit the taxon labels, click the <em>Taxa</em> icon from the project margin on the left. Verify that the taxon labels comply with TreeBASE's requirements (see below) and edit as needed</li>
  <li>If all looks good, select <em>Save File</em> from the <em>File</em> menu. 
    Your NEXUS file is now ready to upload to TreeBASE  </li>
  </ul>   
<p>The most frequent error in submitting data to TreeBASE is when taxon labels in the matrices do not match with taxon labels in the trees. Using the above procedure with Mesquite, however, will avoid the problem. </p>

<h3>Instructional Video on Preparing NEXUS Data in Mesquite</h3>

<p>
<iframe width="560" height="315" src="//www.youtube.com/embed/0bdKW5-Lg7E?list=PL3F38DFE2626A5234" frameborder="0" allowfullscreen></iframe>
</p>

<h3>Preparation of taxon labels</h3>

<ul>
    <li>Do not abbreviate taxon names (e.g. write &quot;Homo sapiens&quot;, not &quot;H. sapiens&quot;)</li>
    
    <li>The NEXUS file should use underscores (&quot;_&quot;) to represent spaces (but when using the Mesquite GUI, use spaces: it will correctly save them as underscores in the NEXUS)</li>
    
    <li>Avoid using quotation marks, brackets, parentheses, commas, colons, and semicolons.</li>
    
    <li>Avoid using codes for non-redundant names (e.g. write &quot;Drosophila melanogaster&quot;, not &quot;DMelan45GRX&quot;). However, it is acceptable two write &quot;Drosophila melanogaster 45GRX&quot;.</li>
    
    <li>Separate the name of parasites and hosts with &quot;ex.,&quot; e.g. &quot;Wolbachia sp. ex. Drosophila melanogaster&quot;</li>
    
</ul>

<h2>Instructional Video on Submitting to TreeBASE</h2>

<p>If you are sufficiently caffeinated, please consider watching this video to understand the process of submitting data to TreeBASE. The video is also available in high-definition if you view in &quot;Full screen&quot;</p>

<p>
<iframe width="560" height="315" src="//www.youtube.com/embed/2npvFJX31hs?list=PL9AD9904B8782D600" frameborder="0" allowfullscreen></iframe>
</p>

</div>