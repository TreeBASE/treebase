package org.cipres.treebase.domain.tree;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * TreeNodeEdge.java
 * 
 * Created on Mar 15, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREENODEEDGE")
@AttributeOverride(name = "id", column = @Column(name = "TREENODEEDGE_ID"))
public class TreeNodeEdge extends AbstractPersistedObject {

	private TreeNode mParenetNode;
	private TreeNode mChildNode;

	/**
	 * Constructor.
	 */
	public TreeNodeEdge() {
		super();
	}

	/**
	 * Return the ChildNode field.
	 * 
	 * @return PhyloTreeNode
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = PhyloTreeNode.class)
	@JoinColumn(name = "CHILDNODE_ID", nullable = false)
	public TreeNode getChildNode() {
		return mChildNode;
	}

	/**
	 * Set the ChildNode field.
	 */
	public void setChildNode(TreeNode pNewChildNode) {
		mChildNode = pNewChildNode;
	}

	/**
	 * Return the ParenetNode field.
	 * 
	 * @return PhyloTreeNode
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = PhyloTreeNode.class)
	@JoinColumn(name = "PARENTNODE_ID", nullable = false)
	public TreeNode getParenetNode() {
		return mParenetNode;
	}

	/**
	 * Set the ParenetNode field.
	 */
	public void setParenetNode(TreeNode pNewParenetNode) {
		mParenetNode = pNewParenetNode;
	}

}
