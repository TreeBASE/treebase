package org.cipres.treebase.domain.matrix;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CharSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARSET")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "CHARSET_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public abstract class CharSet extends AbstractPersistedObject {

	private String mTitle;

	private CharacterMatrix mMatrix;

	/**
	 * Constructor.
	 */
	public CharSet() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}
	
	/**
	 * This method is here to simplify NeXML generation. It overrides the 
	 * method in AbstractPersistedObject
	 * @return
	 */
	@Transient
	public String getLabel() {
		return getTitle();
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return CharacterMatrix mMatrix
	 */
	@ManyToOne
	@JoinColumn(name = "MATRIX_ID")
	public CharacterMatrix getMatrix() {
		return mMatrix;
	}

	/**
	 * Set the Matrix field.
	 */
	public void setMatrix(CharacterMatrix pNewMatrix) {
		mMatrix = pNewMatrix;
	}

	/**
	 * Get a collection of characters in this set.
	 * 
	 * Creation date: May 5, 2006 1:44:34 PM
	 * 
	 * @param pMatrix
	 * @return
	 */
	@Transient
	public abstract Collection<ColumnRange> getColumns(CharacterMatrix pMatrix);

	/**
	 * Return a Nexus formated string describing the char partition.
	 * 
	 * @return
	 */
	@Transient
	public String getNexusString() {
		return getNexusPrefix() + getTitle();
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	protected String getNexusPrefix() {
		return "CHARSET ";
	}

}
