package org.cipres.treebase.domain.matrix;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * ContinuousMatrixElement.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("N")
public class ContinuousMatrixElement extends MatrixElement {

	private static final long serialVersionUID = -1347946025058281105L;

	private double mValue;

	private ItemDefinition mDefinition;

	/**
	 * Constructor.
	 */
	public ContinuousMatrixElement() {
		super();
	}

	/**
	 * Return the Value field.
	 * 
	 * @return String
	 */
	@Column(name = "Value", nullable = true)
	public double getValue() {
		return mValue;
	}

	/**
	 * Set the Value field.
	 */
	public void setValue(double pNewValue) {
		mValue = pNewValue;
	}

	/**
	 * Return the Definition field.
	 * 
	 * @return ItemDefinition
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ITEMDEFINITION_ID", nullable = true)
	public ItemDefinition getDefinition() {
		return mDefinition;
	}

	/**
	 * Set the Definition field.
	 */
	public void setDefinition(ItemDefinition pNewDefinition) {
		mDefinition = pNewDefinition;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixElement#appendValue(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder appendValue(StringBuilder pBuilder) {
		return pBuilder.append(getValue()).append(" ");
	}

	@Override
	public StringBuilder appendValueAsSymbol(StringBuilder pBuf, CharacterMatrix pMatrix) {
		// should not even reach here since continuous data don't have symbol!
		return appendValue(pBuf);
	}

	// /*
	// * Assemeble a string reprenting the items values enclosed in (). Returns null if there is no
	// * items defined.
	// *
	// * @return
	// */
	// @Transient
	// protected String getItemValuesAsString() {
	// if (getItemValues() != null && !getItemValues().isEmpty()) {
	// StringBuffer buf = new StringBuffer();
	//
	// buf.append('(');
	// Iterator itemIter = getItemValues().iterator();
	// while (itemIter.hasNext()) {
	// ItemValue item = (ItemValue) itemIter.next();
	// buf.append(item.getValue());
	//
	// if (itemIter.hasNext()) {
	// buf.append(' ');
	// }
	// }
	// buf.append(')');
	//
	// return buf.toString();
	// }
	// return null;
	// }
	//
}
