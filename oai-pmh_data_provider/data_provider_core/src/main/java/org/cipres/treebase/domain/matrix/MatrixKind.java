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
package org.cipres.treebase.domain.matrix;

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
 * Another matrix property: describing the "kind" of the matrix. 
 * Please also see the "Matrix Data Type", which is defined in Nexus. 
 * 
 * It's very important to realize that the matrix kind is not the same thing as the matrix 
 * DATATYPE. For example, a Nucleotide dataset can be stored in a NEXUS format in which 
 * DATATYPE=STANDARD -- it does not have to be stored in DATATYPE=DNA. Also, the choices 
 * for Matrix kind is much more specific than the broad categories of DATATYPE in NEXUS.
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "MATRIXKIND")
@AttributeOverride(name = "id", column = @Column(name = "MATRIXKIND_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class MatrixKind extends AbstractPersistedObject {

	// All kinds in the database table:
	public static final String KIND_UNSPECIFIED = "Unspecified";
	public static final String KIND_ALLOZYME = "Allozyme";
	public static final String KIND_AA = "Amino Acid";
	public static final String KIND_BEHAVIOR = "Behavior";
	public static final String KIND_COMBINATION = "Combination";
	public static final String KIND_KARYOTYPE = "Karyotype";
	public static final String KIND_MATRIX_REP = "Matrix Representation";
	public static final String KIND_MORPHOLOGICAL = "Morphological";
	public static final String KIND_NUCLEIC_ACID = "Nucleic Acid";
	public static final String KIND_RESTRICTION_SITE = "Restriction Site";
	public static final String KIND_SECONDARY_CHEM = "Secondary Chemistry";

	private String mDescription;

	/**
	 * Constructor.
	 */
	public MatrixKind() {
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
	 * Determine if it is Amino Acid matrix.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isAminoAcidMatrix() {
		return KIND_AA.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if it is Nucleic Acid matrix.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isNucleicAcidMatrix() {
		return KIND_NUCLEIC_ACID.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine whether the type is unSpecified.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isUnspecifiedMatrix() {
		return KIND_UNSPECIFIED.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
