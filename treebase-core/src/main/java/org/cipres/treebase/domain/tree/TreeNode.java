/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer. * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the University of California or
 * the San Diego Supercomputer Center nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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