/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * DistanceMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("T")
public class DistanceMatrix extends Matrix {

	private static final long serialVersionUID = 5338038890070277473L;
	private boolean mDiagonal;
	private String mTriangle;

	private Set<DistanceMatrixElement> mElements = new HashSet<DistanceMatrixElement>();

	/**
	 * Constructor.
	 */
	public DistanceMatrix() {
		super();
	}

	/**
	 * Return the Triangle field.
	 * 
	 * @return String
	 */
	@Column(name = "Triangle", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTriangle() {
		return mTriangle;
	}

	/**
	 * Set the Triangle field.
	 */
	public void setTriangle(String pNewTriangle) {
		mTriangle = pNewTriangle;
	}

	/**
	 * Return the Diagonal field.
	 * 
	 * @return boolean
	 */
	@Column(name = "Diagonal")
	public boolean isDiagonal() {
		return mDiagonal;
	}

	/**
	 * Set the Diagonal field.
	 */
	public void setDiagonal(boolean pNewDiagonal) {
		mDiagonal = pNewDiagonal;
	}

	/**
	 * Append a new element.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pElement DistanceMatrixElement
	 */
	public void addElement(DistanceMatrixElement pElement) {
		if (pElement != null) {
			getElements().add(pElement);
		}
	}

	/**
	 * Remove an element.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pElement DistanceMatrixElement
	 */
	public void removeElment(DistanceMatrixElement pElement) {
		if (pElement != null) {
			getElements().remove(pElement);
		}
	}

	/**
	 * Return an iterator for the elements.
	 * 
	 * @return
	 */
	@Transient
	public Iterator<DistanceMatrixElement> getElementIterator() {
		return getElements().iterator();
	}

	/**
	 * Return the Elements field.
	 * 
	 * @return Set<DistanceMatrixElement>
	 */
	@OneToMany(mappedBy = "matrix", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	protected Set<DistanceMatrixElement> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	protected void setElements(Set<DistanceMatrixElement> pNewElements) {
		mElements = pNewElements;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#cascadePersist(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	@Override
	public void cascadePersist(MatrixHome pMatrixHome) {
	// For now use Hibernate cascade persist.
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#cascadeDelete(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	@Override
	public void cascadeDelete(MatrixHome pMatrixHome) {

		// Cascade delete: remove all distance matrix elements:
		pMatrixHome.deleteAll(getElements());
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#getAllTaxonLabels()
	 */
	@Override
	@Transient
	public List<TaxonLabel> getAllTaxonLabels() {
		List<TaxonLabel> allTaxonLabels = new ArrayList<TaxonLabel>();

		for (DistanceMatrixElement element : getElements()) {
			allTaxonLabels.add(element.getRowLabel());
		}

		return allTaxonLabels;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#getDimensionsInfo()
	 */
	@Override
	@Transient
	public String getDimensionsInfo() {
		// TODO: getDimensions
		return "TO BE IMPLEMENTED";
	}

	@Override
	public void generateMatrixTable(StringBuilder pBuilder) {
		// TODO: getDimensions
		pBuilder.append("TO BE IMPLEMENTED");
	}

	@Override
	public void generateSetsNexusBlock(StringBuilder pBuilder) {
		// TODO: getDimensions
		pBuilder.append("TO BE IMPLEMENTED");
	}

	protected void generateAssumptionsNexusBlock(StringBuilder pBuilder) {
		// TODO: getDimensions
		pBuilder.append("TO BE IMPLEMENTED");
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#getnChar()
	 */
	public Integer getnChar() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#setnChar(java.lang.Integer)
	 */
	public void setnChar(Integer nChar) {
		return; // do nothing
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#getnTax()
	 */
	public Integer getnTax() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#setnTax(java.lang.Integer)
	 */
	public void setnTax(Integer tax) {
		return; // do nothing
	}

}
