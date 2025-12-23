
package org.cipres.treebase.domain;

import java.sql.Connection;
import java.util.Collection;

import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateAccessor;

/**
 * DomainHome.java
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface DomainHome {

	// JDBC/Hibernate related Constants
	// need to match defined in hibernate.cfg.xml
	public static final int JDBC_BATCH_SIZE = 40;

	public SessionFactory getSessionFactory();
	
	/**
	 * Refresh the state of the instance from the data store.
	 * <p>
	 * In an optimistic transaction, the state of instances in the cache might not match the state
	 * in the data store. This method is used to reload the state of the instance from the data
	 * store so that a subsequent commit is more likely to succeed.
	 * 
	 * @param pPersistable
	 */
	void refresh(TBPersistable pPersistable);

	/**
	 * Refresh the state of the instances from the data store.
	 * 
	 * @param pDomainObjects
	 * @see refresh(TBPersistable)
	 */
	void refreshAll(Collection<? extends TBPersistable> pDomainObjects);

	/**
	 * Make the specified object persistent in the database.
	 * <p>
	 * If the object already is presisted, an Exception is thrown.
	 * 
	 * @param pPersistable TBPersistable
	 */
	void store(TBPersistable pPersistable);

	/**
	 * Make the specified objects persistent in the database.
	 * <p>
	 * If any object already is presisted, an Exception is thrown.
	 * 
	 * @param pPersistables
	 */
	void storeAll(Collection<? extends TBPersistable> pPersistables);

	/**
	 * Make the specified object disappear from the database.
	 * <p>
	 * Just return if the object is not presisted.
	 * 
	 * @param pPersistable TBPersistable
	 */
	void deletePersist(TBPersistable pPersistable);

	/**
	 * Delete the specified persistent objects.
	 * 
	 * @param pDomainObjects
	 */
	void deleteAll(Collection<? extends TBPersistable> pDomainObjects);

	/**
	 * Return the connection for the current session.
	 * 
	 * @return
	 */
	Connection getConnection();
	
	/**
	 * Returns a persisted object by id.
	 * 
	 * @param T
	 * @param pID
	 * @return <T extends TBPersistable>
	 */
	<T extends TBPersistable> T findPersistedObjectByID(Class T, Long pID);

	/**
	 * Get a persisted object by id. Returns null if no object is found. 
	 * It always hit the database to return the real object, not a proxy. 
	 * 
	 * 
	 * @param T
	 * @param pID
	 * @return <T extends TBPersistable>
	 * @see loadPersistedObjectByID
	 */
	<T extends TBPersistable> T getPersistedObjectByID(Class T, Long pID);

	/**
	 * Load a persisted object by id. throws an objectNotFoundException if no object is found. 
	 * It may return a proxy. 
	 * 
	 * @param T
	 * @param pID
	 * @return <T extends TBPersistable>
	 * @see getPersistedObjectByID
	 */
	<T extends TBPersistable> T loadPersistedObjectByID(Class T, Long pID);

	/**
	 * Merge the state of the specified object into the current persistence context. If the given
	 * instance is unsaved, save a copy of and return it as a newly persistent instance. The given
	 * instance does not become associated with the session. This operation cascades to associated
	 * instances if the mapping "cascade="merge"".
	 * 
	 * @param pPersistable TBPersistable
	 * @return a merged persistent instance.
	 */
	<T extends TBPersistable> T merge(T pPersistable);

	/**
	 * Flush all pending saves, updates, and deletes to the database.
	 * <p>
	 * Only invoke this for selective eager flushing, for example when JDBC code needs to see
	 * certain changes within the same transaction. Else, it's preferable to rely on auto-flushing
	 * at transaction completion.
	 * 
	 */
	void flush();

	/**
	 * Set the flush mode.
	 * <p>
	 * The flush modes are defined in HibernateAccessor. The default flush mode is FLUSH_AUTO.
	 * 
	 * @see HibernateAccessor
	 */
	void setFlushMode(int pFlushMode);

	/**
	 * Get the current flush mode.
	 * <p>
	 * The flush modes are defined in HibernateAccessor. The default flush mode is FLUSH_AUTO.
	 * 
	 * @see HibernateAccessor
	 */
	int getFlushMode();

	/**
	 * Completely clear the session. Evict all loaded instances and cancel all pending saves,
	 * updates and deletions. Do not close open iterators or instances of ScrollableResults.
	 * 
	 */
	void clear();

	/**
	 * @param pPersistable
	 * @return
	 */
	Long save(TBPersistable pPersistable);

    void reattachUnmodified(TBPersistable pPersistable);
    
	/**
	 * Find all persistent objects for which the specified attribute contains the specified substring
	 * 
	 * <p>Searching is case-sensitive</p>
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought in the specified attribute
	 * @return a collection of all objects whose specified attribute contains the specified target substring
	 * @author mjd 20080729
	 */
	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(Class T,
			String attributeName,
			String target);
			
	/**
	 * Find all persistent objects for which the specified attribute contains the specified substring
	 * 
	 * <p>Searching is case-sensitive or not, according to the caseSensitive parameter</p>
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought in the specified attribute
	 * @param caseSensitive - whether the search should be case-sensitive
	 * @return a collection of all objects whose specified attribute contains the specified target substring
	 * @author mjd 20080729
	 */
    <T extends TBPersistable> Collection<T> findSomethingBySubstring(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive);
    
    
	/**
	 * Find all persistent objects for which the specified attribute equals the specified value
	 * 
	 * Matching is case-sensitive
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought as the specified attribute
	 * @return a collection of all objects whose specified attribute equals the specified target substring
	 * @author mjd 20080813
	 */
    <T extends TBPersistable> Collection<T> findSomethingByAttribute(Class T,
			String attributeName,
			String target);
    
	/**
	 * Find all persistent objects for which the specified attribute equals the specified value
	 *	
	 * Searching is case-sensitive or not, according to the caseSensitive parameter
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought as the specified attribute
	 * @param caseSensitive - whether the search should be case-sensitive
	 * @return a collection of all objects whose specified attribute equals the specified target substring
	 * @author mjd 20080813
	 */
    <T extends TBPersistable> Collection<T> findSomethingByAttribute(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive);
	
	/**
	 * Search all persistent objects of a certain class for those that contain a certain substring in a particular attribute's <i>description</i> field.
	 * 
	 * <p>TODO: This method has no unit tests!</p>
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute whose descriptions will be searched for the target
	 * @param target - the string that will be sought in the specified attribute's description
	 * @param caseSensitive - whether the search should be case-sensitive
	 * @return a collection of all objects whose specified attribute's description contains the specified target substring
	 * @author mjd 20081202
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
			Class T, String attributeName, String target, Boolean caseSensitive);
	
	/**
	 * Search all persistent objects of a certain class for those whose specified attribute
	 * satisfies a given range expression.
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be matched against the range expression
	 * @param rangeExpression 
	 * @return a collection of all objects as required
	 * @author mjd 20081203
	 * @throws MalformedRangeExpression 
	 */
    public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(Class t, String attributeName,
			String rangeExpression) throws MalformedRangeExpression; 
    
    /**
     * 
     * @param theClass 
     * @return all known objects of the specified class
     * @author mjd 20080729
     * For example, findAll(PhyloTree.class) should return all known PhyloTrees.
     */
    public <T extends TBPersistable> Collection<T> findAll(Class T);
}
