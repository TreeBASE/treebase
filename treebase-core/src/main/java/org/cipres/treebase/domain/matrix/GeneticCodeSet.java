package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * GeneticCodeSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "GENETICCODESET")
@AttributeOverride(name = "id", column = @Column(name = "GENETICCODESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class GeneticCodeSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 3417158088515256558L;

	private String mTitle;
	private String mFormat;

	private Collection<GeneticCodeRecord> mCodeRecords = new ArrayList<GeneticCodeRecord>();

	/**
	 * Constructor.
	 */
	public GeneticCodeSet() {
		super();
	}

	/**
	 * Return the CodeRecords field.
	 * 
	 * @return Collection<GeneticCodeRecord>
	 */
	@OneToMany(mappedBy = "codeSet", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<GeneticCodeRecord> getCodeRecords() {
		return mCodeRecords;
	}

	/**
	 * Set the CodeRecords field.
	 */
	public void setCodeRecords(Collection<GeneticCodeRecord> pNewCodeRecords) {
		mCodeRecords = pNewCodeRecords;
	}

	/**
	 * Return the Format field.
	 * 
	 * @return String
	 */
	@Column(name = "Format", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getFormat() {
		return mFormat;
	}

	/**
	 * Set the Format field.
	 */
	public void setFormat(String pNewFormat) {
		mFormat = pNewFormat;
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
