package org.cipres.treebase.domain.tree;

/**
 * TreeNode.java
 * 
 * Created on Mar 15, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface TreeNode {

	/**
	 * Return the nodeDepth field.
	 * 
	 * @return int
	 */
	public int getNodeDepth();

	/**
	 * Return the BranchLength.
	 * 
	 * @return double
	 */
	public Double getBranchLength();

	/**
	 * Set the BranchLength.
	 */
	public void setBranchLength(Double pNewBranchLength);

	/**
	 * Return the Name field.
	 * 
	 * @return String
	 */
	public String getName();

	/**
	 * Set the Name field.
	 */
	public void setName(String pNewName);

	/**
	 * Return true if the node is the root node.
	 * 
	 * Creation date: Mar 15, 2006 12:32:46 PM
	 * 
	 * @return boolean
	 */
	public boolean isRootNode();

	/**
	 * Return true if the node is a leaf node.
	 * 
	 * Creation date: Mar 15, 2006 1:31:09 PM
	 */
	public boolean isLeaf();

	/**
	 * Return the parent node. Returns null if this is the root node.
	 * 
	 * @return TreeNode
	 */
	public TreeNode getParentNode();

	/**
	 * Add a child node.
	 * 
	 * Creation date: Mar 15, 2006 1:04:08 PM
	 * 
	 * @param pChild
	 */
	public void addChildNode(TreeNode pChild);

	/**
	 * Remove a child node.
	 * 
	 * Creation date: Mar 15, 2006 1:04:08 PM
	 * 
	 * @param pChild
	 * @return true if the node is found and removed.
	 */
	public boolean removeChildNode(TreeNode pChild);

}