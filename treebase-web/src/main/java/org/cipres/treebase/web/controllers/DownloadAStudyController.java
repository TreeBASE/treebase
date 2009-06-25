/**
 * 
 */
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author rvosa
 *
 */
public class DownloadAStudyController extends AbstractDownloadController
		implements Controller {
	private StudyService mStudyService;

	public StudyService getStudyService() {
		return mStudyService;
	}

	public void setStudyService(StudyService studyService) {
		mStudyService = studyService;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.AbstractDownloadController#getFileContent(long, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String getFileContent(long pStudyID, HttpServletRequest request) {
		Study study = getStudyService().findByID(pStudyID);
		if ( getFormat(request) == FORMAT_NEXML ) {
			return getNexmlService().serialize(study);
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			return getRdfaService().serialize(study);			
		}		
		else {
			StringBuilder builder = new StringBuilder();
			builder.append("#NEXUS\n\n");
						
			// header:
			TreebaseUtil.attachStudyHeader(study, builder);
	
			// taxa:			
			for ( TaxonLabelSet tls : study.getTaxonLabelSets() ) {
				// one taxon label per line, no line number. 
				tls.buildNexusBlockTaxa(builder, true, false);
			}
			
			// matrices::
			for ( Matrix m : study.getMatrices() ) {
				m.generateNexusBlock(builder);
			}
			
			//tree blocks:
			for ( TreeBlock tb : study.getTreeBlocks() ) {
				tb.generateAFileDynamically(builder);
			}
			
			return builder.toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.AbstractDownloadController#getFileNamePrefix()
	 */
	@Override
	protected String getFileNamePrefix() {
		return TreebaseIDString.getPrefixForClass(Study.class);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getParameter("id") == null) {
			return null;
		}
		long pStudyId = Long.parseLong(request.getParameter("id"));
		generateAFileDynamically(request, response, pStudyId);
		return null;
	}

}
