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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * Analysis.java
 * 
 * Created on February 22, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ANALYSIS")
@AttributeOverride(name = "id", column = @Column(name = "ANALYSIS_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public class Analysis extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mName;
	private String mNotes;
	private boolean mValidated;

	private Study mStudy;

	private List<AnalysisStep> mAnalysisSteps = new ArrayList<AnalysisStep>();

	/**
	 * Constructor.
	 */
	public Analysis() {
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
	 * Return the Name field.
	 * 
	 * @return String
	 */
	@Column(name = "Name", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getName() {
		return mName;
	}

	/**
	 * Set the Name field.
	 */
	public void setName(String pNewName) {
		mName = pNewName;
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study
	 */
	@ManyToOne
	@JoinColumn(name = "STUDY_ID", insertable = false, updatable = false)
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	public void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}

	/**
	 * Return a read only list of analysis steps.
	 * 
	 * @return
	 */
	@Transient
	public List<AnalysisStep> getAnalysisStepsReadOnly() {
		return Collections.unmodifiableList(getAnalysisSteps());
	}

	/**
	 * Return the AnalysisSteps field.
	 * 
	 * @return List
	 */
	// Note: for the true index to work,
	// the one side has to be the owner side, cannot use "mappedby"
	// also need to duplicate specify the JoinColumn here.
	// @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ANALYSIS_ID")
	@IndexColumn(name = "STEP_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	private List<AnalysisStep> getAnalysisSteps() {
		return mAnalysisSteps;
	}

	/**
	 * Set the AnalysisSteps field.
	 */
	private void setAnalysisSteps(List<AnalysisStep> pNewAnalysisSteps) {
		mAnalysisSteps = pNewAnalysisSteps;
	}

	/**
	 * Return the Validated field.
	 * 
	 * @return boolean
	 */
	@Column(name = "Validated")
	public Boolean getValidated() {
		return mValidated;
	}

	/**
	 * Set the Validated field.
	 */
	public void setValidated(Boolean pNewValidated) {
		if (pNewValidated == null) {
			mValidated = false;
		} else {
			mValidated = pNewValidated;
		}
	}

	/**
	 * Append a new analysis step to the end of the list. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pStep AnalysisStep
	 */
	public void addAnalysisStep(AnalysisStep pStep) {
		if (pStep != null && !getAnalysisSteps().contains(pStep)) {
			getAnalysisSteps().add(pStep);
			pStep.setAnalysis(this);
		}
	}

	/**
	 * Remove the analysis step. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pStep AnalysisStep
	 */
	public void removeAnalysisStep(AnalysisStep pStep) {
		if (pStep != null && getAnalysisSteps().contains(pStep)) {
			getAnalysisSteps().remove(pStep);
			pStep.setAnalysis(null);
		}
	}

	/**
	 * Verifying that within a given analysis (ie across all the steps in an analysis), all taxon
	 * labels from trees and all taxon labels from matrices match up perfectly: 
	 * 
	 * * all matrices must have the same set of taxon labels
	 * * all tree taxon labels must be a subset of matrix taxon labels
	 * 
	 * no cases of tree
	 * labels that don't match with a matrix label. (It is allowable for a tree to have only a
	 * subset of labels found in analysis-linked matrices, but it is not allowable for an OTU label
	 * on a tree not have no matching label in corresponding matrices, to the extent that those
	 * matrices exist).
	 * 
	 * The caller is responsible to call the isValidate() method first to determine whether it is
	 * necessary to validate again.
	 * 
	 * @return
	 */
	public ExecutionResult validate() {
		// boolean validate = true;

		ExecutionResult result = new ExecutionResult();

		Set<TaxonLabel> matrixTaxonLabels = new HashSet<TaxonLabel>();
		// Set<TaxonLabel> allTreeTaxonLabels = new HashSet<TaxonLabel>();
		Collection<PhyloTree> allTrees = new ArrayList<PhyloTree>();

		// Build the all taxon labels sets:
		for (AnalysisStep step : getAnalysisSteps()) {

			for (AnalyzedData analyzedData : step.getDataSetReadOnly()) {
				Matrix m = analyzedData.getMatrixData();
				PhyloTree tree = analyzedData.getTreeData();

				if (m != null) {
					if (matrixTaxonLabels.isEmpty()) {
						matrixTaxonLabels.addAll(m.getAllTaxonLabels());
					} else if (!matrixTaxonLabels.containsAll(m.getAllTaxonLabels())) {
						// all matrices must have the same set of taxon labels.
						result.addErrorMessage("Error: matrix(id " + m.getId()
							+ "): all matrices must have the same set of taxon labels");
					}
				}

				if (tree != null) {
					allTrees.add(tree);
				}
			}
		}

		// tree taxon labels must be a subset of matrix taxon labels.
		for (PhyloTree tree : allTrees) {
			if (!matrixTaxonLabels.containsAll(tree.getAllTaxonLabels())) {
				result.addErrorMessage("Error: tree(id " + tree.getId()
					+ ") taxon labels must be a subset of matrix taxon labels");
			}
		}

		setValidated(result.isSuccessful());
		return result;
	}
}
