
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
