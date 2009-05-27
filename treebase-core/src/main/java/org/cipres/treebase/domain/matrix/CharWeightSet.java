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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CharWeightSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARWEIGHTSET")
@AttributeOverride(name = "id", column = @Column(name = "CHARWEIGHTSET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharWeightSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;

	private Set<? extends CharWeight> mCharWeights = new HashSet<CharWeight>();

	/**
	 * Constructor.
	 */
	public CharWeightSet() {
		super();
	}

	/**
	 * Return the CharWeights field.
	 * 
	 * @return Set<CharWeight>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = CharWeight.class)
	@JoinColumn(name = "CHARWEIGHTSET_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<? extends CharWeight> getCharWeights() {
		return mCharWeights;
	}

	/**
	 * Set the CharWeights field.
	 */
	public void setCharWeights(Set<? extends CharWeight> pNewCharWeights) {
		mCharWeights = pNewCharWeights;
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

}
