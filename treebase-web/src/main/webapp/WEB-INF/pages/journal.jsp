<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>

<div class="gutter">
	<h1>Journals</h1>
	<p>In addition to providing a digital library, TreeBASE serves the
	scientific community by offering journal editors and reviewers special
	advanced anonymous access to submitted data. This access improves the
	quality of the review process because reviewers can inspect and evaluate
	the data, and in turn that also improves the quality of data in
	TreeBASE. Below is a list of journals that recommend or require
	submission to TreeBASE as an integral part of their publication
	policies. We encourage other journals and scientific societies to do the
	same because archiving and sharing of scientific data is critical for
	promoting subsequent reuse of published data, leveraging the cumulative
	knowledge of science, and maximizing the impact of scientific
	publications on future research.</p>
	<table>
		<tr>
			<td valign="top">
			<p><b>Journal Hyperlink</b></p>
			</td>
			<td>
			<p><b>PhyloWS URL to Studies in TreeBASE</b><br/>
			(note: add &quot;&amp;format=rss1&quot; to the end of each URL to use
			it in your favorite RSS reader)</p>
			</td>
		</tr>
<!-- aliso -->
		<tr>
			<td>
				<p><a href="http://www.rsabg.org/research-department/scientific-publications"
					title="Aliso"> 
						<img class="journal"
						src="images/journal_files/aliso.jpg" alt="Aliso"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Aliso%22"
				title="Find records in TreeBASE for articles published in Aliso">
			<%=purlBase%>study/find?query=prism.publicationName==&quotAliso&quot
			</a></p>
			</td>
		</tr>
<!-- american journal of botany -->
		<tr>
			<td>
				<p><a href="http://www.amjbot.org/"
					title="American Journal of Botany"> 
						<img class="journal"
						src="images/journal_files/image022.gif" alt="AJB"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22American%20Journal%20of%20Botany%22"
				title="Find records in TreeBASE for articles published in the American Journal of Botany">
			<%=purlBase%>study/find?query=prism.publicationName==&quotAmerican+Journal+of+Botany&quot
			</a></p>
			</td>
		</tr>
<!-- american naturalist -->
		<tr>
			<td>
				<p><a href="http://www.asnamnat.org/"
					title="American Naturalist"> 
						<img class="journal"
						src="images/journal_files/amnat.gif" alt="American Naturalist"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22American%20Naturalist%22"
				title="Find records in TreeBASE for articles published in the American Naturalist">
			<%=purlBase%>study/find?query=prism.publicationName==&quotAmerican+Naturalist&quot
			</a></p>
			</td>
		</tr>
<!-- basic and applied ecology -->
		<tr>
			<td>
				<p><a href="http://www.elsevier.com/locate/baae"
					title="Basic and Applied Ecology"> 
						<img class="journal"
						src="images/journal_files/baae.jpg" alt="Basic and Applied Ecology"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Basic%20and%20Applied%20Ecology%22"
				title="Find records in TreeBASE for articles published in Basic and Applied Ecology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotBasic+and+Applied+Ecology&quot
			</a></p>
			</td>
		</tr>
<!-- the bryologist -->
		<tr>
			<td>
				<p><a href="http://192.104.39.110/bryologist.html"
					title="The Bryologist"> 
						<img class="journal"
						src="images/journal_files/bry.jpg" alt="Bryologist"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22The%20Bryologist%22"
				title="Find records in TreeBASE for articles published in The Bryologist">
			<%=purlBase%>study/find?query=prism.publicationName==&quotThe+Bryologist&quot
			</a></p>
			</td>
		</tr>
<!-- cryptogamie algologie -->
		<tr>
			<td>
				<p><a href="http://www.cryptogamie.com/pagint_en/editeur/revues.php"
					title="Cryptogamie Algologie"> 
						<img class="journal"
						src="images/journal_files/fd_algo.gif" alt="Cryptogamie Algologie"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Cryptogamie%20Algologie%22"
				title="Find records in TreeBASE for articles published in the Cryptogamie Algologie">
			<%=purlBase%>study/find?query=prism.publicationName==&quotCryptogamie+Algologie&quot
			</a></p>
			</td>
		</tr>
