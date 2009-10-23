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
package org.cipres.treebase.domain.study;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cipres.treebase.domain.matrix.GapMode;
import org.cipres.treebase.domain.matrix.PolyTCount;
import org.cipres.treebase.domain.matrix.UserType;

/**
 * ParsimonyAlgorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("P")
public class ParsimonyAlgorithm extends Algorithm {

	private UserType mDefaultType;
	private GapMode mGapMode;
	private PolyTCount mPolyTCount;

	/**
	 * Constructor.
	 */
	public ParsimonyAlgorithm() {
		super();
	}

	/**
	 * Return the DefaultType field.
	 * 
	 * @return UserType mDefaultType
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "USERTYPE_ID", nullable = true)
	public UserType getDefaultType() {
		return mDefaultType;
	}

	/**
	 * Set the DefaultType field.
	 */
	public void setDefaultType(UserType pNewDefaultType) {
		mDefaultType = pNewDefaultType;
	}

	/**
	 * Return the PolyTCount field.
	 * 
	 * @return PolyTCount mPolyTCount
	 */
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "PolyTCount_ID", nullable = true)
	public PolyTCount getPolyTCount() {
		return mPolyTCount;
	}

	/**
	 * Set the PolyTCount field.
	 */
	public void setPolyTCount(PolyTCount pNewPolyTCount) {
		mPolyTCount = pNewPolyTCount;
	}

	/**
	 * Return the GapMode field.
	 * 
	 * @return GapMode mGapMode
	 */
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "GAPMODE_ID", nullable = true)
	public GapMode getGapMode() {
		return mGapMode;
	}

	/**
	 * Set the GapMode field.
	 */
	public void setGapMode(GapMode pNewGapMode) {
		mGapMode = pNewGapMode;
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return ParsimonyAlgorithm;
	}

}
