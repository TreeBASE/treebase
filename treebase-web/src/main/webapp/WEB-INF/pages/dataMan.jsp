<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>

<div class="gutter">

<h1>NSF Data Management Plan</h1>
<p>To foster the sharing and dissemination of data produced by sponsored research, the National Science Foundation requires a data management plan for all proposals. The kinds of data that must be shared generally include whatever the scientific community needs to validate research findings. In particular, researchers must present a plan to share (1) analyzed data,  (2) metadata that provide provenance information, and (3) metadata that describe how the data were generated. </p>
<h2>Data Submission and Storage</h2>
<p>For phylogenetics, the three kinds of data mentioned above and required by NSF are all accepted by TreeBASE, whether submitted directly to TreeBASE or indirectly by way of <a href="http://datadryad.org/" target="_blank">Dryad</a>. For data type (1), we accept NEXUS formatted data with characters of datatype standard, continuous, DNA, RNA, and protein, and non-reticulating phylogenetic trees with branch lengths and clade support values. For metadata type (2) we parse and store morphological character labels and state labels in submitted NEXUS files and we map taxon labels to NCBI and uBio external taxonomies. Additionally, we accept the following metadata: museum specimen numbers in accordance with the Registry of Biological Repositories (<a href="http://www.biorepositories.org" target="_blank">RBR</a>), Genbank accession numbers, other accession numbers, and Darwin Core compatible specimen metadata: collecting date, collector, latitude/longitude, elevation, country, state, and locality. For metadata type (3), we store and share the original uploaded NEXUS files (including any program-specific command blocks that can define substitution models and search parameters) as well as provide data entry fields to describe software, algorithm, and commands used. TreeBASE only shares data that are linked to a manuscript that is accepted by a peer reviewed publication (e.g. journal article, reviewed book or book section, or academic thesis approved by a thesis committee). </p>
<h2>Data Integrity and Verification</h2>
<p>TreeBASE helps to certify data integrity by: </p>
<ul>
  <li>Validating submitted NEXUS files by parsing them with  <a href="http://mesquiteproject.org/" target="_blank">Mesquite</a> on the TreeBASE server</li>
  <li>Verifying that taxon labels in matrices and relevant trees are consistent</li>
  <li>Verifying that data objects are not 'orphaned' (i.e. unlinked to an analysis)</li>
  <li>Verifying that taxon labels are recognizable by biologists, spelled correctly, and mapped to external taxonomies whenever possible</li>
