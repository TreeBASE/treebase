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
