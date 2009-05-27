package org.cipres.treebase.util;

import java.util.Set;

import org.cipres.treebase.domain.taxon.Taxon;

public interface MergeDuplicateTaxaInterface extends Standalone {
	public void doMergeTaxonClass(Set<Taxon> taxonClass) ;

	public void mergeAllTaxa();

	public void mergeSomeTaxa(String[] args);
}
