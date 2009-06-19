package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;
import org.nexml.model.Matrix;
import org.nexml.model.OTUs;
import org.nexml.model.TreeBlock;

public class NexmlDocumentConverter extends NexmlObjectConverter {

	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 * @param document
	 */
	public NexmlDocumentConverter(Study study, TaxonLabelHome taxonLabelHome,
			Document document) {
		super(study, taxonLabelHome, document);
	}
	
	/**
	 * 
	 * @param pDocument
	 * @return
	 */
	public NexusDataSet fromXmlToTreeBase(Document pDocument) {
		NexusDataSet nexusDataSet = new NexusDataSet();
		nexusDataSet.setNexmlProject(pDocument);
		
		NexmlOTUConverter noc = new NexmlOTUConverter(getStudy(),getTaxonLabelHome(),pDocument);
		for ( OTUs xmlOTUs : pDocument.getOTUsList() ) {
			TaxonLabelSet taxonLabelSet = noc.fromXmlToTreeBase(xmlOTUs);
			nexusDataSet.addTaxonLabelSet(xmlOTUs, taxonLabelSet);
		}
		
		NexmlMatrixConverter nmc = new NexmlMatrixConverter(getStudy(),getTaxonLabelHome(),pDocument);
		for ( Matrix<?> xmlMatrix : pDocument.getMatrices() ) {
			org.cipres.treebase.domain.matrix.Matrix matrix = nmc.fromXmlToTreeBase(xmlMatrix);
			nexusDataSet.getMatrices().add(matrix);
		}
		
		NexmlTreeBlockConverter ntc = new NexmlTreeBlockConverter(getStudy(),getTaxonLabelHome(),pDocument);
		for ( TreeBlock xmlTreeBlock : pDocument.getTreeBlockList() ) {
			org.cipres.treebase.domain.tree.TreeBlock treeBlock = ntc.fromXmlToTreeBase(xmlTreeBlock);
			nexusDataSet.getTreeBlocks().add(treeBlock);
		}
		
		return nexusDataSet;		
	}
	
	/**
	 * 
	 * @param pNexusDataSet
	 * @return
	 */
	public Document fromTreeBaseToXml(NexusDataSet pNexusDataSet) {
		
		NexmlOTUConverter noc = new NexmlOTUConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pNexusDataSet.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixConverter nmc = new NexmlMatrixConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pNexusDataSet.getMatrices() ) {
			nmc.fromTreeBaseToXml(matrix);
		}
		
		NexmlTreeBlockConverter ntc = new NexmlTreeBlockConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pNexusDataSet.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}
		
		return getDocument();
		
	}
	
	/**
	 * 
	 * @param citation
	 * @param document
	 */
	private void copyCitationMetadata(Citation citation,Document document) {
		attachAnnotation("prism:publicationDate",citation.getPublishYear().toString(),mPrismURI,document);
		attachAnnotation("prism:doi",citation.getDoi(),mPrismURI,document);
		String[] pages = citation.getPages().split("-");
		if ( pages.length > 2 ) {
			attachAnnotation("prism:startingPage",pages[0],mPrismURI,document);
			attachAnnotation("prism:endingPage",pages[1],mPrismURI,document);
			attachAnnotation("prism:pageRange",citation.getPages(),mPrismURI,document);			
		}
		String[] keywords = citation.getKeywords().split(", ");
		for ( int i = 0; i < keywords.length; i++ ) {
			attachAnnotation("prism:keyword",keywords[i],mPrismURI,document);
		}		
		if ( citation instanceof ArticleCitation ) {
			attachAnnotation("prism:publicationName",((ArticleCitation)citation).getJournal(),mPrismURI,document);
			attachAnnotation("prism:volume",((ArticleCitation)citation).getVolume(),mPrismURI,document);
			attachAnnotation("prism:number",((ArticleCitation)citation).getIssue(),mPrismURI,document);
		}		
	}
	
	/**
	 * 
	 * @param pStudy
	 * @return
	 */
	public Document fromTreeBaseToXml(Study pStudy) {
		attachTreeBaseID(getDocument(), pStudy);
		attachAnnotation("dc:title", pStudy.getName(), mDCURI, getDocument());
		attachAnnotation("dc:abstract",pStudy.getCitation().getAbstract(), mDCURI,getDocument());		
		attachAnnotation(
				"dc:creator",
				pStudy.getSubmission().getSubmitter().getPerson().getFullNameCitationStyle(),
				mDCURI,
				getDocument()
		);
		for ( Person person : pStudy.getAuthors() ) {
			String personName = person.getFullNameCitationStyle();
			attachAnnotation("dc:contributor",personName,mDCURI,getDocument());
		}
		attachAnnotation("prism:creationDate",pStudy.getSubmission().getCreateDate().toString(),mPrismURI,getDocument());
		attachAnnotation("prism:embargoDate",pStudy.getReleaseDate().toString(),mPrismURI,getDocument());
		copyCitationMetadata(pStudy.getCitation(),getDocument());		
		
		NexmlOTUConverter noc = new NexmlOTUConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pStudy.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixConverter nmc = new NexmlMatrixConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pStudy.getMatrices() ) {
			nmc.fromTreeBaseToXml(matrix);
		}
		
		NexmlTreeBlockConverter ntc = new NexmlTreeBlockConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pStudy.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}		
		return getDocument();
	}

}
