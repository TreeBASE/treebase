
package org.cipres.treebase.domain.matrix;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * SequenceMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("Q")
public class SequenceMatrix extends DiscreteMatrix {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public SequenceMatrix() {
		super();
	}

}
