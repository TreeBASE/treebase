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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeType.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREETYPE")
@AttributeOverride(name = "id", column = @Column(name = "TREETYPE_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class TreeType extends AbstractPersistedObject {

	private static final long serialVersionUID = 6628295672594368539L;

	public static final String TYPE_SINGLE = "Single";
	public static final String TYPE_CONSENSUS = "Consensus";
	public static final String TYPE_SUPERTREE = "SuperTree";

	private String mDescription;

	/**
	 * Constructor.
	 */
	public TreeType() {
		super();
	}
	
	// Strings here correspond to values found in the treebase 1 dump file
	// The if-elsif tree should probably be replaced with more generic logic,
	// or perhaps a Map.  20080328 mjd
	public TreeType(String pDescription) {
		if (pDescription.equals("Simple Tree")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("single")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("Single Tree")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("Consensus Tree")) { pDescription = TYPE_CONSENSUS; }
		else if (pDescription.equals("consensus")) { pDescription = TYPE_CONSENSUS; }
		
		mDescription = pDescription;
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if type is single tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSingleTree() {
		return TYPE_SINGLE.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if type is consensus tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isConsensusTree() {
		return TYPE_CONSENSUS.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if type is super tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSuperTree() {
		return TYPE_SUPERTREE.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
