package org.cipres.treebase.domain.matrix;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * ContinuousChar.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("N")
public class ContinuousChar extends PhyloChar {

	private static final long serialVersionUID = -4534749917615135513L;

	private Double mUpperLimit;
	private Double mLowerLimit;

	/**
	 * Constructor.
	 */
	public ContinuousChar() {
		super();
	}

	/**
	 * Return the LowerLimit field.
	 * 
	 * @return Double
	 */
	@Column(name = "LowerLimit", nullable = true)
	public Double getLowerLimit() {
		return mLowerLimit;
	}

	/**
	 * Set the LowerLimit field.
	 */
	public void setLowerLimit(Double pNewLowerLimit) {
		mLowerLimit = pNewLowerLimit;
	}

	/**
	 * Return the UpperLimit field.
	 * 
	 * @return Double
	 */
	@Column(name = "UpperLimit", nullable = true)
	public Double getUpperLimit() {
		return mUpperLimit;
	}

	/**
	 * Set the UpperLimit field.
	 */
	public void setUpperLimit(Double pNewUpperLimit) {
		mUpperLimit = pNewUpperLimit;
	}

}
