package org.cipres.treebase.util;

import java.util.Map;
import java.util.Set;

import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;

interface LoadTaxonDataInterface extends Standalone {

	/* 
	 * **************************************************************************
	 * ***** PHASE 1
	 * Create the underlying taxa, or locate them if they already exist
	 * Correct errors where possible
	 */
	public Map<String, Taxon> buildTaxa(OptionalHashFieldReader fh);

	public Map<String, Taxon> checkTaxa(OptionalHashFieldReader fh);

	public Map<String, Taxon> knownTaxonCache();

	/* 
	 * **************************************************************************
	 * ***** PHASE 2
	 * Create the underlying taxonvariants, or locate them if they already exist
	 */
	public Map<String, TaxonVariant> buildTaxonVariants(
			OptionalHashFieldReader tvFH, Map<String, Taxon> taxon);

	public Map<String, TaxonVariant> checkTaxonVariants(
			OptionalHashFieldReader tvFH, Map<String, Taxon> taxon);

	public Map<String, TaxonVariant> knownTaxonVariantCache();

	/* 
	 * **************************************************************************
	 * ***** PHASE 3
	 * Connect existing taxonlabel objects to the taxonvariant objects from phase 2
	 */
	public void linkTaxonLabels(OptionalHashFieldReader tlFH,
			Map<String, TaxonVariant> taxonVariant);

	public void checkTaxonLabels(OptionalHashFieldReader tlFH,
			Map<String, TaxonVariant> taxonVariant);

	public Map<String, Set<TaxonLabel>> knownTaxonLabelCache();

	/* Misc accessors for bean injection */
	
	public TaxonHome getTaxonHome();

	public void setTaxonHome(TaxonHome taxonHome);

	public TaxonLabelHome getTaxonLabelHome();

	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome);

	/* Strange exception */
	public void populateTaxaLegacyIDs(OptionalHashFieldReader tfh);

}