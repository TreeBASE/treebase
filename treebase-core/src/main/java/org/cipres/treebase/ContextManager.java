/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.domain.admin.PersonService;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.matrix.CharacterHome;
import org.cipres.treebase.domain.matrix.CharacterService;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixColumnHome;
import org.cipres.treebase.domain.matrix.MatrixColumnService;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.matrix.MatrixElementHome;
import org.cipres.treebase.domain.matrix.MatrixElementService;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.matrix.RowSegmentHome;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.search.SearchService;
import org.cipres.treebase.domain.study.AlgorithmHome;
import org.cipres.treebase.domain.study.AnalysisHome;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStepHome;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.study.AnalyzedDataService;
import org.cipres.treebase.domain.study.AnalyzedMatrixHome;
import org.cipres.treebase.domain.study.AnalyzedTreeHome;
import org.cipres.treebase.domain.study.CitationHome;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.CitationStatusHome;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.StudyStatusHome;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

/**
 * ContextManager.java
 * 
 * Created on Sep 23, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class ContextManager {
	private static final Logger LOGGER = Logger.getLogger(ContextManager.class);

	private static ApplicationContext sSpringContext;

	/**
	 * Get the bean instance from the Spring bean factory.
	 * 
	 * @param pBeanClassName
	 * @return
	 */
	public static Object getBean(String pBeanClassName) {
		Object returnVal = getSpringContext().getBean(pBeanClassName);
		return returnVal;
	}

	/**
	 * Create a new Spring context from the specified configuration files. The config file list
	 * cannot be empty.
	 * 
	 * @param pFileName String
	 */
	public static void createSpringContext(String[] pFileNames) {

		if (pFileNames == null || pFileNames.length == 0) {
			LOGGER.fatal("Spring Config File cannot be empty."); //$NON-NLS-1$
			shutdown();
		}

		// Init Spring context, and set username, password for DAO PMF
		setSpringContext(new ClassPathXmlApplicationContext(pFileNames));
	}

	/**
	 * shut down the application.
	 */
	public static void shutdown() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ContextManager.shutdown()"); //$NON-NLS-1$
		}

		System.exit(0);
	}

	/**
	 * @return Returns the springContext.
	 */
	private static ApplicationContext getSpringContext() {
		return sSpringContext;
	}

	/**
	 * @param pSpringContext The springContext to set.
	 */
	private static void setSpringContext(ApplicationContext pSpringContext) {
		sSpringContext = pSpringContext;
	}

	///////////////////////////////////////////////////////////////////////
	//
	// Autogenerated convenience methods
	//
	// 20080729 mjd
	//

        // Admin

	public static PersonHome getPersonHome() {
		return (PersonHome) getBean("personHome");
	}

	public static PersonService getPersonService() {
		return (PersonService) getBean("personService");
	}

	public static UserHome getUserHome() {
		return (UserHome) getBean("userHome");
	}

	public static UserService getUserService() {
		return (UserService) getBean("userService");
	}

        // Matrix

	public static CharacterHome getCharacterHome() {
		return (CharacterHome) getBean("characterHome");
	}

	public static CharacterService getCharacterService() {
		return (CharacterService) getBean("characterService");
	}

	public static ItemDefinitionHome getItemDefinitionHome() {
		return (ItemDefinitionHome) getBean("itemDefinitionHome");
	}

	public static MatrixColumnHome getMatrixColumnHome() {
		return (MatrixColumnHome) getBean("matrixColumnHome");
	}

	public static MatrixColumnService getMatrixColumnService() {
		return (MatrixColumnService) getBean("matrixColumnService");
	}

	public static MatrixDataTypeHome getMatrixDataTypeHome() {
		return (MatrixDataTypeHome) getBean("matrixDataTypeHome");
	}

	public static MatrixElementHome getMatrixElementHome() {
		return (MatrixElementHome) getBean("matrixElementHome");
	}

	public static MatrixElementService getMatrixElementService() {
		return (MatrixElementService) getBean("matrixElementService");
	}

	public static MatrixHome getMatrixHome() {
		return (MatrixHome) getBean("matrixHome");
	}

	public static MatrixRowHome getMatrixRowHome() {
		return (MatrixRowHome) getBean("matrixRowHome");
	}

	public static MatrixRowService getMatrixRowService() {
		return (MatrixRowService) getBean("matrixRowService");
	}

	public static MatrixService getMatrixService() {
		return (MatrixService) getBean("matrixService");
	}

	public static RowSegmentHome getRowSegmentHome() {
		return (RowSegmentHome) getBean("rowSegmentHome");
	}

	public static RowSegmentService getRowSegmentService() {
		return (RowSegmentService) getBean("rowSegmentService");
	}

	public static NexusService getNexusService() {
		return (NexusService) getBean("nexusService");
	}

        // Search

	public static SearchService getSearchService() {
		return (SearchService) getBean("searchService");
	}

        // Study

	public static AlgorithmHome getAlgorithmHome() {
		return (AlgorithmHome) getBean("algorithmHome");
	}

	public static AnalysisHome getAnalysisHome() {
		return (AnalysisHome) getBean("analysisHome");
	}

	public static AnalysisService getAnalysisService() {
		return (AnalysisService) getBean("analysisService");
	}

	public static AnalysisStepHome getAnalysisStepHome() {
		return (AnalysisStepHome) getBean("analysisStepHome");
	}

	public static AnalysisStepService getAnalysisStepService() {
		return (AnalysisStepService) getBean("analysisStepService");
	}

	public static AnalyzedDataHome getAnalyzedDataHome() {
		return (AnalyzedDataHome) getBean("analyzedDataHome");
	}

	public static AnalyzedDataService getAnalyzedDataService() {
		return (AnalyzedDataService) getBean("analyzedDataService");
	}

	public static AnalyzedMatrixHome getAnalyzedMatrixHome() {
		return (AnalyzedMatrixHome) getBean("analyzedMatrixHome");
	}

	public static AnalyzedTreeHome getAnalyzedTreeHome() {
		return (AnalyzedTreeHome) getBean("analyzedTreeHome");
	}

	public static CitationHome getCitationHome() {
		return (CitationHome) getBean("citationHome");
	}

	public static CitationService getCitationService() {
		return (CitationService) getBean("citationService");
	}

	public static CitationStatusHome getCitationStatusHome() {
		return (CitationStatusHome) getBean("citationStatusHome");
	}

	public static StudyHome getStudyHome() {
		return (StudyHome) getBean("studyHome");
	}

	public static StudyService getStudyService() {
		return (StudyService) getBean("studyService");
	}

	public static StudyStatusHome getStudyStatusHome() {
		return (StudyStatusHome) getBean("studyStatusHome");
	}

	public static SubmissionHome getSubmissionHome() {
		return (SubmissionHome) getBean("submissionHome");
	}

	public static SubmissionService getSubmissionService() {
		return (SubmissionService) getBean("submissionService");
	}

        // Taxon

	public static TaxonHome getTaxonHome() {
		return (TaxonHome) getBean("taxonHome");
	}

	public static TaxonLabelHome getTaxonLabelHome() {
		return (TaxonLabelHome) getBean("taxonLabelHome");
	}

	public static TaxonLabelService getTaxonLabelService() {
		return (TaxonLabelService) getBean("taxonLabelService");
	}

        // Tree

	public static PhyloTreeHome getPhyloTreeHome() {
		return (PhyloTreeHome) getBean("phyloTreeHome");
	}

	public static PhyloTreeService getPhyloTreeService() {
		return (PhyloTreeService) getBean("phyloTreeService");
	}

        // Misc

	public static SessionFactory getSessionFactory() {
		return (SessionFactory) getBean("sessionFactory");
	}

	public static HibernateTransactionManager getTransactionManager() {
		return (HibernateTransactionManager) getBean("transactionManager");
	}
}