<!-- cryptogamie bryologie -->
		<tr>
			<td>
				<p><a href="http://www.cryptogamie.com/pagint_en/editeur/revue_bryo.php"
					title="Cryptogamie Bryologie"> 
						<img class="journal"
						src="images/journal_files/fd_bryo.gif" alt="Cryptogamie Bryologie"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Cryptogamie%20Bryologie%22"
				title="Find records in TreeBASE for articles published in the Cryptogamie Bryologie">
			<%=purlBase%>study/find?query=prism.publicationName==&quotCryptogamie+Bryologie&quot
			</a></p>
			</td>
		</tr>
<!-- cryptogamie mycologie -->
		<tr>
			<td>
				<p><a href="http://www.cryptogamie.com/pagint_en/editeur/revue_bryo.php"
					title="Cryptogamie Mycologie"> 
						<img class="journal"
						src="images/journal_files/fd_myco.gif" alt="Cryptogamie Mycologie"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Cryptogamie%20Mycologie%22"
				title="Find records in TreeBASE for articles published in the Cryptogamie Mycologie">
			<%=purlBase%>study/find?query=prism.publicationName==&quotCryptogamie+Mycologie&quot
			</a></p>
			</td>
		</tr>
<!-- european journal of plant pathology" -->
		<tr>
			<td>
				<p><a href="http://www.springer.com/life+sciences/plant+sciences/journal/10658"
					title=""European Journal of Plant Pathology"> 
						<img class="journal"
						src="images/journal_files/ejpp.gif" alt="European Journal of Plant Pathology"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22European%20Journal%20of%20Plant%20Pathology%22"
				title="Find records in TreeBASE for articles published in the European Journal of Plant Pathology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotEuropean+Journal+of+Plant+Pathology&quot
			</a></p>
			</td>
		</tr>
<!-- evolution -->
		<tr>
			<td>
				<p><a href="http://www.wiley.com/bw/journal.asp?ref=0014-3820"
					title="Evolution"> 
						<img class="journal"
						src="images/journal_files/image001.gif" alt="Evolution"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DEvolution"
				title="Find records in TreeBASE for articles published in Evolution">
			<%=purlBase%>study/find?query=prism.publicationName==Evolution
			</a></p>
			</td>
		</tr>
<!-- evolutionary applications -->
		<tr>
			<td>
				<p>
					<a
						href="http://www3.interscience.wiley.com/journal/119423602/home">
						<img class="journal"
							src="images/journal_files/image002.jpg" alt="Evolutionary Applications"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Evolutionary+Applications%22"
				title="Find records in TreeBASE for articles published in Evolutionary Applications">
			<%=purlBase%>study/find?query=prism.publicationName==&quotEvolutionary+Applications&quot
			</a></p>
			</td>
		</tr>
<!-- flora -->
		<tr>
			<td>
				<p><a href="http://www.elsevier.com/locate/flora"
					title="Flora - Morphology, Distribution, Functional Ecology of Plants"> 
						<img class="journal"
						src="images/journal_files/flora.jpg" alt="Flora"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Flora%22"
				title="Find records in TreeBASE for articles published in Flora">
			<%=purlBase%>study/find?query=prism.publicationName==&quotFlora&quot
			</a></p>
			</td>
		</tr>
<!-- fungal biology -->
		<tr>
			<td>
				<p>
					<a
						href="http://www.elsevier.com/wps/find/journaldescription.cws_home/720691/description#description">
						<img class="journal"
						src="images/journal_files/image003.jpg" alt="Fungal Biology"/>
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Fungal+Biology%22"
				title="Find records in TreeBASE for articles published in Fungal Biology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotFungal+Biology&quot
			</a></p>
			</td>
		</tr>
<!-- ima fungus -->
		<tr>
			<td>
				<p>
					<a
						href="http://www.imafungus.org/">
						<img class="journal"
						src="images/journal_files/ima.jpg" alt="IMA Fungus"/>
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22IMA+Fungus%22"
				title="Find records in TreeBASE for articles published in IMA Fungus">
			<%=purlBase%>study/find?query=prism.publicationName==&quotIMA+Fungus&quot
			</a></p>
			</td>
		</tr>
<!-- invertebrate systmatics -->
		<tr>
			<td>
				<p>
					<a href="http://www.publish.csiro.au/nid/120.htm">
						<img class="journal"
							src="images/journal_files/image004.gif"
							alt="Invertebrate Systematics" />
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Invertebrate+Systematics%22"
				title="Find records in TreeBASE for articles published in Invertebrate Systematics">
			<%=purlBase%>study/find?query=prism.publicationName==&quotInvertebrate+Systematics&quot
			</a></p>
			</td>
		</tr>
