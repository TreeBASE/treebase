package org.cipres.treebase.domain.tree;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * TreeAttribute.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREEATTRIBUTE")
@AttributeOverride(name = "id", column = @Column(name = "TREEATTRIBUTE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreeAttribute extends AbstractPersistedObject {

	private static final long serialVersionUID = 1788226643733096059L;

	/**
	 * Constructor.
	 */
	public TreeAttribute() {
		super();
	}

}
