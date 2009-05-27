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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;

import org.cipres.treebase.domain.TBPersistable;

/**
 * ContinuousAncState.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("C")
public class ContinuousAncState extends AncestralState {

	private static final long serialVersionUID = 1L;

	private String mAncValue;

	private Set<String> mChildValues = new HashSet<String>();

	/**
	 * Constructor.
	 */
	public ContinuousAncState() {
		super();
	}

	/**
	 * Return the AncValue field.
	 * 
	 * @return String
	 */
	@Column(name = "AncValue", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getAncValue() {
		return mAncValue;
	}

	/**
	 * Set the AncValue field.
	 */
	public void setAncValue(String pNewAncValue) {
		mAncValue = pNewAncValue;
	}

	/**
	 * Return the ChildValues field.
	 * 
	 * @return Set
	 */
	@CollectionOfElements
	@JoinTable(name = "CONTANCSTATE_VALUE", joinColumns = @JoinColumn(name = "ANCSTATE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<String> getChildValues() {
		return mChildValues;
	}

	/**
	 * Set the ChildValues field.
	 */
	public void setChildValues(Set<String> pNewChildValues) {
		mChildValues = pNewChildValues;
	}

}
