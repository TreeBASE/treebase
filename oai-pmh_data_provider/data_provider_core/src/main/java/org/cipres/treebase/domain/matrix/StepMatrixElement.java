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
