package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.cipres.treebase.domain.TBPersistable;

/**
 * CompoundMatrixElement.java
 * Created on Mar 27, 2006
 * 
 * For matrix elements in a Compound, their column references are set but not the
 * row references. 
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("C")
public class CompoundMatrixElement extends MatrixElement {

	private static final long serialVersionUID = -178256912792612747L;

	private boolean mAndLogic;
	private String mCompoundValue;

	private Set<? extends MatrixElement> mElements = new HashSet<MatrixElement>();

	/**
	 * Constructor.
	 */
	public CompoundMatrixElement() {
		super();
	}

	/**
	 * Return the Elements field.
	 * 
	 * @return Set<MatrixElement>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = MatrixElement.class)
	@JoinTable(name = "COMPOUND_ELEMENT", joinColumns = {@JoinColumn(name = "COMPOUND_ID")}, inverseJoinColumns = @JoinColumn(name = "ELEMENT_ID"))
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<? extends MatrixElement> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	public void setElements(Set<? extends MatrixElement> pNewElements) {
		mElements = pNewElements;
	}

	/**
	 * Return the CompoundValue field.
	 * 
	 * @return String mCompoundValue
	 */
	@Column(name = "CompoundValue", length = TBPersistable.COLUMN_LENGTH_STRING_1K)
	public String getCompoundValue() {
		return mCompoundValue;
	}

	/**
	 * It is a string representing all the elements in this compound element. It is the parser's
	 * responsiblility to set the compound values as string.
	 * 
	 * @param pNewCompoundValue
	 */
	public void setCompoundValue(String pNewCompoundValue) {
		mCompoundValue = pNewCompoundValue;
	}

	/**
	 * Return the AndLogic field.
	 * 
	 * @return boolean
	 */
	@Column(name = "AndLogic")
	public boolean isAndLogic() {
		return mAndLogic;
	}

	/**
	 * Set the AndLogic field.
	 */
	public void setAndLogic(boolean pNewAndLogic) {
		mAndLogic = pNewAndLogic;
	}

	/**
	 * Use the pre calculated compound value field.
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValue(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendValue(StringBuilder pBuilder) {
		return pBuilder.append(getCompoundValue()).append(' ');
		
		// FIXME: wait for jdbc version of compound elements
		// we cannot use pre calculated compoundvalue since we now need to use the
		// dynamically calculated
		// statelabels and symbols.

		// StringBuffer buf = new StringBuffer();
		//
		// buf.append('{');
		// Iterator<? extends MatrixElement> elementIter = getElements().iterator();
		// while (elementIter.hasNext()) {
		// MatrixElement item = elementIter.next();
		// buf.append(item.getValueAsString());
		//
		// if (elementIter.hasNext()) {
		// buf.append(' ');
		// }
		// }
		// buf.append('}');
	}

	@Override
	public StringBuilder appendValueAsSymbol(StringBuilder pBuf, CharacterMatrix pMatrix) {
		// FIXME
		return pBuf.append("TODO Compound ---");
	}

}
