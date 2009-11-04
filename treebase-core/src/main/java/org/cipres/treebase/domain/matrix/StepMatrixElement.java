package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * StepMatrixElement.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STEPMATRIXELEMENT")
@AttributeOverride(name = "id", column = @Column(name = "DISCRETECHARSTATE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class StepMatrixElement extends AbstractPersistedObject {

	private static final long serialVersionUID = 3106836119774684421L;

	private Double mTransCost;

	private DiscreteCharState mState1;
	private DiscreteCharState mState2;
	private StepMatrix mStepMatrix;

	/**
	 * Constructor.
	 */
	public StepMatrixElement() {
		super();
	}

	/**
	 * Return the StepMatrix field.
	 * 
	 * @return StepMatrix
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STEPMATRIX_ID", nullable = false)
	public StepMatrix getStepMatrix() {
		return mStepMatrix;
	}

	/**
	 * Set the StepMatrix field.
	 */
	public void setStepMatrix(StepMatrix pNewStepMatrix) {
		mStepMatrix = pNewStepMatrix;
	}

	/**
	 * Return the State2 field.
	 * 
	 * @return DiscreteCharState
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STATE2_ID", nullable = true)
	public DiscreteCharState getState2() {
		return mState2;
	}

	/**
	 * Set the State2 field.
	 */
	public void setState2(DiscreteCharState pNewState2) {
		mState2 = pNewState2;
	}

	/**
	 * Return the State1 field.
	 * 
	 * @return DiscreteCharState
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STATE1_ID", nullable = true)
	public DiscreteCharState getState1() {
		return mState1;
	}

	/**
	 * Set the State1 field.
	 */
	public void setState1(DiscreteCharState pNewState1) {
		mState1 = pNewState1;
	}

	/**
	 * Return the TransCost field.
	 * 
	 * @return Double
	 */
	@Column(name = "TransCost", nullable = true)
	public Double getTransCost() {
		return mTransCost;
	}

	/**
	 * Set the TransCost field.
	 */
	public void setTransCost(Double pNewTransCost) {
		mTransCost = pNewTransCost;
	}

}
