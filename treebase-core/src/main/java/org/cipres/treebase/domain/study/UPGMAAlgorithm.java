package org.cipres.treebase.domain.study;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * UPGMAAlgorithm.java
 * 
 * Created on Feb 23, 2010
 * 
 * @author Youjun Guo
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("U")
public class UPGMAAlgorithm extends Algorithm {

	/**
	 * Constructor.
	 */
	public UPGMAAlgorithm() {
		super();
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return UPGMAAlgorithm;
	}

}