<!-- journal of fish and wildlife management -->
		<tr>
			<td>
				<p>
					<a href="http://www.fws.gov/science/jfwm.html">
						<img class="journal"
							src="images/journal_files/jfwm.jpg"
							alt="Journal of Fish and Wildlife Management" />
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Journal+of+Fish+and+Wildlife+Management%22"
				title="Find records in TreeBASE for articles published in Journal of Fish and Wildlife Management">
			<%=purlBase%>study/find?query=prism.publicationName==&quotJournal+of+Fish+and+Wildlife+Management&quot
			</a></p>
			</td>
		</tr>
<!-- journal of heredity -->
		<tr>
			<td>
				<p><a href="http://jhered.oxfordjournals.org/"
					title="Journal of Heredity"> 
						<img class="journal"
						src="images/journal_files/jh.gif" alt="Journal of Heredity"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Journal%20of%20Heredity%22"
				title="Find records in TreeBASE for articles published in the Journal of Heredity">
			<%=purlBase%>study/find?query=prism.publicationName==&quotJournal+of+Heredity&quot
			</a></p>
			</td>
		</tr>
<!-- molecular ecology -->
		<tr>
			<td>
			<p><a href="http://www.blackwellpublishing.com/journal.asp?ref=0962-1083"> 
			<img class="journal" src="images/journal_files/image021.jpg" alt="Molecular Ecology" /></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Molecular+Ecology%22"
				title="Find records in TreeBASE for articles published in Molecular Ecology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotMolecular+Ecology&quot</a>
			</p>
			</td>
		</tr>
<!-- molecular ecology resources -->
		<tr>
			<td>
				<p><a href="http://www.wiley.com/bw/journal.asp?ref=1755-098X"
					title="Molecular Ecology Resources"> 
						<img class="journal"
						src="images/journal_files/mer.gif" alt="Molecular Ecology Resources"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Molecular%20Ecology%20Resources%22"
				title="Find records in TreeBASE for articles published in the Molecular Ecology Resources">
			<%=purlBase%>study/find?query=prism.publicationName==Molecular+&quotEcology+Resources&quot
			</a></p>
			</td>
		</tr>
<!-- mycologia -->
		<tr>
			<td>
			<p><a href="http://www.msafungi.org/"> 
			<img class="journal" src="images/journal_files/image005.gif" alt="Mycologia" /></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DMycologia"
				title="Find records in TreeBASE for articles published in Mycologia">
			<%=purlBase%>study/find?query=prism.publicationName==Mycologia</a>
			</p>
			</td>
		</tr>
<!-- mycological progress -->
		<tr>
			<td>
			<p><a
				href="http://www.springer.com/life+sciences/plant+sciences/journal/11557">
			<img class="journal"
				src="images/journal_files/image006.jpg" alt="Mycologial Progress"/> </a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Mycologial%20Progress%22"
				title="Find records in TreeBASE for articles published in Mycologial Progress">
			<%=purlBase%>study/find?query=prism.publicationName==&quotMycologial+Progress&quot</a>
			</p>
			</td>
		</tr>
<!-- mycological research -->
		<tr>
			<td>
			<p><a
				href="http://www.elsevier.com/wps/find/journaldescription.cws_home/707043/description#description">
			<img class="journal"
				src="images/journal_files/image007.gif" alt="Mycologial Research"/>
			</a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Mycologial%20Research%22"
				title="Find records in TreeBASE for articles published in Mycologial Research">
			<%=purlBase%>study/find?query=prism.publicationName==&quotMycologial+Research&quot</a></p>
			</td>
		</tr>
<!-- mycology -->
		<tr>
			<td>
			<p><a
				href="http://www.tandf.co.uk/journals/journal.asp?issn=2150-1203">
			<img class="journal"
				src="images/journal_files/myc.jpg" alt="Mycology"/>
			</a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Mycology%22"
				title="Find records in TreeBASE for articles published in Mycology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotMycology&quot</a></p>
			</td>
		</tr>
<!-- mycoscience -->
		<tr>
			<td>
			<p><a
				href="http://www.springer.com/life+sciences/microbiology/journal/10267">
			<img class="journal"
				src="images/journal_files/image008.gif" alt="Mycoscience"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DMycoscience"
				title="Find records in TreeBASE for articles published in Mycoscience">
			<%=purlBase%>study/find?query=prism.publicationName==Mycoscience</a></p>
			</td>
		</tr>
