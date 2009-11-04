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

/**
 * StateModifier.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STATEMODIFIER")
@AttributeOverride(name = "id", column = @Column(name = "STATEMODIFIER_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class StateModifier extends AbstractPersistedObject {

	private static final long serialVersionUID = -5945363678676808194L;

	private Double mFrequency;
	private Integer mCount;

	private MatrixElement mElement;
	private StateFormat mFormat;
	private DiscreteCharState mCharState;

	/**
	 * Constructor.
	 */
	public StateModifier() {
		super();
	}

	/**
	 * Return the Count field.
	 * 
	 * @return Integer
	 */
	@Column(name = "Count", nullable = true)
	public Integer getCount() {
		return mCount;
	}

	/**
	 * Set the Count field.
	 */
	public void setCount(Integer pNewCount) {
		mCount = pNewCount;
	}

	/**
	 * Return the Frequency field.
	 * 
	 * @return Double
	 */
	@Column(name = "Frequency", nullable = true)
	public Double getFrequency() {
		return mFrequency;
	}

	/**
	 * Set the Frequency field.
	 */
	public void setFrequency(Double pNewFrequency) {
		mFrequency = pNewFrequency;
	}

	/**
	 * Return the Element field.
	 * 
	 * @return MatrixElement
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

	/**
	 * Return the CharState field.
	 * 
	 * @return DiscreteCharState
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "DISCRETECHARSTATE_ID", nullable = true)
	public DiscreteCharState getCharState() {
		return mCharState;
	}

	/**
	 * Set the CharState field.
	 */
	public void setCharState(DiscreteCharState pNewCharState) {
		mCharState = pNewCharState;
	}

	/**
	 * Return the Format field.
	 * 
	 * @return StateFormat
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STATEFORMAT_ID", nullable = false)
	public StateFormat getFormat() {
		return mFormat;
	}

	/**
	 * Set the Format field.
	 */
	public void setFormat(StateFormat pNewFormat) {
		mFormat = pNewFormat;
	}

}
