package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * UserTypeRecord.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "USERTYPERECORD")
@AttributeOverride(name = "id", column = @Column(name = "USERTYPERECORD_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class UserTypeRecord extends AbstractPersistedObject {

	private static final long serialVersionUID = 4400606797639711337L;

	private UserType mUserType;

	private Collection<ColumnRange> mCharColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public UserTypeRecord() {
		super();
	}

	/**
	 * Return the UserType field.
	 * 
	 * @return UserType
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "USERTYPE_ID", nullable = true)
	public UserType getUserType() {
		return mUserType;
	}

	/**
	 * Set the UserType field.
	 */
	public void setUserType(UserType pNewUserType) {
		mUserType = pNewUserType;
	}

	/**
	 * Return the weight columns.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "UserTypeRrd_ColRange", joinColumns = {@JoinColumn(name = "USERTYPERECORD_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getCharColumns() {
		return mCharColumns;
	}

	/**
	 * Set the weight columns.
	 */
	public void setCharColumns(Collection<ColumnRange> pWeightColumns) {
		mCharColumns = pWeightColumns;
	}

	@Transient
	public String getWeightAndColumnAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append(": ");

		for (ColumnRange colRange : getCharColumns()) {
			colRange.appendRange(sb).append(" ");
		}
		return sb.toString();
	}

}
