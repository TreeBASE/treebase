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

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DownloadAMatrixController.java
 * 
 * Generate a file file dynamically for download
 * 
 * Created on July 30, 2007
 * 
 * @author Madhu
 * 
 * 
 */

public class DownloadAMatrixController extends AbstractDownloadController implements Controller {

	private MatrixService mMatrixService;
	private StudyService mStudyService;

	/**
	 * Return the MatrixService field.
	 * 
	 * @return MatrixService mMatrixService
	 */
	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	/**
	 * Set the MatrixService field.
	 */
	public void setMatrixService(MatrixService pNewMatrixService) {
		mMatrixService = pNewMatrixService;
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
		if (request.getParameter("matrixid") == null) {
			return null;
		}
		long matrixId = Long.parseLong(request.getParameter("matrixid"));
		generateAFileDynamically(request, response, matrixId);
		return null;
	}

	@Override
	protected String getFileNamePrefix() {
		return "M";
	}

	@Override
	protected String getFileContent(long objectId, HttpServletRequest request) {
		Study pStudy = ControllerUtil.findStudy(request, getStudyService());
		Matrix matrix = getMatrixService().findByID(objectId);
		TaxonLabelSet taxa = matrix.getTaxa();		
		if ( getFormat(request) == FORMAT_NEXML ) {
			NexusDataSet pNexusDataSet = new NexusDataSet();
			pNexusDataSet.getTaxonLabelSets().add(taxa);
			pNexusDataSet.getMatrices().add(matrix);
			return getNexmlService().serialize(pNexusDataSet,getDefaultProperties(request));
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			NexusDataSet pNexusDataSet = new NexusDataSet();
			pNexusDataSet.getTaxonLabelSets().add(taxa);
			pNexusDataSet.getMatrices().add(matrix);
			return getRdfaService().serialize(pNexusDataSet,getDefaultProperties(request));			
		}
		else { // FORMAT_NEXUS or none
			StringBuilder matrixContent = new StringBuilder();
			matrixContent.append("#NEXUS\n");
			TreebaseUtil.attachStudyHeader(pStudy, matrixContent);
			if (taxa != null) {
				//one taxon per line, no line number:
				taxa.buildNexusBlockTaxa(matrixContent, true, false);
			}
			matrix.generateNexusBlock(matrixContent);		
			return matrixContent.toString();			
		}
	}

}
