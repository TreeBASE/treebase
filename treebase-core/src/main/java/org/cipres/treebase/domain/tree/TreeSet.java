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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeSet.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREESET")
@AttributeOverride(name = "id", column = @Column(name = "TREESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreeSet extends AbstractPersistedObject {

	private String mTitle;

	private List<PhyloTree> mTreeList = new ArrayList<PhyloTree>();

	/**
	 * Constructor.
	 */
	public TreeSet() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Append a new Tree to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void addPhyloTree(PhyloTree pTree) {
		if (pTree != null) {
			getTreeList().add(pTree);
		}
	}

	/**
	 * Remove the Tree.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void removePhyloTree(PhyloTree pTree) {
		if (pTree != null) {
			getTreeList().remove(pTree);
		}
	}

	/**
	 * Return the TreeList field.
	 * 
	 * @return List<PhyloTree>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "TREESET_PHYLOTREE", joinColumns = {@JoinColumn(name = "TREESET_ID")}, inverseJoinColumns = @JoinColumn(name = "PHYLOTREE_ID"))
	@IndexColumn(name = "Tree_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	protected List<PhyloTree> getTreeList() {
		return mTreeList;
	}

	/**
	 * Set the TreeList field.
	 */
	protected void setTreeList(List<PhyloTree> pTreeList) {
		mTreeList = pTreeList;
	}

	/**
	 * Return the tree list interator.
	 * 
	 * Creation date: Apr 19, 2006 1:16:57 PM
	 */
	@Transient
	public Iterator<PhyloTree> getTreeListIterator() {
		return getTreeList().iterator();
	}

	/**
	 * Return the tree count.
	 * 
	 * Creation date: Apr 19, 2006 1:17:53 PM
	 */
	@Transient
	public int getTreeCount() {
		return getTreeList().size();
	}
}
