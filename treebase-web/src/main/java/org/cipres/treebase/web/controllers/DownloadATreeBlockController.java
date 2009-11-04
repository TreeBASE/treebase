
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
			return getNexmlService().serialize(nexusDataSet,getDefaultProperties(request));
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			NexusDataSet nexusDataSet = new NexusDataSet();
			nexusDataSet.getTaxonLabelSets().add(treeBlock.getTaxonLabelSet());
			nexusDataSet.getTreeBlocks().add(treeBlock);			
			return getRdfaService().serialize(nexusDataSet,getDefaultProperties(request));			
		}		
		else {
			StringBuilder bldr = new StringBuilder("#NEXUS\n\n");
			treeBlock.generateAFileDynamically(bldr);
			return bldr.toString();
		}
	}

}
