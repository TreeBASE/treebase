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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * GeneticCodeRecord.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "GeneticCodeRecord")
@AttributeOverride(name = "id", column = @Column(name = "GeneticCodeRecord_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class GeneticCodeRecord extends AbstractPersistedObject {

	private static final long serialVersionUID = -2369630256269276608L;

	private GeneticCodeSet mCodeSet;
	private GeneticCode mCode;

	private Collection<ColumnRange> mColumnRanges = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public GeneticCodeRecord() {
		super();
	}

	/**
	 * Return the ColumnRanges field.
	 * 
	 * @return Collection<ColumnRange>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "CodeRecord_ColRange", joinColumns = {@JoinColumn(name = "GENETICCODERECORD_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getColumnRanges() {
		return mColumnRanges;
	}

	/**
	 * Set the ColumnRanges field.
	 */
	public void setColumnRanges(Collection<ColumnRange> pNewColumnRanges) {
		mColumnRanges = pNewColumnRanges;
	}

	/**
	 * Return the Code field.
	 * 
	 * @return GeneticCode
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "GENETICCODE_ID", nullable = true)
	public GeneticCode getCode() {
		return mCode;
	}

	/**
	 * Set the Code field.
	 */
	public void setCode(GeneticCode pNewCode) {
		mCode = pNewCode;
	}

	/**
	 * Return the CodeSet field.
	 * 
	 * @return GeneticCodeSet
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "GENETICCODESET_ID", nullable = false)
	public GeneticCodeSet getCodeSet() {
		return mCodeSet;
	}

	/**
	 * Set the CodeSet field.
	 */
	public void setCodeSet(GeneticCodeSet pNewCodeSet) {
		mCodeSet = pNewCodeSet;
	}

}
