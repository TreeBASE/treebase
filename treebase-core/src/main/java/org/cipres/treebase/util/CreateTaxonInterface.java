package org.cipres.treebase.util;

import org.cipres.treebase.domain.taxon.TaxonHome;
import org.hibernate.SessionFactory;

public interface CreateTaxonInterface extends Standalone {
	public SessionFactory getSessionFactory();

	public void createTaxon(String[] args);
	public TaxonHome getTaxonHome();
	public void setTaxonHome(TaxonHome taxonHome);
} 
