package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CharWeightSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARWEIGHTSET")
@AttributeOverride(name = "id", column = @Column(name = "CHARWEIGHTSET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharWeightSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;

	private Set<? extends CharWeight> mCharWeights = new HashSet<CharWeight>();

	/**
	 * Constructor.
	 */
	public CharWeightSet() {
		super();
	}

	/**
	 * Return the CharWeights field.
	 * 
	 * @return Set<CharWeight>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = CharWeight.class)
	@JoinColumn(name = "CHARWEIGHTSET_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<? extends CharWeight> getCharWeights() {
		return mCharWeights;
	}

	/**
	 * Set the CharWeights field.
	 */
	public void setCharWeights(Set<? extends CharWeight> pNewCharWeights) {
		mCharWeights = pNewCharWeights;
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

}
