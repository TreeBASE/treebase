/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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