<!-- mycosphere -->
		<tr>
			<td>
			<p><a href="http://www.mycosphere.org/">
				<img class="journal" src="images/journal_files/image009.jpg" alt="Mycosphere"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DMycosphere"
				title="Find records in TreeBASE for articles published in Mycosphere">
			<%=purlBase%>study/find?query=prism.publicationName==Mycosphere</a></p>
			</td>
		</tr>
<!-- north american fauna -->
		<tr>
			<td>
			<p><a
				href="http://www.fws.gov/science/naf.html">
			<img class="journal"
				src="images/journal_files/naf.gif"
				alt="North American Fauna"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22North%20American%20Fauna%22"
				title="Find records in TreeBASE for articles published in North American Fauna">
			<%=purlBase%>study/find?query=prism.publicationName==&quotNorth+American+Fauna&quot</a></p>
			</td>
		</tr>
<!-- organisms, diversity, and evolution -->
		<tr>
			<td>
			<p><a
				href="http://www.elsevier.com/wps/find/journaldescription.cws_home/701789/description#description">
			<img class="journal"
				src="images/journal_files/image010.gif"
				alt="Organisms, Diversity, and Evolution"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Organisms%20Diversity%20&amp;%20Evolution%22"
				title="Find records in TreeBASE for articles published in Organisms Diversity &amp; Evolution">
			<%=purlBase%>study/find?query=prism.publicationName==&quotOrganisms+Diversity+&amp;+Evolution&quot</a></p>
			</td>
		</tr>
<!-- persoonia -->
		<tr>
			<td>
			<p><a href="http://www.persoonia.org/">
			<img class="journal" src="images/journal_files/image011.jpg" alt="Persoonia"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DPersoonia"
				title="Find records in TreeBASE for articles published in Persoonia">
			<%=purlBase%>study/find?query=prism.publicationName==Persoonia</a></p>
			</td>
		</tr>
<!-- perspectives in ecology, evolution and systematics -->
		<tr>
			<td>
				<p><a href="http://www.elsevier.com/locate/ppees"
					title="Perspectives in Ecology, Evolution and Systematics"> 
						<img class="journal"
						src="images/journal_files/ppees.jpg" alt="Perspectives in Ecology, Evolution and Systematics"/> 
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Perspectives%20in%20Ecology,%20Evolution%20and%20Systematics%22"
				title="Find records in TreeBASE for articles published in Perspectives in Ecology, Evolution and Systematics">
			<%=purlBase%>study/find?query=prism.publicationName==&quotPerspectives+in+Ecology,+Evolution+and+Systematics&quot
			</a></p>
			</td>
		</tr>
<!-- phytopathology -->
		<tr>
			<td>
				<p>
					<a href="http://apsjournals.apsnet.org/loi/phyto?cookieSet=1"> 
						<img 
							class="journal"
							src="images/journal_files/image012.jpg" 
							alt="Phytopathology"/>
					</a>
				</p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DPhytopathology"
				title="Find records in TreeBASE for articles published in Phytopathology">
			<%=purlBase%>study/find?query=prism.publicationName==Phytopathology</a></p>
			</td>
		</tr>
<!-- plant disease -->
		<tr>
			<td>
			<p><a href="http://www.apsnet.org/pd/">
			<img class="journal" src="images/journal_files/image013.jpg" alt="Plant Disease"/>
			</a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Plant%20Disease%22"
				title="Find records in TreeBASE for articles published in Plant Disease">
			<%=purlBase%>study/find?query=prism.publicationName==&quotPlant+Disease&quot</a></p>
			</td>
		</tr>
<!-- plos currents tol -->
		<tr>
			<td>
			<p><a href="http://currents.plos.org/treeoflife"><img
				class="journal"
				src="images/journal_files/image020.jpg" alt="PLoS Currents ToL"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22PLoS%20Currents:%20Tree%20of%20Life%22"
				title="Find records in TreeBASE for articles published in PLoS Currents: Tree of Life">
			<%=purlBase%>study/find?query=prism.publicationName==&quotPLoS+Currents:+Tree+of+Life&quot</a></p>
			</td>
		</tr>
<!-- rhodora -->
		<tr>
			<td>
			<p><a href="http://www.rhodora.org/Rhodora.html"><img
				class="journal"
				src="images/journal_files/image014.gif" alt="Rhodora"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DRhodora"
				title="Find records in TreeBASE for articles published in Rhodora">
			<%=purlBase%>study/find?query=prism.publicationName==Rhodora</a></p>
			</td>
		</tr>
