package org.cipres.treebase.domain.matrix;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * PredefinedCharType.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("P")
public class PredefinedCharType extends UserType {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PredefinedCharType() {
		super();
	}

}
