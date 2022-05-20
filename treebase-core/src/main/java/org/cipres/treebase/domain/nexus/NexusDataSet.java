package org.cipres.treebase.domain.nexus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesquite.lib.MesquiteProject;
import mesquite.lib.Taxa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cipres.treebase.dao.jdbc.NexusDataSetJDBC;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.nexml.model.Document;
import org.nexml.model.OTUs;

/**
 * Contains nexus data set after a nexus file is parsed.
 * 
 * @author Jin Ruan
 */
public class NexusDataSet {
	private static final Logger LOGGER = LogManager.getLogger(NexusDataSet.class);

	private List<Matrix> mMatrices;
	private List<PhyloTree> mPhyloTrees;
	private List<TreeBlock> mTreeBlocks;
	private List<TaxonLabel> mTaxonLabels;
	private List<TaxonLabelSet> mTaxonLabelSets;
	private Map<Taxa, TaxonLabelSet> mTaxonLabelSetMap;
	private Map<OTUs, TaxonLabelSet> mTaxonLabelSetMapNexml;

	private NexusDataSetJDBC mDataJDBC = new NexusDataSetJDBC();
	private MesquiteProject mMesqProject;
	private Document mNexmlDocument;


	/**
	 * Constructor.
	 */
	public NexusDataSet() {
		super();
	}

	/**
	 * Return the DataJDBC field.
	 * 
	 * @return NexusDataSetJDBC mDataJDBC
	 */
	public NexusDataSetJDBC getDataJDBC() {
		return mDataJDBC;
	}

	/**
	 * Return the MesqProject field.
	 * 
	 * @return MesquiteProject mMesqProject
	 */
	public MesquiteProject getMesqProject() {
		return mMesqProject;
	}

	/**
	 * Set the MesqProject field.
	 */
	public void setMesqProject(MesquiteProject pNewMesqProject) {
		mMesqProject = pNewMesqProject;
	}
	
	/**
	 * Return the Matrices field. Uses lazy initialization.
	 * 
	 * @return List<Matrix> mMatrices
	 */
	public List<Matrix> getMatrices() {
		if (mMatrices == null) {
			mMatrices = new ArrayList<Matrix>();
		}
		return mMatrices;
	}

	/**
	 * Return the TaxonLabelSets field.
	 * 
	 * @return List<TaxonLabelSet> mTaxonLabelSets
	 */
	public List<TaxonLabelSet> getTaxonLabelSets() {
		if (mTaxonLabelSets == null) {
			mTaxonLabelSets = new ArrayList<TaxonLabelSet>();
		}
		return mTaxonLabelSets;
	}

	/**
	 * Return the TaxonLabelSetMap field. Uses lazy initialization.
	 * 
	 * @return Map<Taxa, TaxonLabelSet> mTaxonLabelSetMap
	 */
	private Map<Taxa, TaxonLabelSet> getTaxonLabelSetMap() {
		if (mTaxonLabelSetMap == null) {
			mTaxonLabelSetMap = new HashMap<Taxa, TaxonLabelSet>();
		}
		return mTaxonLabelSetMap;
	}
	
	/**
	 * Return the TaxonLabelSetMapNexml field. Uses lazy initialization.
	 * 
	 * @return Map<Taxa, TaxonLabelSet> mTaxonLabelSetMapNexml
	 */
	private Map<OTUs, TaxonLabelSet> getTaxonLabelSetMapNexml() {
		if (mTaxonLabelSetMapNexml == null) {
			mTaxonLabelSetMapNexml = new HashMap<OTUs, TaxonLabelSet>();
		}
		return mTaxonLabelSetMapNexml;
	}	

	/**
	 * Add a Taxon label set.
	 * 
	 * @param pSet
	 */
	public void addTaxonLabelSet(Taxa pTaxa, TaxonLabelSet pSet) {
		if (pSet != null) {
			if (getTaxonLabelSetMap().put(pTaxa, pSet) == null) {
				// it is a new entry:
				getTaxonLabelSets().add(pSet);
			}
		}
	}
	
	/**
	 * Add a Taxon label set.
	 * 
	 * @param pSet
	 */
	public void addTaxonLabelSet(OTUs pTaxa, TaxonLabelSet pSet) {
		if (pSet != null) {
			if (getTaxonLabelSetMapNexml().put(pTaxa, pSet) == null) {
				// it is a new entry:
				getTaxonLabelSets().add(pSet);
			}
		}
	}	

