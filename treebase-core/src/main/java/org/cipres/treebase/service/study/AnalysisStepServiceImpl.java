
package org.cipres.treebase.service.study;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.study.AlgorithmHome;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepHome;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedDataService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * AnalysisStepServiceImpl.java
 * 
 * Created on Jun 8, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class AnalysisStepServiceImpl extends AbstractServiceImpl implements AnalysisStepService {

	private AnalysisStepHome mAnalysisStepHome;
	private AnalyzedDataService mAnalyzedDataService;
	private AlgorithmHome mAlgorithmHome;

	/**
	 * Constructor.
	 */
	public AnalysisStepServiceImpl() {
		super();
	}

	/**
	 * Return the AlgorithmHome field.
	 * 
	 * @return AlgorithmHome mAlgorithmHome
	 */
	private AlgorithmHome getAlgorithmHome() {
		return mAlgorithmHome;
	}

	/**
	 * Set the AlgorithmHome field.
	 */
	public void setAlgorithmHome(AlgorithmHome pNewAlgorithmHome) {
		mAlgorithmHome = pNewAlgorithmHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getAnalysisStepHome();
	}

	/**
	 * Return the mAnalysisStepHome field.
	 * 
	 * @return AnalysisStepHome mmAnalysisStepHome
	 */
	private AnalysisStepHome getAnalysisStepHome() {
		return mAnalysisStepHome;
	}

	/**
	 * Set the mAnalysisStepHome field.
	 */
	public void setAnalysisStepHome(AnalysisStepHome pNewAnalysisStepHome) {
		mAnalysisStepHome = pNewAnalysisStepHome;
	}

	/**
	 * Return the AnalyzedDataService field.
	 * 
	 * @return AnalyzedDataService AnalyzedDataService
	 */
	private AnalyzedDataService getAnalyzedDataService() {
		return mAnalyzedDataService;
	}

	/**
	 * Set the AnalyzedDataService field.
	 */
	public void setAnalyzedDataService(AnalyzedDataService pNewAAnalyzedDataService) {
		mAnalyzedDataService = pNewAAnalyzedDataService;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisStepService#deleteAnalysisStep(org.cipres.treebase.domain.study.AnalysisStep)
	 */
	public boolean deleteAnalysisStep(AnalysisStep pAnalysisStep) {
		if (pAnalysisStep == null) {
			return false;
		}

		// manage bi-directional relationships:
		Analysis analysis = pAnalysisStep.getAnalysis();
		if (analysis != null) {
			analysis.removeAnalysisStep(pAnalysisStep);
		}

		// Cascade delete:
		// * software
		// * algorithm:
		// * analyzedData:
		// FIXME: delete algorithm		
		getAnalysisStepHome().deletePersist(pAnalysisStep.getAlgorithmInfo());
		getAnalysisStepHome().deletePersist(pAnalysisStep.getSoftwareInfo());
		getAnalyzedDataService().deleteAnalyzedDataCollection(pAnalysisStep.getDataSetReadOnly());
		getAnalysisStepHome().deletePersist(pAnalysisStep);
		return true;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisStepService#deleteAnalysisSteps(java.util.Collection)
	 */
	public void deleteAnalysisSteps(Collection<AnalysisStep> pAnalysisSteps) {
		if (pAnalysisSteps == null || pAnalysisSteps.isEmpty()) {
			return;
		}

		// make a copy to avoid the potential concurrent modification.
		Set<AnalysisStep> copy = new HashSet<AnalysisStep>(pAnalysisSteps);

		for (AnalysisStep step : copy) {
			deleteAnalysisStep(step);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisStepService#findByID(java.lang.Long)
	 */
	public AnalysisStep findByID(Long pAnalysisStepID) {
		if (pAnalysisStepID == null) {
			return null;
		}
		return getAnalysisStepHome().findPersistedObjectByID(AnalysisStep.class, pAnalysisStepID);
	}

	public List<String> findUniqueAlgorithmDescriptions() {
		return getAlgorithmHome().findAllUniqueAlgorithmDescriptions();
	}

	@Override
	public Class defaultResultClass() {
		return AnalysisStep.class;
	}

}
