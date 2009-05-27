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
import org.cipres.treebase.domain.TBPersistable;

/**
 * DiscreteCharState.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "DISCRETECHARSTATE")
@AttributeOverride(name = "id", column = @Column(name = "DISCRETECHARSTATE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class DiscreteCharState extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	public static final Character GAP_SYMBOL = '-';
	public static final Character MISSING_SYMBOL = '?';

	private String mDescription;
	private String mNotes;
	private Character mSymbol;

	private DiscreteChar mChar;

	/**
	 * Constructor.
	 */
	public DiscreteCharState() {
		super();
	}

	/**
	 * Return the Symbol field.
	 * 
	 * @return char mSymbol
	 */
	@Column(name = "Symbol", nullable = true)
	public Character getSymbol() {
		return mSymbol;
	}

	/**
	 * Set the Symbol field.
	 */
	public void setSymbol(Character pNewSymbol) {
		mSymbol = pNewSymbol;
	}
	
	/**
	 * Return the Char field.
	 * 
	 * @return DiscreteChar
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "PHYLOCHAR_ID", nullable = false)
	public DiscreteChar getChar() {
		return mChar;
	}

	/**
	 * Set the Char field.
	 */
	public void setChar(DiscreteChar pNewChar) {
		mChar = pNewChar;
	}

	/**
	 * Return the description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the description field.
	 */
	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

	/**
	 * Return the Notes field.
	 * 
	 * @return String mNotes
	 */
	@Column(name = "Notes", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getNotes() {
		return mNotes;
	}

	/**
	 * Set the Notes field.
	 */
	public void setNotes(String pNewNotes) {
		mNotes = pNewNotes;
	}

}
