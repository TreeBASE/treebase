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
