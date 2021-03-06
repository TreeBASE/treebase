
package org.cipres.treebase.domain.study;

import java.util.List;

import org.cipres.treebase.domain.AbstractQueryCriteria;

/**
 * Captures the criteria for advanced study query.
 * 
 * Created on Jun 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class StudyCriteria extends AbstractQueryCriteria {

	private List<String> mAuthorLastNames;
	private List<String> mCitationTitles;
	private List<String> mStudyNames;
	private List<String> mAnalysisNames;
	private List<String> mAnalysisStepNames;

	private List<String> mTaxonLabels;
	private String mAlgorithm;
	private String mSoftware;

	private List<String> mTreePatterns;
	private List<String> mGenBankIDs;
	private List<String> mCharacterNames;

	/**
	 * Constructor.
	 */
	public StudyCriteria() {
		super();
	}

	/**
	 * Return the CharacterNames field.
	 * 
	 * @return List<String> mCharacterNames
	 */
	public List<String> getCharacterNames() {
		return mCharacterNames;
	}

	/**
	 * Set the CharacterNames field.
	 */
	public void setCharacterNames(List<String> pNewCharacterNames) {
		mCharacterNames = pNewCharacterNames;
	}

	/**
	 * Return the GenBankIDs field.
	 * 
	 * @return List<String> mGenBankIDs
	 */
	public List<String> getGenBankIDs() {
		return mGenBankIDs;
	}

	/**
	 * Set the GenBankIDs field.
	 */
	public void setGenBankIDs(List<String> pNewGenBankIDs) {
		mGenBankIDs = pNewGenBankIDs;
	}

	/**
	 * Return the TreePatterns field.
	 * 
	 * @return List<String> mTreePatterns
	 */
	public List<String> getTreePatterns() {
		return mTreePatterns;
	}

	/**
	 * Set the TreePatterns field.
	 */
	public void setTreePatterns(List<String> pNewTreePatterns) {
		mTreePatterns = pNewTreePatterns;
	}

	/**
	 * Return the AnalysisStepNames field.
	 * 
	 * @return List<String> mAnalysisStepNames
	 */
	public List<String> getAnalysisStepNames() {
		return mAnalysisStepNames;
	}

	/**
	 * Set the AnalysisStepNames field.
	 */
	public void setAnalysisStepNames(List<String> pNewAnalysisStepNames) {
		mAnalysisStepNames = pNewAnalysisStepNames;
	}

	/**
	 * Return the Software field.
	 * 
	 * @return String mSoftware
	 */
	public String getSoftware() {
		return mSoftware;
	}

	/**
	 * Set the Software field.
	 */
	public void setSoftware(String pNewSoftware) {
		mSoftware = pNewSoftware;
	}

	/**
	 * Return the Algorithm field.
	 * 
	 * @return String mAlgorithm
	 */
	public String getAlgorithm() {
		return mAlgorithm;
	}

	/**
	 * Set the Algorithm field.
	 */
	public void setAlgorithm(String pNewAlgorithm) {
		mAlgorithm = pNewAlgorithm;
	}

	/**
	 * Return the TaxonLabels field.
	 * 
	 * @return List<String> mTaxonLabels
	 */
	public List<String> getTaxonLabels() {
		return mTaxonLabels;
	}

	/**
	 * Set the TaxonLabels field.
	 */
	public void setTaxonLabels(List<String> pNewTaxonLabels) {
		mTaxonLabels = pNewTaxonLabels;
	}

	/**
	 * Return the AnalysisNames field.
	 * 
	 * @return List<String> mAnalysisNames
	 */
	public List<String> getAnalysisNames() {
		return mAnalysisNames;
	}

	/**
	 * Set the AnalysisNames field.
	 */
	public void setAnalysisNames(List<String> pNewAnalysisNames) {
		mAnalysisNames = pNewAnalysisNames;
	}

	/**
	 * Return the StudyNames field.
	 * 
	 * @return List<String> mStudyNames
	 */
	public List<String> getStudyNames() {
		return mStudyNames;
	}

	/**
	 * Set the StudyNames field.
	 */
	public void setStudyNames(List<String> pNewStudyNames) {
		mStudyNames = pNewStudyNames;
	}

	/**
	 * Return the CitationTitles field.
	 * 
	 * @return List<String> mCitationTitles
	 */
	public List<String> getCitationTitles() {
		return mCitationTitles;
	}

	/**
	 * Set the CitationTitles field.
	 */
	public void setCitationTitles(List<String> pNewCitationTitles) {
		mCitationTitles = pNewCitationTitles;
	}

	/**
	 * Return the AuthorLastNames field.
	 * 
	 * @return List<String> mAuthorLastNames
	 */
	public List<String> getAuthorLastNames() {
		return mAuthorLastNames;
	}

	/**
	 * Set the AuthorLastNames field.
	 */
	public void setAuthorLastNames(List<String> pNewAuthorLastNames) {
		mAuthorLastNames = pNewAuthorLastNames;
	}

}
