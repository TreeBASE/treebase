
package org.cipres.treebase.web;

import org.cipres.treebase.domain.study.Algorithm;

/**
 * Constants.java
 * 
 * Created on May 1, 2006
 * 
 * @author lcchan
 * 
 */
public class Constants {

	public static final String ENCRYPTION_ALGORITHM = "SHA";

	/*
	 * ID to be stored in session
	 */
	public static final String SUBMISSION_ID = "submission_id";
	public static final String ANALYSIS_ID = "analysis_id";

	/*
	 * objects to be stores in the session
	 */
	public static final String X_ACCESS_CODE = "x-access-code";
	public static final String REVIEWER_AGREEMENT_ACCEPTED = "reviewerAgreementAccepted";
	
	public static final String STUDY_MAP = "studyMap";
	public static final String ANALYSIS_MAP = "analysisMap";
	public static final String ANALYSIS_STEP_MAP = "analysisStepMap";
	public static final String MATRIX_MAP = "matrixMap";
	public static final String MATRIX_ROW_MAP = "matrixRowMap";

	public static final String USER_KEY = "user";
	public static final String STUDY_KEY = "study";
	public static final String STUDY_COMMAND_KEY = "studyCommand";

	public static final String SUBMISSION_LIST = "submissionList";
	public static final String STUDY_LIST = "studyList";
	public static final String AUTHOR_LIST = "authorList";
	public static final String PEOPLE_LIST = "peopleList";
	public static final String USER_LIST = "userList";
	public static final String PERSON_LIST = "personList";
	public static final String ANALYSIS_LIST = "analysisList";
	public static final String ANALYSIS_STEP_LIST = "analysisStepList";
	public static final String DATA_LIST = "dataList";
	public static final String MATRIX_LIST = "matrixList";
	public static final String MATRIX_ROW_LIST = "matrixRowList";
	public static final String MATRIX_ROW_SEGMENT_LIST = "matrixRowSegmentList";
	public static final String TAXON_LABELS = "taxonLabels";
	public static final String TREE_LIST = "treeList";
	public static final String READONLY_TREE_LIST = "readOnlyTreeList";
	public static final String RESULT_SET = "resultSet";

	/*
	 * submission status
	 */
	public static final String SUBMISSION_TYPES = "submissiontypes";
	/*
	 * value of submission dropdown list; they will be used as the method name in
	 * ListSubmissionController
	 */
	public static final String SUBMISSION_INPROGRESS = "submissionsInProgress";
	public static final String SUBMISSION_SUBMITTED = "submissionsSubmitted";
	public static final String SUBMISSION_PUBLISHED = "submissionsPublished";

	/*
	 * citation type
	 */
	public static final String CITATION_TYPES = "citationtypes";

	/*
	 * algorithm type (value for the drop down list)
	 */
	public static final String ALGORITHM_TYPES = "algorithmtypes";
	public static final String ALGORITHM_LIKELIHOOD = Algorithm.LikelihoodAlgorithm;
	public static final String ALGORITHM_PARSIMONY = Algorithm.ParsimonyAlgorithm;
	public static final String ALGORITHM_DISTANCE = Algorithm.DistanceAlgorithm;
	public static final String ALGORITHM_OTHER = Algorithm.OtherAlgorithm;

	public static final String SEARCH_COMMAND = "searchCommand";

	public static final String MATRIX_KEY = "matrix";
	public static final String TREE_KEY = "tree";
	public static final String INPUT_KEY = "input";
	public static final String OUTPUT_KEY = "output";

	public static final int MAX_MATRIX_COLUMNS_SHOW = 30;

	public static final String SEARCH_AUTHOR = "Author Last Name";
	public static final String SEARCH_CITATION_TITLE = "Citation Title";
	public static final String SEARCH_ALGORITHM = "Algorithm";
	public static final String SEARCH_SOFTWARE = "Software";
	public static final String SEARCH_COUNT = "count";
	public static final String SEARCH_ACTION = "search";

	public static final String SEARCH_STUDY = "Study";
	public static final String SEARCH_MATRIX = "Matrix";
	public static final String SEARCH_TREE = "Tree";
	
	public static final String SEARCH_MESSAGES = "searchMessage";

	public static final String AND_OPERATOR = "And";
	public static final String OR_OPERATOR = "OR";

	public static final String STUDYSTATUS_ALL = "All";
	public static final String ITEMS_ALL = "All";
}
