<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="login.title"/></title>

<content tag="heading"><fmt:message key="login.title"/>

</content>
<P>
   A Special Note to Submitters to the Previous Version 
   of TreeBASE:  If you started a submission in the previous 
   version, but that submission remained "in progress," 
   you will need to start the submission all over again 
   in this new version of TreeBASE.
</P>
<h2>The Submission Process</h2>

<P>Typically an author submits a paper to a journal for review 
	and initiates a TreeBASE submission. At this stage, the 
	submission is classified as "in progress." A submission 
	number is issued and the submitter is given a special URL 
	that can be mailed to the journal editor so that referees 
	or reviewers can inspect (but not change) the uploaded data.
</P>

<P>When the journal editor says that the paper is "accepted" or 
	"accepted with minor revision," the submitter can return to the 
	submission and change its status from "in progress" to "ready." 
	At that point, the submitter can cite a URI in his or her paper 
	that serves as an accession number and as a permanent, resolvable 
	URL to the data. TreeBASE staff will verify that the submission 
	is complete, and if so, the data are made public. 
</P>

<h2>General Requirements</h2>

<ul>
    <li>The paper must be published in a peer-reviewed journal or book. Data for manuscripts that are "accepted with minor revision," "accepted," or "in press" can be included in TreeBASE. Data for manuscripts that are "submitted" or "in preparation" must not move beyond the initial "in progress" stage of TreeBASE submission.</li>
    
    <li>The names of all authors listed with the paper must be included.</li>
    
    <li>At least one data matrix must be included.</li>
    
    <li>At least one tree must result from an analysis of one or more data matrices. TreeBASE does not accept matrices that were not analyzed to produce a tree or trees for which the matrices used to produce them are not available.</li>
    
    <li>Normally the trees should be limited to those that were published as figures in the manuscript. However, a set of trees that were used to produce a published consensus tree is also acceptable.</li>
    
    <li>Only matrices and trees listed with an analysis will ultimately become available to the public. In other words, matrices or trees must not be orphaned but instead must be either the inputs or outputs of explicit analyses.</li>
</ul>

<h2>Preparation of NEXUS files</h2>

<P>
	Data are uploaded to TreeBASE in the NEXUS format, which is 
	used by many popular phylogenetics software packages (e.g., 
	MacClade and Mesquite: Maddison and Maddison, 1992; PAUP: 
	Swofford, 1993). These programs allow other file formats (e.g. 
	Hennig86, PHYLIP, etc.) to be converted into NEXUS.  TreeBASE 
	uses Mesquite to parse incoming data: if your data cannot be 
	parsed by Mesquite then they will not be parsed by TreeBASE, 
	so it's always a good idea to open and then save your data in 
	Mesquite prior to uploading.  To be sure that the data are free 
	of syntax errors, please prepare them with MacClade or Mesquite, 
	and have these programs write the characters and trees in the 
	same NEXUS file. The most frequent error is when taxon labels 
	in the character block do not match perfectly with taxon labels 
	in the trees block. 
</P>

<P>Preparation of taxon labels</P>

<ul>
    <li>Do not abbreviate taxon names (e.g. write "Homo sapiens", not "H. sapiens")</li>
    
    <li>In the NEXUS file, use underscores ("_") to represent spaces</li>
    
    <li>Avoid using quotation marks, brackets, parentheses, commas, colons, and semicolons.</li>
    
    <li>Avoid using codes for non-redundant names (e.g. write "Drosophila melanogaster", not "DMelan45GRX"). However, it is acceptable two write "Drosophila melanogaster 45GRX".</li>
    
    <li>Separate the name of parasites and hosts with "ex.," e.g. "Wolbachia sp. ex. Drosophila melanogaster"</li>
    
</ul>
<hr/>
<p>Please fill in following information to login<br/>
<span class="required">* Required Fields</span></p>

<body id="info"/>

<!-- loginForm.jsp -->
<c:import url="/WEB-INF/pages/loginForm.jsp"/>
<!-- password -->
<p>Forgot your password? Have your <a href="<c:url value="passwordForm.html"/>">password e-mailed to you</a></p>
