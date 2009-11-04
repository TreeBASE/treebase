 
 package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.hibernate.Transaction;

class CreateTaxonVariant extends AbstractStandalone implements CreateTaxonVariantInterface {
	TaxonHome taxonHome;

	public static void main(String[] args) {
	
		if (args.length != 5) {
			throw new Error("Usage: CreateTaxonVariant name fullName lexicalQualifier uBIOid [taxonId]");
		}
	
		setupContext();
		CreateTaxonVariantInterface me = (CreateTaxonVariantInterface) ContextManager.getBean("createTaxonVariant");
		me.createTaxonVariant(args);
	}
	
	public void createTaxonVariant(String[] args) {
		String name = args[0];
		String fullName = args[1];
		String lexicalQualifier = args[2].equals("") ? null : args[2];
		Long ubio = args[3].equals("") ? null : Long.decode(args[3]);

		Taxon t = null;
		if (args.length == 5) {
			Long taxonId = Long.decode(args[4]);
			t = taxonHome.findPersistedObjectByID(Taxon.class, taxonId);
			if (t == null) {
				throw new Error("No taxon found with ID " + taxonId);
			}
//			getTaxonHome().reattachUnmodified(t);
		}
		
//		Transaction transaction = getTransaction();
		TaxonVariant tv = new TaxonVariant(ubio, name, fullName, lexicalQualifier);
		tv.setTaxon(t);
		taxonHome.save(tv);
//		transaction.commit();
	}
	
	public TaxonHome getTaxonHome() {
		return taxonHome;
	}

	public void setTaxonHome(TaxonHome taxonHome) {
		this.taxonHome = taxonHome;
	}

}