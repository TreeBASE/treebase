package org.cipres.treebase.domain.nexus.nexml;

import java.io.File;
import java.util.Collection;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.nexus.AbstractNexusConverter;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusParserConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.event.ProgressionListener;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.OTUs;

public class NexmlConverter extends AbstractNexusConverter implements
		NexusParserConverter {

	/**
	 * 
	 */
	public void processLoadFile(Collection<File> nexusFiles, Study study,
			NexusDataSet dataSet, ProgressionListener listener) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @param pFile
	 * @param pStudy
	 * @param pDataSet
	 */
	public void parseOneFile(File pFile, Study pStudy, NexusDataSet pDataSet) {
		Document document = null;
		try {
			document = DocumentFactory.parse(pFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Taxa
		NexmlOTUReader noc = new NexmlOTUReader(pStudy,getTaxonLabelHome(),document);
		for ( OTUs xmlOTUs : document.getOTUsList() ) {
			pDataSet.addTaxonLabelSet(xmlOTUs, noc.fromXmlToTreeBase(xmlOTUs));
		}
	
		// Matrices
		NexmlMatrixReader nmc = new NexmlMatrixReader(pStudy,getTaxonLabelHome(),document);
		for ( org.nexml.model.Matrix<?> xmlMatrix : document.getMatrices() ) {
			Matrix tbMatrix = nmc.fromXmlToTreeBase(xmlMatrix);
			tbMatrix.setNexusFileName(pFile.getName());
			pDataSet.getMatrices().add(tbMatrix);
		}		
		
		// Trees
		NexmlTreeBlockReader ntbc = new NexmlTreeBlockReader(pStudy,getTaxonLabelHome(),document);
		for ( org.nexml.model.TreeBlock xmlTreeBlock : document.getTreeBlockList() ) {
			TreeBlock tbTreeBlock = ntbc.fromXmlToTreeBase(xmlTreeBlock);
			for ( PhyloTree phyloTree : tbTreeBlock.getTreeList() ) {
				phyloTree.setNexusFileName(pFile.getName());
			}
			pDataSet.getTreeBlocks().add(tbTreeBlock);
		}	
		
		// Store document
		pDataSet.setNexmlProject(document);
	}
	
}
