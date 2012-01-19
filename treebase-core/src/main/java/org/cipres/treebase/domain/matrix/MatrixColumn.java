package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.study.Study;

/**
 * MatrixColumn.java
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "MATRIXCOLUMN")
@AttributeOverride(name = "id", column = @Column(name = "MATRIXCOLUMN_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@BatchSize(size = 10)
public class MatrixColumn extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private CharacterMatrix mMatrix;
	private PhyloChar mCharacter;
	private StateFormat mStateFormat;

	//Note: as requested, each column can have different item definitions. 
	//However, nexus file only supports a set of item definitions for the entire matrix. 
	//That information is stored at ContinuousMatrx.itemDefinitions. 
	
	//@see ContinuousMatrx.itemDefinitions. 
	private Set<ItemDefinition> mItemDefinitions = new HashSet<ItemDefinition>();

	/**
	 * Constructor.
	 * 
	 */
	public MatrixColumn() {
		super();
	}

	/**
	 * Return the default item.
	 * 
	 * Creation date: Mar 21, 2006 4:32:35 PM
	 */
	@Transient
	public void getDefaultItem() {
	// TODO: later.
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return CharacterMatrix
	 */
	@ManyToOne
	@JoinColumn(name = "MATRIX_ID", insertable = false, updatable = false)
	// the nullable = false cause the add error code -407, the matrix column is null!
	// @JoinColumn(name = "MATRIX_ID", insertable = false, updatable = false, nullable = false)
	@Index(name = "COLUMN_M_IDX")
	@Fetch(FetchMode.JOIN)
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
	 * Return the PhyloChar field.
	 * 
	 * @return PhyloChar
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "PHYLOCHAR_ID", nullable = true)
	public PhyloChar getCharacter() {
		return mCharacter;
	}

	/**
	 * Set the PhyloChar field.
	 */
	public void setCharacter(PhyloChar pNewCharacter) {
		mCharacter = pNewCharacter;
	}

	/**
	 * Return the StateFormat field.
	 * 
	 * @return StateFormat
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "STATEFORMAT_ID", nullable = true)
	public StateFormat getStateFormat() {
		return mStateFormat;
	}

	/**
	 * Set the StateFormat field.
	 */
	public void setStateFormat(StateFormat pNewStateFormat) {
		mStateFormat = pNewStateFormat;
	}

	/**
	 * Return the ItemDefinitions field.
	 * 
	 * @return Set<ItemDefinition>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "MATRIXCOLUMN_ITEMDEFINITION", joinColumns = {@JoinColumn(name = "MATRIXCOLUMN_ID")}, inverseJoinColumns = @JoinColumn(name = "ITEMDEFINITION_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<ItemDefinition> getItemDefinitions() {
		return mItemDefinitions;
	}

	/**
	 * Set the ItemDefinitions field.
	 */
	public void setItemDefinitions(Set<ItemDefinition> pNewItemDefinitions) {
		mItemDefinitions = pNewItemDefinitions;
	}
	
	@Transient
	public Integer getColumnOrder() {
		CharacterMatrix theMatrix = getMatrix();
		if (theMatrix == null) { return null; }
		return theMatrix.getColumnsReadOnly().indexOf(this);
	}
	
	@Transient
	public Study getStudy() {
		return getMatrix().getStudy();
	}
}
