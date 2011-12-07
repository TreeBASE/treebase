package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabel;
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
		if ( getFormat(request) == FORMAT_NEXML || getFormat(request) == FORMAT_RDF ) {
			return getNexmlService().serialize(study,getDefaultProperties(request));
		}
		/*else if ( getFormat(request) == FORMAT_RDF ) {
			return getRdfaService().serialize(study,getDefaultProperties(request));			
		}*/		
		else {
			StringBuilder builder = new StringBuilder();
			builder.append("#NEXUS\n\n");
						
			// header:
			TreebaseUtil.attachStudyHeader(study, builder);
	
			// taxa:
			
			//set a unique number for each block when the title is Taxa
			Integer taxaCount = 1;
			List<List <TaxonLabel>> taxonCompare = new ArrayList<List <TaxonLabel>>();
			for ( TaxonLabelSet tls : study.getTaxonLabelSets() ) {
				Boolean isDuplicateTaxa = false;
				Integer taxaCompareCount = 1;
				
				List<TaxonLabel> taxonLblSet = tls.getTaxonLabelsReadOnly();
				if (taxonCompare != null) {
					for (List<TaxonLabel> lstTls : taxonCompare) {
						if (taxonLblSet.equals(lstTls)) {
							isDuplicateTaxa = true;
							break;
						}
						else {
							taxaCompareCount++;
						}
					}
				}
				
				if (isDuplicateTaxa) {
					tls.setTitle("Taxa" + taxaCompareCount.toString());
				}
				else {
					tls.setTitle("Taxa" + taxaCount.toString());
					taxaCount++;
					// one taxon label per line, no line number. 
					tls.buildNexusBlockTaxa(builder, true, false);
					taxonCompare.add(taxonLblSet);
				}
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

	@Override
	protected Study getStudy(long objectId, HttpServletRequest request) {
		return getStudyService().findByID(objectId);
	}

}
