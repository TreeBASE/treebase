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
@DiscriminatorValue("J")

public class JoiningAlgorithm extends Algorithm {
	/**
	 * Constructor.
	 */
	public JoiningAlgorithm() {
		super();
	}

	@Override
	@Transient
	public String getAlgorithmType() {
		return JoiningAlgorithm;
	}

}