<!-- muelleria -->
		<tr>
			<td>
			<p><a
				href="http://www.rbg.vic.gov.au/science/information-and-resources/science-publications/muelleria">
			<img class="journal"
				src="images/journal_files/image015.jpg" alt="Muelleria"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DMuelleria"
				title="Find records in TreeBASE for articles published in Muelleria">
			<%=purlBase%>study/find?query=prism.publicationName==Muelleria</a></p>
			</td>
		</tr>
<!-- studies in mycology -->
		<tr>
			<td>
			<p><a href="http://www.studiesinmycology.org/">
			<img class="journal" src="images/journal_files/image016.gif"
				alt="Studies in Mycology"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Studies%20in%20Mycology%22"
				title="Find records in TreeBASE for articles published in Studies in Mycology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotStudies+in+Mycology&quot</a></p>
			</td>
		</tr>
<!-- systematic biology -->
		<tr>
			<td>
			<p><a href="http://systbiol.org/">
			<img class="journal" src="images/journal_files/image017.gif" alt="Systematic Biology"/>
			</a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Systematic%20Biology%22"
				title="Find records in TreeBASE for articles published in Systematic Biology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotSystematic+Biology&quot</a></p>
			</td>
		</tr>
<!-- systematic botany -->
		<tr>
			<td>
			<p><a href="http://www.sysbot.org/">
			<img class="journal" src="images/journal_files/image018.gif"
				alt="Systematic Botany"/></a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Systematic%20Botany%22"
				title="Find records in TreeBASE for articles published in Systematic Botany">
			<%=purlBase%>study/find?query=prism.publicationName==&quotSystematic+Botany&quot</a></p>
			</td>
		</tr>
<!-- tropical bryology -->
		<tr>
			<td>
			<p><a href="http://tropical-bryology.org/">
			<img class="journal" src="images/journal_files/image019.jpg" alt="Tropical Bryology"/>
			</a></p>
			</td>
			<td>
			<p><a
				href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Tropical%20Bryology%22"
				title="Find records in TreeBASE for articles published in Tropical Bryology">
			<%=purlBase%>study/find?query=prism.publicationName==&quotTropical+Bryology&quot</a></p>
			</td>
		</tr>
	</table>
	
	<p><b>Other Journals with a Significant Presence in TreeBASE</b>: <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Annals%20of%20the%20Missouri%20Botanical%20Garden%22"
		title="Annals of the Missouri Botanical Garden">Annals of the
	Missouri Botanical Garden</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Biological+Journal+of+the+Linnean+Society%22"
		title="Biological Journal of the Linnean Society">Biological
	Journal of the Linnean Society</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22BMC+Evolutionary+Biology%22"
		title="BMC Evolutionary Biology">BMC Evolutionary Biology</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Canadian+Journal+of+Botany%22"
		title="Canadian Journal of Botany">Canadian Journal of Botany</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DCladistics"
		title="Cladistics">Cladistics</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Fungal+Diversity%22"
		title="Fungal Diversity">Fungal Diversity</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22International+Journal+of+Plant+Sciences%22"
		title="International Journal of Plant Sciences">International
	Journal of Plant Sciences</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Journal+of+Phycology%22"
		title="Journal of Phycology">Journal of Phycology</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Molecular+Biology+and+Evolution%22"
		title="Molecular Biology and Evolution">Molecular Biology and
	Evolution</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Molecular+Phylogenetics+and+Evolution%22"
		title="Molecular Phylogenetics and Evolution">Molecular
	Phylogenetics and Evolution</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Plant+Systematics+and+Evolution%22"
		title="Plant Systematics and Evolution">Plant Systematics and
	Evolution</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Proceeding+of+the+National+Academy+of+Sciences+of+the+United+States+of+America%22"
		title="Proceeding of the National Academy of Sciences of the United States of America">
	Proceeding of the National Academy of Sciences of the United States of
	America</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3D%22Proceedings+of+the+Royal+Society+B%22"
		title="Proceedings of the Royal Society B">Proceedings of the Royal
	Society B</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DTaxon"
		title="Taxon">Taxon</a>; <a
		href="<%=purlBase%>study/find?query=prism.publicationName%3D%3DZootaxa"
		title="Zootaxa">Zootaxa</a></p>

</div>