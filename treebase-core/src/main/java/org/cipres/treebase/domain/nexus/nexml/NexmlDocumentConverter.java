package org.cipres.treebase.domain.nexus.nexml;

//import org.cipres.treebase.Constants;
//import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.NexusDataSet;
//import org.cipres.treebase.domain.study.ArticleCitation;
//import org.cipres.treebase.domain.study.Citation;
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
	 * @param pStudy
	 * @return
	 */
	public Document fromTreeBaseToXml(Study pStudy) {
		attachTreeBaseID(getDocument(), pStudy,Study.class);
		getDocument().addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using NexmlDocumentConverter $Rev$");
		
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

}
