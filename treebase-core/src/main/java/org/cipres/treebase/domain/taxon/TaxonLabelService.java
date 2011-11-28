
package org.cipres.treebase.domain.taxon;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

/**
 * @author madhu
 * 
 */
public interface TaxonLabelService extends AbstractService {
	public class booleanplaceholder {
		boolean ncbiRecordFound = false;

		/**
		 * @return the ncbiRecordFound
		 */
		public boolean isNcbiRecordFound() {
			return ncbiRecordFound;
		}

		/**
		 * @param pNcbiRecordFound the ncbiRecordFound to set
		 */
		public void setNcbiRecordFound(boolean pNcbiRecordFound) {
			ncbiRecordFound = pNcbiRecordFound;
		}

	}

	public class longplaceholder {
		long nameBankIDForTaxa = 0L;

		/**
		 * @return the nameBankIDForTaxa
		 */
		public long getNameBankIDForTaxa() {
			return nameBankIDForTaxa;
		}

		/**
		 * @param pNameBankIDForTaxa the nameBankIDForTaxa to set
		 */
		public void setNameBankIDForTaxa(long pNameBankIDForTaxa) {
			nameBankIDForTaxa = pNameBankIDForTaxa;
		}

	}

	/**
	 * Return a Taxon Label. Return null if not found.
	 * 
	 * @param pTaxonLabelID
	 */
	TaxonLabel findByID(Long pTaxonLabelID);
	
    /**
     * Analogous to findByIDString, but finds taxa instead of taxonlabels.
     * 
     * @param idString String like "1234" or "Tx1234"
     * @return the taxon so denoted
     * @throws MalformedTreebaseIDString 
     */
    Taxon findTaxonByIDString(String idString) throws MalformedTreebaseIDString ;

	/**
	 * Update the changed taxon labels. It also triggers regenerating Newick strings for affected
	 * trees.
	 * 
	 * @param pChangedLabels
	 * @return a list of updated taxon labels.
	 */
	List<TaxonLabel> updateChanged(Collection<TaxonLabel> pChangedLabels);

	/**
	 * 
	 * @param pTerm a string
	 * @return all the taxon labels that contain the specified string
	 * @author mjd
	 */
	Collection<TaxonLabel> findBySubstring(String pTerm);

	/**
	 * 
	 * @param pTerm a string
	 * @return all the taxon labels that contain the specified string
	 * @author madhu
	 */
	Collection<TaxonLabel> findByExactString(String pTerm);
	
	/**
	 * 
	 * @param pTerm a string
	 * @param caseSensitive whether match is case sensitive
	 * @return all the taxon labels that contain the specified string
	 * @author rvosa
	 */
	Collection<TaxonLabel> findBySubstring(String pTerm,boolean caseSensitive);
	
	
	/**
	 * 
	 * @param pTerm a string
	 * @return all the taxon objects that contain the specified string
	 * @author rvosa
	 */
	Collection<Taxon> findTaxaByName(String pTerm);
	
	/**
	 *  
	 * @param pTerm
	 * @param caseSensitive
	 * @return
	 */
	Collection<Taxon> findTaxaBySubstring(String pTerm, boolean caseSensitive);
	

	/**
	 * 
	 * Returns the studies that the given taxon labels inhabit
	 * 
	 * @param pTaxonLabels, a collection of taxon labels
	 * @return all studies <var>S</var> such that <var>S</var> is inhabited by some member of
	 *         pTaxonLabels
	 * @author mjd
	 */
	Collection<Study> findStudiesWithTaxonLabels(Collection<TaxonLabel> pTaxonLabels);
	
	/**
	 * Return the studies that some taxonvariant inhabits
	 * 
	 * @param pTaxonVariant 
	 * @return all studies <var>S</var> such that <var>S</var> is inhabited by some {@see TaxonLabel} <var>T</var> such that <var>T</var> refers to the specified TaxonVariant
	 * @author mjd 20080807
	 */
	Set<Study> findStudies(TaxonVariant pTaxonVariant);
	
