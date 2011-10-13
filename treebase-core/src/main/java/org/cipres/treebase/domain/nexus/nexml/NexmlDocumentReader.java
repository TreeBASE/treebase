package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;
import org.nexml.model.Matrix;
import org.nexml.model.OTUs;
import org.nexml.model.TreeBlock;

public class NexmlDocumentReader extends NexmlObjectConverter {

	public NexmlDocumentReader(Study study, TaxonLabelHome taxonLabelHome,
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
		
		NexmlOTUReader noc = new NexmlOTUReader(getStudy(),getTaxonLabelHome(),pDocument);
		for ( OTUs xmlOTUs : pDocument.getOTUsList() ) {
			TaxonLabelSet taxonLabelSet = noc.fromXmlToTreeBase(xmlOTUs);
			nexusDataSet.addTaxonLabelSet(xmlOTUs, taxonLabelSet);
		}
		
		NexmlMatrixReader nmc = new NexmlMatrixReader(getStudy(),getTaxonLabelHome(),pDocument);
		for ( Matrix<?> xmlMatrix : pDocument.getMatrices() ) {
			org.cipres.treebase.domain.matrix.Matrix matrix = nmc.fromXmlToTreeBase(xmlMatrix);
			nexusDataSet.getMatrices().add(matrix);
		}
		
		NexmlTreeBlockReader ntc = new NexmlTreeBlockReader(getStudy(),getTaxonLabelHome(),pDocument);
		for ( TreeBlock xmlTreeBlock : pDocument.getTreeBlockList() ) {
			org.cipres.treebase.domain.tree.TreeBlock treeBlock = ntc.fromXmlToTreeBase(xmlTreeBlock);
			nexusDataSet.getTreeBlocks().add(treeBlock);
		}
		
		return nexusDataSet;		
	}	
	
}
