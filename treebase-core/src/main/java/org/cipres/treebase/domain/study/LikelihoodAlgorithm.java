package org.cipres.treebase.domain.study;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * LikelihoodAlgorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("L")
public class LikelihoodAlgorithm extends Algorithm {

	/**
	 * Constructor.
	 */
	public LikelihoodAlgorithm() {
		super();
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return LikelihoodAlgorithm;
	}

}
