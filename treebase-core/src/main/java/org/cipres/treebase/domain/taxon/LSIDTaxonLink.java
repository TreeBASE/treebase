package org.cipres.treebase.domain.taxon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * LSIDTaxonLink.java
 * 
 * Created on Mar 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("L")
public class LSIDTaxonLink extends TaxonLink {

	/**
	 * Constructor.
	 */
	public LSIDTaxonLink() {
		super();
	}

}
