
package org.cipres.treebase.domain.study;

import java.util.ArrayList;
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
import org.hibernate.annotations.Cascade;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalysisStep.java
 * 
 * Created on Feb 22, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ANALYSISSTEP")
@AttributeOverride(name = "id", column = @Column(name = "ANALYSISSTEP_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public class AnalysisStep extends AbstractPersistedObject {

	private static final long serialVersionUID = 4341471045867309690L;

	private String mName;
	private String mCommands;
	private String mNotes;

	private Analysis mAnalysis;
	private Software mSoftwareInfo;
	private Algorithm mAlgorithmInfo;

	private Set<AnalyzedData> mDataSet = new HashSet<AnalyzedData>();

	/**
	 * Constructor.
	 */
	public AnalysisStep() {
		super();
	}

	/**
	 * Return the Commands field.
	 * 
	 * @return String
	 */
	@Column(name = "Commands", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getCommands() {
		return mCommands;
	}

	/**
	 * Set the Commands field.
	 */
	public void setCommands(String pNewCommands) {
		mCommands = pNewCommands;
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
	 * Get a name suitable for display
	 * 
	 * <P>The AnalysisStep might be unnamed.  In that case, get a generic description like "Step 3"
	 * 
	 * @author mjd 20090401
	 * @return The name, if there is one, or a generic descriptor
	 */
	@Transient
	public String getDisplayName() {
		String displayName = getName();
		if (displayName != null && ! displayName.equals("")) return displayName;
		Integer stepNumber = getStepNumber();
		return stepNumber == null ? "" : "step " + (stepNumber + 1);
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
	 * Return the Analysis field.
	 * 
	 * @return Analysis
	 */
	@ManyToOne
	@JoinColumn(name = "ANALYSIS_ID", insertable = false, updatable = false)
	public Analysis getAnalysis() {
		return mAnalysis;
	}

	/**
	 * Set the Analysis field.
	 */
	public void setAnalysis(Analysis pNewAnalysis) {
		mAnalysis = pNewAnalysis;
	}

	/**
	 * Return the SoftwareInfo field.
	 * 
	 * @return Software
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "SOFTWARE_ID")
	public Software getSoftwareInfo() {
		return mSoftwareInfo;
	}

	/**
	 * Set the SoftwareInfo field.
	 */
	public void setSoftwareInfo(Software pNewSoftwareInfo) {
		mSoftwareInfo = pNewSoftwareInfo;
	}

	/**
	 * Return the Algorithm info field.
	 * 
	 * @return Algorithm
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ALGORITHM_ID", nullable = true)
	public Algorithm getAlgorithmInfo() {
		return mAlgorithmInfo;
	}

	/**
	 * Set the Algorithm field.
	 */
	public void setAlgorithmInfo(Algorithm pNewAlgorithm) {
		mAlgorithmInfo = pNewAlgorithm;
	}

	/**
	 * Return the read only DataSet in following order:
	 * first input items, then output items; 
	 * within each input or output, first matrices then trees; 
	 * within each matrix or tree list, order alphabetically by matrix title or tree name/label. 
	 * 
	 * @return Set<AnalyzedData>
	 */
	@Transient
	public List<AnalyzedData> getDataSetReadOnly() {
		List<AnalyzedData> orderedList = new ArrayList<AnalyzedData>();
		orderedList.addAll(getDataSet());
		
		//TODO: define a comparator and call Collections.sortList()
		
		return orderedList;
	}

	/**
	 * Return the DataSet field.
	 * 
	 * @return Set<AnalyzedData> mDataSet
	 */
	@OneToMany(mappedBy = "analysisStep", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	private Set<AnalyzedData> getDataSet() {
		return mDataSet;
	}

	/**
	 * Set the DataSet field.
	 */
	protected void setDataSet(Set<AnalyzedData> pNewDataSet) {
		mDataSet = pNewDataSet;
	}

	/**
	 * Append a new analyzed data to the end of the list. Manage bi-directional relationship.
	 * Reset the analysis validated flag.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pData AnalyzedData
	 */
	public void addAnalyzedData(AnalyzedData pData) {
		if (pData != null && !getDataSet().contains(pData)) {
			getDataSet().add(pData);
			pData.registerAnalysisStep(this);
			
			if (getAnalysis() != null) {
				getAnalysis().setValidated(false);
			}
		}
	}

	/**
	 * Remove the analyzed data. Manage bi-directional relationship.
	 * Reset the analysis validated flag.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pData AnalyzedData
	 */
	public void removeAnalyzedData(AnalyzedData pData) {
		if (pData != null) {
			getDataSet().remove(pData);
			pData.setAnalysisStep(null);
			//pData.registerAnalysisStep(null);
			
			if (getAnalysis() != null) {
				getAnalysis().setValidated(false);
			}			
		}
	}
	
	/**
	 * Return whether the input- and output taxon label sets are
	 * identical, or the output taxon label set is a subset of the
	 * input set. The idea is that an analysis step is invalid if
	 * taxon labels appear out of nowhere.
	 * 
	 * @return boolean validated
	 */
	@Transient
	public boolean getValidated() {
		TaxonLabelSet inputLabelSet = getInputTaxonLabelSet ();
		TaxonLabelSet outputLabelSet = getOutputTaxonLabelSet();
		if ( inputLabelSet.getTaxonLabelsReadOnly().size() == 0 || outputLabelSet.getTaxonLabelsReadOnly().size() == 0 ) {
			return false;
		}
		boolean valid = true;
		OUTPUTLABEL: for ( TaxonLabel outputLabel : outputLabelSet.getTaxonLabelsReadOnly() ) {
			for ( TaxonLabel inputLabel : inputLabelSet.getTaxonLabelsReadOnly() ) {
				if ( outputLabel.getId() == inputLabel.getId() ) {
					valid = true;
					continue OUTPUTLABEL;
				}
			}
			valid = false;		
			break OUTPUTLABEL;
		}
		return valid;
	}

	/**
	 * Returns all unique taxon label objects associated with
	 * all output data objects (i.e. trees and matrices) 
	 */
	@Transient
	public TaxonLabelSet getOutputTaxonLabelSet() {
		TaxonLabelSet outputLabelSet = new TaxonLabelSet();
		outputLabelSet.setTitle("OutputTaxaForAnalysisStep"+getId());
		Set<TaxonLabel> labelSet = new HashSet<TaxonLabel>();
		for ( AnalyzedData data : getDataSetReadOnly() ) {
			if ( ! data.isInputData() ) {
				if ( data.getDataType().equals("tree") ) {
					PhyloTree tree = data.getTreeData();
					labelSet.addAll(tree.getAllTaxonLabels());
				}
				if ( data.getDataType().equals("matrix") ) {
					Matrix matrix = data.getMatrixData();
					labelSet.addAll(matrix.getTaxa().getTaxonLabelsReadOnly());
				}									
			}
		}
		for ( TaxonLabel label : labelSet ) {
			outputLabelSet.addPhyloTaxonLabel(label);
		}
		return outputLabelSet;
	}
	
	/**
	 * Returns all unique taxon label objects associated with
	 * all input data objects (i.e. trees and matrices) 
	 */
	@Transient
	public TaxonLabelSet getInputTaxonLabelSet() {
		TaxonLabelSet inputLabelSet = new TaxonLabelSet();
		inputLabelSet.setTitle("InputTaxaForAnalysisStep"+getId());
		Set<TaxonLabel> labelSet = new HashSet<TaxonLabel>();
		for ( AnalyzedData data : getDataSetReadOnly() ) {
			if ( data.isInputData() ) {
				if ( data.getDataType().equals("tree") ) {
					PhyloTree tree = data.getTreeData();
					labelSet.addAll(tree.getAllTaxonLabels());
				}
				if ( data.getDataType().equals("matrix") ) {
					Matrix matrix = data.getMatrixData();
					labelSet.addAll(matrix.getTaxa().getTaxonLabelsReadOnly());
				}									
			}
		}
		for ( TaxonLabel label : labelSet ) {
			inputLabelSet.addPhyloTaxonLabel(label);
		}
		return inputLabelSet;
	}

	/**
	 * Return the number of this step in the analysis of which it is a member
	 * 
	 * <p>Or possibly null if it is not part of any analysis
	 * @return
	 */
	@Transient
	private Integer getStepNumber() {
		Analysis an = getAnalysis();
		if (an == null) return null;
		int index = an.getAnalysisStepsReadOnly().indexOf(this);
		if (index == -1) return null;
		return index;
	}
	
	@Transient
	public Study getStudy() {
		return getAnalysis().getStudy();
	}

}
