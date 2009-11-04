
package org.cipres.treebase.domain.taxon;

import java.util.Collection;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * TaxonLabelHome.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface TaxonLabelHome extends DomainHome {

	/**
	 * First try to find default taxon label by its description. The search is case sensitive. The
	 * default taxon label does not have a study reference.
	 * 
	 * Create and return a new default label if one is not found.
	 * 
	 * @param pDescription
	 * @return TaxonLabel default taxon label.
	 */
	TaxonLabel getDefaultByDescription(String pDescription);

	/**
	 * First try to find a taxon label by its description and study. The search is case sensitive.
	 * 
	 * Create and return a new label if one is not found (when the label string is not empty).
	 * 
	 * @param pLabel if empty string, null TaxonLabel is returned. 
	 * @param pStudy
	 * @return
	 */
	TaxonLabel getByDescriptionAndStudy(String pLabel, Study pStudy);

	/**
	 * Find a taxon label by its description. The search is case sensitive. Null is returned if the
	 * study is null,
	 * 
	 * @param pDescription
	 * @param pStudy
	 * 
	 * @return TaxonLabel taxon label.
	 */
	TaxonLabel findByDescription(String pDescription, Study pStudy);

	/**
	 * Find all taxon labels for the specified study.
	 * Empty collection is returned if the study is null.
	 * 
	 * @param pStudy
	 * @return
	 */
	Collection<TaxonLabel> findByStudy(Study pStudy);

	/**
	 * Delete the instances from the database.
	 * 
	 * @param pTaxonLabels
	 */
	void delete(Collection<TaxonLabel> pTaxonLabels);

	/**
	 * Delete the instance from the database. Handles bi-directional relationships and cascade
	 * delete.
	 * 
	 * @param pTaxonLabel
	 */
	void delete(TaxonLabel pTaxonLabel);
	void delete(TaxonVariant pTaxonVariant);

	/**
	 * Delete all taxon labels for one study.
	 * 
	 * @param pStudy
	 */
	void deleteByStudy(Study pStudy);

	/**
	 * Case-insensitive taxon label substring search
	 * 
	 * @param pTerm a string
	 * @return all the taxon labels that contain the specified string
	 * @author mjd
	 */
	Collection<TaxonLabel> findBySubstring(String pTerm);
	
	/**
	 * Optionally case-sensitive label substring search
	 * 
	 * @param pTerm a string
	 * @param caseSensitive whether to match case sensitively
	 * @return all the taxon labels that contain the specified string
	 * @author rvosa
	 */
	Collection<TaxonLabel> findBySubstring(String pTerm, boolean caseSensitive);

	/**
	 * 
	 * @param pTerm a string
	 * @return all the taxon labels that equal the specified string
	 * @author madhu
	 */
	Collection<TaxonLabel> findByExactString(String pTerm);

	/**
	 * 
	 * Return the studies that the given taxon labels inhabit
	 * 
	 * @param pTaxonLabels
	 * @return
	 * @author mjd
	 */
	Collection<Study> findStudiesWithTaxonLabels(Collection<TaxonLabel> pTaxonLabels);

	/**
	 * @author mjd 20080807
	 */
	Set<Study> findStudies(TaxonVariant taxonVariant);
	
	/**
	 * Return the studies that some taxon inhabits
	 * 
	 * 
	 * @param pTaxon 
	 * @return all studies <var>S</var> such that <var>S</var> is inhabited by the specified taxon
	 * @author mjd 20081204
	 */
	Collection<Study> findStudies(Taxon t);

    Collection<Matrix> findMatricesWithTaxonLabels(Collection<TaxonLabel> taxonLabels);
	Collection<Matrix> findMatrices(Taxon t);

    Set<Matrix> findMatrices(TaxonVariant taxonVariant);
    
    Set<PhyloTree> findTrees(TaxonVariant taxonVariant);
	Collection<PhyloTree> findTrees(Taxon t);

    Collection<PhyloTree> findTreesWithTaxonLabels(Collection<TaxonLabel> taxonLabels);
    
	/**
	 * Set the study for each taxonlabel in the specified tree
	 * @param tree
	 * @param study
	 * @author mjd
	 */
	void updateStudyForAllLabels(PhyloTree tree, Study study);
	
	/**
	 * Set the study for each taxonlabel in the specified matrix
	 * @param matrix
	 * @param study
	 * @author mjd
	 */
	void updateStudyForAllLabels(Matrix matrix, Study study);
	
	/**
	 * @param s - a string
	 * @param caseSensitive - is matching case sensitive?
	 * @return All TaxonVariants whose fullname contains the specified string
	 * @author mjd 20080728 
	 */
	public Set<TaxonVariant> findTaxonVariantWithSubstring(String s, Boolean caseSensitive);
	
	/**
	 * Find the TaxonVariants equivalent to the specified one
	 * 
	 * <p>Given a {@see TaxonVariant}, find and return all the TaxonVariants that denote
	 * to the same {@see Taxon}.</p>
	 * 
	 * @param pTV - the TaxonVariant of interest
	 * @return the set of equivalent taxonvariants
	 * @author mjd 20080806
	 */
	Set<TaxonVariant> expandTaxonVariant(TaxonVariant pTV);
	
	/**
	 * Find the TaxonVariants equivalent to the specified ones
	 * 
	 * <p>Like {@see #expandTaxonVariant(TaxonVariant)}, but the input is a set of
	 * {@see TaxonVariant} objects.  The method returns every TaxonVariant that is
	 * equivalent to some member of the input set.</p>
	 * 
	 * <p>This function should be idempotent.  That is, <code>expandTaxonVariantSet(expandTaxonvariantSet(<i>x</i>))</code> 
	 * should be equal to <code>expandTaxonvariantSet(<i>x</i>)</code> for all <i>x</i>.</p>
	 *  
	 * @param tvIn - a set of TaxonVariants
	 * @return the set of TaxonVariants equivalent to these
	 * @author mjd 20080806
	 */
	Set<TaxonVariant> expandTaxonVariantSet(Set<TaxonVariant> pTVSet);

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findByTaxonVariant(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	Set<TaxonLabel> findByTaxonVariant(TaxonVariant ptv);
	
	/**
	 * Exact name query for {@see TaxonVariant}s (case sensitive)
	 * @param name
	 * @return all taxonVariants with that exact name
	 */
	Set<TaxonVariant> findTaxonVariantByName(String name);

	/**
	 * Exact fullname query for {@see TaxonVariant}s
	 * @param name
	 * @return all taxonVariants with that exact fullname
	 */
	Collection<TaxonVariant> findTaxonVariantByFullName(String tvFullName);

	/**
	 * @param taxonLabel
	 * @return all the submissions that contain this taxonlabel
	 */
	Set<Submission> findSubmissions(TaxonLabel taxonLabel);

	/**
	 * @param tl The taxonlabel in question
	 * @return All taxonlabelsets of which this taxonlabel is a member
	 */
	Set<TaxonLabelSet> findTaxonLabelSets(TaxonLabel tl);
}
