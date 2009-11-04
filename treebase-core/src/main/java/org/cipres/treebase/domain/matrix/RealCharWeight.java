package org.cipres.treebase.domain.matrix;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * RealCharWeight.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("R")
public class RealCharWeight extends CharWeight {

	private static final long serialVersionUID = 1L;

	private Double mRealWeight;

	/**
	 * Constructor.
	 */
	public RealCharWeight() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param pWeight
	 */
	public RealCharWeight(Double pWeight) {
		super();

		setRealWeight(pWeight);
	}

	/**
	 * Return the Weight field.
	 * 
	 * @return Double
	 */
	@Column(name = "RealWeight", nullable = true)
	public Double getRealWeight() {
		return mRealWeight;
	}

	/**
	 * Set the Weight field.
	 */
	public void setRealWeight(Double pNewWeight) {
		mRealWeight = pNewWeight;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharWeight#appendWeight(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendWeight(StringBuilder pBuilder) {
		if (getRealWeight() != null) {
			pBuilder.append(getRealWeight());
		}
		return pBuilder;
	}

}
