 
 package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;

class CreateTaxon extends AbstractStandalone implements CreateTaxonInterface {
	TaxonHome taxonHome;
	
	public static void main(String[] args) {
		if (args.length != 3) {
			throw new Error("Usage: CreateTaxon name uBIO NCBI");
		}

		setupContext();
		CreateTaxonInterface me = (CreateTaxonInterface) ContextManager.getBean("createTaxon");
		me.createTaxon(args);
	}
	
	public void createTaxon(String[] args) {
		String name = args[0];
		Long ubio = Long.decode(args[1]);
		Integer ncbi = Integer.decode(args[2]);

		Taxon t = new Taxon(name, ubio, ncbi);
		getTaxonHome().save(t);
	}

	public TaxonHome getTaxonHome() {
		return taxonHome;
	}

	public void setTaxonHome(TaxonHome taxonHome) {
		this.taxonHome = taxonHome;
	}
}