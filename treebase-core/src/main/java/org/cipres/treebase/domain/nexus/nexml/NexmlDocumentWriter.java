package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;

public class NexmlDocumentWriter extends NexmlObjectConverter {

	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 * @param document
	 */
	public NexmlDocumentWriter(Study study, TaxonLabelHome taxonLabelHome,
			Document document) {
		super(study, taxonLabelHome, document);
	}
	
	public NexmlDocumentWriter(Study study, TaxonLabelHome taxonLabelHome,
			Document document,String baseURI) {
		super(study, taxonLabelHome, document,baseURI);
	}
	
	/**
	 * 
	 * @param pNexusDataSet
	 * @return
	 */
	public Document fromTreeBaseToXml(NexusDataSet pNexusDataSet) {
		
		NexmlOTUWriter noc = new NexmlOTUWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pNexusDataSet.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pNexusDataSet.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix);
			}
		}
		
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(getStudy(),getTaxonLabelHome(),getDocument());
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
		getDocument().addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		
		NexmlOTUWriter noc = new NexmlOTUWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pStudy.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pStudy.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix);
			}
		}
		
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pStudy.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}		
		
		return getDocument();
	}

}
