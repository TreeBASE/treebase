package org.cipres.treebase.util;

import java.util.Collection;

import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;

/**
 * @author mjd 20090414
 *
 */
public class TaxonDeleter extends ObjectDeleter<Taxon> {
	TaxonHome th;

	public TaxonDeleter(Taxon preferred) {
		super();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.ObjectDeleter#delete(java.lang.Object)
	 */
	@Override
	public void delete(Taxon target) {
		Collection<TaxonVariant> tvs = getTaxonHome().findVariantsByTaxon(target);
		for (TaxonVariant tv : tvs)
			tv.setTaxon(getPreferredObject());
		getTaxonHome().delete(target);
	}

	public TaxonHome getTaxonHome() {
		return th;
	}

	public void setTaxonHome(TaxonHome th) {
		this.th = th;
	}

}
