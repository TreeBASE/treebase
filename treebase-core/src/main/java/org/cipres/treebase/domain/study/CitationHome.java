
package org.cipres.treebase.domain.study;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.Person;

/**
 * The home interface for the Citation domain objects.
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface CitationHome extends DomainHome {

	/**
	 * Replace the person record in all citation-author and citation-editor relationships. 
	 * 
	 * @param pSrc
	 * @param pTarget
	 * @return number of replaced records. 
	 */
	int replaceAuthorAndEditor(Person pSrc, Person pTarget);
}
