package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.nexml.model.Annotatable;
import org.nexml.model.Document;
import org.nexml.model.FloatEdge;
import org.nexml.model.Node;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;
import org.nexml.model.Tree;

public class NexmlTreeBlockWriter extends NexmlObjectConverter {
	
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlTreeBlockWriter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);
	}
		
	
	/**
	 * 
	 * @param phyloTree
	 * @return
	 */
	private Tree<?> fromTreeBaseToXml(PhyloTree phyloTree,org.nexml.model.TreeBlock xmlTreeBlock) {
		Tree<FloatEdge> xmlTree = xmlTreeBlock.createFloatTree();
		if ( null != phyloTree.getLabel() ) {
			xmlTree.setLabel(phyloTree.getLabel());
		}
		attachTreeBaseID(xmlTree, phyloTree,PhyloTree.class);
		copyTreeBaseTree(phyloTree, xmlTree);
		return xmlTree;
	}
	
	/**
	 * 
	 * @param treeBlock
	 * @return
	 */
	public org.nexml.model.TreeBlock fromTreeBaseToXML(TreeBlock treeBlock) {
		TaxonLabelSet taxonLabelSet = treeBlock.getTaxonLabelSet();
		OTUs xmlOTUs = getOTUsById(taxonLabelSet.getId());
		org.nexml.model.TreeBlock xmlTreeBlock = getDocument().createTreeBlock(xmlOTUs);
		
		// attach base uri and skos:historyNote
		xmlTreeBlock.addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		xmlTreeBlock.setBaseURI(mTreeBaseURI);
		
		if ( null != treeBlock.getTitle() ) {
			xmlTreeBlock.setLabel(treeBlock.getTitle());
		}
		attachTreeBaseID((Annotatable)xmlTreeBlock,treeBlock,TreeBlock.class);
		for ( PhyloTree phyloTree : treeBlock.getTreeList() ) {
			fromTreeBaseToXml(phyloTree,xmlTreeBlock);
		}
		return xmlTreeBlock;
	}
	
	/**
	 * 
	 * @param phyloTree
	 * @param xmlTree
	 */
	private void copyTreeBaseTree(PhyloTree phyloTree,Tree<FloatEdge> xmlTree) {
		traverseTreeBaseTree(phyloTree,phyloTree.getRootNode(),xmlTree.createNode(),xmlTree);
	}
	
	/**
	 * 
	 * @param tbTree
	 * @param tbNode
	 * @param xmlNode
	 * @param xmlTree
	 */
	private void traverseTreeBaseTree(PhyloTree tbTree,PhyloTreeNode tbNode,Node xmlNode,Tree<FloatEdge> xmlTree) {
		if ( null != tbNode.getName() ) {
			xmlNode.setLabel(tbNode.getName());
		}
		if ( tbNode.isRootNode() ) {
			xmlNode.setRoot(true);
		}
		attachTreeBaseID(xmlNode, tbNode,PhyloTreeNode.class);	
		TaxonLabel taxonLabel = tbNode.getTaxonLabel();
		if ( null != taxonLabel ) {
			Long taxonId = taxonLabel.getId();
			for ( OTUs xmlOTUs : getDocument().getOTUsList() ) {
				OTU xmlOTU = getOTUById(xmlOTUs, taxonId);
				if ( null != xmlOTU ) {
					xmlNode.setOTU(xmlOTU);
					break;
				}
			}
		}
		for ( PhyloTreeNode tbChildNode : tbNode.getChildNodes() ) {
			Node xmlChildNode = xmlTree.createNode();
			xmlChildNode.setId(tbChildNode.getTreebaseIDString().toString());
			FloatEdge xmlEdge = xmlTree.createEdge(xmlNode, xmlChildNode);
			if ( null != tbChildNode.getBranchLength() ) {
				xmlEdge.setLength(tbChildNode.getBranchLength());
			}
			traverseTreeBaseTree(tbTree, tbChildNode, xmlChildNode, xmlTree);
		}
	}

}
