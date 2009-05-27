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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.cipres.treebase.domain.TBPersistable;

/**
 * CompoundMatrixElement.java
 * Created on Mar 27, 2006
 * 
 * For matrix elements in a Compound, their column references are set but not the
 * row references. 
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("C")
public class CompoundMatrixElement extends MatrixElement {

	private static final long serialVersionUID = -178256912792612747L;

	private boolean mAndLogic;
	private String mCompoundValue;

	private Set<? extends MatrixElement> mElements = new HashSet<MatrixElement>();

	/**
	 * Constructor.
	 */
	public CompoundMatrixElement() {
		super();
	}

	/**
	 * Return the Elements field.
	 * 
	 * @return Set<MatrixElement>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = MatrixElement.class)
	@JoinTable(name = "COMPOUND_ELEMENT", joinColumns = {@JoinColumn(name = "COMPOUND_ID")}, inverseJoinColumns = @JoinColumn(name = "ELEMENT_ID"))
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<? extends MatrixElement> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	public void setElements(Set<? extends MatrixElement> pNewElements) {
		mElements = pNewElements;
	}

	/**
	 * Return the CompoundValue field.
	 * 
	 * @return String mCompoundValue
	 */
	@Column(name = "CompoundValue", length = TBPersistable.COLUMN_LENGTH_STRING_1K)
	public String getCompoundValue() {
		return mCompoundValue;
	}

	/**
	 * It is a string representing all the elements in this compound element. It is the parser's
	 * responsiblility to set the compound values as string.
	 * 
	 * @param pNewCompoundValue
	 */
	public void setCompoundValue(String pNewCompoundValue) {
		mCompoundValue = pNewCompoundValue;
	}

	/**
	 * Return the AndLogic field.
	 * 
	 * @return boolean
	 */
	@Column(name = "AndLogic")
	public boolean isAndLogic() {
		return mAndLogic;
	}

	/**
	 * Set the AndLogic field.
	 */
	public void setAndLogic(boolean pNewAndLogic) {
		mAndLogic = pNewAndLogic;
	}

	/**
	 * Use the pre calculated compound value field.
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValue(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendValue(StringBuilder pBuilder) {
		return pBuilder.append(getCompoundValue()).append(' ');
		
		// FIXME: wait for jdbc version of compound elements
		// we cannot use pre calculated compoundvalue since we now need to use the
		// dynamically calculated
		// statelabels and symbols.

		// StringBuffer buf = new StringBuffer();
		//
		// buf.append('{');
		// Iterator<? extends MatrixElement> elementIter = getElements().iterator();
		// while (elementIter.hasNext()) {
		// MatrixElement item = elementIter.next();
		// buf.append(item.getValueAsString());
		//
		// if (elementIter.hasNext()) {
		// buf.append(' ');
		// }
		// }
		// buf.append('}');
	}

	@Override
	public StringBuilder appendValueAsSymbol(StringBuilder pBuf, CharacterMatrix pMatrix) {
		// FIXME
		return pBuf.append("TODO Compound ---");
	}

}
