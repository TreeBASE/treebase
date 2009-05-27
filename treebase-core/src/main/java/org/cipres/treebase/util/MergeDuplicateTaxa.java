package org.cipres.treebase.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;



public class MergeDuplicateTaxa extends AbstractStandalone implements MergeDuplicateTaxaInterface {
	
	public static void main(String[] args) {
		setupContext();
		MergeDuplicateTaxa me = new MergeDuplicateTaxa();
		if (args.length == 0)
			me.mergeAllTaxa();
		else 
			me.mergeSomeTaxa(args);
	}
	
	public void mergeSomeTaxa(String [] args) {
		Set<Taxon> taxonClass = new HashSet<Taxon> ();
		boolean bad = false;
		MergeDuplicateTaxaInterface me = (MergeDuplicateTaxaInterface) ContextManager.getBean("mergeDuplicateTaxa");

		for (String arg : args) {
			Long tId = Long.parseLong(arg);
			Taxon t = getTaxonHome().findPersistedObjectByID(Taxon.class, tId);
			if (t == null) {
				warn("No taxon with id T" + tId);
				bad = true;
			} else 
				taxonClass.add(t);
		}
		
		if (bad) return;
		me.doMergeTaxonClass(taxonClass);
	}
	
	public void mergeAllTaxa() {
		MergeDuplicateTaxaInterface me = (MergeDuplicateTaxaInterface) ContextManager.getBean("mergeDuplicateTaxa");
		Map<String,Set<Taxon>> taxonClasses = new HashMap<String,Set<Taxon>> ();
		
		warn("Scanning database");
		int taxonCount = 0;
		for (TBPersistable obj : getTaxonHome().findAll(Taxon.class)) {
			Taxon t = (Taxon) obj;
			String key = key(t);
			if (! taxonClasses.containsKey(key)) 
				taxonClasses.put(key, new HashSet<Taxon> ());
			taxonClasses.get(key).add(t);
			taxonCount++;
		}
		warn("Found " + taxonCount + " taxa in " + taxonClasses.size() + " classes.");
		
		for (Set<Taxon> taxonClass : taxonClasses.values()) {
			if (taxonClass.size() > 1)
				me.doMergeTaxonClass(taxonClass);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.MergeDuplicateTaxaInterface#doMergeTaxonClass(java.util.Set)
	 */
	/* WARNING: This method should modify the TaxonSet and TaxonLink objects too, but doesn't.
	 * At the time it was written, there were no such objects.  20090409 MJD 
	 */
	public void doMergeTaxonClass(Set<Taxon> taxonClass) {
		Iterator<Taxon> it = taxonClass.iterator();
		Taxon canonicalTaxon = it.next();
				
		// Find the taxon in the set with the lowest ID number
		while (it.hasNext()) {
			Taxon t = it.next();
			if (t.getId() < canonicalTaxon.getId()) canonicalTaxon = t;
		}
		
		{
			StringBuilder s = new StringBuilder ("Merging taxon class : ");
			for (Taxon t : taxonClass) s.append(t.getId()).append(" ");
			s.append("\n");
			warn(s.toString());
		}
		warn("  Canonical taxon for this set is T" + canonicalTaxon.getId());
		
		// Map all the other taxa to the canonical one
		for (Taxon t : taxonClass) {
			if (t.equals(canonicalTaxon)) continue;
			for (TaxonVariant tv : getTaxonHome().findVariantsByTaxon(t)) {
				tv.setTaxon(canonicalTaxon);	
				getTaxonHome().merge(tv);
			}
			getCurrentSession().flush();
			getTaxonHome().delete(t);
		}
	}
	
	public void doMergeTaxonVariantClass(Set<TaxonVariant> taxonVariantClass) {
		Iterator<TaxonVariant> it = taxonVariantClass.iterator();
		TaxonVariant canonicalTaxonVariant = it.next();
				
		// Find the taxonVariant in the set with the lowest ID number
		while (it.hasNext()) {
			TaxonVariant t = it.next();
			if (t.getId() < canonicalTaxonVariant.getId()) canonicalTaxonVariant = t;
		}
		
		{
			StringBuilder s = new StringBuilder ("Merging taxonVariant class : ");
			for (TaxonVariant t : taxonVariantClass) s.append(t.getId()).append(" ");
			s.append("\n");
			warn(s.toString());
		}
		warn("  Canonical taxonVariant for this set is T" + canonicalTaxonVariant.getId());
		
		// Map all the other taxa to the canonical one
		for (TaxonVariant tv : taxonVariantClass) {
			if (tv.equals(canonicalTaxonVariant)) continue;
			for (TaxonLabel tl : getTaxonVariantHome().findByTaxonVariant(tv)) {
				tl.setTaxonVariant(canonicalTaxonVariant);	
				getTaxonVariantHome().merge(tl);
			}
			getCurrentSession().flush();
			getTaxonVariantHome().delete(tv);
		}

	}

	private String key(Taxon t) {
		return t.getName() + "," + t.getNcbiTaxId();
	}
	
	private TaxonHome getTaxonHome() {
		return ContextManager.getTaxonHome();
	}
	
	private TaxonLabelHome getTaxonVariantHome() {
		return ContextManager.getTaxonLabelHome();
	}
}
