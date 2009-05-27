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

package org.cipres.treebase.domain.taxon;

import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;

/**
 * TaxonHome.java
 * 
 * Created on April 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface TaxonHome extends DomainHome {

	/**
	 * Delete the instance from the database. Handles bi-directional relationships and cascade
	 * delete.
	 * 
	 * @param pTaxon
	 */
	void delete(Taxon pTaxon);

	/**
	 * Find taxon by name. Search is case sensitive.
	 * 
	 * @param pTaxonName
	 * @return
	 */
	Taxon findByName(String pTaxonName);
	
	/**
	 * Find all taxa by same name (i.e. homonyms). Search is case sensitive.
	 * 
	 * @param pTaxonName
	 * @return
	 */
	Collection<Taxon> findTaxaByName(String pTaxonName);
	
	/**
	 * Find all taxa that contain the string. Optionally case sensitive;
	 * 
	 * @param pTaxonName
	 * @param caseSensitive
	 * @return
	 */
	Collection<Taxon> findTaxaBySubstring(String pTaxonName, boolean caseSensitive);


	/**
	 * Find the taxon variant by full name. Return null if not found. The search is case sensitive.
	 * 
	 * @param pFullName
	 * @return
	 */
	TaxonVariant findTaxonVariantByFullName(String pFullName);
	
	/**
	 * Find all matching taxon variants by full name. Return null if not found. The search is case sensitive.
	 * 
	 * @param pFullName
	 * @return
	 */
	Collection<TaxonVariant> findVariantsByFullName(String pFullName);

	/**
	 * Find all taxon variants by taxon. 
	 * 
	 * @param pTaxon
	 * @return
	 */
	Collection<TaxonVariant> findVariantsByTaxon(Taxon pTaxon);

	/**
	 * Find the taxon that has the specified NCBI genBank ID
	 * 
	 * Throw an exception if there are multiple matches
	 * 
	 * @param genbankId
	 * @return the taxon object, or null if none is found
	 * @throws NonUniqueObjectException
	 */
	Taxon findByNcbiTaxId(Integer genbankId);
	
	/**
	 * Find the taxon that has the specified NCBI genBank ID
	 * 
	 * Throw an exception if there are multiple matches
	 * 
	 * @param genbankId
	 * @return the taxon object, or null if none is found
	 * @author mjd 20080821
	 * @throws NonUniqueObjectException
	 */
	Taxon findByUBIOTaxId(Long nameBankId);
}
