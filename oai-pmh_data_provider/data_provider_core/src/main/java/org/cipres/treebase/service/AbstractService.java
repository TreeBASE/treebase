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

package org.cipres.treebase.service;

import java.util.Collection;

import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;

/**
 * AbstractService.java
 * 
 * Created on May 22, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface AbstractService {

	/**
	 * Update the persistable object to the database. If the given instance is unsaved, save a copy
	 * of and return it as a newly persistent instance. The given instance does not become
	 * associated with the session.
	 * <p>
	 * This operation cascades to associated instances if the mapping "cascade="merge"" is defined.
	 * 
	 * @param pPersistable TBPersistable
	 * @return a merged persistent instance.
	 */
	<T extends TBPersistable> T update(T pPersistable);

	/**
	 * Update the persistable objects to the database. Please see the comments for update().
	 * 
	 * Open one transaction for updating all the objects.
	 * 
	 * @param pPersistable TBPersistable
	 * @return a merged persistent instance.
	 */
	<T extends TBPersistable> Collection<T> updateCollection(Collection<T> pPersistables);
	
	/**
	 * Given an ID number and a class, try to find the object with that ID and class.
	 * 
	 * <P>May return null; may return a proxy.
	 * 
	 * @param T - the class 
	 * @param id 
	 * @return returns the object of the specified class with the specified ID
	 * @author mjd 20090306
	 */
	<T extends TBPersistable> T findByID(Class T, Long id);

    /**
	 * Like findByID, but be liberal in accepting leading and trailing white space, "S" prefix, etc.
	 * 
	 * @param idString
	 * @return the object with this ID number
	 * @author mjd 20081121
     * @throws MalformedTreebaseIDString 
	 */
	TBPersistable findByIDString(String searchTerm) throws MalformedTreebaseIDString;

    /**
     * @param <T>
     * @param pPersistable
     * @return object ID of persistent object
     */
    <T extends TBPersistable> Long save(T pPersistable);
    
    /**
     * @param pPersistable a persistable object that might be detached from the database
     * @return an attached version of the same object
     * @author mjd
     */
    <T extends TBPersistable> T resurrect(T pPersistable);

    /**
     * @param pPersistable a collection of persistent objects that might be detached from the database
     * @return a collection of the attached versions of the same objects 
     * @throws InstantiationException
     * @author mjd
     */
    <T extends TBPersistable> Collection<T> resurrectAll(Collection<T> pPersistable) throws InstantiationException;

	
	/**
	 * Search all persistent objects of a certain class for those that contain a string in a particular attribute field.
	 * 
	 *  <p>Searching is case-sensitive.</p>
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought in the specified attribute
	 * @return a collection of all objects whose specified attribute contains the specified target string
	 * @author mjd 20090319
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByString(Class T,
			String attributeName,
			String target);
	
	/**
	 * Search all persistent objects of a certain class for those that contain a string in a particular attribute field.
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute that will be searched for the target
	 * @param target - the string that will be sought in the specified attribute
	 * @return a collection of all objects whose specified attribute contains the specified target string
	 * @author mjd 20090319
	 */
	public <T extends TBPersistable> Collection<T> findSomethingByString(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive);
	
	
	/**
	 * Search all persistent objects of a certain class for those that contain a certain substring in a particular attribute field.
	 * 
	 *  <p>Searching is case-sensitive.</p>
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
	 * Search all persistent objects of a certain class for those that contain a certain substring in a particular attribute field.
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
	 * Search all persistent objects of a certain class for those that contain a certain substring in a particular attribute's <i>description</i> field.
	 * 
	 * @param T - the class in which you want to find objects
	 * @param attributeName - the name of the attribute whose descriptions will be searched for the target
	 * @param target - the string that will be sought in the specified attribute's description
	 * @param caseSensitive - whether the search should be case-sensitive
	 * @return a collection of all objects whose specified attribute's description contains the specified target substring
	 * @author mjd 20081202
	 */
    <T extends TBPersistable> Collection<T> findSomethingByItsDescription(Class T,
			String attributeName,
			String target,
			Boolean caseSensitive);
    
    
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
    <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(Class T,
			String attributeName,
			String rangeExpression) throws MalformedRangeExpression;
    
    /**
     * A service class should throw this exception if it is not prepared to 
     * perform the requested service.
     * 
     * @author mjd 20080805
     */
    public class UnimplementedServiceException extends RuntimeException { }
}
