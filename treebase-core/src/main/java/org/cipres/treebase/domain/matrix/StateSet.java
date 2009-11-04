package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;
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
 * StateSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STATESET")
@AttributeOverride(name = "id", column = @Column(name = "STATESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class StateSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 750958962696488206L;

	private String mTitle;

	private Collection<DiscreteCharState> mStates = new ArrayList<DiscreteCharState>();

	/**
	 * Constructor.
	 */
	public StateSet() {
		super();
	}

	/**
	 * Return the States field.
	 * 
	 * @return Collection<DiscreteCharState>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "STATESET_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<DiscreteCharState> getStates() {
		return mStates;
	}

	/**
	 * Set the States field.
	 */
	public void setStates(Set<DiscreteCharState> pNewStates) {
		mStates = pNewStates;
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
