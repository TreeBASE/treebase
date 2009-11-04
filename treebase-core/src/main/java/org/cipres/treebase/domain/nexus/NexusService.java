package org.cipres.treebase.domain.nexus;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.event.ProgressionListener;

/**
 * Interface for parsing and generating nexus file.
 * 
 * @author Jin Ruan
 */
public interface NexusService {

	/**
	 * Parse the nexus files into a nexus data set object.
	 * Notify the progress listener for the parsing progress. 
	 * 
	 * Deprecated: use the version to parse one file at a time.
	 * 
	 * @param pStudy
	 * @param pNexusFiles
	 * @param pListener
	 * @return
	 */
	@Deprecated 
	NexusDataSet parseNexus(Study pStudy, Collection<File> pNexusFiles, ProgressionListener pListener);

	/**
	 * Parse the nexus file into a nexus data set object.
	 * 
	 * @param pStudy
	 * @param pNexusFile
	 * @return
	 */
	NexusDataSet parseNexus(Study pStudy, File pNexusFile);
	
	/**
	 * Write the NexusDataSet to a string that represents the object.
	 * The exact output format is up to the implementing service, i.e.
	 * this can either be nexus or nexml.
	 * 
	 * @param pNexusDataSet
	 * @return a serialization of the supplied NexusDataSet
	 */
	String serialize(NexusDataSet pNexusDataSet);
	String serialize(NexusDataSet pNexusDataSet, Properties pProperties);
	
	/**
	 * Write the NexusDataSet to a string that represents the object.
	 * The exact output format is up to the implementing service, i.e.
	 * this can either be nexus or nexml.
	 * 
	 * @param pStudy
	 * @return a serialization of the supplied Study
	 */	
	String serialize(Study pStudy);
	String serialize(Study pStudy, Properties pProperties);
	

}
