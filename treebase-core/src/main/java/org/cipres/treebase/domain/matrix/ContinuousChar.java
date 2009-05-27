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
