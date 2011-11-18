
package org.cipres.treebase.web.controllers;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.web.util.ControllerUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * DownloadATreeController.java
 * 
 * Generate a file file dynamically for download
 * 
 * Created on July 26, 2007
 * 
 * @author Madhu
 * 
 * 
 */

public class DownloadATreeController extends AbstractDownloadController implements Controller {

	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;

	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		if (request.getParameter("treeid") == null) {
			return null;
		}
		long treeid = Long.parseLong(request.getParameter("treeid"));
		generateAFileDynamically(request, response, treeid);
		return null;
	}

	@Override
	protected String getFileNamePrefix() {
		return "T";
	}

	@Override
	protected String getFileContent(long pTreeId, HttpServletRequest request) {
		Study study = ControllerUtil.findStudy(request, mStudyService);
		PhyloTree tree = getPhyloTreeService().findByID(pTreeId);
		tree = getPhyloTreeService().resurrect(tree);
		TreeBlock enclosingTreeBlock = getPhyloTreeService().resurrect(tree.getTreeBlock());
		TaxonLabelSet tls = getPhyloTreeService().resurrect(enclosingTreeBlock.getTaxonLabelSet());
		if ( getFormat(request) == FORMAT_NEXML || getFormat(request) == FORMAT_RDF ) {
			NexusDataSet nds = new NexusDataSet();
			nds.getTaxonLabelSets().add(tls);
			TreeBlock treeBlock = new TreeBlock();
			treeBlock.setTaxonLabelSet(tls);
			treeBlock.addPhyloTree(tree);
			nds.getTreeBlocks().add(treeBlock);
			return getNexmlService().serialize(nds,getDefaultProperties(request),tree.getStudy());
		}
		/*else if ( getFormat(request) == FORMAT_RDF ) {
			NexusDataSet nds = new NexusDataSet();
			nds.getTaxonLabelSets().add(tls);
			TreeBlock treeBlock = new TreeBlock();
			treeBlock.setTaxonLabelSet(tls);
			treeBlock.addPhyloTree(tree);
			nds.getTreeBlocks().add(treeBlock);			
			return getRdfaService().serialize(nds,getDefaultProperties(request),tree.getStudy());			
		}	*/	
		else {
			StringBuilder builder = new StringBuilder();
			builder.append("#NEXUS\n\n");
	
			// header:
			TreebaseUtil.attachStudyHeader(study, builder);
	
			// taxa:
			// one taxon label per line, no line number. 
			tls.buildNexusBlockTaxa(builder, true, false);
			
			//tree block:
			tree.buildNexusBlock(builder);
			
			return builder.toString();
		}
	}

	@Override
	protected Study getStudy(long objectId, HttpServletRequest request) {
		PhyloTree tree = getPhyloTreeService().findByID(objectId);
		return tree.getStudy();
	}

}
