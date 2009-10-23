/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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