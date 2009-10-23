/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CodonPositionSet
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CodonPositionSet")
@AttributeOverride(name = "id", column = @Column(name = "CodonPositionSet_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CodonPositionSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;

	private Collection<ColumnRange> mChar1Columns = new ArrayList<ColumnRange>();
	private Collection<ColumnRange> mChar2Columns = new ArrayList<ColumnRange>();
	private Collection<ColumnRange> mChar3Columns = new ArrayList<ColumnRange>();
	private Collection<ColumnRange> mNonCodingColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public CodonPositionSet() {
		super();

	}

	/**
	 * Return the NonCodingColumns field.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CodonNonCoding_ColRange", joinColumns = {@JoinColumn(name = "CodonPositionSet_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getNonCodingColumns() {
		return mNonCodingColumns;
	}

	/**
	 * Set the NonCodingColumns field.
	 */
	public void setNonCodingColumns(Collection<ColumnRange> pNewNonCodingColumns) {
		mNonCodingColumns = pNewNonCodingColumns;
	}

	/**
	 * Return the Char3Columns field.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CodonChar3_ColRange", joinColumns = {@JoinColumn(name = "CodonPositionSet_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getChar3Columns() {
		return mChar3Columns;
	}

	/**
	 * Set the Char3Columns field.
	 */
	public void setChar3Columns(Collection<ColumnRange> pNewChar3Columns) {
		mChar3Columns = pNewChar3Columns;
	}

	/**
	 * Return the Char2Columns field.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CodonChar2_ColRange", joinColumns = {@JoinColumn(name = "CodonPositionSet_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getChar2Columns() {
		return mChar2Columns;
	}

	/**
	 * Set the Char2Columns field.
	 */
	public void setChar2Columns(Collection<ColumnRange> pNewChar2Columns) {
		mChar2Columns = pNewChar2Columns;
	}

	/**
	 * Return the Char1Columns field.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CodonChar1_ColRange", joinColumns = {@JoinColumn(name = "CodonPositionSet_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getChar1Columns() {
		return mChar1Columns;
	}

	/**
	 * Set the Char1Columns field.
	 */
	public void setChar1Columns(Collection<ColumnRange> pNewChar1Columns) {
		mChar1Columns = pNewChar1Columns;
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
	 * Generate the nexus string and append to the string builder.
	 * 
	 * @param pBuilder
	 * @return
	 */
	@Transient
	public StringBuilder generateNexusString(StringBuilder pBuilder) {
		if (pBuilder == null) {
			return null;
		}
		
		boolean hasFirst = false;
		boolean isLast = false;
		Collection<ColumnRange> nonCoding = getNonCodingColumns();
		if (nonCoding != null && !nonCoding.isEmpty()) {
			pBuilder.append("\t\tN: ");
			generateColumnsInfo(pBuilder, nonCoding);
			
			hasFirst = true;
		}

		Collection<ColumnRange> pos1 = getChar1Columns();
		Collection<ColumnRange> pos2 = getChar2Columns();
		Collection<ColumnRange> pos3 = getChar3Columns();

		if (pos1 != null && !pos1.isEmpty()) {
			if (hasFirst) {
				pBuilder.append(",").append(TreebaseUtil.LINESEP);
			}
			pBuilder.append("\t\t1: ");
			generateColumnsInfo(pBuilder, pos1);
			
			hasFirst = true;
		}
			
		if (pos2 != null && !pos2.isEmpty()) {
			if (hasFirst) {
				pBuilder.append(",").append(TreebaseUtil.LINESEP);
			}
			pBuilder.append("\t\t2: ");
			generateColumnsInfo(pBuilder, pos2);
			
			hasFirst = true;
		}
			
		if (pos3 != null && !pos3.isEmpty()) {
			if (hasFirst) {
				pBuilder.append(",").append(TreebaseUtil.LINESEP);
			}
			pBuilder.append("\t\t3: ");
			generateColumnsInfo(pBuilder, pos3);
			
			hasFirst = true;
		}

		//now append the ";" for the last row:
		if (hasFirst) {
			pBuilder.append(";").append(TreebaseUtil.LINESEP);
		}
		
		return pBuilder;
	}

	/**
	 * Generate a string representing a list of columns.
	 * Return the string builder for chaining. 
	 * 
	 * @param pBuilder
	 * @param pNonCoding
	 */
	private StringBuilder generateColumnsInfo(StringBuilder pBuilder, 
		Collection<ColumnRange> pRanges) {
		
		boolean isFirst = true;
		for (ColumnRange colRange : pRanges) {
			if (isFirst) {
				isFirst = false;
			} else {
				pBuilder.append(" ");
			}
			colRange.appendRange(pBuilder);
			//pBuilder.append(" ");
		}
		
		return pBuilder;
	}
}
