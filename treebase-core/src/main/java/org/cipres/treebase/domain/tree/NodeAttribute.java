package org.cipres.treebase.domain.tree;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * NodeAttribute.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "NODEATTRIBUTE")
@AttributeOverride(name = "id", column = @Column(name = "NODEATTRIBUTE_ID"))
public class NodeAttribute extends AbstractPersistedObject {

	/**
	 * Constructor.
	 */
	public NodeAttribute() {
		super();
	}

}
