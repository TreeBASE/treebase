
package org.cipres.treebase.web.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import org.cipres.treebase.domain.tree.TreeKind;
import org.cipres.treebase.domain.tree.TreeQuality;
import org.cipres.treebase.domain.tree.TreeType;
import org.cipres.treebase.domain.admin.UserRole;
import org.cipres.treebase.domain.matrix.MatrixKind;
import org.cipres.treebase.domain.study.StudyStatus;

import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.BookCitation;
import org.cipres.treebase.domain.study.InBookCitation;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.LabelValue;

/**
 * StartupListener.java
 * 
 * Created on May 6, 2006
 * 
 * @author lcchan
 * 
 */
public class StartupListener extends ContextLoaderListener {
	private static final Logger LOGGER = LogManager.getLogger(StartupListener.class);

	public void contextInitialized(ServletContextEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER
				.debug("contextInitialized(ServletContextEvent) -FORMAT_DELIMITER_MSG= :FORMAT_RETURN_VALUE_NAME=return valuestart"); //$NON-NLS-1$
		}

		// call Spring's context ContextLoaderListener to initialize
		// all context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();

		// ApplicationContext ctx =
		// WebApplicationContextUtils.getRequiredWebApplicationContext(context);

		/*
		 * citation drop down list
		 */
		List<String> citationTypes = new ArrayList<String>();
		citationTypes.add(ArticleCitation.CITATION_TYPE_ARTICLE);
		citationTypes.add(BookCitation.CITATION_TYPE_BOOK);
		citationTypes.add(InBookCitation.CITATION_TYPE_BOOKSECTION);
		context.setAttribute(Constants.CITATION_TYPES, citationTypes);

		/*
		 * submission drop down list
		 */
		List submissionTypes = new ArrayList();
		submissionTypes.add(new LabelValue("In Progress", Constants.SUBMISSION_INPROGRESS));
		submissionTypes.add(new LabelValue("Submitted", Constants.SUBMISSION_SUBMITTED));
		submissionTypes.add(new LabelValue("Published", Constants.SUBMISSION_PUBLISHED));
		context.setAttribute(Constants.SUBMISSION_TYPES, submissionTypes);

		/*
		 * algorithm drop down list
		 */
		List algorithmTypes = new ArrayList();
		algorithmTypes.add(Constants.ALGORITHM_LIKELIHOOD);
		algorithmTypes.add(Constants.ALGORITHM_PARSIMONY);
		algorithmTypes.add(Constants.ALGORITHM_Bayesian);
		algorithmTypes.add(Constants.ALGORITHM_Evolution);
		algorithmTypes.add(Constants.ALGORITHM_Joining);
		algorithmTypes.add(Constants.ALGORITHM_UPGMA);
		algorithmTypes.add(Constants.ALGORITHM_OTHER);
		context.setAttribute(Constants.ALGORITHM_TYPES, algorithmTypes);

		List<String> studyStatusTypes = new ArrayList<String>();
		studyStatusTypes.add(Constants.STUDYSTATUS_ALL);
		studyStatusTypes.add(StudyStatus.INPROGRESS);
		studyStatusTypes.add(StudyStatus.READY);
		studyStatusTypes.add(StudyStatus.PUBLISHED);
		context.setAttribute("studyStatusTypes", studyStatusTypes);

		List<String> userRoles = new ArrayList<String>();
		userRoles.add(UserRole.ROLE_ADMIN);
		userRoles.add(UserRole.ROLE_ASSO_EDITOR);
		userRoles.add(UserRole.ROLE_USER);
		context.setAttribute("userRoles", userRoles);
		
		List<String> matrixKinds = new ArrayList<String>();
		matrixKinds.add(MatrixKind.KIND_UNSPECIFIED);
		matrixKinds.add(MatrixKind.KIND_ALLOZYME);
		matrixKinds.add(MatrixKind.KIND_AA);
		matrixKinds.add(MatrixKind.KIND_BEHAVIOR);
		matrixKinds.add(MatrixKind.KIND_COMBINATION);
		matrixKinds.add(MatrixKind.KIND_KARYOTYPE);
		matrixKinds.add(MatrixKind.KIND_MATRIX_REP);
		matrixKinds.add(MatrixKind.KIND_MORPHOLOGICAL);
		matrixKinds.add(MatrixKind.KIND_NUCLEIC_ACID);
		matrixKinds.add(MatrixKind.KIND_RESTRICTION_SITE);
		matrixKinds.add(MatrixKind.KIND_SECONDARY_CHEM);
		context.setAttribute("matrixKinds", matrixKinds);
				
		List<String> treeKinds = new ArrayList<String>();
		treeKinds.add(TreeKind.KIND_SPECIES);
		treeKinds.add(TreeKind.KIND_GENE);
		treeKinds.add(TreeKind.KIND_LANGUAGE);
		treeKinds.add(TreeKind.KIND_AREA);
		treeKinds.add(TreeKind.KIND_BARCODE);
		treeKinds.add(TreeKind.KIND_OBJ_CLASSIFICATION);
		context.setAttribute("treeKinds", treeKinds);

		List<String> treeQuality = new ArrayList<String>();
		treeQuality.addAll(TreeQuality.allInstanceDescriptions());
		context.setAttribute("treeQuality", treeQuality);

		List<String> treeTypes = new ArrayList<String>();
		treeTypes.add(TreeType.TYPE_SINGLE);
		treeTypes.add(TreeType.TYPE_CONSENSUS);
		treeTypes.add(TreeType.TYPE_SUPERTREE);
		context.setAttribute("treeTypes", treeTypes);
	}

}
