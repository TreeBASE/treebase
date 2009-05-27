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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ContinuousAncState.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("D")
public class DiscreteAncState extends AncestralState {

	private static final long serialVersionUID = 1L;

	private DiscreteCharState mAncestralState;
	private Set<DiscreteCharState> mChildStates = new HashSet<DiscreteCharState>();

	/**
	 * Constructor.
	 */
	public DiscreteAncState() {
		super();
	}

	/**
	 * Return the ChildStates field.
	 * 
	 * @return Set<DiscreteCharState>
	 */
	@OneToMany
	@JoinColumn(name = "ANCESTRALSTATE_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<DiscreteCharState> getChildStates() {
		return mChildStates;
	}

	/**
	 * Set the ChildStates field.
	 */
	public void setChildStates(Set<DiscreteCharState> pNewChildStates) {
		mChildStates = pNewChildStates;
	}

	/**
	 * Return the AncestralState field.
	 * 
	 * @return DiscreteCharState
	 */
	@OneToOne
	@JoinColumn(name = "DISCRETECHARSTATE_ID", nullable = true)
	public DiscreteCharState getAncestralState() {
		return mAncestralState;
	}

	/**
	 * Set the AncestralState field.
	 */
	public void setAncestralState(DiscreteCharState pNewAncestralState) {
		mAncestralState = pNewAncestralState;
	}

}
