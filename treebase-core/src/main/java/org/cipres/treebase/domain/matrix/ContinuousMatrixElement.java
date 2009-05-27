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

/**
 * ContinuousMatrixElement.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("N")
public class ContinuousMatrixElement extends MatrixElement {

	private static final long serialVersionUID = -1347946025058281105L;

	private double mValue;

	private ItemDefinition mDefinition;

	/**
	 * Constructor.
	 */
	public ContinuousMatrixElement() {
		super();
	}

	/**
	 * Return the Value field.
	 * 
	 * @return String
	 */
	@Column(name = "Value", nullable = true)
	public double getValue() {
		return mValue;
	}

	/**
	 * Set the Value field.
	 */
	public void setValue(double pNewValue) {
		mValue = pNewValue;
	}

	/**
	 * Return the Definition field.
	 * 
	 * @return ItemDefinition
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ITEMDEFINITION_ID", nullable = true)
	public ItemDefinition getDefinition() {
		return mDefinition;
	}

	/**
	 * Set the Definition field.
	 */
	public void setDefinition(ItemDefinition pNewDefinition) {
		mDefinition = pNewDefinition;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValue(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendValue(StringBuilder pBuilder) {
		return pBuilder.append(getValue()).append(" ");
	}

	@Override
	public StringBuilder appendValueAsSymbol(StringBuilder pBuf, CharacterMatrix pMatrix) {
		// should not even reach here since continuous data don't have symbol!
		return appendValue(pBuf);
	}

	// /*
	// * Assemeble a string reprenting the items values enclosed in (). Returns null if there is no
	// * items defined.
	// *
	// * @return
	// */
	// @Transient
	// protected String getItemValuesAsString() {
	// if (getItemValues() != null && !getItemValues().isEmpty()) {
	// StringBuffer buf = new StringBuffer();
	//
	// buf.append('(');
	// Iterator itemIter = getItemValues().iterator();
	// while (itemIter.hasNext()) {
	// ItemValue item = (ItemValue) itemIter.next();
	// buf.append(item.getValue());
	//
	// if (itemIter.hasNext()) {
	// buf.append(' ');
	// }
	// }
	// buf.append(')');
	//
	// return buf.toString();
	// }
	// return null;
	// }
	//
}
