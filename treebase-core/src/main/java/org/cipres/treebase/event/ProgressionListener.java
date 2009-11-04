
package org.cipres.treebase.event;

import java.util.EventListener;

/**
 * Keep track of progress for interested parties.
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface ProgressionListener extends EventListener {

	/**
	 * Update the process. The task is completed when the processed count equals the total count.
	 * 
	 * @param pProcessedCount
	 * @param pTotalCount
	 */
	void updateProgress(int pProcessedCount, int pTotalCount);
}
