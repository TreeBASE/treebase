
package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * ContinuousMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("N")
public class ContinuousMatrix extends CharacterMatrix {

	private static final long serialVersionUID = -707595581486261067L;

	private Set<ItemDefinition> mItemDefinitions = new HashSet<ItemDefinition>();

	/**
	 * Constructor.
	 */
	public ContinuousMatrix() {
		super();
	}

	/**
	 * Return the ItemDefinitions field.
	 * 
	 * @return Set<ItemDefinition>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "MATRIX_ITEMDEFINITION", joinColumns = {@JoinColumn(name = "MATRIX_ID")}, inverseJoinColumns = @JoinColumn(name = "ITEMDEFINITION_ID"))
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<ItemDefinition> getItemDefinitions() {
		return mItemDefinitions;
	}

	/**
	 * Set the ItemDefinitions field.
	 */
	public void setItemDefinitions(Set<ItemDefinition> pNewItemDefinitions) {
		mItemDefinitions = pNewItemDefinitions;
	}

}
