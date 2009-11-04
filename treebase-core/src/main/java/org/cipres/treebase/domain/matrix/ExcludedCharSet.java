package org.cipres.treebase.domain.matrix;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * ExcludedCharSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("E")
public class ExcludedCharSet extends UserDefinedCharSet {

	private static final long serialVersionUID = -5326845679053249764L;

	private CharacterMatrix mMatrixForExclude;

	/**
	 * Constructor.
	 */
	public ExcludedCharSet() {
		super();
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return CharacterMatrix mMatrixForExclude
	 */
	@OneToOne(mappedBy = "defaultExcludedSet")
	public CharacterMatrix getMatrixForExclude() {
		return mMatrixForExclude;
	}

	/**
	 * Set the Matrix field.
	 */
	public void setMatrixForExclude(CharacterMatrix pNewMatrix) {
		mMatrixForExclude = pNewMatrix;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharSet#getNexusPrefix()
	 */
	@Transient
	@Override
	protected String getNexusPrefix() {
		return "exset ";
	}

}
