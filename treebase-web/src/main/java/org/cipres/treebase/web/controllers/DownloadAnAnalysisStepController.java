
package org.cipres.treebase.web.controllers;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
//import org.cipres.treebase.web.util.WebUtil;

/**
 * 
 * DownloadAnAnalysisStepController.java
 * 
 * Created on May 7, 2009
 * @author rvosa
 *
 */
public class DownloadAnAnalysisStepController extends AbstractDownloadController implements Controller {
	private MatrixService mMatrixService;
	private PhyloTreeService mPhyloTreeService;
	private AnalysisStepService mAnalysisStepService;

	/**
	 * Return the PhyloTreeService field
	 * 
	 * @return PhyloTreeService mPhyloTreeService
	 */
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
	 * Get the AnalysisStepService field
	 * 
	 * @return AnalysisStepService mAnalysisStepService
	 */
	public AnalysisStepService getAnalysisStepService() {
		return mAnalysisStepService;
	}

	/**
	 * Set the AnalysisStepService field
	 * 
	 * @param pNewAnalysisStepService
	 */
	public void setAnalysisStepService(AnalysisStepService pNewAnalysisStepService) {
		mAnalysisStepService = pNewAnalysisStepService;
	}	
	
	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		if ( request.getParameter("analysisid") == null ) {
			return null;
		}
		long analysisId = Long.parseLong(request.getParameter("analysisid"));		
		generateAFileDynamically(request, response, analysisId);
		return null;
	}

	@Override
	protected String getFileNamePrefix() {
		return "AnalysisStep";
	}

	@Override
	protected String getFileContent(long analysisId, HttpServletRequest request) {
		AnalysisStep step = getAnalysisStepService().findByID(analysisId);
		StringBuilder stepContent = new StringBuilder();
		stepContent.append("#NEXUS\n");
		
		//header:
		TreebaseUtil.attachStudyHeader(step.getAnalysis().getStudy(), stepContent);
		stepContent.append("[ The following blocks are input data for analysis step " + analysisId + " ]\n");
		TaxonLabelSet inputLabelSet = step.getInputTaxonLabelSet();
		inputLabelSet.buildNexusBlockTaxa(stepContent, true, false);
		for ( AnalyzedData data : step.getDataSetReadOnly() ) {
			if ( data.isInputData() ) {
				PhyloTree tree = data.getTreeData();
				Matrix matrix  = data.getMatrixData();
				if ( tree != null ) {
					tree.getTreeBlock().setTaxonLabelSet(inputLabelSet);
					tree.buildNexusBlock(stepContent);
				}
				if ( matrix != null ) {
					matrix.setTaxa(inputLabelSet);
					matrix.generateNexusBlock(stepContent);
				}				
			}
		}
		
		stepContent.append("[ The following blocks are output data for analysis step " + analysisId + " ]\n");
		TaxonLabelSet outputLabelSet = step.getOutputTaxonLabelSet();
		outputLabelSet.buildNexusBlockTaxa(stepContent, true, false);
		for ( AnalyzedData data : step.getDataSetReadOnly() ) {
			if ( ! data.isInputData() ) {
				PhyloTree tree = data.getTreeData();
				Matrix matrix  = data.getMatrixData();
				if ( tree != null ) {
					tree.getTreeBlock().setTaxonLabelSet(outputLabelSet);
					tree.buildNexusBlock(stepContent);
				}
				if ( matrix != null ) {
					matrix.setTaxa(outputLabelSet);
					matrix.generateNexusBlock(stepContent);
				}				
			}
		}		
		return stepContent.toString();
	}

}
