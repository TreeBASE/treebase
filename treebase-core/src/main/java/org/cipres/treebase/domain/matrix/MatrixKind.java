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
