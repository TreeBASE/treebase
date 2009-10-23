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
package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeGroup.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARGROUP")
@AttributeOverride(name = "id", column = @Column(name = "CHARGROUP_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharGroup extends AbstractPersistedObject {

	private static final long serialVersionUID = -6215673532165359873L;

	private String mTitle;

	private CharPartition mPartition;

	private Collection<ColumnRange> mCharColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public CharGroup() {
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
	 * Return the Partition field.
	 * 
	 * @return CharPartition mPartition
	 */
	@ManyToOne
	@JoinColumn(name = "CHARPARTITION_ID")
	public CharPartition getPartition() {
		return mPartition;
	}

	/**
	 * Set the Partition field.
	 */
	public void setPartition(CharPartition pNewPartition) {
		mPartition = pNewPartition;
	}

	/**
	 * Return the weight columns.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CharGroup_ColRange", joinColumns = {@JoinColumn(name = "CHARGROUP_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getCharColumns() {
		return mCharColumns;
	}

	/**
	 * Set the weight columns.
	 */
	public void setCharColumns(Collection<ColumnRange> pWeightColumns) {
		mCharColumns = pWeightColumns;
	}

	/**
	 * Append the description.
	 * 
	 * @param pBuilder
	 * @return
	 */
	public StringBuilder appendNexusString(StringBuilder pBuilder) {
		pBuilder.append(getTitle()).append(":");

		for (ColumnRange colRange : getCharColumns()) {
			pBuilder.append(" ");
			colRange.appendRange(pBuilder);
		}
		return pBuilder;
	}

	// /**
	// * Append a new PhyloChar to the Set.
	// *
	// * Creation date: Mar 14, 2006
	// *
	// * @param pChar PhyloChar
	// */
	// public void addCharacter(PhyloChar pChar) {
	// if (pChar != null) {
	// getCharSet().add(pChar);
	// }
	// }
	//
	// /**
	// * Remove a character.
	// *
	// * Creation date: Mar 14, 2006
	// *
	// * @param pChar PhyloChar
	// */
	// public void removeCharacter(PhyloChar pChar) {
	// if (pChar != null) {
	// getCharSet().remove(pChar);
	// }
	// }
	//
	// /**
	// * Return the PhyloChar Set field.
	// *
	// * @return Collection<PhyloChar>
	// */
	// @ManyToMany
	// @JoinTable(name = "CHARGROUP_PHYLOCHAR", joinColumns = {@JoinColumn(name = "CHARGROUP_ID")},
	// inverseJoinColumns = @JoinColumn(name = "PHYLOCHAR_ID"))
	// protected Collection<PhyloChar> getCharSet() {
	// return mCharSet;
	// }
	//
	// /**
	// * Set the character Set field.
	// */
	// protected void setCharSet(Collection<PhyloChar> pCharSet) {
	// mCharSet = pCharSet;
	// }
	//
}
