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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CharPartition.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARPARTITION")
@AttributeOverride(name = "id", column = @Column(name = "CHARPARTITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharPartition extends AbstractPersistedObject {

	private static final long serialVersionUID = 538572333294239135L;

	private String mTitle;

	private Collection<CharGroup> mCharGroups = new ArrayList<CharGroup>();

	/**
	 * Constructor.
	 */
	public CharPartition() {
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
	 * Append a new PhyloChar Groups to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pCharGroup CharGroup
	 */
	public void addCharGroup(CharGroup pCharGroup) {
		if (pCharGroup != null) {
			getCharGroups().add(pCharGroup);

			pCharGroup.setPartition(this);
		}
	}

	/**
	 * Remove a character group.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pCharGroup CharGroup
	 */
	public void removeCharGroup(CharGroup pCharGroup) {
		if (pCharGroup != null) {
			getCharGroups().remove(pCharGroup);

			pCharGroup.setPartition(null);
		}
	}

	/**
	 * Return the CharGroup Set field.
	 * 
	 * @return Collection<CharGroup>
	 */
	@OneToMany(mappedBy = "partition", cascade = {CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REMOVE})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	protected Collection<CharGroup> getCharGroups() {
		return mCharGroups;
	}

	/**
	 * Set the CharGroups field.
	 * 
	 * @param pCharGroups
	 */
	protected void setCharGroups(Collection<CharGroup> pCharGroups) {
		mCharGroups = pCharGroups;
	}

	/**
	 * Return a Nexus formated string describing the char partition.
	 * 
	 * @return
	 */
	@Transient
	public String getNexusString() {
		StringBuilder sb = new StringBuilder();
		sb.append("charpartition ").append(getTitle()).append(" = ");

		for (CharGroup aGroup : getCharGroups()) {
			aGroup.appendNexusString(sb).append(", ");
		}
		return sb.toString();
	}
}
