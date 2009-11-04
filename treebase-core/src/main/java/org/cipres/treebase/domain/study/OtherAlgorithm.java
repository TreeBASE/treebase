package org.cipres.treebase.domain.study;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * OtherAlgorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("O")
public class OtherAlgorithm extends Algorithm {

	/**
	 * Constructor.
	 */
	public OtherAlgorithm() {
		super();
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return OtherAlgorithm;
	}

}
