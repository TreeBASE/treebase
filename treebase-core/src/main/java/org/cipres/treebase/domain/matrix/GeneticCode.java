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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * GeneticCode.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "GeneticCode")
@AttributeOverride(name = "id", column = @Column(name = "GeneticCode_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class GeneticCode extends AbstractPersistedObject {

	private static final long serialVersionUID = 5320812309978672110L;

	private String mTitle;
	private String mCodeOrder;
	private String mNucOrder;
	private String mExtensions;
	private String mCodeDescription;

	private boolean mPredefined;

	/**
	 * Constructor.
	 */
	public GeneticCode() {
		super();
	}

	/**
	 * Return the CodeDescription field.
	 * 
	 * @return String mCodeDescription
	 */
	@Column(name = "CodeDescription", length = TBPersistable.COLUMN_LENGTH_STRING_1K)
	public String getCodeDescription() {
		return mCodeDescription;
	}

	/**
	 * Set the CodeDescription field.
	 */
	public void setCodeDescription(String pNewCodeDescription) {
		mCodeDescription = pNewCodeDescription;
	}

	/**
	 * Return the Extensions field.
	 * 
	 * @return String mExtensions
	 */
	@Column(name = "Extensions", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getExtensions() {
		return mExtensions;
	}

	/**
	 * Set the Extensions field.
	 */
	public void setExtensions(String pNewExtensions) {
		mExtensions = pNewExtensions;
	}

	/**
	 * Return the NucOrder field.
	 * 
	 * @return String mNucOrder
	 */
	@Column(name = "NucOrder", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getNucOrder() {
		return mNucOrder;
	}

	/**
	 * Set the NucOrder field.
	 */
	public void setNucOrder(String pNewNucOrder) {
		mNucOrder = pNewNucOrder;
	}

	/**
	 * Return the CodeOrder field.
	 * 
	 * @return String mCodeOrder
	 */
	@Column(name = "CodeOrder", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getCodeOrder() {
		return mCodeOrder;
	}

	/**
	 * Set the CodeOrder field.
	 */
	public void setCodeOrder(String pNewCodeOrder) {
		mCodeOrder = pNewCodeOrder;
	}

	/**
	 * Return the Predefined field.
	 * 
	 * @return boolean
	 */
	@Column(name = "Predefined")
	public boolean isPredefined() {
		return mPredefined;
	}

	/**
	 * Set the Predefined field.
	 */
	public void setPredefined(boolean pNewPredefined) {
		mPredefined = pNewPredefined;
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

}
