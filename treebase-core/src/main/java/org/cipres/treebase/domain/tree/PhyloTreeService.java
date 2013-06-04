
package org.cipres.treebase.domain.tree;

import java.util.Collection;
import java.util.Set;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.service.AbstractService;

/**
 * PhyloTreeService.java
 * 
 * Created on Jun 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
/**
 * @author mjd
 *
 */

/**
 * @author mjd
 *
 */
public interface PhyloTreeService extends AbstractService {
	
	TreeBlock findTreeBlockByID(Long pTreeBlockID);

	/**
	 * Return a phylo tree object by id.
	 * 
	 * @param pPhyloTreeID
	 * @return
	 */
	PhyloTree findByID(Long pPhyloTreeID);
	
	/**
	 * Delete a tree.
	 * 
	 * @param pPhyloTree
	 * @return
	 */
	boolean deletePhyloTree(PhyloTree pPhyloTree);
	
	/**
	 * Update the phylotree based on the updated newick string.
	 * This handles "reroot" of tree, which means the taxonLabel does not
	 * change. 
	 * 
	 * @param pPhyloTreeId
	 * @param pNewick
	 */
	void updateByRearrangeNodes(Long pPhyloTreeId, String pNewick);
	
	/**
	 * Find trees containing these three labels, where c is less closely related to a and b then either is to the other
	 * 
	 * 	Note that although the arguments are taxon variants, the trees returned may not refer to these
	 *  taxon variants.  But they will refer to the same underlying taxa that the argument taxonvariants do. 
	 *  
	 * @param a
	 * @param b
	 * @param c
	 * @return the set of matching trees
	 * @author mjd
	 */
	public Set<PhyloTree> findByTopology3(TaxonVariant a, TaxonVariant b, TaxonVariant c) ;
	
	/**
	 * Symmetric 4-taxon tree topology search
	 * 
	 *  Find trees containing these four labels, arranged as follows:
	 *  <pre>
	 *     a
	 *    /
	 *   /\
	 *  /  b
	 * /
	 * \
	 *  \  c
	 *   \/
	 *    \
	 *     d
	 *  </pre>
	 *  
	 *  Note that although the arguments are taxon variants, the trees returned may not refer to these
	 *  taxon variants.  But they will refer to the same underlying taxa that the argument taxonvariants do. 
	 *  
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return the set of matching trees
	 * @author mjd
	 */
	public Set<PhyloTree> findByTopology4s(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d);
	
	
	/**
	 * Asymmetric 4-taxon tree topology search
	 * 
	 *  Find trees containing these four labels, arranged as follows:
	 *  <pre>
	 *            a
	 *           /
	 *          /\
	 *         /  b
	 *        /\
	 *       /  c
	 *       \
	 *        d
	 *  </pre>
	 *  
	 *  Note that although the arguments are taxon variants, the trees returned may not refer to these
	 *  taxon variants.  But they will refer to the same underlying taxa that the argument taxonvariants do. 
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return the set of matching trees
	 * @author mjd
	 */
	public Set<PhyloTree> findByTopology4a(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d);

	/**
	 * @param label
	 * @return collection of trees with that label
	 */
	public Set<PhyloTree> findTreesByLabel(String label);

	Collection<PhyloTree> findByStudies(Set<Study> results);
	Collection<PhyloTree> findByStudy(Study pStudy);
	
	public Collection<PhyloTree> findTreesByNCBINodes(String pNcbiId);

 }
