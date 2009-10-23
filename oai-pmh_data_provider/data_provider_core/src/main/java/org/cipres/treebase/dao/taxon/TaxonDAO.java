/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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
