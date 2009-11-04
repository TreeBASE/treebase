package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

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
 * Ancestral State Set
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ANCSTATESET")
@AttributeOverride(name = "id", column = @Column(name = "ANCSTATESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class AncStateSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;

	private Collection<AncestralState> mAncStates = new ArrayList<AncestralState>();

	/**
	 * Constructor.
	 */
	public AncStateSet() {
		super();
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

	/**
	 * Return the AncStates field.
	 * 
	 * @return Collection<AncestralState>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "ANCSTATESET_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<AncestralState> getAncStates() {
		return mAncStates;
	}

	/**
	 * Set the AncStates field.
	 */
	public void setAncStates(Collection<AncestralState> pNewAncStates) {
		mAncStates = pNewAncStates;
	}

}
