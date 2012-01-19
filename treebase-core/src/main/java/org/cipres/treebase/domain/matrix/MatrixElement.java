package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.study.Study;

/**
 * MatrixElement.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "MATRIXELEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "MATRIXELEMENT_ID"))
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@BatchSize(size = 10)
@DiscriminatorValue("-")
public abstract class MatrixElement extends AbstractPersistedObject {

	private MatrixColumn mColumn;
	private MatrixRow mRow;

	private Set<StateModifier> mStateModifiers = new HashSet<StateModifier>();

	// private Set<ItemValue> mItemValues;

	/**
	 * Constructor.
	 */
	public MatrixElement() {
		super();
	}

	/*
	 * Return the ItemValues field.
	 * 
	 * @return Set<ItemValue>
	 */
	// @OneToMany(mappedBy = "element", cascade = CascadeType.ALL)
	// public Set<ItemValue> getItemValues() {
	// return mItemValues;
	// }
	/*
	 * Set the ItemValues field.
	 */
	// public void setItemValues(Set<ItemValue> pNewItemValues) {
	// mItemValues = pNewItemValues;
	// }
	/**
	 * Return the StateModifiers field.
	 * 
	 * @return Set<StateModifier>
	 */
	@OneToMany(mappedBy = "element", cascade = CascadeType.ALL)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<StateModifier> getStateModifiers() {
		return mStateModifiers;
	}

	/**
	 * Set the StateModifiers field.
	 */
	public void setStateModifiers(Set<StateModifier> pNewStateModifiers) {
		mStateModifiers = pNewStateModifiers;
	}

	/**
	 * Return the Row field.
	 * 
	 * @return MatrixRow mRow
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATRIXROW_ID", insertable = false, updatable = false)
	// NOTE: see matrix -> matrixColumn, the nullable = false cause the add error code -407
	// @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	// @JoinColumn(name = "MATRIXROW_ID", insertable = false, updatable = false, nullable = false)
	@Index(name = "Element_ROW_IDX")
	public MatrixRow getRow() {
		return mRow;
	}

	/**
	 * Set the Row field.
	 */
	public void setRow(MatrixRow pNewRow) {
		mRow = pNewRow;
	}

	/**
	 * Return the Column field.
	 * 
	 * @return MatrixColumn
	 */
	@ManyToOne
	@JoinColumn(name = "MATRIXCOLUMN_ID", nullable = true)
	@Index(name = "Element_COL_IDX")
	@Fetch(FetchMode.JOIN)
	public MatrixColumn getColumn() {
		return mColumn;
	}

	/**
	 * Set the Column field.
	 */
	public void setColumn(MatrixColumn pNewColumn) {
		mColumn = pNewColumn;
	}

	/**
	 * Append a string representation to the string builder. Use the symbol representation
	 * if one is defined. 
	 * <nl>
	 * Return the string builder for call chaining.
	 * 
	 * @param pBuilder
	 * @return
	 */
	public abstract StringBuilder appendValue(StringBuilder pBuilder);

	/**
	 * Append the element as symbol to the symbol string.
	 * 
	 * @param pBuf
	 * @param pMatrix
	 * @return
	 */
	public abstract StringBuilder appendValueAsSymbol(StringBuilder pBuf, CharacterMatrix pMatrix);
	
	@Transient
	public CharacterMatrix getContext() {
		return getColumn().getMatrix();
	}
	
	@Transient
	public Study getStudy() {
		return getContext().getStudy();
	}
}
