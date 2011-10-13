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
import org.nexml.model.Document;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.IntEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.OTUs;
import org.nexml.model.Tree;

public class NexmlTreeBlockReader extends NexmlObjectConverter {

	public NexmlTreeBlockReader(Study study, TaxonLabelHome taxonLabelHome,
			Document document) {
		super(study, taxonLabelHome, document);
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
	private PhyloTree fromXmlToTreeBase(Tree<?> xmlTree) {
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
