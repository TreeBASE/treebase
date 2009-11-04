
package org.cipres.treebase.dao.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyCriteria;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

/**
 * StudyDAO.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class StudyDAO extends AbstractDAO implements StudyHome {

    private static final Logger LOGGER = Logger.getLogger(StudyDAO.class);
    private TaxonLabelHome taxonLabelHome;

	/**
	 * Constructor.
	 */
	public StudyDAO() {
		super();
	}

	/**
	 * @return the taxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return taxonLabelHome;
	}

	/**
	 * @param taxonLabelHome the taxonLabelHome to set
	 */
	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		this.taxonLabelHome = taxonLabelHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findByAccessionNumber(java.lang.String)
	 */
	public Study findByAccessionNumber(String pAccessionNumber) {
		Study returnVal = null;

		if (!TreebaseUtil.isEmpty(pAccessionNumber)) {
			Criteria c = getSession().createCriteria(Study.class);
			c.add(Expression.eq("accessionNumber", pAccessionNumber));

			returnVal = (Study) c.uniqueResult();
		}
		return returnVal;
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findByAuthor(org.cipres.treebase.domain.admin.Person)
	 */
	public Collection<Study> findByAuthor(Person pAuthor) {
		Collection<Study> returnVal = new ArrayList<Study>();

		// FIXME: unit test
		if (pAuthor != null) {
			// String query = "from Study as s join Study.citation as c where :author in elements
			// (c.authors)";
			String query = "from Study join Study.citation c where :author in elements (c.authors)";
			returnVal = getHibernateTemplate().findByNamedParam(query, "author", pAuthor);
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findByName(java.lang.String, boolean)
	 */
	public Collection<Study> findByName(String pStudyName, boolean pCaseSensitive) {
		Collection<Study> returnVal = new ArrayList<Study>();

		if (!TreebaseUtil.isEmpty(pStudyName)) {
			Criteria c = getSession().createCriteria(Study.class);

			if (pCaseSensitive) {
				c.add(Expression.eq("name", pStudyName));
			} else {
				c.add(Expression.eq("name", pStudyName).ignoreCase());
			}

			returnVal = c.list();
		}

		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findBySubmitter(org.cipres.treebase.domain.admin.User)
	 */
	public Collection<Study> findBySubmitter(User pUser) {
		// FIXME: findBySubmitter
		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findByCriteria(org.cipres.treebase.domain.study.StudyCriteria)
	 */
	public Collection<Study> findByCriteria(StudyCriteria pCriteria) {
		if (pCriteria == null) {
			return null;
		}

		List<String> authorLastNames = pCriteria.getAuthorLastNames();
		List<String> citationTitles = pCriteria.getCitationTitles();
		List<String> taxonLabels = pCriteria.getTaxonLabels();

		String algorithm = pCriteria.getAlgorithm();
		String software = pCriteria.getSoftware();

		// FIXME: findByCriteria

		Criteria c = getSession().createCriteria(Study.class);
		if (authorLastNames != null && !authorLastNames.isEmpty()) {

			c.createAlias("citation.authors", "author");

			for (String lname : authorLastNames) {
				c.add(Restrictions.ilike("author.lastName", "%" + lname + "%"));
			}
		}

		if (citationTitles != null && !citationTitles.isEmpty()) {
			c.createAlias("citation", "c");

			for (String cTitle : citationTitles) {
				c.add(Restrictions.ilike("c.title", "%" + cTitle + "%"));
			}
		}

		c.createAlias("analyses", "analysis");
		c.createAlias("analysis.analysisSteps", "step");

		if (!TreebaseUtil.isEmpty(software)) {
			c.createAlias("step.softwareInfo", "software");

			c.add(Restrictions.ilike("software.name", "%" + software + "%"));
		}

		if (!TreebaseUtil.isEmpty(algorithm)) {
			// c.createAlias("analyses", "analysis");
			// c.createAlias("analysis.analysisSteps", "step");
			c.createAlias("step.algorithmInfo", "algorithm");

			c.add(Restrictions.ilike("algorithm.description", "%" + algorithm + "%"));
		}

		List studies = c.list();

		return studies;
	}

	public void cleanCache() {
		getSession().flush();
		getSession().clear();
	}

    public Collection<Study> findByKeyword(String keywordPattern) {
        Collection<Study> studies = new ArrayList<Study>();
        Criteria c; 
 
        if (keywordPattern == null) { return studies; }
          LOGGER.info("StudyDAO.findByKeyword " + keywordPattern);  

          c = getSession().createCriteria(Study.class).createAlias("citation", "cit");
          LOGGER.info("StudyDAO.findByKeyword: created criterion object");  
          c.add( Restrictions.disjunction()
                          .add(Restrictions.ilike("name", keywordPattern))
                          .add(Restrictions.ilike("notes", keywordPattern))
                            .add(Restrictions.ilike("cit.keywords", keywordPattern))
                              .add(Restrictions.ilike("cit.title", keywordPattern))
                                .add(Restrictions.ilike("cit.URL", keywordPattern))
                                  .add(Restrictions.ilike("cit.journal", keywordPattern))
                                    .add(Restrictions.ilike("cit.abstract", keywordPattern))
                          );
 
          studies = (Collection<Study>) c.list();
                LOGGER.info("keyword query complete, returned " + studies.size() + " item(s)");  
          
          return studies;
}

	public Collection<Study> findByAuthor(String authorNamePattern) {
		Collection<Study> studies = new ArrayList<Study>();
		Criteria c; 
	
		if (authorNamePattern == null) { return studies; }
		LOGGER.info("StudyDAO.findByAuthor " + authorNamePattern);  

		Criteria studyCrit = getSession().createCriteria(Study.class).createAlias("citation", "cit");
		Criteria authorCrit = studyCrit.createCriteria("cit.authors", "au");
		authorCrit.add(Restrictions.ilike("lastName", authorNamePattern));

		studies = (Collection<Study>) authorCrit.list();
		LOGGER.info("author query complete, returned " + studies.size() + " item(s)");  

		return studies;
	}

	// TODO
	public Collection<Study> findByAbstract(String abstractPattern) {
		Collection<Study> studies = new ArrayList<Study>();
		Criteria c; 

		if (abstractPattern == null) { 
			return studies; 
		}
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("StudyDAO.findByAbstract " + abstractPattern);  
		}
		
		c = getSession().createCriteria(Study.class).createAlias("citation", "cit");
		c.add(Restrictions.ilike("cit.abstract", abstractPattern));

		studies = (Collection<Study>) c.list();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("abstract query complete, returned " + studies.size() + " item(s)");  
		}
		
		return studies;
	}

	public Collection<Study> findByTitle(String titlePattern) {
		Collection<Study> studies = new ArrayList<Study>();
		Criteria c; 
	
		if (titlePattern == null) { return studies; }
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("StudyDAO.findByTitle " + titlePattern);  
		}
		
		c = getSession().createCriteria(Study.class).createAlias("citation", "cit");
		c.add(Restrictions.ilike("cit.title", titlePattern));

		studies = (Collection<Study>) c.list();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("title query complete, returned " + studies.size() + " item(s)");  
		}
		return studies;
	}

	public Collection<Study> findByCitation(String citationPattern) {
		Collection<Study> studies = new ArrayList<Study>();
		Criteria c; 

		if (citationPattern == null) { return studies; }
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("StudyDAO.findByCitation " + citationPattern);  
		}
        c = getSession().createCriteria(Study.class).createAlias("citation", "cit");
		
        if (LOGGER.isInfoEnabled()) {
			LOGGER.info("StudyDAO.findByCitation: created criterion object");  
		}
		c.add( Restrictions.disjunction()
                          .add(Restrictions.ilike("cit.keywords", citationPattern))
                            .add(Restrictions.ilike("cit.title", citationPattern))
                              .add(Restrictions.ilike("cit.URL", citationPattern))
                                .add(Restrictions.ilike("cit.journal", citationPattern))
                                  .add(Restrictions.ilike("cit.abstract", citationPattern))
                        );
 
		studies = (Collection<Study>) c.list();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("citation query complete, returned " + studies.size() + " item(s)");  
		}
		
		return studies;

	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#findByTB1StudyID(java.lang.String)
	 */
	public Collection<Study> findByTB1StudyID(String studyID) {
		
		//Note: java bean naming convention: for all cap property like
		// TBxxxx, the bean name is TBxxxx. 
		List<Study> studies =
		getSession().createQuery("from Study where TB1StudyID = :sid")
			.setParameter("sid", studyID)
			.list();
		
		return studies;
	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.study.StudyHome#setTBStudyID(org.cipres.treebase.domain.study.Study, java.lang.String)
	 */
	public void setTBStudyID(Study pStudy, String tbStudyID) {
		if (pStudy == null) {
			return;
		}
		
		SQLQuery sqlQuery = getSession().createSQLQuery("UPDATE STUDY set TB_STUDYID = :tbsid where STUDY_id = :sid");
		
		sqlQuery.setParameter("sid", pStudy.getId());
		
		//if (tbStudyID != null) {
			sqlQuery.setParameter("tbsid", tbStudyID);
		//} else {
			
		sqlQuery.executeUpdate();
	}

	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.StudyHome#findByTrees(java.util.Set)
	 */
	public Collection<Study> findByTrees(Set<PhyloTree> trees) {
		Set<Study> result = new HashSet<Study>();
		for (PhyloTree t : trees) {
			Study s = t.getStudy();
			if (s != null) {
				result.add(s);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.StudyHome#findByTaxonLabelName(java.lang.String)
	 */
	public Collection<Study> findByTaxonLabelName(String label) {
		Collection<TaxonLabel> taxonLabels = getTaxonLabelHome().findByExactString(label);
		Collection<Study> studies = getTaxonLabelHome().findStudiesWithTaxonLabels(taxonLabels);
		return studies;
	}


}
