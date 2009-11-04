
package org.cipres.treebase.dao.study;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.CitationHome;

/**
 * CitationDAO.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class CitationDAO extends AbstractDAO implements CitationHome {

	/**
	 * Constructor.
	 */
	public CitationDAO() {
		super();
	}

	/**
	 * @param pPartialName
	 * @return a list of the matches.
	 */
	public List<String> findCompleteJournalName(String pPartialName) {
		List<String> alist = new ArrayList<String>();

		if (!TreebaseUtil.isEmpty(pPartialName) && pPartialName.trim().length() >= 1) {

			Query q = getSession().createQuery(
				"select distinct journal from Citation where lower(journal) like :mStr");

			q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
			alist = q.list();
		}
		return alist;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.CitationHome#replaceAuthorAndEditor(org.cipres.treebase.domain.admin.Person, org.cipres.treebase.domain.admin.Person)
	 */
	public int replaceAuthorAndEditor(Person pSrc, Person pTarget) {
		if (pSrc == null || pTarget == null) {
			return 0;
		}
		
		int changeCount = 0;
		
		String query = "UPDATE CITATION_EDITOR SET EDITORS_PERSON_ID= :targetId WHERE EDITORS_PERSON_ID=:srcId";
		changeCount += replaceQuery(pSrc, pTarget, query);

		query = "UPDATE CITATION_AUTHOR SET AUTHORS_PERSON_ID= :targetId WHERE AUTHORS_PERSON_ID=:srcId";
		changeCount += replaceQuery(pSrc, pTarget, query);
		
		return changeCount;
	}

	/**
	 * 
	 * @param pSrc
	 * @param pTarget
	 * @param query
	 */
	private int replaceQuery(Person pSrc, Person pTarget, String pQuery) {
		Query q = getSession().createSQLQuery(pQuery);
		
		q.setParameter("srcId", pSrc.getId());
		q.setParameter("targetId", pTarget.getId());
		
		return q.executeUpdate();
	}

}