</ul>
<p>TreeBASE provides an advanced access URL for anonymous reviewers and referees to provide additional quality control before the data are made public. Although additional NSF requirements relating to provenance and how data were generated are not normally required or scrutinized by TreeBASE staff, submitters who flag their submission (in the submission notes section) as NSF-sponsored data will receive special attention by our staff. In these cases, TreeBASE staff will check to make sure that provenance and analysis metadata are adequately provided, and, as needed, communicate with the submitter and assist in properly formatting and ingesting these data. </p>
<h2>Data Standards and Dissemination</h2>
<p>TreeBASE plans to remain in compliance with the emerging, but still evolving, standard of Minimal Information for a Phylogenetic Analysis (<a href="http://www.nescent.org/sites/evoio/MIAPA" target="_blank">MIAPA</a>). In addition, TreeBASE publishes persistant and resolvable globally unique identifiers (GUIDs) for all major data objects and disseminates data and metadata using commonly accepted standards. A Restful <a href="http://www.nescent.org/wg/evoinfo/index.php?title=PhyloWS" target="_blank">PhyloWS</a>  API exposes metadata using RSS feeds in RDF; a <a href="http://www.nexml.org/" target="_blank">NeXML</a>  serialization exposes data marked up with metadata using published vocabularies and fully qualified URIs in compliance with <a href="http://linkeddata.org/" target="_blank">Linked Data</a>  standards. Basic record metadata are published through an OIA-PMH service, and TreeBASE records are mirrored by Dryad, which provides a secondary Dryad <a href="http://www.datacite.org" target="_blank">DataCite DOI</a>. However, for most people in the scientific community, data will be retrieved using the web user interface and downloaded in the NEXUS format, while metadata can be downloaded separately in a tab-separated text format. </p>
<h2>Data Persistence</h2>
<p>Although no data service can guarantee indefinite persistence, TreeBASE will make every effort to preserve its services as long as possible. Additionally, the Articles of Incorporation of the <a href="http://www.phylofoundation.org" target="_blank">Phyloinformatics Research Foundation</a>, which oversees TreeBASE activities, specify that if dissolution is ever required the assets will be transferred to a similar entity with a comparable mission. </p>
<h2>Preparing a Data Management Plan for NSF</h2>
<p>Scientists are welcome to designate TreeBASE as their selected repository and dissemination service for phylogenetic data generated by sponsored research. In their Data Management Plan, we suggest that the following  be mentioned:</p>
<ul>
  <li>Specify the name(s) of  person(s) responsible for preparing the data matrices, trees, and metadata for submission to TreeBASE.</li>
  <li>Identify the kinds of data that will be submitted, including provenance and analysis metadata as outlined above. For metadata not accepted by TreeBASE (e.g. digital images of specimens), identify other repositories where these will be stored (e.g. <a href="http://www.morphbank.net/" target="_blank">Morphbank</a> or <a href="http://www.morphobank.org/" target="_blank">Morphobank</a>), and indicate how  entities in TreeBASE  and in the other repository will be linked (e.g. using shared specimen catalog numbers). </li>
  <li>For data standards, you can state that your data will be serialized using TreeBASE's data formats: NEXUS, for character and tree data alone, and NeXML for character and tree data  with additional metadata (e.g. basic Dublin Core publication data,  Darwin Core specimen information, RBR collection codes and catalog numbers, uBio and NCBI taxon identification numbers, and Genbank accession numbers).</li>
  <li>Provide an overview of access and sharing. For your TreeBASE-submitted data, you can state that TreeBASE makes all data and metadata freely available to the public once the manuscript under review has been accepted by a peer-review publication. TreeBASE will allow  data embargo periods according to the policies of the journal, but once data are public they are assumed to be released to the public domain without any restrictions on reuse. We recommend that you state that you will provide TreeBASE's resolvable globally unique identifiers (GUIDs) for your deposited data in future progress reports to NSF, in relevant publications, and in your lab's web page. </li>
  <li>State that you will flag your submissions to TreeBASE as data subject to your data management plan so as to receive special attention by TreeBASE staff to help ensure that the data are richly annotated and fully compliant for  reuse in accordance with community standards in phylogenetics. </li>
</ul>
<p>TreeBASE suggests that for each submission of data from sponsored research you contribute at least $100 towards defraying the costs of storage and dissemination, as well as in support of the additional scrutiny by TreeBASE staff for NSF data management compliance. This fee is collected by the Phyloinformatics Research Foundation, which oversees TreeBASE activities. Anticipated costs can be budgeted under publication expenses in your grant proposal's budget. </p>
<hr /></p>
<table width="100%" border="0">
  <tr>
    <td width="50%" valign="top">Data storage fee for submissions resulting from sponsored research where TreeBASE provides added validation to help with data preparation and to ensure compliance with NSF directives:</td>
    <td width="50%" valign="top">Alternatively, for sponsored research that had not budgeted for data sharing with TreeBASE, please consider making a voluntary donation:</td>
  </tr>
  <tr>
    <td><form target="paypal" action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="DMT5Y327LZMDA">
<table>
<tr><td><input type="hidden" name="on0" value="TreeBASE Study URI:">TreeBASE Study URI:</td></tr><tr><td><input type="text" name="os0" maxlength="60"></td></tr>
</table>
<input type="image" src="images/paypalsub.jpg" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/scr/pixel.gif" width="1" height="1">
</form></td>
    <td><form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="W9YAU65RHGZRC">
<input type="image" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/btn/btn_donate_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/scr/pixel.gif" width="1" height="1">
</form></td>
  </tr>
</table>


</div>