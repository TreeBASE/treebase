
package org.cipres.treebase.domain.study;

import java.util.List;

import org.cipres.treebase.domain.DomainHome;

/**
 * The home interface for the Analysis step domain objects.
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalysisStepHome extends DomainHome {
	public List<String> findCompleteSoftwareName(String pPartialName);
}
