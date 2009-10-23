package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
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
	
	public NexmlDocumentConverter(Study study, TaxonLabelHome taxonLabelHome,
			Document document,String baseURI) {
		super(study, taxonLabelHome, document,baseURI);
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
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix);
			}
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
		attachAnnotation("dcterms.bibliographicCitation",citation.getAuthorsCitationStyleWithoutHtml(),mDCURI,document);
		if ( null != citation.getTitle() ) {
			attachAnnotation("tb:title.study",citation.getTitle(),mTBTermsURI,document);
		}
		if ( null != citation.getPublishYear() ) {
			attachAnnotation("prism:publicationDate",citation.getPublishYear().toString(),mPrismURI,document);
		}
		if ( null != citation.getDoi() ) {
			attachAnnotation("prism:doi",citation.getDoi(),mPrismURI,document);
		}
		if ( null != citation.getPages() ) {
			String[] pages = citation.getPages().split("\\-");
			if ( pages.length == 2 ) {
				attachAnnotation("prism:startingPage",pages[0],mPrismURI,document);
				attachAnnotation("prism:endingPage",pages[1],mPrismURI,document);
				attachAnnotation("prism:pageRange",citation.getPages(),mPrismURI,document);			
			}
		}
		if ( null != citation.getKeywords() ) {
			String[] keywords = citation.getKeywords().split(", ");
			for ( int i = 0; i < keywords.length; i++ ) {
				attachAnnotation("dcterms:subject",keywords[i],mDCURI,document);
			}		
		}
		if ( citation instanceof ArticleCitation ) {
			ArticleCitation ac = (ArticleCitation)citation;
			if ( null != ac.getJournal() ) {
				attachAnnotation("prism:publicationName",ac.getJournal(),mPrismURI,document);
			}
			if ( null != ac.getVolume() ) {
				attachAnnotation("prism:volume",ac.getVolume(),mPrismURI,document);
			}
			if ( null != ac.getIssue() ) {
				attachAnnotation("prism:number",ac.getIssue(),mPrismURI,document);
			}
		}		
	}
	
	/**
	 * 
	 * @param pStudy
	 * @return
	 */
	public Document fromTreeBaseToXml(Study pStudy) {
		copyMetadata(pStudy);		
		
		NexmlOTUConverter noc = new NexmlOTUConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pStudy.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixConverter nmc = new NexmlMatrixConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pStudy.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix);
			}
		}
		
		NexmlTreeBlockConverter ntc = new NexmlTreeBlockConverter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pStudy.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}		
		
		return getDocument();
	}

	private void copyMetadata(Study pStudy) {
		attachTreeBaseID(getDocument(), pStudy,Study.class);
		attachAnnotation("tb:identifier.study",pStudy.getId().toString(), mTBTermsURI, getDocument());
		attachAnnotation("tb:identifier.study.tb1",pStudy.getTB1StudyID(), mTBTermsURI, getDocument());
		if ( null != pStudy.getName() ) {
			attachAnnotation("tb:title.study", pStudy.getName(), mTBTermsURI, getDocument());
		}		
		for ( Person person : pStudy.getAuthors() ) {
			String personName = person.getFullNameCitationStyle();
			attachAnnotation("dcterms:contributor",personName,mDCURI,getDocument());
		}
		if ( null != pStudy.getReleaseDate() ) {
			attachAnnotation("prism:embargoDate",pStudy.getReleaseDate().toString(),mPrismURI,getDocument());
		}
		if ( null != pStudy.getSubmission() ) {
			if ( null != pStudy.getSubmission().getSubmitter() ) {
				attachAnnotation(
						"dcterms:creator",
						pStudy.getSubmission().getSubmitter().getPerson().getFullNameCitationStyle(),
						mDCURI,
						getDocument()
				);		
			}
			if ( null != pStudy.getSubmission().getCreateDate() ) {
				attachAnnotation("prism:creationDate",pStudy.getSubmission().getCreateDate().toString(),mPrismURI,getDocument());
			}
		}		
		if ( null != pStudy.getCitation() ) {			
			//attachAnnotation("dc:abstract",forXML(pStudy.getCitation().getAbstract()), mDCURI,getDocument());
			copyCitationMetadata(pStudy.getCitation(),getDocument());
		}
	}

}
