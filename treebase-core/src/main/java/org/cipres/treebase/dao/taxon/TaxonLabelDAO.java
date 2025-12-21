
package org.cipres.treebase.dao.taxon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

/**
 * TaxonLabelDAO.java
 * 
 * Created on Apr 24, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class TaxonLabelDAO extends AbstractDAO implements TaxonLabelHome {

	/**
	 * Constructor.
	 */
	public TaxonLabelDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findByDescription(java.lang.String,
	 *      org.cipres.treebase.domain.study.Study)
	 */
	public TaxonLabel findByDescription(String pDescription, Study pStudy) {
		TaxonLabel returnVal = null;

		if (!TreebaseUtil.isEmpty(pDescription) && pStudy != null) {
			Criteria c = getSession().createCriteria(TaxonLabel.class);
			c.add(Expression.eq("taxonLabel", pDescription));
			c.add(Expression.eq("study", pStudy));

			// ALERT: the result might not be unique since mesquite allows duplicated taxonlabels
			// in the same tree.
			// returnVal = (TaxonLabel) c.uniqueResult();

			List results = c.list();
			if (results != null && !results.isEmpty()) {
				returnVal = (TaxonLabel) results.get(0);
			}
		}
		return returnVal;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findByStudy(org.cipres.treebase.domain.study.Study)
	 */
	public Collection<TaxonLabel> findByStudy(Study pStudy) {
		Collection<TaxonLabel> returnVal = new ArrayList<TaxonLabel>();

		if (pStudy != null) {
			Criteria c = getSession().createCriteria(TaxonLabel.class);
			c.add(Expression.eq("study", pStudy));

			// ALERT: the result might not be unique since mesquite allows duplicated taxonlabels
			// in the same tree.
			// returnVal = (TaxonLabel) c.uniqueResult();
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); 
			returnVal = c.list();
		}
		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findBySubstring(java.lang.String)
	 */
	public Collection<TaxonLabel> findBySubstring(String pTerm) {
		Criteria c = getSession().createCriteria(TaxonLabel.class);
		String termPattern = "%" + pTerm.toLowerCase() + "%";
		c.add(Expression.ilike("taxonLabel", termPattern));

		Collection<TaxonLabel> results = c.list();

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findBySubstring(java.lang.String)
	 */
	public Collection<TaxonLabel> findByExactString(String pTerm) {
		Criteria c = getSession().createCriteria(TaxonLabel.class);
		c.add(Expression.eq("taxonLabel", pTerm));

		Collection<TaxonLabel> results = c.list();

		return results;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#delete(java.util.Collection)
	 */
	public void delete(Collection<TaxonLabel> pTaxonLabels) {
		if (pTaxonLabels == null || pTaxonLabels.isEmpty()) {
			return;
		}

		for (TaxonLabel label : pTaxonLabels) {
			delete(label);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#getDefaultByDescription(java.lang.String)
	 */
	public TaxonLabel getDefaultByDescription(String pDescription) {
		String desc = "";
		if (pDescription != null) {
			desc = pDescription;
		}
		TaxonLabel defaultLabel = findByDescription(desc, null);

		// create new one if it is not found:
		if (defaultLabel == null) {
			defaultLabel = new TaxonLabel();
			defaultLabel.setTaxonLabel(desc);
			// TODO: how to set Taxon??
		}

		return defaultLabel;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#getByDescriptionAndStudy(java.lang.String,
	 *      org.cipres.treebase.domain.study.Study)
	 */
	public TaxonLabel getByDescriptionAndStudy(String pLabel, Study pStudy) {
		String desc = "";
		if (pLabel != null) {
			desc = pLabel;
		}

		TaxonLabel aLabel = findByDescription(desc, pStudy);

		// create new one if it is not found:
		if (aLabel == null && !TreebaseUtil.isEmpty(desc)) {
			aLabel = new TaxonLabel();
			aLabel.setTaxonLabel(desc);
			aLabel.setStudy(pStudy);
		}

		return aLabel;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#delete(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public void delete(TaxonLabel pTaxonLabel) {
		// ALERT: caller is responsible to verify the taxon label is deletable.

		// cascade delete
		// None

		deletePersist(pTaxonLabel);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#delete(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	public void delete(TaxonVariant pTaxonVariant) {
		deletePersist(pTaxonVariant);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#deleteByStudy(org.cipres.treebase.domain.study.Study)
	 */
	public void deleteByStudy(Study pStudy) {

		if (pStudy != null) {
			
			//use batch delete:
			String query = "delete from taxonLabel where study_id = :studyID";
			org.hibernate.Query q = getSession().createSQLQuery(query);
			q.setParameter("studyID", pStudy.getId());
			q.executeUpdate();
			
//			Criteria c = getSession().createCriteria(TaxonLabel.class);
//			c.add(Expression.eq("study", pStudy));
//			List returnVal = c.list();
//
//			delete(returnVal);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findStudies(java.util.Collection)
	 */
	public Collection<Study> findStudiesWithTaxonLabels(Collection<TaxonLabel> taxonLabels) {
		Collection<Study> results = new HashSet<Study>();
		for (TaxonLabel label : taxonLabels) {
			results.add(label.getStudy());
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findStudies(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	// TODO: Reimplement this using a join and a projection
	public Set<Study> findStudies(TaxonVariant taxonVariant) {
		Set<Study> result = new HashSet<Study> ();
		Set<TaxonLabel> tlSet = findByTaxonVariant(taxonVariant);
		for (TaxonLabel tl : tlSet) {
			result.add(tl.getStudy());
		}
		return result;
	}	
	
	// TODO: This is way too slow.   mjd 20080911
	public Collection<Matrix> findMatricesWithTaxonLabels(Collection<TaxonLabel> taxonLabels) {
		// Return empty collection if input is null or empty to avoid invalid HQL query
		if (taxonLabels == null || taxonLabels.isEmpty()) {
			return new ArrayList<Matrix>();
		}
		
		Query q = getSession().createQuery("select distinct mr.matrix from MatrixRow mr where mr.taxonLabel in (:tl)");
		q.setParameterList("tl", taxonLabels);
		List<Matrix> results = q.list();

		return results;
	}

	public Set<Matrix> findMatrices(TaxonVariant taxonVariant) {
		SQLQuery q = getSession()
		.createSQLQuery(
				"select distinct {m.*} from Matrix m, MatrixRow mr, TaxonLabel tl " 
				+ "where mr.taxonLabel_id = tl.taxonLabel_id "
				+ "and m.matrix_id = mr.matrix_id "
				+ "and tl.taxonVariant_id = :tv")
		.addEntity("m", Matrix.class)
		;
		
		q.setParameter("tv", taxonVariant.getId());

		List<Matrix> results = q.list();
		Set<Matrix> retval = new HashSet<Matrix> ();
		retval.addAll(results);

		return retval;
	}

	public Set<PhyloTree> findTrees(TaxonVariant taxonVariant) {
/*
		SQLQuery q = getSession()
		.createSQLQuery(
				"select distinct {t.*} from PhyloTree t, PhyloTreeNode tn, TaxonLabel tl " 
				+ "where tn.taxonLabel_id = tl.taxonLabel_id "
				+ "and tn.phylotree_id = t.phylotree_id "
				+ "and tl.taxonVariant_id = :tv")
		.addEntity("t", PhyloTree.class)
		;
	*/
		Query q = getSession()
		.createQuery("select tn.tree from PhyloTreeNode tn "
				+ "where tn.taxonLabel.taxonVariant = :tv")
		.setParameter("tv", taxonVariant);

		List<PhyloTree> results = q.list();
		Set<PhyloTree> retval = new HashSet<PhyloTree> ();
		retval.addAll(results);

		return retval;
	}

	public Collection<PhyloTree> findTreesWithTaxonLabels(Collection<TaxonLabel> taxonLabels) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#setStudyForAllLabels(org.cipres.treebase.domain.tree.PhyloTree, org.cipres.treebase.domain.study.Study)
	 */
	public void updateStudyForAllLabels(PhyloTree tree, Study study) {
		// XXX What if study is null?  Proceed anyway?
		for (TaxonLabel tl : tree.getAllTaxonLabels()) {
			tl.setStudy(study);
			save(tl);
		}	
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#setStudyForAllLabels(org.cipres.treebase.domain.matrix.Matrix, org.cipres.treebase.domain.study.Study)
	 */
	public void updateStudyForAllLabels(Matrix matrix, Study study) {
		// XXX What if study is null?  Proceed anyway?
		for (TaxonLabel tl : matrix.getAllTaxonLabels()) {
			tl.setStudy(study);
			save(tl);
		}	
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findTaxonVariantWithSubstring(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Set<TaxonVariant> findTaxonVariantWithSubstring(String s, Boolean caseSensitive) {
		Set<TaxonVariant> result = new HashSet<TaxonVariant> ();
		for (TBPersistable tv : findSomethingBySubstring(TaxonVariant.class, "fullName", s, caseSensitive)) {
		    result.add((TaxonVariant) tv);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#expandTaxonVariant(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	@SuppressWarnings("unchecked")
	public Set<TaxonVariant> expandTaxonVariant(TaxonVariant pTV) {
		Set<TaxonVariant> result = new HashSet<TaxonVariant> ();
		Criteria c = getSession().createCriteria(TaxonVariant.class);
		c.add(Restrictions.eq("taxon", pTV.getTaxon()));
		result.addAll(c.list());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#expandTaxonVariantSet(java.util.Set)
	 */
	public Set<TaxonVariant> expandTaxonVariantSet(Set<TaxonVariant> pTVSet) {
		Set<TaxonVariant> result = new HashSet<TaxonVariant> ();
		for (TaxonVariant tv : pTVSet) {
			result.addAll(expandTaxonVariant(tv));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findByTaxonVariant(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	public Set<TaxonLabel> findByTaxonVariant(TaxonVariant pTV) {
		Set<TaxonVariant> expansion = expandTaxonVariant(pTV);
		Criteria c = getSession().createCriteria(TaxonLabel.class);
		c.add(Restrictions.in("taxonVariant", expansion));
		Set<TaxonLabel> result = new HashSet<TaxonLabel> ();
		result.addAll(c.list());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findTaxonVariantByName(java.lang.String)
	 */
	public Set<TaxonVariant> findTaxonVariantByName(String name) {
		Set<TaxonVariant> result = new HashSet<TaxonVariant> ();
		for (TBPersistable tv : findSomethingByAttribute(TaxonVariant.class, "name", name)) {
			result.add((TaxonVariant) tv);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findTaxonVariantByFullName(java.lang.String)
	 */
	public Collection<TaxonVariant> findTaxonVariantByFullName(String tvFullName) {
		Set<TaxonVariant> result = new HashSet<TaxonVariant> ();
		for (TBPersistable tv : findSomethingByAttribute(TaxonVariant.class, "fullName", tvFullName)) {
			result.add((TaxonVariant) tv);
		}
		return result;
	}

	public Set<Submission> findSubmissions(TaxonLabel taxonLabel) {
		Query q = getSession()
		.createQuery("select s from Submission s where :tl member of s.submittedTaxonLabels");
		q.setParameter("tl", taxonLabel);
		Set<Submission> result = new HashSet<Submission> ();
		result.addAll(q.list());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#getTaxonLabelSetsReadOnly(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public Set<TaxonLabelSet> findTaxonLabelSets(TaxonLabel taxonLabel) {
		Query q = getSession()
		.createQuery("select tls from TaxonLabelSet tls join tls.taxonLabelList tl where tl = :tl");
		q.setParameter("tl", taxonLabel);
		Set<TaxonLabelSet> result = new HashSet<TaxonLabelSet> ();
		result.addAll(q.list());
		return result;

	}
    // need refactoring later
	//all the sql statement should go to same file or class 
	public Collection<Matrix> findMatrices(Taxon t) {
		Query q = getSession()
		.createSQLQuery("select distinct m.* from matrix m join matrixrow using(matrix_id) join taxonlabel " +
				"using (taxonlabel_id) join taxonvariant using (taxonvariant_id) where taxon_id = :id").addEntity(Matrix.class);
		q.setParameter("id", t.getId());
		Collection<Matrix> result = q.list();
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findStudies(org.cipres.treebase.domain.taxon.Taxon)
	 */
	public Collection<Study> findStudies(Taxon t) {
		Query q = getSession()
		.createQuery("select distinct s from Study s, TaxonLabel tl where " +
				"tl.study = s and tl.taxonVariant.taxon = :t");
		q.setParameter("t", t);
		Collection<Study> result = q.list();
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findTrees(org.cipres.treebase.domain.taxon.Taxon)
	 */
	public Collection<PhyloTree> findTrees(Taxon t) {
		Query q = getSession()
		.createQuery("select distinct pt from PhyloTree pt inner join fetch pt.treeNodes tn where " +
		"tn.taxonLabel.taxonVariant.taxon = :t"); 
		
		//("select pt from PhyloTree pt, TaxonLabel tl where " +
		//		"tl member of pt.treeBlock.taxonLabelSet.taxonLabelList and tl.taxonVariant.taxon = :t");
		q.setParameter("t", t);
		Collection<PhyloTree> result = new HashSet<PhyloTree>();
		for (Object o: q.list()) {  // Can't select distinct over phylotrees today 20081204 mjd
			result.add((PhyloTree) o);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findBySubstring(java.lang.String, boolean)
	 */
	public Collection<TaxonLabel> findBySubstring(String term, boolean caseSensitive) {
		Collection<TaxonLabel> result = new ArrayList<TaxonLabel> ();
		for (TBPersistable tv : findSomethingBySubstring(TaxonLabel.class, "taxonLabel", term, caseSensitive)) {
		    result.add((TaxonLabel) tv);
		}
		return result;
	}

	public void clean(TaxonLabelSet tSet) {
		// TODO Auto-generated method stub
		Query q = getSession()
		.createQuery("select count(*) from TreeBlock tb where tb.taxonLabelSet = :ts"); 
		q.setParameter("ts", tSet);
	    int count=((Long)q.iterate().next()).intValue();
	    
	    q = getSession()
		.createQuery("select count(*) from Matrix m where m.taxa = :ts"); 
		q.setParameter("ts", tSet);
	    count += ((Long)q.iterate().next()).intValue();
	    
	    if(count==0)deletePersist(tSet);
	}

	public void clean(List<TaxonLabel> tList) {
		// TODO Auto-generated method stub
		for(TaxonLabel tl : tList){
			Query q = getSession()
			.createQuery("select count(*) from PhyloTreeNode pn where pn.taxonLabel = :tl"); 
			q.setParameter("tl", tl);
		    int count=((Long)q.iterate().next()).intValue();
		    
		    q = getSession()
			.createQuery("select count(*) from MatrixRow mr where mr.taxonLabel = :tl"); 
			q.setParameter("tl", tl);
		    count += ((Long)q.iterate().next()).intValue();
		    
		    if(count==0){
		    	Submission sub=tl.getSubmission();
		    	if(sub!=null)sub.removeTaxonLabel(tl);
		    	deletePersist(tl);
		    }
		}
	}
}
