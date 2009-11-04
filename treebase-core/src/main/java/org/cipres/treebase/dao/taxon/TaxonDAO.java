
package org.cipres.treebase.dao.taxon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * TaxonDAO.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class TaxonDAO extends AbstractDAO implements TaxonHome {

	/**
	 * Constructor.
	 */
	public TaxonDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#delete(org.cipres.treebase.domain.taxon.Taxon)
	 */
	public void delete(Taxon pTaxon) {
		if (pTaxon != null && pTaxon.getId() != null) {

			// FIXME: cascade delete all taxon variants, and taxon labels (or set the ref to null).

			getHibernateTemplate().delete(pTaxon);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findTaxonVariantByFullName(java.lang.String)
	 */
	public TaxonVariant findTaxonVariantByFullName(String pFullName) {
		TaxonVariant returnVal = null;

		if (!TreebaseUtil.isEmpty(pFullName)) {
			Criteria c = getSession().createCriteria(TaxonVariant.class);
			c.add(Expression.eq("fullName", pFullName));

			returnVal = (TaxonVariant) c.uniqueResult();
		}
		return returnVal;
	}
	
	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findVariantsByFullName(java.lang.String)
	 */
	public Collection<TaxonVariant> findVariantsByFullName(String pFullName) {
		List<TaxonVariant> returnVal = new ArrayList<TaxonVariant>();
		if ( !TreebaseUtil.isEmpty(pFullName)) {
			Criteria c = getSession().createCriteria(TaxonVariant.class);
			c.add(Expression.eq("fullName", pFullName));
			returnVal = (List<TaxonVariant>)c.list();
		}
		return returnVal;
	}	


	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findByName(java.lang.String)
	 */
	public Taxon findByName(String pTaxonName) {
		Taxon returnVal = null;

		if (!TreebaseUtil.isEmpty(pTaxonName)) {
			Criteria c = getSession().createCriteria(Taxon.class);
			c.add(Expression.eq("name", pTaxonName));

			returnVal = (Taxon) c.uniqueResult();
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findTaxaByName(java.lang.String)
	 */
	public Collection<Taxon> findTaxaByName(String pTaxonName) {
		List<Taxon> returnVal = new ArrayList<Taxon>();
		if ( !TreebaseUtil.isEmpty(pTaxonName)) {
			Criteria c = getSession().createCriteria(Taxon.class);
			c.add(Expression.eq("name", pTaxonName));
			returnVal = c.list();
		}		
		return returnVal;
	}		
	
	/** 
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findVariantsByTaxon(org.cipres.treebase.domain.taxon.Taxon)
	 */
	public Collection<TaxonVariant> findVariantsByTaxon(Taxon pTaxon) {
		List<TaxonVariant> returnVal = new ArrayList<TaxonVariant>();

		if (pTaxon != null) {
			Criteria c = getSession().createCriteria(TaxonVariant.class);
			c.add(Expression.eq("taxon", pTaxon));

			returnVal = c.list();
		}
		return returnVal;		
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findByGenbankId(java.lang.Integer)
	 */
	public Taxon findByNcbiTaxId(Integer genbankId) {
		Criteria c = getSession().createCriteria(Taxon.class);
		c.add(Expression.eq("ncbiTaxId", genbankId));
		return (Taxon) c.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findByUBIOTaxId(java.lang.Integer)
	 */
	public Taxon findByUBIOTaxId(Long nameBankId) {
		Criteria c = getSession().createCriteria(Taxon.class);
		c.add(Expression.eq("UBioNamebankId", nameBankId));
		return (Taxon) c.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonHome#findTaxaBySubstring(java.lang.String, boolean)
	 */
	public Collection<Taxon> findTaxaBySubstring(String taxonName, boolean caseSensitive) {
		Collection<Taxon> result = new ArrayList<Taxon> ();
		for (TBPersistable tv : findSomethingBySubstring(Taxon.class, "name", taxonName, caseSensitive)) {
		    result.add((Taxon) tv);
		}
		return result;
	}
}
