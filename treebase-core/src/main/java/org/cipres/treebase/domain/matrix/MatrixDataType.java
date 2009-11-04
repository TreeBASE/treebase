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
