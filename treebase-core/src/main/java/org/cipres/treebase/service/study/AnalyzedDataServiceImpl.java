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

package org.cipres.treebase.service.study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.study.AnalyzedDataService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * AnalyzedDataServiceImpl.java
 * 
 * Created on Jun 8, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class AnalyzedDataServiceImpl extends AbstractServiceImpl implements AnalyzedDataService {

	private AnalyzedDataHome mAnalyzedDataHome;

	/**
	 * Constructor.
	 */
	public AnalyzedDataServiceImpl() {
		super();
	}

	/**
	 * Return the AnalyzedDataHome field.
	 * 
	 * @return AnalyzedDataHome mAnalyzedDataHome
	 */
	private AnalyzedDataHome getAnalyzedDataHome() {
		return mAnalyzedDataHome;
	}

	/**
	 * Set the AnalyzedDataHome field.
	 */
	public void setAnalyzedDataHome(AnalyzedDataHome pNewAnalyzedDataHome) {
		mAnalyzedDataHome = pNewAnalyzedDataHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getAnalyzedDataHome();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedDataService#deleteAnalyzedData(org.cipres.treebase.domain.study.AnalyzedData)
	 */
	public boolean deleteAnalyzedData(AnalyzedData pData) {
		if (pData == null) {
			return false;
		}

		// manage bi-directional relationships:
		AnalysisStep analysisStep = pData.getAnalysisStep();
		if (analysisStep != null) {
			analysisStep.removeAnalyzedData(pData);
		}

		// Do not cascade delete, needs to handle manually:
		// * matrix
		// * tree:

		getAnalyzedDataHome().deletePersist(pData);

		return true;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedDataService#deleteAnalyzedDataCollection(java.util.Collection)
	 */
	public void deleteAnalyzedDataCollection(Collection<? extends AnalyzedData> pAnalyzedData) {
		if (pAnalyzedData == null || pAnalyzedData.isEmpty()) {
			return;
		}

		// make a copy to avoid the potential concurrent modification.
		Set<? extends AnalyzedData> copy = new HashSet<AnalyzedData>(pAnalyzedData);

		for (AnalyzedData data : copy) {
			deleteAnalyzedData(data);
		}
	}

	@Override
	public Class defaultResultClass() {
		return AnalyzedData.class;
	}

}
