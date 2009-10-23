package org.cipres.treebase.util;

import org.cipres.treebase.domain.taxon.Taxon;

public class TaxonDeleterFactory extends ObjectDeleterFactory<Taxon> {

	@Override
	public ObjectDeleter<Taxon> deleter(Taxon preferred) {
		return new TaxonDeleter(preferred);
	}
}