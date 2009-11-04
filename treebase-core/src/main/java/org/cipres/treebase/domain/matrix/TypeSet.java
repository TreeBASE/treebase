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
 * TypeSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TYPESET")
@AttributeOverride(name = "id", column = @Column(name = "TYPESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class TypeSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 7690764876029252176L;

	private String mTitle;

	private Set<UserTypeRecord> mTypeRecords = new HashSet<UserTypeRecord>();

	/**
	 * Constructor.
	 */
	public TypeSet() {
		super();
	}

	/**
	 * Return the TypeRecords field.
	 * 
	 * @return Set<UserTypeRecord>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "TYPESET_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<UserTypeRecord> getTypeRecords() {
		return mTypeRecords;
	}

	/**
	 * Set the TypeRecords field.
	 */
	public void setTypeRecords(Set<UserTypeRecord> pNewTypeRecords) {
		mTypeRecords = pNewTypeRecords;
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
