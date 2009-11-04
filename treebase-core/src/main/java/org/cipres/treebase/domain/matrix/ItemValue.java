package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * ItemValue.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ITEMVALUE")
@AttributeOverride(name = "id", column = @Column(name = "ITEMVALUE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class ItemValue extends AbstractPersistedObject {

	private static final long serialVersionUID = -4800441154716328554L;

	private String mValue;

	private MatrixElement mElement;

	/**
	 * Constructor.
	 */
	public ItemValue() {
		super();
	}

	/**
	 * Return the Value field.
	 * 
	 * @return String
	 */
	@Column(name = "Value", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getValue() {
		return mValue;
	}

	/**
	 * Set the Value field.
	 */
	public void setValue(String pNewValue) {
		mValue = pNewValue;
	}

	/**
	 * Return the Element field.
	 * 
	 * @return MatrixElement
	 */
//	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToOne
	@JoinColumn(name = "ELEMENT_ID", nullable = false)
	public MatrixElement getElement() {
		return mElement;
	}

	/**
	 * Set the Element field.
	 */
	public void setElement(MatrixElement pNewElement) {
		mElement = pNewElement;
	}

}