	/**
	 * Return the studies that these taxonvariants inhabit
	 * 
	 * <p>Like {@see #findStudies(TaxonVariant)}, but mapped over the argument set
	 * @param pTaxonVariants
	 * @return the matching studies
	 * @author mjd 20080807
	 */
	Set<Study> findStudiesWithTaxonVariants(Collection<TaxonVariant> pTaxonVariants);
	
	/**
	 * Return the studies that some taxon inhabits
	 * 
	 * 
	 * @param pTaxon 
	 * @return all studies <var>S</var> such that <var>S</var> is inhabited by the specified taxon
	 * @author mjd 20081204
	 */
	Collection<Study> findStudies(Taxon t);

	Collection<Matrix> findMatricesWithTaxonLabels(Collection<TaxonLabel> pTaxonLabels); // ??? TODO
	Set<Matrix> findMatrices(TaxonVariant pTaxonVariant);
	Set<Matrix> findMatricesWithTaxonVariants(Collection<TaxonVariant> pTaxonVariants);
	Collection<Matrix> findMatrices(Taxon t);
	
	Collection<PhyloTree> findTreesWithTaxonLabels(Collection<TaxonLabel> pTaxonLabels); // ??? TODO
	Set<PhyloTree> findTrees(TaxonVariant pTaxonVariant);
	Set<PhyloTree> findTreesWithTaxonVariants(Collection<TaxonVariant> pTaxonVariants);
	Collection<PhyloTree> findTrees(Taxon t);
	
	Set<Submission> findSubmissions(TaxonLabel taxonLabel);
	Set<Submission> findSubmissions(Collection<TaxonLabel> taxonLabels);
	
	
	/**
	 * Fetches the NCBI taxon id corresponding with the provided uBio taxon ID.
	 * This method is used when the user provides a uBio ID in the taxon label
	 * editor and we have neither a taxon variant, nor a taxon in our database
	 * (and so we need to create both from scratch).
	 * 
	 * @param nameBankId
	 * @return
	 */
	Integer findNcbiTaxIdByUBIOTaxId(Long nameBankId);

	/**
	 * This method is used to test if the taxon Label String is already present either in full or at
	 * least two words are present if third word contains a digit. First search is made for the
	 * whole String if no match is found then test is made if label consists of three or more
	 * words. If third word contains a digit then search is made for two words. If match is found
	 * then we are done. Third word containing a digit is generally not a useful bit.
	 * 
	 * @param pTaxonLabel Taxon label to be tested against the local repository
	 * @return
	 */
	public TaxonVariant findTaxonVariant(TaxonLabel pTaxonLabel);
	
	/**
	 * Instantiates a TaxonVariant object from an ID. This method is used e.g. when users select a 
	 * homonym from a list of variants and the form returns that homonym's ID based on which an
	 * association is made between a TaxonLabel and a TaxonVariant. 
	 * 
	 * @param pTaxonVariantID
	 * @return
	 */
	public TaxonVariant findTaxonVariantByID(Long pTaxonVariantID);
	
	/**
	 * This method is used to test if the taxon Label String is already present either in full or at
	 * least two words are present if third word contains a digit. First search is made for the
	 * whole String if no match is found then test is made if label consists of the three or more
	 * words. If third word contains a digit then search is made for two words. If match is found
	 * then we are done. Third word containing a digit is generally not a useful bit. However, this
	 * method returns a collection of variants. If the collection's size > 1 we are dealing with a
	 * label for which homonyms exist (which consequently have to be resolved by the user).
	 * 
	 * @param pTaxonLabel
	 * @return
	 */
	public Collection<TaxonVariant> findTaxonVariants(TaxonLabel pTaxonLabel);

	/**
	 * 
	 * @param pTaxonLabel
	 * @return
	 */
	TaxonVariant createFromUBIOService(TaxonLabel pTaxonLabel);

	/**
	 * TaxonLabel changed. Regenerate Newick strings for all affected trees.
	 * 
	 * @param pTaxonLabel the instance to be updated.
	 * @return the updated instance.
	 */
	public TaxonLabel updateAndRegenerateNewick(TaxonLabel pTaxonLabel);

