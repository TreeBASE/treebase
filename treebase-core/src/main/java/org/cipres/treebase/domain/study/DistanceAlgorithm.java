package org.cipres.treebase.domain.study;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * DistanceAlgorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("D")
public class DistanceAlgorithm extends Algorithm {

	/**
	 * Constructor.
	 */
	public DistanceAlgorithm() {
		super();
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return DistanceAlgorithm;
	}

}
