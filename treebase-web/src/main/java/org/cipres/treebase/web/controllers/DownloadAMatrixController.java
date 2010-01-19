
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

	@Override
	protected Study getStudy(long objectId, HttpServletRequest request) {
		Matrix matrix = getMatrixService().findByID(objectId);
		return matrix.getStudy();
	}

}
