
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
	 * Find the taxon that has the specified uBio namebank ID
	 * 
	 * Throw an exception if there are multiple matches
	 * 
	 * @param nameBankId
	 * @return the taxon object, or null if none is found
	 * @author mjd 20080821
	 * @throws NonUniqueObjectException
	 */
	Taxon findByUBIOTaxId(Long nameBankId);
	
	/**
	 * Find the taxon that has the specified TreeBASE1 legacy ID
	 * 
	 * @param tb1LegacyId
	 * @return the taxon object, or null if none is found
	 */
	Taxon findByTB1LegacyId(Integer tb1LegacyId);
	
	/**
	 * Find the taxonVariant that has the specified TreeBASE1 legacy ID
	 * 
	 * @param tb1LegacyId
	 * @return the taxonVariant object, or null if none is found
	 */	
	TaxonVariant findVariantByTB1LegacyId(Integer tb1LegacyId);
}
