package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * PhyloTreeNode.java
 * 
 * The treebase implementation of a general tree node.
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "PHYLOTREENODE")
@AttributeOverride(name = "id", column = @Column(name = "PHYLOTREENODE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
@BatchSize(size = 40)
public class PhyloTreeNode extends AbstractPersistedObject implements TreeNode {

	private static final long serialVersionUID = 1L;

	private String mName;
	private Double mBranchLength;
	private int mNodeDepth;
	private long mLeftNode;
	private long mRightNode;

	private PhyloTree mTree;
	private PhyloTreeNode mChildNode;
	private PhyloTreeNode mSiblingNode;
	private NodeAttribute mNodeAttribute;
	private TaxonLabel mTaxonLabel;

	// used internally to navigate the inverse relationships.
	private PhyloTreeNode mParentNode;
	//private PhyloTreeNode mInverseChild;
	//private PhyloTreeNode mInverseSibling;

	/**
	 * Constructor.
	 */
	public PhyloTreeNode() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#getNodeDepth()
	 */
	@Column(name = "nodeDepth", nullable = true)
	public int getNodeDepth() {
		return mNodeDepth;
	}

	/**
	 * Set the nodeDepth field.
	 */
	public void setNodeDepth(int pNewnodeDepth) {
		mNodeDepth = pNewnodeDepth;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#getBranchLength()
	 */
	@Column(name = "BranchLength", nullable = true)
	public Double getBranchLength() {
		return mBranchLength;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#setBranchLength(java.lang.String)
	 */
	public void setBranchLength(Double pNewBranchLength) {
		mBranchLength = pNewBranchLength;
	}

	/**
	 * Return true if the branch length is defined. 
	 * 
	 * @return
	 */
	public boolean hasBranchLength() {
		return getBranchLength() != null;
	}
	
	/**
	 * Return the RightNode field.
	 * 
	 * @return long mRightNode
	 */
	@Column(name = "RightNode", nullable = true)
	public long getRightNode() {
		return mRightNode;
	}

	/**
	 * Set the RightNode field.
	 */
	public void setRightNode(long pNewRightNode) {
		mRightNode = pNewRightNode;
	}

	/**
	 * Return the LeftNode field.
	 * 
	 * @return long mLeftNode
	 */
	@Column(name = "LeftNode", nullable = true)
	public long getLeftNode() {
		return mLeftNode;
	}

	/**
	 * Set the LeftNode field.
	 */
	public void setLeftNode(long pNewLeftNode) {
		mLeftNode = pNewLeftNode;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#getName()
	 */
	@Column(name = "Name", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getName() {
		return mName;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#setName(java.lang.String)
	 */
	public void setName(String pNewName) {
		mName = pNewName;
	}

	/**
	 * Return the Tree field.
	 * 
	 * @return PhyloTree
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHYLOTREE_ID", nullable = false)
	@Index(name = "TNODE_TREE_IDX")
	public PhyloTree getTree() {
		return mTree;
	}

	/**
	 * This is for internal use only. Use the public method PhyloTree.addNode() instead.
	 */
	protected void setTree(PhyloTree pNewTree) {
		mTree = pNewTree;
	}

	/**
	 * Return the TaxonLabel field.
	 * 
	 * @return TaxonLabel 
	 */
	//Use eager fetch since the taxonlabel is always needed after loading a node.
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinColumn(name = "TAXONLABEL_ID", nullable = true)
	@Index(name = "TNODE_TAXLABEL_IDX")
	public TaxonLabel getTaxonLabel() {
		return mTaxonLabel;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	public void setTaxonLabel(TaxonLabel pNewTaxonLabel) {
		mTaxonLabel = pNewTaxonLabel;
	}

	/**
	 * Get the TaxonLabel as a String. Return an empty string if the node has no taxon label.
	 */
	@Transient
	public String getTaxonLabelAsString() {
		if (getTaxonLabel() != null) {
			return getTaxonLabel().getTaxonLabel();
		}
		return null;
	}

	/**
	 * Get the TaxonLabel as a String. If empty return the node name. 
	 */
	@Transient
	public String getTaxonLabelOrName() {
		String label = getTaxonLabelAsString();
		if (TreebaseUtil.isEmpty(label)) {
			return getName();
		}
		return label;
	}

	/**
	 * Return the SibilingNode field.
	 * 
	 * @return PhyloTreeNode
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIBLING_ID", nullable = true)
	protected PhyloTreeNode getSiblingNode() {
		return mSiblingNode;
	}

	/**
	 * Set the SibilingNode field.
	 */
	protected void setSiblingNode(PhyloTreeNode pNewSibilingNode) {
		mSiblingNode = pNewSibilingNode;
	}

	/**
	 * Return the ChildNode field.
	 * 
	 * @return PhyloTreeNode
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHILD_ID", nullable = true)
	protected PhyloTreeNode getChildNode() {
		return mChildNode;
	}

	/**
	 * Set the ChildNode field.
	 */
	protected void setChildNode(PhyloTreeNode pNewChildNode) {
		mChildNode = pNewChildNode;
	}

//	/**
//	 * Return the InverseSibling field.
//	 * 
//	 * @return PhyloTreeNode
//	 */
//	@OneToOne(mappedBy = "siblingNode", fetch= FetchType.LAZY)
//	protected PhyloTreeNode getInverseSibling() {
//		return mInverseSibling;
//	}
//
//	/**
//	 * Set the InverseSibling field.
//	 */
//	protected void setInverseSibling(PhyloTreeNode pNewInverseSibling) {
//		mInverseSibling = pNewInverseSibling;
//	}
//
//	/**
//	 * Return the InverseChild field.
//	 * 
//	 * @return PhyloTreeNode
//	 */
//	@OneToOne(mappedBy = "childNode", fetch = FetchType.LAZY)
//	protected PhyloTreeNode getInverseChild() {
//		return mInverseChild;
//	}
//
//	/**
//	 * Set the InverseChild field.
//	 */
//	protected void setInverseChild(PhyloTreeNode pNewInverseChild) {
//		mInverseChild = pNewInverseChild;
//	}
//
	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#isRootNode()
	 */
	@Transient
	public boolean isRootNode() {
		return getParentNode() == null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#isLeaf()
	 */
	@Transient
	public boolean isLeaf() {
		return getChildNode() == null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#getParentNode()
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID", nullable = true)
	public PhyloTreeNode getParentNode() {
		return mParentNode;
	}

	/**
	 * This is for internal use only. Use the public methods addChildNode() 
	 * or PhyloTree.addNode() instead.
	 */
	protected void setParentNode(PhyloTreeNode pParentNode) {
		mParentNode = pParentNode;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#addChildNode(org.cipres.treebase.domain.tree.PhyloTreeNode)
	 */
	public void addChildNode(TreeNode pChild) {
		if (pChild == null || !(pChild instanceof PhyloTreeNode)) {
			return;
		}

		PhyloTreeNode bChild = (PhyloTreeNode) pChild;
		bChild.setParentNode(this);

		getTree().addTreeNode(bChild);
		
		if (getChildNode() == null) {
			setChildNode(bChild);
		} else {
			getChildNode().addSiblingNode(bChild);
		}
	}

	/**
	 * Add a sibling node. This method does not set the parent node. It is done 
	 * by addChildNode().
	 * 
	 * @param pSibling
	 */
	private void addSiblingNode(PhyloTreeNode pSibling) {
		if (pSibling == null) {
			return;
		}

		if (getSiblingNode() == null) {
			setSiblingNode(pSibling);
			//Note: this is a private method so we don't need to call the following:
			//pSibling.setParentNode(this.getParentNode());
		} else {
			getSiblingNode().addSiblingNode(pSibling);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.TreeNode#RemoveChildNode(org.cipres.treebase.domain.tree.PhyloTreeNode)
	 */
	public boolean removeChildNode(TreeNode pChild) {
		if (pChild == null || getChildNode() == null || !(pChild instanceof PhyloTreeNode)) {
			return false;
		}

		//Note: remove a child node, ADD all the sub node from this child node.
		// So actually a clad is removed. 
		PhyloTreeNode bChild = (PhyloTreeNode) pChild;
		boolean removed = false;
		if (getChildNode() == bChild) {
			PhyloTreeNode nextChild = bChild.getSiblingNode();
//			if (nextChild != null) {
//				nextChild.setInverseChild(this);
//				nextChild.setInverseSibling(null);
//			}
			
			setChildNode(nextChild);

			bChild.setParentNode(null);
			
			removed = true;
		} else {
			removed = getChildNode().removeSibling(bChild);
		}

		return removed;
	}

	/**
	 * Remove a sibling node.
	 * 
	 * @param pSibling
	 * @return true if the node is found and removed
	 */
	private boolean removeSibling(PhyloTreeNode pSibling) {
		if (pSibling == null || getSiblingNode() == null) {
			return false;
		}

		boolean removed = false;
		if (getSiblingNode() == pSibling) {
			
			PhyloTreeNode nextSibling = pSibling.getSiblingNode();
			
//			if (nextSibling != null) {
//				nextSibling.setInverseSibling(this);
//			}
						
			setSiblingNode(nextSibling);
			
//			pSibling.setInverseChild(null);
//			pSibling.setInverseSibling(null);
			pSibling.setSiblingNode(null);
			pSibling.setParentNode(null);
			
			removed = true;
		} else {
			removed = getSiblingNode().removeSibling(pSibling);
		}

		return removed;
	}

	/**
	 * Return the NodeAttribute field.
	 * 
	 * @return NodeAttribute
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "NODEATTRIBUTE_ID", nullable = true)
	public NodeAttribute getNodeAttribute() {
		return mNodeAttribute;
	}

	/**
	 * Set the NodeAttribute field.
	 */
	public void setNodeAttribute(NodeAttribute pNewNodeAttribute) {
		mNodeAttribute = pNewNodeAttribute;
	}

	/**
	 * Return all the children for this node.
	 * 
	 * @return
	 */
	@Transient
	public List<PhyloTreeNode> getChildNodes() {
		List<PhyloTreeNode> nodes = new ArrayList<PhyloTreeNode>();

		PhyloTreeNode child = getChildNode();
		if (child != null) {
			nodes.add(child);

			PhyloTreeNode sibling = child.getSiblingNode();
			while (sibling != null) {
				nodes.add(sibling);

				sibling = sibling.getSiblingNode();
			}
		}

		return nodes;
	}
	
	/*
	 * TODO: In an annotated tree, there is a more efficient way to do this.
	 * Simply select every node N such that all of:
	 *   0. N.tree == this.tree
	 *   1. N.leftNode < this.leftNode
	 *   2. N.rightNode > this.rightNode
	 *   
	 *   To get them in order, simply sort by leftNode value; the least leftNode is the root, and the 
	 *   greatest is the parent.  To include this node itself, change < and > to <= and >=.
	 */
	/**
	 * @return the list of nodes that are ancestors of this one, in order with the root last
	 */
	@Transient
	List<PhyloTreeNode> getAncestorNodes() {
		List<PhyloTreeNode> nodes = new ArrayList<PhyloTreeNode>();
		PhyloTreeNode cur;
		for (cur = (PhyloTreeNode) this.getParentNode(); 
		     cur != null; 
		     cur = (PhyloTreeNode) cur.getParentNode()) {
			nodes.add(cur);
		}
		return nodes;
	}
	
	/**
	 * Annotate the leftNode and rightNode members of this node and
	 * all its child nodes in the tree.  Afterwards, every node in the subtree rooted here
	 * will have the following properties:
	 *   a.leftNode < b.leftNode < b.rightNode < a.rightNode whenever b is a child of a
	 *   (a.rightNode - a.leftNode + 1)/2 is the size of the tree rooted at a
	 * 
	 * @param start The leftNode value that should be assigned to this node
	 * @return The rightNode value that was assigned to this node
	 * @author mjd
	 */
	public long updateSubtreeBounds(long start) {
		this.setLeftNode(start);
		for (PhyloTreeNode c : this.getChildNodes()) {
			start = c.updateSubtreeBounds(start+1);
		}
		this.setRightNode(start+1);
		return start+1;
	}
	
	/**
	 * @return True if and only if the leftNode and rightNode elements have been set
	 * @author mjd
	 */
	@Transient
	public boolean hasSubtreeBounds() {
		return this.getLeftNode() != 0 && this.getRightNode() != 0;
	}
	
	/**
	 * 
	 * @param that some other node in the same tree
	 * @return whether this node is an ancestor of that node
	 * @author mjd
	 */
	public boolean isAncestorOf(PhyloTreeNode that) {
		return this.getLeftNode() < that.getLeftNode()
		&& that.getRightNode() < this.getRightNode();
	}
	
	/**
	 * 
	 * @param that some other node in the same tree
	 * @return whether this node is a descdendant of that node
	 * @author mjd
	 */
	public boolean isDescendantOf(PhyloTreeNode that) {
		return that.isAncestorOf(this);
	}
	
	/**
	 * 
	 * TODO: in an annotated tree, there is a much better way to do this.
	 * we can do a single Hibernate query that selects the first node N such that all of: 
	 *    0. N.tree == this.tree
	 *    1. N.leftNode  < this.leftNode
	 *    2. N.leftNode  < that.leftNode
	 *    3. N.rightNode > this.rightNode
	 *    4. N.rightNode > that.rightNode
	 *    6. order descending by this.leftNode
	 */
	/** 
	 * @param that some other node in the same tree
	 * @return the lowest node in the chain of common ancestors of this node and that
	 */
	public PhyloTreeNode nearestCommonAncestor(PhyloTreeNode that) {
		for (PhyloTreeNode p : this.getAncestorNodes()) {
			if (p.isAncestorOf(that)) { return p; }
		}
		return null;  // This should never happen
	}
	
	/* TODO: as with the methods above, there is an abbreviation for this when the tree is annotated */
	/**
	 * 
	 * @param n1 another node in the same tree
	 * @param n2 another node in the same tree 
	 * @return whether n1 and n2 are more closely related to each other than either is to this node
	 */
	public boolean haveABCTopology(PhyloTreeNode n1, PhyloTreeNode n2) {
		return ! n1.nearestCommonAncestor(n2).isAncestorOf(this);
	}
	
	@Override
	@Transient
	public String getLabel() {
		return getTaxonLabelAsString();
	}
}
