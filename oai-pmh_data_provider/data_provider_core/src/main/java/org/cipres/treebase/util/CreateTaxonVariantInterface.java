package org.cipres.treebase.util;

import org.cipres.treebase.domain.taxon.TaxonHome;

public interface CreateTaxonVariantInterface extends Standalone {
	public TaxonHome getTaxonHome() ;
	public void setTaxonHome(TaxonHome taxonHome);
	public void createTaxonVariant(String[] args);
		
}
