
package org.cipres.treebase.service.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * PhyloTreeServiceImpl.java
 * 
 * Created on Jun 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class PhyloTreeServiceImpl extends AbstractServiceImpl implements PhyloTreeService {

	private PhyloTreeHome mPhyloTreeHome;

	/**
	 * Constructor.
	 */
	public PhyloTreeServiceImpl() {
		super();
	}

	/**
	 * Return the PhyloTreeHome field.
	 * 
	 * @return PhyloTreeHome mPhyloTreeHome
	 */
	private PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeHome field.
	 */
	public void setPhyloTreeHome(PhyloTreeHome pNewPhyloTreeHome) {
		mPhyloTreeHome = pNewPhyloTreeHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getPhyloTreeHome();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeService#findByID(java.lang.Long)
	 */
	public PhyloTree findByID(Long pPhyloTreeID) {
		if (pPhyloTreeID == null) {
			return null;
		}
		return getPhyloTreeHome().getPersistedObjectByID(PhyloTree.class, pPhyloTreeID);
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeService#findTreeBlockByID(java.lang.Long)
	 */
	public TreeBlock findTreeBlockByID(Long pTreeBlockID) {
		if ( pTreeBlockID == null ) {
			return null;
		}
		return getPhyloTreeHome().findTreeBlockById(pTreeBlockID);
	}
	
	/** 
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeService#deletePhyloTree(org.cipres.treebase.domain.tree.PhyloTree)
	 */
	public boolean deletePhyloTree(PhyloTree pPhyloTree) {

		if (pPhyloTree == null) {
			return false;
		}

		// Let the home object handle the cascade delete.
		getPhyloTreeHome().delete(pPhyloTree);

		return true;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeService#updateByRearrangeNodes(java.lang.Long, java.lang.String)
	 */
	public void updateByRearrangeNodes(Long pPhyloTreeId, String pNewick) {
		PhyloTree tree = findByID(pPhyloTreeId);
		if (tree == null) {
			return;
		}

		List<TaxonLabel> taxonLabels = new ArrayList<TaxonLabel>();
		taxonLabels.addAll(tree.getAllTaxonLabels());
		
		MesquiteConverter converter = new MesquiteConverter();
		//List<PhyloTreeNode> allnodes = new ArrayList<PhyloTreeNode>(tree.getTreeNodesReadOnly());
		
		getPhyloTreeHome().deleteNodes(tree);
		
		converter.buildNodesFromNewick(tree, taxonLabels, pNewick);
		
		//ALERT: for whatever reason, needs to save tree nodes explicitly. 
		//seems the tree.treenodes merge cascade does not work!
		getDomainHome().storeAll(tree.getTreeNodesReadOnly());
		
		update(tree);
	}

	public Set<PhyloTree> findByTopology3(TaxonVariant a, TaxonVariant b, TaxonVariant c) {
		return getPhyloTreeHome().findByTopology3(a, b, c);
	}

	public Set<PhyloTree> findByTopology4a(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d) {
		return getPhyloTreeHome().findByTopology4a(a, b, c, d);
	}

	public Set<PhyloTree> findByTopology4s(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d) {
		return getPhyloTreeHome().findByTopology4s(a, b, c, d);
	}

	public Set<PhyloTree> findTreesByLabel(String label) {
		return getPhyloTreeHome().findTreesByLabel(label);
	}

	public Collection<PhyloTree> findByStudies(Set<Study> results) {
		return getPhyloTreeHome().findByStudies(results);
	}
	
	public Collection<PhyloTree> findTreesByNCBINodes(String pNcbiId) {
		if ( pNcbiId == null ) {
			return null;
		}
		return getPhyloTreeHome().findTreesByNCBINodes(pNcbiId);
	} 
	

	/**
	 * @param pStudy - a study
	 * @return the phylotrees in that study
	 * @author mjd 20080723
	 * 
	 * XXX - a better implementation is in r5447, but right now
	 * the TB1 data is garbled, and that implementation doesn't work.
	 * It would work if the database were in a consistent state.
	 */
	public Collection<PhyloTree> findByStudy(Study pStudy) {
		Collection<PhyloTree> trees = new HashSet<PhyloTree>();
		for (Analysis an : pStudy.getAnalysesReadOnly()) {
			for (AnalysisStep step : an.getAnalysisStepsReadOnly()) {
				for (AnalyzedData data : step.getDataSetReadOnly()) {
					PhyloTree treeData = data.getTreeData();
					if (treeData != null) {
						trees.add(treeData);
					}
				}
			}
		}
		return trees;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.tree.PhyloTreeService#findByIDString(java.lang.String)
	 */
	public String getIDPrefix() 
	{
		return "Tr";	
	}

	@Override
	public Class defaultResultClass() {
		return PhyloTree.class;
	}
}
