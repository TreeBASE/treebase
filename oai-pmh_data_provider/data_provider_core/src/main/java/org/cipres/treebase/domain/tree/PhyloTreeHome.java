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
package org.cipres.treebase.domain.tree;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonVariant;

/**
 * PhyloTreeHome.java The home interface for the PhyloTree domain objects.
 * 
 * Created on Mar 17, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface PhyloTreeHome extends DomainHome {

	/**
	 * Find all trees that have node that matches at least one taxon label.
	 * 
	 * @param pTaxonLabels
	 * @return
	 */
	Collection<PhyloTree> findByAnyTaxonLabel(List<TaxonLabel> pTaxonLabels);

	/**
	 * Find all trees for the given studies.
	 * 
	 * @param pStudies
	 * @return
	 */
	Collection<PhyloTree> findByStudies(Collection<Study> pStudies);

	/**
	 * Find all trees that have node that matches at least one taxon label.
	 * 
	 * @param pTreeBlocks
	 * @return
	 */
	TreeBlock findTreeBlockById(Long pId);

	/**
	 * Find the tree type by its description.
	 * 
	 * @param pDescription
	 * @return TreeType
	 */
	TreeType findTypeByDescription(String pDescription);

	/**
	 * Find the tree kind by its description.
	 * 
	 * @param pDescription
	 * @return TreeKind
	 */
	TreeKind findKindByDescription(String pDescription);

	/**
	 * Find the tree quality by its description. 
	 * 
	 * @param pDescription
	 * @return
	 */
	TreeQuality findQualityByDescription(String pDescription);

	/**
	 * Delete the instances from the database.
	 * 
	 * @param pTrees
	 */
	void delete(Collection<PhyloTree> pTrees);

	/**
	 * Delete the instances from the database.
	 * 
	 * @param pTreeBlocks
	 */
	void deleteTreeBlocks(Collection<TreeBlock> pTreeBlocks);

	/**
	 * Delete the instance from the database. Handles bi-directional relationships and cascade
	 * delete.
	 * 
	 * @param pTree
	 */
	void delete(PhyloTree pTree);

	/**
	 * Delete the instance from the database and all the associated trees. Handles bi-directional
	 * relationships and cascade delete.
	 * 
	 * @param pTreeBlock
	 */
	void deleteTreeBlock(TreeBlock pTreeBlock);

	/**
	 * Delete the instance from the database and all the associated trees. Handles bi-directional
	 * relationships and cascade delete.
	 * 
	 * @param pTreeBlockID
	 */
	void deleteTreeBlockByID(Long pTreeBlockID);

	/**
	 * Delete all nodes. 
	 * 
	 * @param pTree
	 */
	void deleteNodes(PhyloTree pTree);

	/**
	 * Get all tree blocks from the nexus file in the specified submission.
	 * 
	 * @param pSubmissionId
	 * @param pFileName
	 * @return
	 */
	Collection<TreeBlock> findTreeBlocksByNexusFileName(Long pSubmissionId, String pFileName);

	/**
	 * Update the published flag for all trees in the study. 
	 * Returns the updated count. 
	 * 
	 * @param pStudy
	 * @param pPublished
	 * @return
	 */
	int updatePublishedFlagByStudy(Study pStudy, boolean pPublished);
	
	/**
	 * Tree-topology search: Given three strings <var>a</var>, <var>b</var>, and <var>c</var>
	 * that denote taxa, find all the trees that contain all three, such that <var>a</var> and
	 * <var>b</var> are more closely related to each other than either is to <var>c</var>.
	 * 
	 * @param a taxon name
	 * @param b taxon name
	 * @param c taxon name
	 * @return a set of trees as above
	 * 
	 * @author mjd
	 */
	public Set<PhyloTree> findByTopology3(TaxonVariant a, TaxonVariant b, TaxonVariant c);

	public Set<PhyloTree> findByTopology4s(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d);

	public Set<PhyloTree> findByTopology4a(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d);

	/**
	 * @param fn, the name of the Nexus file
	 * @return whether there is a PhyloTree that seems to have come from that file
	 * @author mjd 
	 */
	public Collection<PhyloTree> findByNexusFile(String fn);
	
	/**
	 * @param label, the target label
	 * @return set of PhyloTrees that have that label
	 * @author mjd 
	 */
	public Set<PhyloTree> findTreesByLabel(String label);

	
	/**
	 * <p>Find all tree nodes that refer to this taxonlabel</p>
	 * 
	 * @author mjd 200920225
	 * @param tl
	 * @return
	 */
	public Set<PhyloTreeNode> findNodesByTaxonLabel(TaxonLabel tl);

	/**
	 * Find trees by TB1 legacy ID
	 * @author mjd 20090326
	 * @param legacyID
	 * @return all matching trees
	 */
	Collection<PhyloTree> findByTB1TreeID(String legacyID);
}
