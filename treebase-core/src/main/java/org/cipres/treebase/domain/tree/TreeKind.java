package org.cipres.treebase.domain.tree;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * Another tree property: describing the "kind" of the tree. Please also see the "Tree Type". 
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREEKIND")
@AttributeOverride(name = "id", column = @Column(name = "TREEKIND_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class TreeKind extends AbstractPersistedObject {

	//default tree type is: species
	public static final String KIND_SPECIES = "Species Tree";
	public static final String KIND_GENE = "Gene Tree";
	public static final String KIND_LANGUAGE = "Language Tree";
	public static final String KIND_AREA = "Area Tree";
	public static final String KIND_BARCODE = "Barcode Tree";
	public static final String KIND_OBJ_CLASSIFICATION = "Object Classification Tree";

	private String mDescription;

	/**
	 * Constructor.
	 */
	public TreeKind() {
		super();
	}
	
	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_100)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if it is species tree.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isSpeciesTree() {
		return KIND_SPECIES.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
