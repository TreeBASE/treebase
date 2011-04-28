
package org.cipres.treebase.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.TBPersistable;

/**
 * The abstract super class for all service objects.
 * 
 * Created on Oct 13, 2005
 * 
 * @author Jin Ruan
 * 
 */
public abstract class AbstractServiceImpl implements AbstractService {

	/**
	 * Constructor
	 */
	public AbstractServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractService#update(T)
	 */
	public <T extends TBPersistable> T update(T pPersistable) {
		return getDomainHome().merge(pPersistable);
	}

	/**
	 * Return the domain home.
	 * 
	 * @return
	 */
	protected abstract DomainHome getDomainHome();

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractService#updateCollection(java.util.Collection)
	 */
	public <T extends TBPersistable> Collection<T> updateCollection(Collection<T> pPersistables) {
		Collection<T> returnVal = new ArrayList<T>();

		if (pPersistables != null) {
			for (T t : pPersistables) {
				returnVal.add(update(t));
			}
		}

		return returnVal;
	}
	
    /* (non-Javadoc)
     * @see org.cipres.treebase.service.AbstractService#save(org.cipres.treebase.domain.TBPersistable)
     */
    public <T extends TBPersistable> Long save (T pPersistable) {
        return getDomainHome().save(pPersistable);
    }

	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#resurrect(org.cipres.treebase.domain.TBPersistable)
	 */
	public <T extends TBPersistable> T resurrect(T pPersistable) {
		getDomainHome().reattachUnmodified(pPersistable);
		return pPersistable;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#resurrectAll(java.util.Collection)
	 */
	public <T extends TBPersistable> Collection<T> resurrectAll(Collection<T> pPersistableItems) {
		/* Collection<Study> newItems = pPersistableItems.getClass().newInstance(); */
		if (pPersistableItems == null) {
			return null;
		}
		LinkedList<T> newItems = new LinkedList<T>();
		for (T s : pPersistableItems) {
			T r = resurrect(s);
			newItems.add(r);
		}

		return newItems;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingByString(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByString(
			Class T, String attributeName, String target) {
		// TODO Auto-generated method stub
		return findSomethingByString(T, attributeName, target, true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findAll(java.lang.Class)
	 */
	public <T extends TBPersistable> Collection<T> findAll(Class T) {
		return getDomainHome().findAll(T);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingByString(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByString(
			Class T, String attributeName, String target, Boolean caseSensitive) {
		// TODO Auto-generated method stub
		return getDomainHome().findSomethingByAttribute(T, attributeName, target, caseSensitive);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(
			Class T, String attributeName, String target) {
		// TODO Auto-generated method stub
		return getDomainHome().findSomethingBySubstring(T, attributeName, target);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingBySubstring(java.lang.Class, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(
			Class T, String attributeName, String target, Boolean caseSensitive) {
		return getDomainHome().findSomethingBySubstring(T, attributeName, target, caseSensitive);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingByItsDescription(java.lang.Class, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
			Class T, String attributeName, String target, Boolean caseSensitive) {
		return getDomainHome().findSomethingByItsDescription(T, attributeName, target, caseSensitive);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.service.AbstractService#findSomethingByRangeExpression(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(Class T,
				String attributeName,
				String rangeExpression) throws MalformedRangeExpression {
		return getDomainHome().findSomethingByRangeExpression(T, attributeName, rangeExpression);
	}
	
	public <T extends TBPersistable> T findByID(Class T, Long id) {
		return (T) getDomainHome().getPersistedObjectByID(T, id);
	}
	
	public TBPersistable findByIDString(String idString) throws MalformedTreebaseIDString {
		TreebaseIDString id;
		
		id = new TreebaseIDString(idString, defaultResultClass());
		if (id == null) return null;
		else return  getDomainHome().findPersistedObjectByID(id.getTBClass(), id.getId());
	}
	
	public abstract Class defaultResultClass();
}
