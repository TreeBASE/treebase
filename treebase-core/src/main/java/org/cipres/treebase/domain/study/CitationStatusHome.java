
package org.cipres.treebase.domain.study;

import org.cipres.treebase.domain.DomainHome;

/**
 * CitationStatusHome
 * 
 * Created on Apr 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface CitationStatusHome extends DomainHome {

	/**
	 * Find the citation status based on the description.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusByDescription(String pDescription);

	/**
	 * Find the status published.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusPublished();

	/**
	 * Find the status in press.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusInPress();

	/**
	 * Find the status in prep.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusInPrep();

	/**
	 * Find the status in review.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusInReview();

	/**
	 * Find the status accepted with minor changes.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findStatusAccepted();

}
