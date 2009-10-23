/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
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
