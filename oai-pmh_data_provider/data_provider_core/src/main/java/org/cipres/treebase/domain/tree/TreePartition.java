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
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreePartition.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREEPARTITION")
@AttributeOverride(name = "id", column = @Column(name = "TREEPARTITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreePartition extends AbstractPersistedObject {

	private static final long serialVersionUID = -1624253859305916674L;

	private String mTitle;

	private Collection<TreeGroup> mTreeGroups = new ArrayList<TreeGroup>();

	/**
	 * Constructor.
	 */
	public TreePartition() {
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
	 * Append a new TreeGroups to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeGroup TreeGroup
	 */
	public void addTreeGroup(TreeGroup pTreeGroup) {
		if (pTreeGroup != null) {
			getTreeGroups().add(pTreeGroup);
			pTreeGroup.setPartition(this);
		}
	}

	/**
	 * Remove the TreeGroups.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeGroup TreeGroup
	 */
	public void removeTreeGroup(TreeGroup pTreeGroup) {
		if (pTreeGroup != null) {
			getTreeGroups().remove(pTreeGroup);
			pTreeGroup.setPartition(null);
		}
	}

	/**
	 * Return the TreeGroupsSet field.
	 * 
	 * @return Collection<TreeGroup>
	 */
	@OneToMany(mappedBy = "partition", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	protected Collection<TreeGroup> getTreeGroups() {
		return mTreeGroups;
	}

	/**
	 * Set the TreeGroupsSet field.
	 */
	protected void setTreeGroups(Collection<TreeGroup> pTreeGroups) {
		mTreeGroups = pTreeGroups;
	}

}
