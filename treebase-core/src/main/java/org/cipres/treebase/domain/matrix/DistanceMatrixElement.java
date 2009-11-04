package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * DistanceMatrixElement.java
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "DistanceMatrixElement")
@AttributeOverride(name = "id", column = @Column(name = "DistanceMatrixElement_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class DistanceMatrixElement extends AbstractPersistedObject {

	private static final long serialVersionUID = 1918964095607788877L;

	private Double mDistance;

	private TaxonLabel mRowLabel;
	private TaxonLabel mColumnLabel;
	private DistanceMatrix mMatrix;

	/**
	 * Constructor.
	 */
	public DistanceMatrixElement() {
		super();
	}

	/**
	 * Return the Distance field.
	 * 
	 * @return Double
	 */
	@Column(name = "DISTANCE", nullable = true)
	public Double getDistance() {
		return mDistance;
	}

	/**
	 * Set the Distance field.
	 */
	public void setDistance(Double pNewDistance) {
		mDistance = pNewDistance;
	}

	/**
	 * Return the ColumnLabel field.
	 * 
	 * @return TaxonLabel
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "COLUMNLABEL_ID", nullable = true)
	public TaxonLabel getColumnLabel() {
		return mColumnLabel;
	}

	/**
	 * Set the ColumnLabel field.
	 */
	public void setColumnLabel(TaxonLabel pNewColumnLabel) {
		mColumnLabel = pNewColumnLabel;
	}

	/**
	 * Return the RowLabel field.
	 * 
	 * @return TaxonLabel
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ROWLABEL_ID", nullable = false)
	public TaxonLabel getRowLabel() {
		return mRowLabel;
	}

	/**
	 * Set the RowLabel field.
	 */
	public void setRowLabel(TaxonLabel pNewRowLabel) {
		mRowLabel = pNewRowLabel;
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return DistanceMatrix mMatrix
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "MATRIX_ID", nullable = false)
	public DistanceMatrix getMatrix() {
		return mMatrix;
	}

	/**
	 * Set the Matrix field.
	 */
	public void setMatrix(DistanceMatrix pNewMatrix) {
		mMatrix = pNewMatrix;
	}

}
