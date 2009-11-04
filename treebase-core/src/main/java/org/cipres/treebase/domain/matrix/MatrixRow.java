package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * MatrixRow.java
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "MATRIXROW")
@AttributeOverride(name = "id", column = @Column(name = "MATRIXROW_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@BatchSize(size = 100)
public class MatrixRow extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private static final int DISPLAY_STRING_LENGTH = 30;

	private String mSymbolString;

	private TaxonLabel mTaxonLabel;
	private CharacterMatrix mMatrix;

	private Set<RowSegment> mSegments = new HashSet<RowSegment>();
	private List<MatrixElement> mElements = new ArrayList<MatrixElement>();

	// transient:
	private String mElementAsString;

	/**
	 * Constructor.
	 */
	public MatrixRow() {
		super();
	}

	/**
	 * Assemble a string representing the Elements .
	 * 
	 * @return Stringhttp://treebase-demo.cis.upenn.edu:8080/treebase-web/search/study/summary.html?id=4427
	 */
	@Transient
	public static String buildElementAsString(List<MatrixElement> pElements) {
			StringBuilder buf = new StringBuilder();

			for (MatrixElement element : pElements) {
				element.appendValue(buf);
			}

			return buf.toString();
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return StandardMatrix
	 */
	@ManyToOne
	@JoinColumn(name = "MATRIX_ID", insertable = false, updatable = false)
	// Note: the nullable = false cause the add error code -407, the matrix column is null!
	// @JoinColumn(name = "MATRIX_ID", insertable = false, updatable = false, nullable = false)
	@Index(name = "ROW_M_IDX")
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
	 * Return the TaxonLabel field.
	 * 
	 * @return TaxonLabel
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "TAXONLABEL_ID", nullable = false)
	public TaxonLabel getTaxonLabel() {
		return mTaxonLabel;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	public void setTaxonLabel(TaxonLabel pNewTaxonLabel) {
		mTaxonLabel = pNewTaxonLabel;
	}

	/**
	 * Return the Elements field.
	 * 
	 * Note: do NOT use this method directly. 
	 * 
	 * @return List<MatrixElement> 
	 */
	// Note: the true index, see characterMatrix-> column
	//@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	//@OneToMany(cascade = {CascadeType.MERGE})
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATRIXROW_ID")
	@IndexColumn(name = "ELEMENT_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public List<MatrixElement> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	public void setElements(List<MatrixElement> pNewElements) {
		mElements = pNewElements;
	}

	/**
	 * Represents all the elements as symbols in one string. 
	 * For one character one symbol, This can handle 512k size of characters in a matrix.
	 * 
	 * It is precalculated and stored for faster generating nexus blocks. 
	 * 
	 * @return String 
	 */
	@Lob
	@Column(name = "SymbolString", length = 524288)
	public String getSymbolString() {
		return mSymbolString;
	}

	/**
	 * Set the SymbolString field.
	 */
	public void setSymbolString(String pNewSymbolString) {
		mSymbolString = pNewSymbolString;
	}
	
	/**
	 * Add a row segment. Manage bi-directional relationship.
	 * 
	 * The caller is responsible to adjust the range of all the other segments to avoid overlap.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pSegment RowSegment
	 */
	public void addSegment(RowSegment pSegment) {
		if (pSegment != null && !getSegments().contains(pSegment)) {
			getSegments().add(pSegment);
			pSegment.setMatrixRow(this);
		}
	}

	/**
	 * Remove a row segment. Manage bi-directional relationship.
	 * 
	 * The caller is responsible to adjust the range of all the other segments to avoid overlap.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pSegment RowSegment
	 */
	public void removeSegment(RowSegment pSegment) {
		if (pSegment != null && getSegments().contains(pSegment)) {
			getSegments().remove(pSegment);
			pSegment.setMatrixRow(null);
		}
	}

	/**
	 * Return the Segments field.
	 * 
	 * @return Set<RowSegment>
	 */
	@OneToMany(mappedBy = "matrixRow", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	protected Set<RowSegment> getSegments() {
		return mSegments;
	}

	/**
	 * Set the Segments field.
	 */
	protected void setSegments(Set<RowSegment> pNewSegments) {
		mSegments = pNewSegments;
	}

	/**
	 * Returns a set of row segments. Access is restricted to read only.
	 * 
	 * @return
	 */
	@Transient
	public Set<RowSegment> getSegmentsReadOnly() {
		return Collections.unmodifiableSet(mSegments);
	}

	/**
	 * Add a matrix element. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pElement
	 */
	public void addElement(MatrixElement pElement) {
		if (pElement != null && !getElements().contains(pElement)) {
			getElements().add(pElement);
			pElement.setRow(this);
		}
	}

	/**
	 * Remove a matrix element. Manage bi-directional relationship.
	 * 
	 * Creation date: February 22, 2006 12:06:25 PM
	 * 
	 * @param pElement
	 */
	public void removeElement(MatrixElement pElement) {
		if (pElement != null && getElements().contains(pElement)) {
			getElements().remove(pElement);
			pElement.setRow(null);
		}
	}

	/**
	 * Return a read only set of matrix elements.
	 * 
	 * @return
	 */
	@Transient
	public List<MatrixElement> getElementsReadOnly() {
		return Collections.unmodifiableList(getElements());
	}

	/**
	 * Return element iterator.
	 * 
	 * @return
	 */
	@Transient
	public Iterator<MatrixElement> getElementIterator() {
		return getElements().iterator();
	}

	/**
	 * Assemble a string representing the Elements .
	 * 
	 * @return String
	 */
	@Transient
	private String getElementAsString() {
		if (mElementAsString == null) {
			StringBuilder buf = new StringBuilder();

			for (MatrixElement element : getElements()) {
				element.appendValue(buf);
			}

			mElementAsString = buf.toString();
		}
		return mElementAsString;
	}

	/**
	 * Returns a string representation of all elements. Use the symbolString field if it is set, 
	 * otherwise build the string from elements from scratch. 
	 * 
	 * @return String
	 */
	@Transient
	public String buildElementAsString() {
		if (!TreebaseUtil.isEmpty(getSymbolString())) {
			return getSymbolString();
		}
		
		return getElementAsString();
	}

	/**
	 * Show a string representing the Elements .
	 * 
	 * @return String
	 */
	@Transient
	public String getElementAsString(int pStart, int pEnd) {
		String allElements = getElementAsString();
		return StringUtils.substring(allElements, pStart, pEnd);
	}

	/**
	 * Assemble a string representing the Elements as symbols .
	 * 
	 * @return String
	 */
	@Transient
	public String getElementAsSymbols() {
		// if (mElementAsString == null) {
		StringBuilder buf = new StringBuilder();

		for (MatrixElement element : getElements()) {
			element.appendValueAsSymbol(buf, getMatrix());
		}

		return buf.toString();
	}

	/**
	 * Clear the cached element states as a string.
	 * 
	 */
	public void clearElementStates() {
		mElementAsString = null;
	}
	
	/**
	 * @return string of length <= 30 representing first elements, or "(none)"
	 * @author mjd 200807
	 */
	@Transient
	public String getElementStringForDisplay() {
		String syms = getSymbolString();
		if (syms == null) { return "(no data)"; }
		return syms.length() >= DISPLAY_STRING_LENGTH ? syms.substring(0, DISPLAY_STRING_LENGTH) : syms; 
	}
}
