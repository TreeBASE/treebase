package org.cipres.treebase.domain.matrix;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * IntegerCharWeight.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("I")
public class IntegerCharWeight extends CharWeight {

	private static final long serialVersionUID = 1L;
	private Integer mWeight;

	/**
	 * Constructor.
	 */
	public IntegerCharWeight() {
		super();
	}

	/**
	 * Constructor.
	 */
	public IntegerCharWeight(Integer pWeight) {
		super();

		setWeight(pWeight);
	}

	/**
	 * Return the Weight field.
	 * 
	 * @return Integer
	 */
	@Column(name = "Weight", nullable = true)
	public Integer getWeight() {
		return mWeight;
	}

	/**
	 * Set the Weight field.
	 */
	public void setWeight(Integer pNewWeight) {
		mWeight = pNewWeight;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharWeight#appendWeight(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendWeight(StringBuilder pBuilder) {
		if (getWeight() != null) {
			pBuilder.append(getWeight());
		}
		return pBuilder;
	}

}
