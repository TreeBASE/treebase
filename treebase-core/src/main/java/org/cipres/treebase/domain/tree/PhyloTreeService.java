/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

 }
