/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * 
 * @author madhu
 * 
 */

public class DownloadATreeBlockController extends AbstractDownloadController implements Controller {

	private PhyloTreeHome mPhyloTreeHome;

	/**
	 * @return the phyloTreeHome
	 */
	public PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * @param pPhyloTreeHome the phyloTreeHome to set
	 */
	public void setPhyloTreeHome(PhyloTreeHome pPhyloTreeHome) {
		mPhyloTreeHome = pPhyloTreeHome;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		if (request.getParameter("treeblockid") == null) {
			return null;
		}
		long blockid = Long.parseLong(request.getParameter("treeblockid"));
		generateAFileDynamically(request, response, blockid);
		return null;
	}

	@Override
	protected String getFileNamePrefix() {
		return "TB";
	}

	@Override
	protected String getFileContent(long blockid, HttpServletRequest request) {
		TreeBlock treeBlock = getPhyloTreeHome().findTreeBlockById(blockid);		
		if ( getFormat(request) == FORMAT_NEXML ) {
			NexusDataSet nexusDataSet = new NexusDataSet();
			nexusDataSet.getTaxonLabelSets().add(treeBlock.getTaxonLabelSet());
			nexusDataSet.getTreeBlocks().add(treeBlock);
			return getNexmlService().serialize(nexusDataSet);
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			NexusDataSet nexusDataSet = new NexusDataSet();
			nexusDataSet.getTaxonLabelSets().add(treeBlock.getTaxonLabelSet());
			nexusDataSet.getTreeBlocks().add(treeBlock);			
			return getRdfaService().serialize(nexusDataSet);			
		}		
		else {
			StringBuilder bldr = new StringBuilder("#NEXUS\n\n");
			treeBlock.generateAFileDynamically(bldr);
			return bldr.toString();
		}
	}

}
