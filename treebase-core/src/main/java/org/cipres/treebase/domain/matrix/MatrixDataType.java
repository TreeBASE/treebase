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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * MatrixDataType.java
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "MATRIXDATATYPE")
@AttributeOverride(name = "id", column = @Column(name = "MATRIXDATATYPE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class MatrixDataType extends AbstractPersistedObject {

	private static final long serialVersionUID = 1208732648506944463L;

	// TODO: add a subclass for build-in immutable data types:
	public static final String MATRIX_DATATYPE_STANDARD = "Standard";
	public static final String MATRIX_DATATYPE_DNA = "DNA";
	public static final String MATRIX_DATATYPE_RNA = "RNA";
	public static final String MATRIX_DATATYPE_NUCLEOTIDE = "Nucleotide";
	public static final String MATRIX_DATATYPE_PROTEIN = "Protein";
	public static final String MATRIX_DATATYPE_CONTINUOUS = "Continuous";
	public static final String MATRIX_DATATYPE_DISTANCE = "Distance";
	public static final String MATRIX_DATATYPE_MIXED = "Mixed";

	private String mDescription;

	private PhyloChar mDefaultCharacter;

	/**
	 * Constructor.
	 */
	public MatrixDataType() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

	/**
	 * Return the DefaultCharacter field.
	 * 
	 * @return PhyloChar mDefaultCharacter
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "PHYLOCHAR_ID", nullable = true)
	public PhyloChar getDefaultCharacter() {
		return mDefaultCharacter;
	}

	/**
	 * Set the DefaultCharacter field.
	 */
	public void setDefaultCharacter(PhyloChar pNewDefaultCharacter) {
		mDefaultCharacter = pNewDefaultCharacter;
	}

	/**
	 * Returns true if the data type is dna.
	 * 
	 * @return
	 */
	@Transient
	public boolean isDNA() {
		return MATRIX_DATATYPE_DNA.equals(getDescription());
	}

	/**
	 * Returns true if the data type is rna.
	 * 
	 * @return
	 */
	@Transient
	public boolean isRNA() {
		return MATRIX_DATATYPE_RNA.equals(getDescription());
	}

	/**
	 * Returns true if the data type is protein.
	 * 
	 * @return
	 */
	@Transient
	public boolean isProtein() {
		return MATRIX_DATATYPE_PROTEIN.equals(getDescription());
	}

	/**
	 * Returns true if the data type is Standard.
	 * 
	 * @return
	 */
	@Transient
	public boolean isStandard() {
		return MATRIX_DATATYPE_STANDARD.equals(getDescription());
	}

	/**
	 * Returns true if the data type is sequence.
	 * 
	 * @return
	 */
	@Transient
	public boolean isSequence() {
		return (MATRIX_DATATYPE_DNA.equals(getDescription())
			|| MATRIX_DATATYPE_NUCLEOTIDE.equals(getDescription())
			|| MATRIX_DATATYPE_PROTEIN.equals(getDescription()) || MATRIX_DATATYPE_RNA
			.equals(getDescription()));
	}
}
