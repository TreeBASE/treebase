package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * StateChangeSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STATECHANGESET")
@AttributeOverride(name = "id", column = @Column(name = "STATECHANGESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class StateChangeSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 3905335590716069677L;

	private String mTitle;
	private boolean mReversible;

	private Collection<DiscreteCharState> mLeftCharSet = new ArrayList<DiscreteCharState>();
	private Collection<DiscreteCharState> mRightCharSet = new ArrayList<DiscreteCharState>();

	/**
	 * Constructor.
	 */
	public StateChangeSet() {
		super();
	}

	/**
	 * Return the RightCharSet field.
	 * 
	 * @return
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "RIGHTCHANGESET_CHARSTATE", joinColumns = {@JoinColumn(name = "STATECHANGESET_ID")}, inverseJoinColumns = @JoinColumn(name = "DISCRETECHARSTATE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<DiscreteCharState> getRightCharSet() {
		return mRightCharSet;
	}

	/**
	 * Set the RightCharSet field.
	 */
	public void setRightCharSet(Collection<DiscreteCharState> pNewRightCharSet) {
		mRightCharSet = pNewRightCharSet;
	}

	/**
	 * Return the LeftCharSet field.
	 * 
	 * @return
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "LEFTCHANGESET_CHARSTATE", joinColumns = {@JoinColumn(name = "STATECHANGESET_ID")}, inverseJoinColumns = @JoinColumn(name = "DISCRETECHARSTATE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<DiscreteCharState> getLeftCharSet() {
		return mLeftCharSet;
	}

	/**
	 * Set the LeftCharSet field.
	 */
	public void setLeftCharSet(Collection<DiscreteCharState> pNewLeftCharSet) {
		mLeftCharSet = pNewLeftCharSet;
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

	/**
	 * Return the Reversible field.
	 * 
	 * @return boolean
	 */
	@Column(name = "Reversible")
	public boolean getReversible() {
		return mReversible;
	}

	/**
	 * Set the Reversible field.
	 */
	public void setReversible(boolean pNewReversible) {
		mReversible = pNewReversible;
	}

}
