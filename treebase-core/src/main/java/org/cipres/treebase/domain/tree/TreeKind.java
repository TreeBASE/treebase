/*
 * CIPRES Copyright (c) 2005- 2008, The Regents of the University of California All rights reserved.
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
 * Another tree property: describing the "kind" of the tree. Please also see the "Tree Type". 
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREEKIND")
@AttributeOverride(name = "id", column = @Column(name = "TREEKIND_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class TreeKind extends AbstractPersistedObject {

	//default tree type is: species
	public static final String KIND_SPECIES = "Species Tree";
	public static final String KIND_GENE = "Gene Tree";
	public static final String KIND_LANGUAGE = "Language Tree";
	public static final String KIND_AREA = "Area Tree";
	public static final String KIND_BARCODE = "Barcode Tree";
	public static final String KIND_OBJ_CLASSIFICATION = "Object Classification Tree";

	private String mDescription;

	/**
	 * Constructor.
	 */
	public TreeKind() {
		super();
	}
	
	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_100)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if it is species tree.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSpeciesTree() {
		return KIND_SPECIES.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
