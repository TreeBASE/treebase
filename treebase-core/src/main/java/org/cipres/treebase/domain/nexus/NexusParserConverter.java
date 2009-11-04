
package org.cipres.treebase.domain.nexus;

import java.io.File;
import java.util.Collection;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.event.ProgressionListener;

/**
 * NexusParserConverter.java
 * 
 * Created on Aug 23, 2006
 * @author Jin Ruan
 *
 */
public interface NexusParserConverter {

	/**
	 * Parse the nexus files, and append the results in the NexusDataSet
	 * object. 
	 * 
	 * @param pNexusFiles
	 * @param pStudy
	 * @param pDataSet
	 * @param pListener
	 */
	void processLoadFile(Collection<File> pNexusFiles, Study pStudy, NexusDataSet pDataSet, ProgressionListener pListener);
}