	/**
	 * Find a taxon label set based on the taxa.
	 * 
	 * @param pTaxa
	 * @return
	 */
	public TaxonLabelSet getTaxonLabelSet(Taxa pTaxa) {
		return getTaxonLabelSetMap().get(pTaxa);
	}
	
	/**
	 * Find a taxon label set based on the nexml OTUs.
	 * 
	 * @param xmlOTUs
	 * @return
	 */	
	public TaxonLabelSet getTaxonLabelSet(OTUs xmlOTUs) {
		return getTaxonLabelSetMapNexml().get(xmlOTUs);
	}	

	/**
	 * Return the PhyloTrees field. Uses lazy initialization.
	 * 
	 * Deprecated. Use the getTreeBlocks() instead
	 * 
	 * @return List<PhyloTree> mPhyloTrees
	 */
	@Deprecated
	public List<PhyloTree> getPhyloTrees() {
		if (mPhyloTrees == null) {
			mPhyloTrees = new ArrayList<PhyloTree>();
		}
		return mPhyloTrees;
	}

	/**
	 * Return the tree block field. Uses lazy initialization.
	 * 
	 * @return List<TreeBlock>
	 */
	public List<TreeBlock> getTreeBlocks() {
		if (mTreeBlocks == null) {
			mTreeBlocks = new ArrayList<TreeBlock>();
		}
		return mTreeBlocks;
	}

	/**
	 * Return the total tree count in all tree blocks.
	 * 
	 * @return int
	 */
	public int getTotalTreeCount() {
		int count = 0;
		for (TreeBlock aBlock : getTreeBlocks()) {
			count += aBlock.getTreeCount();
		}
		return count;
	}

	/**
	 * Return the TaxonLabels field. Uses lazy initialization.
	 * 
	 * Deprecated. Use getAllTaxonLabels() instead.
	 * 
	 * @return List<TaxonLabel> mTaxonLabels
	 */
	@Deprecated
	public List<TaxonLabel> getTaxonLabels() {
		if (mTaxonLabels == null) {
			mTaxonLabels = new ArrayList<TaxonLabel>();
		}
		return mTaxonLabels;
	}

	/**
	 * Return all TaxonLabels.
	 * 
	 * @return Set<TaxonLabel>
	 */
	public Set<TaxonLabel> getAllTaxonLabels() {
		Set<TaxonLabel> allLabels = new HashSet<TaxonLabel>();

		for (TaxonLabelSet labelSet : getTaxonLabelSets()) {
			allLabels.addAll(labelSet.getTaxonLabelsReadOnly());
		}
		return allLabels;
	}

	/**
	 * Clear all internal data and data references.
	 * 
	 */
	public void clearData() {

		mMatrices = null;
		mPhyloTrees = null;
		mTaxonLabels = null;
		mTaxonLabelSets = null;
		mTaxonLabelSetMap = null;
		mTreeBlocks = null;

	}

	/**
	 * Add all data from the parameter data set.
	 * 
	 * @param pDataSet
	 */
	public void addAllData(NexusDataSet pDataSet) {
		if (pDataSet != null) {
			getMatrices().addAll(pDataSet.getMatrices());
			getPhyloTrees().addAll(pDataSet.getPhyloTrees());
			getTreeBlocks().addAll(pDataSet.getTreeBlocks());
			getTaxonLabels().addAll(pDataSet.getTaxonLabels());
			getTaxonLabelSets().addAll(pDataSet.getTaxonLabelSets());
			getTaxonLabelSetMap().putAll(pDataSet.getTaxonLabelSetMap());
		}
	}

	/**
	 * Find a set of taxon labels based on name.
	 * 
	 * @param pTaxaName
	 * @return
	 */
	public List<TaxonLabel> findTaxonLabels(Taxa pTaxa) {
		List<TaxonLabel> treeTaxonLabels = null;
		TaxonLabelSet taxonLabelSet = getTaxonLabelSet(pTaxa);
		if (taxonLabelSet != null) {
			treeTaxonLabels = taxonLabelSet.getTaxonLabelsReadOnly();
		}
		return treeTaxonLabels;
	}

	/**
	 * Set the NexmlProject field.
	 */	
	public void setNexmlProject(Document document) {
		mNexmlDocument = document;
		
	}


}
