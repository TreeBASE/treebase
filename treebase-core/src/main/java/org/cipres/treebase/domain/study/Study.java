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
package org.cipres.treebase.domain.study;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;
import org.jdom.Element;

@Entity
@Table(name = "STUDY")
@AttributeOverride(name = "id", column = @Column(name = "STUDY_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public class Study extends AbstractPersistedObject {

	private static final long serialVersionUID = -3985996848459154996L;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Study.class);

	private String mName;
	private String mAccessionNumber;
	private String mNotes;
	private Date mReleaseDate;
	private Date mLastModifiedDate;
	private String mTB1StudyID;

	private StudyStatus mStudyStatus;
	private Submission mSubmission;
	private Citation mCitation;
	private User mReviewer;

	private List<Analysis> mAnalyses = new ArrayList<Analysis>();
	private Set<TaxonLabelSet> mTaxonLabelSets = new HashSet<TaxonLabelSet>();
	private Map<String, Clob> mNexusFiles = new HashMap<String, Clob>();

	// transient fields
	private String mTransientDescription;

	/**
	 * Constructor.
	 */
	public Study() {
		super();
		
		setLastModifiedDate(new Date());
	}

	/**
	 * Return the AccessionNumber field.
	 * 
	 * @return String
	 */
	@Column(name = "ACCESSIONNUMBER", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getAccessionNumber() {
		return mAccessionNumber;
	}

	/**
	 * Set the AccessionNumber field.
	 */
	public void setAccessionNumber(String pNewAccessionNumber) {
		mAccessionNumber = pNewAccessionNumber;
	}

	/**
	 * Return the Name field.
	 * 
	 * @return String
	 */
	@Column(name = "NAME", length = TBPersistable.COLUMN_LENGTH_STRING)
	@Index(name = "study_name_idx")
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
	 * Return the TB1 StudyID field.
	 * 
	 * @return String 
	 */
	@Column(name = "TB_STUDYID", length = TBPersistable.COLUMN_LENGTH_30)
	public String getTB1StudyID() {
		return mTB1StudyID;
	}

	/**
	 * Set the TB1 StudyID field.
	 */
	public void setTB1StudyID(String pNewTB1StudyID) {
		mTB1StudyID = pNewTB1StudyID;
	}
	
	/**
	 * The date when the study is scheduled to be released for public access.
	 * 
	 * @return Date
	 */
	@Column(name = "ReleaseDate")
	@Temporal(TemporalType.DATE)
	public Date getReleaseDate() {
		return mReleaseDate;
	}

	/**
	 * Set the ReleaseDate field.
	 */
	public void setReleaseDate(Date pNewReleaseDate) {
		mReleaseDate = pNewReleaseDate;
	}

	/**
	 * Ideally, the last modified date would be the last time any changes 
	 * were made to the study record and all related records (citation, 
	 * trees, matrices, etc). But that is a pain to keep track. 
	 * 
	 * In the current implementation, it is the "last classified date"--
	 * date that the author last changed the status field 
	 * (i.e. from "in progress" to "ready").  That way the admin person 
	 * can sort by the date the study was last classified by the author 
	 * as ready to be processed. 
	 * 
	 * @return Date 
	 */
	@Column(name = "LastModifiedDate")
	@Temporal(TemporalType.DATE)
	public Date getLastModifiedDate() {
		return mLastModifiedDate;
	}

	/**
	 * Set the LastModifiedDate field.
	 */
	public void setLastModifiedDate(Date pNewLastModifiedDate) {
		mLastModifiedDate = pNewLastModifiedDate;
	}
	
	/**
	 * Return the StudyStatus field.
	 * 
	 * @return StudyStatus
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinColumn(name = "STUDYSTATUS_ID")
	public StudyStatus getStudyStatus() {
		return mStudyStatus;
	}

	/**
	 * Set the StudyStatus field.
	 */
	public void setStudyStatus(StudyStatus pNewStudyStatus) {
		mStudyStatus = pNewStudyStatus;
	}

	/**
	 * Return the Reviewer field.
	 * 
	 * @return User 
	 */
	@Deprecated
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public User getReviewer() {
		return mReviewer;
	}

	/**
	 * Set the Reviewer field.
	 */
	@Deprecated
	public void setReviewer(User pNewReviewer) {
		mReviewer = pNewReviewer;
	}

	/**
	 * Return the list of authors.
	 * 
	 * @return List<Person>
	 */
	@Transient
	public List<Person> getAuthors() {
		if (getCitation() != null) {
			return getCitation().getAuthors();
		}
		return null;
	}

	/**
	 * Return the Submission field.
	 * 
	 * @return Submission
	 */
	@OneToOne(mappedBy = "study")
	public Submission getSubmission() {
		return mSubmission;
	}

	/**
	 * Set the Submission field.
	 */
	public void setSubmission(Submission pNewSubmission) {
		mSubmission = pNewSubmission;
	}

	/**
	 * Return a read only list of analyses.
	 * 
	 * @return
	 */
	@Transient
	public List<Analysis> getAnalysesReadOnly() {
		return Collections.unmodifiableList(getAnalyses());
	}

	/**
	 * Return the Analyses field.
	 * 
	 * @return List<Analysis>
	 */
	// Note: for the true index to work,
	// the one side has to be the owner side, cannot use "mappedby"
	// also need to duplicate specify the JoinColumn here.
	// @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "Study_ID")
	@IndexColumn(name = "ANALYSIS_ORDER")
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	public List<Analysis> getAnalyses() {
		return mAnalyses;
	}

	/**
	 * Set the Analyses field.
	 */
	public void setAnalyses(List<Analysis> pNewAnalyses) {
		mAnalyses = pNewAnalyses;
	}

	/**
	 * Return the NexusFiles field.
	 * 
	 * @return Set<NexusFileObject> mNexusFiles
	 */
	// @OneToMany(mappedBy = "study", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
	// CascadeType.REMOVE})
	@CollectionOfElements
	@JoinTable(name = "Study_NexusFile", joinColumns = @JoinColumn(name = "STUDY_ID"))
	@org.hibernate.annotations.MapKey(columns = @Column(name = "FILENAME", nullable = false))
	// // @Lob
	@Column(name = "NEXUS", nullable = false, length = 4194304)
	@Cascade( {org.hibernate.annotations.CascadeType.ALL,
		org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	// @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	// @Transient
	public Map<String, Clob> getNexusFiles() {
		return mNexusFiles;
	}

	/**
	 * Set the NexusFiles field.
	 */
	public void setNexusFiles(Map<String, Clob> pNewNexusFiles) {
		mNexusFiles = pNewNexusFiles;
	}

	/**
	 * Return the Citations field.
	 * 
	 * @return Citation
	 */
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, optional = true)
	@JoinColumn(name = "CITATION_ID")
	// @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @JoinTable(name = "STUDY_CITATION", joinColumns = {@JoinColumn(name = "STUDY_ID")},
	// inverseJoinColumns = @JoinColumn(name = "CITATION_ID"))
	public Citation getCitation() {
		return mCitation;
	}

	/**
	 * Set the Citations field.
	 */
	public void setCitation(Citation pNewCitation) {
		mCitation = pNewCitation;
	}

	/**
	 * Append a new analysis to the end of the list. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pAnalysis Analysis
	 */
	public void addAnalysis(Analysis pAnalysis) {
		if (pAnalysis != null && !getAnalyses().contains(pAnalysis)) {
			getAnalyses().add(pAnalysis);
			pAnalysis.setStudy(this);
		}
	}

	/**
	 * Replace the analysis at the specified location. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pAnalysis
	 * @param pIndex
	 * @return the analysis previously at the pIndex location
	 * @throws IndexOutOfBoundsException if index out of range.
	 */
	public Analysis setAnalysis(Analysis pAnalysis, int pIndex) {

		if (pAnalysis == null) {
			throw new IllegalArgumentException("Analysis parameter is null.");
		}

		int analysisCount = getAnalyses().size();
		if (pIndex >= analysisCount || pIndex < 0) {
			throw new IndexOutOfBoundsException("Index: " + pIndex + ", Size: " + analysisCount);
		}

		Analysis oldAnalysis = getAnalyses().get(pIndex);
		oldAnalysis.setStudy(null);

		getAnalyses().set(pIndex, pAnalysis);
		pAnalysis.setStudy(this);

		return oldAnalysis;
	}

	/**
	 * Remove the analysis. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pAnalysis Analysis
	 */
	public void removeAnalysis(Analysis pAnalysis) {

		if ((pAnalysis != null) && getAnalyses().contains(pAnalysis)) {
			getAnalyses().remove(pAnalysis);
			pAnalysis.setStudy(null);
		}
	}

	/**
	 * Append a new nexus file.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pFileName
	 * @param pNexusFile Clob
	 */
	public void addNexusFile(String pFileName, Clob pNexusFile) {
		if (TreebaseUtil.isEmpty(pFileName)) {
			if (pNexusFile == null) {
				return;
			}

			int fileNum = getNexusFiles().size();
			pFileName = "Unnamed " + fileNum;
		}

		if (!getNexusFiles().containsKey(pFileName)) {
			getNexusFiles().put(pFileName, pNexusFile);
		}
	}

	/**
	 * Remove the nexus file.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pFileName
	 */
	public void removeNexusFile(String pFileName) {

		if (!TreebaseUtil.isEmpty(pFileName)) {
			getNexusFiles().remove(pFileName);
		}
	}
	
	/**
	 * Get the names of all submitted nexus files in
	 * a collection of strings
	 * 
	 * @return Collection<String>
	 */
	@Transient
	public Collection<String> getNexusFileNames() {
		return getNexusFiles().keySet();
	}

	/**
	 * Return the TaxonLabelSets field.
	 * 
	 * @return Set<TaxonLabelSet> mTaxonLabelSets
	 */
	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	@BatchSize(size=20)
	public Set<TaxonLabelSet> getTaxonLabelSets() {
		return mTaxonLabelSets;
	}

	/**
	 * Set the TaxonLabelSets field.
	 */
	public void setTaxonLabelSets(Set<TaxonLabelSet> pNewTaxonLabelSets) {
		mTaxonLabelSets = pNewTaxonLabelSets;
	}
	
	/**
	 * @return all the taxon labels in this study
	 * @author mjd 20081205
	 */
	@Transient
	public Collection<TaxonLabel> getTaxonLabels() {
		Collection<TaxonLabel> taxonLabels = new HashSet<TaxonLabel> ();
		for (TaxonLabelSet tls : getTaxonLabelSets()) 
			taxonLabels.addAll(tls.getTaxonLabelsReadOnly());
		return taxonLabels;
	}
	
	/**
	 * @return all the taxon variants in this study
	 * @author mjd 20081205
	 */
	@Transient
	public Collection<TaxonVariant> getTaxonVariants() {
		Collection<TaxonVariant> taxonVariants = new HashSet<TaxonVariant> ();
		for (TaxonLabel tl : getTaxonLabels()) 
			if (tl.getTaxonVariant() != null)
				taxonVariants.add(tl.getTaxonVariant());
		return taxonVariants;
	}
	
	/**
	 * @return all the taxa in this study
	 * @author mjd 20081205
	 */
	@Transient
	public Collection<Taxon> getTaxa() {
		Collection<Taxon> taxa = new HashSet<Taxon> ();
		for (TaxonVariant tv : getTaxonVariants()) 
			if (tv.getTaxon() != null)
				taxa.add(tv.getTaxon());
		return taxa;
	}

	/**
	 * Returns true if the submission is published.
	 * 
	 * @return
	 */
	@Transient
	public boolean isPublished() {
		return getStudyStatus().isPublished();
	}

	/**
	 * Returns true if the submission is ready for publish.
	 * 
	 * @return
	 */
	@Transient
	public boolean isReady() {
		return getStudyStatus().isReady();
	}

	/**
	 * Returns true if the submission is in progress.
	 * 
	 * @return
	 */
	@Transient
	public boolean isInProgress() {
		return getStudyStatus().isInProgress();
	}

	/**
	 * Used only for the spring binding to the web gui, to capture the user inputs.
	 * 
	 * @return String mTransientDescription
	 */
	@Transient
	public String getTransientDescription() {
		if (mTransientDescription == null) {
			mTransientDescription = new String(getStudyStatus().getDescription());
		}
		return mTransientDescription;
	}

	/**
	 * Set the TransientDescription field.
	 */
	public void setTransientDescription(String pNewTransientDescription) {
		mTransientDescription = pNewTransientDescription;
	}

	/**
	 * Reset the transient description field.
	 * 
	 */
	public void resetTransientDescription() {
		setTransientDescription(null);
	}

	@Transient
	public Element generateXMLComponents() {
		Element element = new Element("study");
		element.addContent(new Element("id").setText(String.valueOf(getId())));
		if (!TreebaseUtil.isEmpty(getName())) {
			element.addContent(new Element("name").setText(getName()));
		}
		if (!TreebaseUtil.isEmpty(getNotes())) {
			element.addContent(new Element("notes").setText(getNotes()));
		}

		String citationInfo = getCitation().getAuthorsCitationStyleWithoutHtml();
		if (!TreebaseUtil.isEmpty(citationInfo)) {
			element.addContent(new Element("citation").setText(citationInfo));
		}

		if (!TreebaseUtil.isEmpty(getStudyStatus().getDescription())) {
			element.addContent(new Element("status").setText(getStudyStatus().getDescription()));
		}
		return element;
	}

	@Transient
	public Boolean getAnalysesStatus() {
		Boolean bTest = Boolean.TRUE;
		for (Analysis analysis : getAnalyses()) {
			bTest = analysis.getValidated();
			System.out.println("Value Of BTEST: " + bTest);
			if (!bTest) {
				break;
			}
		}
		return bTest;
	}

	public Study parseXMLComponents(Element pStudyElement) {
		// in the future: parse the study element to get a study.

		return null;
	}
	
	/**
	 * @return all the steps in all the analyses in this study
	 * @author mjd 20080801
	 */
	@Transient
	public Set<AnalysisStep> getAnalysisSteps() {
		Set<AnalysisStep> steps = new HashSet<AnalysisStep> ();
		for (Analysis an : getAnalyses()) {
			steps.addAll(an.getAnalysisStepsReadOnly());
		}
		return steps;
	}
	
	/**
	 * @return all the analyzed data objects in all the analyses in this study
	 * @author mjd 20080801
	 */
	@Transient
	public Set<AnalyzedData> getAnalyzedData() {
		Set<AnalyzedData> data = new HashSet<AnalyzedData> ();
		for (AnalysisStep as : getAnalysisSteps()) {
			data.addAll(as.getDataSetReadOnly());
		}
		return data;
	}
	
	/**
	 * @return all the matrices in all the analyses in this study
	 * @author mjd 20080801
	 */
	@Transient
	public Set<Matrix> getMatrices() {
		Set<Matrix> matrices = new HashSet<Matrix> ();
		for (AnalyzedData ad : getAnalyzedData()) {
			Matrix m = ad.getMatrixData();
			if (m != null) {
				matrices.add(m);
			}
		}
		return matrices;
	}	
	
	/**
	 * @return all the phylotrees in all the analyses in this study
	 * @author mjd 20080801
	 */
	@Transient
	public Set<PhyloTree> getTrees() {
		Set<PhyloTree> trees = new HashSet<PhyloTree> ();
		for (AnalyzedData ad : getAnalyzedData()) {
			PhyloTree t = ad.getTreeData();
			if (t != null) {
				trees.add(t);
			}
		}
		return trees;
	}
}
