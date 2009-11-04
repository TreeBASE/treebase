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
import org.cipres.treebase.domain.TBPersistable;

/**
 * DiscreteCharState.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "DISCRETECHARSTATE")
@AttributeOverride(name = "id", column = @Column(name = "DISCRETECHARSTATE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class DiscreteCharState extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	public static final Character GAP_SYMBOL = '-';
	public static final Character MISSING_SYMBOL = '?';

	private String mDescription;
	private String mNotes;
	private Character mSymbol;

	private DiscreteChar mChar;

	/**
	 * Constructor.
	 */
	public DiscreteCharState() {
		super();
	}

	/**
	 * Return the Symbol field.
	 * 
	 * @return char mSymbol
	 */
	@Column(name = "Symbol", nullable = true)
	public Character getSymbol() {
		return mSymbol;
	}

	/**
	 * Set the Symbol field.
	 */
	public void setSymbol(Character pNewSymbol) {
		mSymbol = pNewSymbol;
	}
	
	/**
	 * Return the Char field.
	 * 
	 * @return DiscreteChar
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "PHYLOCHAR_ID", nullable = false)
	public DiscreteChar getChar() {
		return mChar;
	}

	/**
	 * Set the Char field.
	 */
	public void setChar(DiscreteChar pNewChar) {
		mChar = pNewChar;
	}

	/**
	 * Return the description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the description field.
	 */
	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

	/**
	 * Return the Notes field.
	 * 
	 * @return String mNotes
	 */
	@Column(name = "Notes", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getNotes() {
		return mNotes;
	}

	/**
	 * Set the Notes field.
	 */
	public void setNotes(String pNewNotes) {
		mNotes = pNewNotes;
	}

}
