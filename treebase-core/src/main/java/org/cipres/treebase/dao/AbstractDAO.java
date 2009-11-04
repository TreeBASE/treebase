
package org.cipres.treebase.dao;

import java.sql.Connection;
import java.util.Collection;

import org.cipres.treebase.RangeExpression;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.TBPersistable;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * AbstractDAO.java
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public abstract class AbstractDAO extends HibernateDaoSupport implements DomainHome {

	/**
	 * Constructor.
	 */
	public AbstractDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refresh(org.cipres.treebase.domain.TBPersistable)
	 */
	public void refresh(TBPersistable pPersistable) {
		getHibernateTemplate().refresh(pPersistable);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refreshAll(java.util.Collection)
	 */
	public void refreshAll(Collection<? extends TBPersistable> pDomainObjects) {
		if (pDomainObjects == null) {
			return;
		}

		HibernateTemplate template = getHibernateTemplate();
		for (TBPersistable persistedObj : pDomainObjects) {
			template.refresh(persistedObj);
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#store(org.cipres.treebase.domain.TBPersistable)
	 */
	public void store(TBPersistable pPersistable) {

		getHibernateTemplate().persist(pPersistable);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#storeAll(java.util.Collection)
	 */
	public void storeAll(Collection<? extends TBPersistable> pPersistables) {
		for (TBPersistable persistable : pPersistables) {
			getHibernateTemplate().persist(persistable);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#deletePersist(org.cipres.treebase.domain.TBPersistable)
	 */
	public void deletePersist(TBPersistable pPersistable) {

		if (pPersistable != null && pPersistable.getId() != null) {
			getHibernateTemplate().delete(pPersistable);
		}
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#getConnection()
	 */
	public Connection getConnection() {
		//Note: Hibernate 3 deprecated this method. 
		// use Spring HibernateDAOSupport.connection() when we upgrade to 
		// spring 2.5+
		return getSession().connection();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<? extends TBPersistable> pDomainObjects) {
		getHibernateTemplate().deleteAll(pDomainObjects);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#findPersistedObjectByID(java.lang.Class,
	 *      java.lang.Long)
	 */
	public <T extends TBPersistable> T findPersistedObjectByID(Class T, Long pID) {
		T result = null;
		if (pID != null) {

			Criteria c = getSession().createCriteria(T);
			c.add(Expression.eq("id", pID));
			T uniqueResult = (T) c.uniqueResult();
			result = uniqueResult;
		}
		return result;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#getPersistedObjectByID(java.lang.Class, java.lang.Long)
	 */
	public <T extends TBPersistable> T getPersistedObjectByID(Class T, Long pID) {
		return (T) getHibernateTemplate().get(T, pID);
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#loadPersistedObjectByID(java.lang.Class, java.lang.Long)
	 */
	public <T extends TBPersistable> T loadPersistedObjectByID(Class T, Long pID) {
		return (T) getHibernateTemplate().load(T, pID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#merge(T)
	 */
	public <T extends TBPersistable> T merge(T pPersistable) {
		if (pPersistable == null) {
			return null;
		}

		Object obj = getHibernateTemplate().merge(pPersistable);
		return (T) obj;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#flush()
	 */
	public void flush() {
		getHibernateTemplate().flush();

	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#clear()
	 */
	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#setFlushMode(int)
	 */
	public void setFlushMode(int pFlushMode) {
		getHibernateTemplate().setFlushMode(pFlushMode);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#getFlushMode()
	 */
	public int getFlushMode() {
		return getHibernateTemplate().getFlushMode();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#save(org.cipres.treebase.domain.TBPersistable)
	 */
	public Long save(TBPersistable pPersistable) {
		getHibernateTemplate().save(pPersistable);
		return pPersistable.getId();
	}

	public void reattachUnmodified(TBPersistable pPersistable) {
		if (pPersistable != null) {
			getHibernateTemplate().lock(pPersistable, LockMode.NONE);
		}
	}


	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(Class T,
			String attributeName,
			String target) {
		return findSomethingBySubstring(T, attributeName, target, true);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive) {
		Criteria c = getSession().createCriteria(T);
		// XXX check target for metacharacters here
		String termPattern = "%" + target + "%";
		if (caseSensitive) {
			c.add(Expression.like(attributeName, termPattern));
		} else {
			c.add(Expression.ilike(attributeName, termPattern));
		}
		
		Collection<T> results = c.list();
		return results;	
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByAttribute(Class T,
			String attributeName,
			String target) {
		return findSomethingByAttribute(T, attributeName, target, true);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByAttribute(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive) {
		Criteria c = getSession().createCriteria(T);
		if (caseSensitive) {
			c.add(Expression.eq(attributeName, target));
		} else {
			throw new UnsupportedOperationException("case insensitive findSomethingByAttribute");
		}
		
		Collection<T> results = c.list();
		return results;	
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findAll(java.lang.Class)
	 */
	// TODO: This duplicates HibernateTemplate.loadAll(pClass); 
	// reimplement using that.  MJD 20081016
	@SuppressWarnings("unchecked")
	public <T extends TBPersistable> Collection<T> findAll(Class T) {
		Collection<T> results = getSession().createCriteria(T).list();
		return results;
	}
	

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.DomainHome#findSomethingByItsDescription(java.lang.Class, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
			Class T, String attributeName, String target, Boolean caseSensitive) {
		Collection<T> results;
		Criteria crit = getSession().createCriteria(T);
		crit.createAlias( attributeName, "something");
		if (caseSensitive) {
			crit.add(Expression.like("something.description", "%" + target + "%"));			
		} else {
			crit.add(Expression.ilike("something.description", "%" + target + "%"));
		}
		results = crit.list();
		return results;
	}


    /* (non-Javadoc)
     * @see org.cipres.treebase.domain.DomainHome#findSomethingByRangeExpression(java.lang.Class, java.lang.String, java.lang.String)
     */
    public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(Class t, String attributeName,
			String rangeExpression) throws MalformedRangeExpression {
    	RangeExpression re = new RangeExpression(rangeExpression);
    	Criteria criteria = getSession().createCriteria(t);
//    	criteria.createAlias(attributeName, "alias");
    	criteria.add(re.getCriteria(attributeName));
    	Collection<T> results = criteria.list();
      	return results;
    }
}
