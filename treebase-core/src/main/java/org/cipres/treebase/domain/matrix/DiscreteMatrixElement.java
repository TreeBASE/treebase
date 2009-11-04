package org.cipres.treebase.domain.matrix;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * DiscreteMatrixElement.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("D")
public class DiscreteMatrixElement extends MatrixElement {

	private static final long serialVersionUID = 1L;

	private DiscreteCharState mCharState;
	private Boolean mGap;

	/**
	 * Constructor.
	 */
	public DiscreteMatrixElement() {
		super();
	}

	/**
	 * Return the Gap field.
	 * 
	 * @return boolean mGap
	 */
	@Column(name = "Gap")
	public Boolean isGap() {
		return mGap;
	}

	/**
	 * Set the Gap field.
	 */
	public void setGap(Boolean pNewGap) {
		mGap = pNewGap;
	}

	/**
	 * Return true if the state is "missing".
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isMissingState() {
		return (getCharState() == null && !isGap());
	}

	/**
	 * Return the CharState field.
	 * 
	 * @return DiscreteCharState
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "DISCRETECHARSTATE_ID", nullable = true)
	public DiscreteCharState getCharState() {
		return mCharState;
	}

	/**
	 * Set the CharState field.
	 */
	public void setCharState(DiscreteCharState pNewCharState) {
		mCharState = pNewCharState;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValue(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendValue(StringBuilder pBuilder) {
		if (getCharState() == null) {
			if (isGap()) {
				pBuilder.append(getColumn().getMatrix().getGapSymbol());
			} else {
				pBuilder.append(getColumn().getMatrix().getMissingSymbol());
			}
		} else {
			if (getCharState().getSymbol() != null){
				pBuilder.append(getCharState().getSymbol());
			} else {
				pBuilder.append(getCharState().getDescription());
			}
		}
		return pBuilder;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValueAsSymbol(java.lang.StringBuilder,
	 *      org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	@Override
	public StringBuilder appendValueAsSymbol(StringBuilder pBuilder, CharacterMatrix pMatrix) {
		if (getCharState() == null) {
			if (isGap()) {
				pBuilder.append(pMatrix.getGapSymbol());
			} else {
				pBuilder.append(pMatrix.getMissingSymbol());
			}
		} else {
			pBuilder.append(getCharState().getSymbol());
		}
		return pBuilder;
	}

}
