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
