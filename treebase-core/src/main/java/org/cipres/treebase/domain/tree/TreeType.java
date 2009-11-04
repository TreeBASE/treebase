package org.cipres.treebase.domain.tree;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeType.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREETYPE")
@AttributeOverride(name = "id", column = @Column(name = "TREETYPE_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class TreeType extends AbstractPersistedObject {

	private static final long serialVersionUID = 6628295672594368539L;

	public static final String TYPE_SINGLE = "Single";
	public static final String TYPE_CONSENSUS = "Consensus";
	public static final String TYPE_SUPERTREE = "SuperTree";

	private String mDescription;

	/**
	 * Constructor.
	 */
	public TreeType() {
		super();
	}
	
	// Strings here correspond to values found in the treebase 1 dump file
	// The if-elsif tree should probably be replaced with more generic logic,
	// or perhaps a Map.  20080328 mjd
	public TreeType(String pDescription) {
		if (pDescription.equals("Simple Tree")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("single")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("Single Tree")) { pDescription = TYPE_SINGLE; }
		else if (pDescription.equals("Consensus Tree")) { pDescription = TYPE_CONSENSUS; }
		else if (pDescription.equals("consensus")) { pDescription = TYPE_CONSENSUS; }
		
		mDescription = pDescription;
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if type is single tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSingleTree() {
		return TYPE_SINGLE.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if type is consensus tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isConsensusTree() {
		return TYPE_CONSENSUS.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if type is super tree
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSuperTree() {
		return TYPE_SUPERTREE.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