	/**
	 * 
	 * 
	 * @param preferredName
	 * @param txnVariantList
	 * @param hpholder
	 * @return
	 */
	boolean isNcbiPreferredNameInList(
		String preferredName,
		List<TaxonVariant> txnVariantList,
		longplaceholder hpholder);

	void getValuesFromElementList(org.w3c.dom.NodeList elementList, List<TaxonVariant> txnVariantList);

	String getNcbiID(String testString);

	String getStringFromURL(String urlString, String searchElement, booleanplaceholder ncbiString);

	String getNCBIPreferredName(String ncbiID);
	
	/**
	 * Set the study for each taxonlabel in the specified tree
	 * @param pTree
	 * @param pStudy
	 * @author mjd
	 */
	void updateStudyForAllLabels(PhyloTree pTree, Study pStudy);
	
	/**
	 * Set the study for each taxonlabel in the specified matrix
	 * @param pMatrix
	 * @param pStudy
	 * @author mjd
	 */
	void updateStudyForAllLabels(Matrix pMatrix, Study pStudy);
	
	/**
	 * find the TaxonVariants containing this substring
	 * 
	 * @param s - a string
	 * @param caseSensitive - optional boolean to specify whether matching is case-sensitive (default false)
	 * @return All TaxonVariants whose fullname contains the specified string
	 * @author mjd 20080728 
	 */
	Collection<TaxonVariant> findTaxonVariantWithSubstring(String s);
	Collection<TaxonVariant> findTaxonVariantWithSubstring(String s, Boolean caseSensitive);

	/**
	 * Return all the TaxonLabels associated with the specified study
	 * 
	 * @param theStudy
	 * @return all the taxonLabels attached to the specified study
	 * @author mjd 20080801
	 */
	Collection<TaxonLabel> findByStudy(Study theStudy);
	
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
	
	/**
	 * Given a set of TaxonVariants, find all the TaxonLabels that ultimately refer to the same taxa
	 * 
	 * <p>That is, find every <var>TL</var> such that there exists <var>TV</var> in <tt>pTVSet</tt> with 
	 * <var>TL</var><tt>.taxonvariant.taxon</tt> = <var>TV</var><tt>.taxon</tt>.</p>
	 * 
	 * <p>Why is this operation interesting?  Say user searches for <tt>Homo sapiens</tt>.  This ultimately
	 * refers to a certain taxon.  What we are doing here is locating the various terms that the objects in the database use
	 * when they refer to that same taxon.  Then we can serve up a list of trees, studies, or matrices that 
	 * mention the same Homo sapiens, even if the names they use to refer to it are more specific, or less specific, or obsolete, 
	 * or misspelled, or whatever.</p>
	 * 
	 * @param pTVSet - the set of TaxonVariants
	 * @return a set of TaxonLabels
	 * @author mjd 20080806
	 * @see #findStudies
	 */
	Set<TaxonLabel> findByTaxonVariantSet(Set<TaxonVariant> pTVSet);

	/**
	 * Given a TaxonVariant, find all the TaxonLabels that ultimately refer to the same taxon
	 * 
	 * <p>Like {@see #findByTaxonVariantSet}, except for a single TaxonVariant
	 * instead of a set of them.  Finds every <var>TL</var> such that 
	 * <var>TL</var><tt>.taxonvariant.taxon</tt> = <tt>pTV.taxon</tt>.</p>
	 * 
	 * @param pTVt - the TaxonVariant
	 * @return a set of TaxonLabels
	 * @author mjd 20080806
	 * @see #findStudies
	 * @see #findByTaxonVariantSet
	 */
	Set<TaxonLabel> findByTaxonVariant(TaxonVariant pTV);

	/**
	 * Exact fullname query for {@see TaxonVariant}s
	 * @param name
	 * @return all taxonVariants with that exact fullname
	 */
	Collection<TaxonVariant> findTaxonVariantByFullName(String s);
	
	/**
	 * Exact name query for {@see TaxonVariant}s
	 * @param name
	 * @return all taxonVariants with that exact name
	 */
	Set<TaxonVariant> findTaxonVariantByName(String s);

	boolean getuBioTimeOutError();

}
