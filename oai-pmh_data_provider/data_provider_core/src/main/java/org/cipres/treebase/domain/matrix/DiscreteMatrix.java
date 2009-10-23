/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * DiscreteMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("D")
public class DiscreteMatrix extends CharacterMatrix {

	private static final long serialVersionUID = 1L;

	private boolean mAligned;

	private Collection<StateSet> mStateSets = new ArrayList<StateSet>();

	//Transient
	private List<List<DiscreteCharState>> mStateLabels;

	/**
	 * Constructor.
	 */
	public DiscreteMatrix() {
		super();
	}

	/**
	 * Return the StateSets field.
	 * 
	 * @return Collection<StateSet>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<StateSet> getStateSets() {
		return mStateSets;
	}

	/**
	 * Set the StateSets field.
	 */
	public void setStateSets(Collection<StateSet> pNewStateSets) {
		mStateSets = pNewStateSets;
	}

	/**
	 * Return the Aligned field.
	 * 
	 * @return boolean
	 */
	@Column(name = "Aligned")
	public boolean isAligned() {
		return mAligned;
	}

	/**
	 * Set the Aligned field.
	 */
	public void setAligned(boolean pNewAligned) {
		mAligned = pNewAligned;
	}

	/**
	 * Store the statelabels. First outer list corresponding to columns. Inner list corresponding to
	 * discreteStates.
	 * 
	 * The "STATELABELS" are used for generating nexus file. It is a transient property to make sure
	 * the same statelabels list is used for the entire matrix.
	 * 
	 * Return the StateLabels field.
	 * 
	 * @return List<List<DiscreteCharState>> mStateLabels
	 */
	@Transient
	public List<List<DiscreteCharState>> getStateLabels() {
		if (mStateLabels == null) {
			// construct:

			// create list only for non-sequence data types:
			MatrixDataType dataType = getDataType();
			if (dataType == null || !dataType.isSequence()) {

				int columnSize = getColumns().size();
				mStateLabels = new ArrayList<List<DiscreteCharState>>(columnSize);
				for (int i = 0; i < columnSize; i++) {
					MatrixColumn aColumn = getColumns().get(i);
					DiscreteChar aDiscreteChar = (DiscreteChar) aColumn.getCharacter();
					// TODO: likely get NPE here...

					mStateLabels.add(aDiscreteChar.getCharStateList());
				}
			}
		}
		return mStateLabels;
	}

	/**
	 * Return a list of char states for a given column.
	 * 
	 * @return List<DiscreteCharState>
	 */
	@Transient
	public List<DiscreteCharState> getStateList(MatrixColumn pColumn) {
		if (getStateLabels() == null || pColumn == null) {
			return null;
		}

		int index = getColumns().indexOf(pColumn);
		return getStateLabels().get(index);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharacterMatrix#cascadeDelete(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	// @Override
	// public void cascadeDelete(MatrixHome pMatrixHome) {
	// super.cascadeDelete(pMatrixHome);
	//
	// // Cascade delete: remove all state sets:
	// pMatrixHome.deleteAll(getStateSets());
	// }
}
