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
