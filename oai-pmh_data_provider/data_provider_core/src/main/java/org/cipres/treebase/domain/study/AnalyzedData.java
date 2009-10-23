/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.domain.study;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalyzedData.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ANALYZEDDATA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "ANALYZEDDATA_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public abstract class AnalyzedData extends AbstractPersistedObject {

	private Boolean mInput;
	private String mNotes;

	private AnalysisStep mAnalysisStep;

	/**
	 * Constructor.
	 */
	public AnalyzedData() {
		super();
	}

	/**
	 * Return the Notes field.
	 * 
	 * @return String
	 */
	@Column(name = "Notes", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getNotes() {
		return mNotes;
	}

	/**
	 * Set the Notes field.
	 */
	public void setNotes(String pNewNotes) {
		mNotes = pNewNotes;
	}

	/**
	 * Return the Input field.
	 * 
	 * @return Boolean
	 */
	@Column(name = "Input")
	protected Boolean getInput() {
		return mInput;
	}

	/**
	 * Set the Input field.
	 */
	public void setInput(Boolean pNewInput) {
		mInput = pNewInput;
	}

	/**
	 * Return true if the data is an input for the analysis step.
	 * 
	 * Creation date: Feb 23, 2006 11:44:02 AM
	 */
	@Transient
	public boolean isInputData() {
		return (getInput() != null && getInput().booleanValue());
	}
	
	/**
	 * @return The string "input" or "output", depending on whether this object is input or output
	 * @author mjd 20080723
	 */
	@Transient
	public String getInputOutput() {
		return isInputData() ? "input" : "output";
	}

	/**
	 * Return the AnalysisStep field.
	 * 
	 * @return AnalysisStep mAnalysisStep
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ANALYSISSTEP_ID", nullable = false)
	public AnalysisStep getAnalysisStep() {
		return mAnalysisStep;
	}

	/**
	 * Set the AnalysisStep field.
	 */
	public void setAnalysisStep(AnalysisStep pNewAnalysisStep) {
		mAnalysisStep = pNewAnalysisStep;
	}

	/**
	 * Override this method to register analysis step. 
	 * 
	 * @param pAnalysisStep
	 */
	public void registerAnalysisStep(AnalysisStep pAnalysisStep) {
		setAnalysisStep(pAnalysisStep);
	}
	
	/**
	 * Return a matrix if a matrix is contained. 
	 * 
	 * @return
	 */
	@Transient
	public abstract Matrix getMatrixData();
	
	/**
	 * Return the tree if a tree is contained. 
	 * 
	 * @return
	 */
	@Transient
	public abstract PhyloTree getTreeData();
	
	/**
	 * @return "matrix" or "tree" according as whether the object is a matrix or a tree
	 * @author mjd 20080723
	 */
	@Transient
	public abstract String getDataType();
	
	/**
	 * @return a string suitable for describing the object, such as a title or label
	 * @author mjd 20080723
	 */
	@Transient
	public abstract String getDisplayName();
}
