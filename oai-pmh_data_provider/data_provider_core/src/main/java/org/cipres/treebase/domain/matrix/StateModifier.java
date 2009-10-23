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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * StateModifier.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STATEMODIFIER")
@AttributeOverride(name = "id", column = @Column(name = "STATEMODIFIER_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class StateModifier extends AbstractPersistedObject {

	private static final long serialVersionUID = -5945363678676808194L;

	private Double mFrequency;
	private Integer mCount;

	private MatrixElement mElement;
	private StateFormat mFormat;
	private DiscreteCharState mCharState;

	/**
	 * Constructor.
	 */
	public StateModifier() {
		super();
	}

	/**
	 * Return the Count field.
	 * 
	 * @return Integer
	 */
	@Column(name = "Count", nullable = true)
	public Integer getCount() {
		return mCount;
	}

	/**
	 * Set the Count field.
	 */
	public void setCount(Integer pNewCount) {
		mCount = pNewCount;
	}

	/**
	 * Return the Frequency field.
	 * 
	 * @return Double
	 */
	@Column(name = "Frequency", nullable = true)
	public Double getFrequency() {
		return mFrequency;
	}

	/**
	 * Set the Frequency field.
	 */
	public void setFrequency(Double pNewFrequency) {
		mFrequency = pNewFrequency;
	}

	/**
	 * Return the Element field.
	 * 
	 * @return MatrixElement
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ELEMENT_ID", nullable = false)
	public MatrixElement getElement() {
		return mElement;
	}

	/**
	 * Set the Element field.
	 */
	public void setElement(MatrixElement pNewElement) {
		mElement = pNewElement;
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
	 * Return the Format field.
	 * 
	 * @return StateFormat
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STATEFORMAT_ID", nullable = false)
	public StateFormat getFormat() {
		return mFormat;
	}

	/**
	 * Set the Format field.
	 */
	public void setFormat(StateFormat pNewFormat) {
		mFormat = pNewFormat;
	}

}
