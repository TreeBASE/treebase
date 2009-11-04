package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * StepMatrix.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("S")
public class StepMatrix extends UserType {

	private static final long serialVersionUID = 3897186216591603446L;

	private Set<StepMatrixElement> mElements = new HashSet<StepMatrixElement>();

	/**
	 * Constructor.
	 */
	public StepMatrix() {
		super();
	}

	/**
	 * Return the Elements field.
	 * 
	 * @return Set<StepMatrixElement>
	 */
	@OneToMany(mappedBy = "stepMatrix", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<StepMatrixElement> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	public void setElements(Set<StepMatrixElement> pNewElements) {
		mElements = pNewElements;
	}

}
