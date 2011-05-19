package org.cipres.treebase.domain.nexus.nexml;

import java.util.Iterator;
import java.util.Set;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.nexml.model.Annotatable;
import org.nexml.model.Document;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.IntEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;
import org.nexml.model.Tree;

public class NexmlTreeBlockConverter extends NexmlObjectConverter {
	
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlTreeBlockConverter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);
	}
	
	/**
	 * 
	 * @param xmlTreeBlock
	 * @return
	 */
	public TreeBlock fromXmlToTreeBase(org.nexml.model.TreeBlock xmlTreeBlock) {
		OTUs xmlOTUs = xmlTreeBlock.getOTUs();
		Long tbTaxonLabelSetId = readTreeBaseID(xmlOTUs);
		TreeBlock tbTreeBlock = new TreeBlock();
		tbTreeBlock.setTitle(xmlTreeBlock.getLabel());
		if ( null != tbTaxonLabelSetId ) {
			TaxonLabelSet tbTaxonLabelSet = getTaxonLabelHome()
				.findPersistedObjectByID(TaxonLabelSet.class, tbTaxonLabelSetId);
			tbTreeBlock.setTaxonLabelSet(tbTaxonLabelSet);
		}
		Iterator<Network<?>> xmlNetworkIterator = xmlTreeBlock.iterator();
		while ( xmlNetworkIterator.hasNext() ) {
			Network<?> xmlNetwork = xmlNetworkIterator.next();
			if ( xmlNetwork instanceof Tree ) {
				PhyloTree tbPhyloTree = fromXmlToTreeBase((Tree<?>) xmlNetwork); 
				tbPhyloTree.setStudy(getStudy());
				tbTreeBlock.addPhyloTree(tbPhyloTree);
			}				
		}		
		return tbTreeBlock;
	}
	
	/**
	 * 
	 * @param xmlTree
	 * @return
	 */
	public PhyloTree fromXmlToTreeBase(Tree<?> xmlTree) {
		PhyloTree tbPhyloTree = new PhyloTree();
		tbPhyloTree.setLabel(xmlTree.getLabel());
		tbPhyloTree.setPublished(false);
		tbPhyloTree.setRootedTree(((Node)xmlTree.getRoot()).isRoot());
		copyXmlTree(xmlTree, tbPhyloTree);		
		tbPhyloTree.updateSubtreeBounds();		
		return tbPhyloTree;		
	}
	
	/**
	 * 
	 * @param phyloTree
	 * @return
	 */
	public Tree<?> fromTreeBaseToXml(PhyloTree phyloTree,org.nexml.model.TreeBlock xmlTreeBlock) {
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
	
	/**
	 * 
	 * @param xmlTree
	 * @param tbPhyloTree
	 */
	private void copyXmlTree(Tree<?> xmlTree, PhyloTree tbPhyloTree) {
		StringBuilder sb = new StringBuilder();
		PhyloTreeNode tbRoot = new PhyloTreeNode();		
		traverseXmlTree(xmlTree,sb,xmlTree.getRoot(), tbRoot, tbPhyloTree);
		tbPhyloTree.setRootNode(tbRoot);
		sb.append(';');
		tbPhyloTree.setNewickString(sb.toString());
	}
	
	/**
	 * 
	 * @param xmlTree
	 * @param sb
	 * @param xmlNode
	 * @param tbNode
	 * @param tbTree
	 */
	private void traverseXmlTree(Tree<?> xmlTree, StringBuilder sb, Node xmlNode, PhyloTreeNode tbNode, PhyloTree tbTree) {
		tbTree.addTreeNode(tbNode);
		if ( null != xmlNode.getOTU() ) {
			Long tbTaxonLabelId = readTreeBaseID(xmlNode.getOTU());
			if ( null != tbTaxonLabelId ) {
				TaxonLabel tbTaxonLabel = getTaxonLabelHome()
					.findPersistedObjectByID(TaxonLabel.class, tbTaxonLabelId);
				tbNode.setTaxonLabel(tbTaxonLabel);
			}
		}
		
		Set<Node> xmlChildren = xmlTree.getOutNodes(xmlNode);			
		if ( ! xmlChildren.isEmpty() ) {// xmlNode is internal
			sb.append('(');
		}
		int currentChild = 1;
		for ( Node child : xmlChildren ) {
			PhyloTreeNode tbChild = new PhyloTreeNode();
			tbNode.addChildNode(tbChild);
			traverseXmlTree(xmlTree,sb,child,tbChild,tbTree);
			if ( currentChild < xmlChildren.size() ) {
				sb.append(',');
			}
			currentChild++;
		}		
		if ( ! xmlChildren.isEmpty() ) {// xmlNode is internal
			sb.append(')');
		}
		String label = xmlNode.getLabel();
		if ( null != label ) {
			sb.append(label);
			tbNode.setName(label);
		}
		
		Set<Node> xmlParents = xmlTree.getInNodes(xmlNode);	
		if ( ! xmlParents.isEmpty() ) { // xmlNode is not topological root
			Edge xmlEdge = xmlTree.getEdge(xmlParents.iterator().next(), xmlNode);
			if ( xmlEdge instanceof FloatEdge ) {
				Double length = ((FloatEdge)xmlEdge).getLength();
				sb.append(':').append(length);
				tbNode.setBranchLength(length);
			}
			else if ( xmlEdge instanceof IntEdge ) {
				Integer length = ((IntEdge)xmlEdge).getLength();
				sb.append(':').append(length);
				tbNode.setBranchLength(length.doubleValue());
			}
		}		
	}

}
