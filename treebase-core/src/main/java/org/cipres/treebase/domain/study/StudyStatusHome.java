
package org.cipres.treebase.domain.study;

import org.cipres.treebase.domain.DomainHome;

/**
 * StudyStatusHome.java
 * 
 * Created on Apr 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface StudyStatusHome extends DomainHome {

	/**
	 * Find the study status "in progress".
	 * 
	 * @return StudyStatus
	 */
	StudyStatus findStatusInProgress();

	/**
	 * Find the study status "ready".
	 * 
	 * @return StudyStatus
	 */
	StudyStatus findStatusReady();

	/**
	 * Find the study status "published".
	 * 
	 * @return StudyStatus
	 */
	StudyStatus findStatusPublished();

}
