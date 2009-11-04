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
