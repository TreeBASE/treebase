


package org.cipres.treebase.web.controllers;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * @author madhu
 * 
 * Created on November 27, 2007
 */
public class NexusFilesController implements Controller {

	private static final Logger LOGGER = LogManager.getLogger(EditTaxonLabelController.class);

	private StudyService mStudyService;

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

	public ModelAndView handleRequest(HttpServletRequest pRequest, HttpServletResponse pResponse)
		throws Exception {

		Study study = ControllerUtil.findStudy(pRequest, mStudyService);
		Map<String, String> nexusFilesMap = study.getNexusFiles();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("MAP SIZE IS: " + nexusFilesMap.size());
		}

		List<String> nexusFileNamesList = new ArrayList<String>();

		if (nexusFilesMap != null) {
			for (String nexusfilename : nexusFilesMap.keySet()) {
				nexusFileNamesList.add(nexusfilename.replaceAll(TreebaseUtil.ANEMPTYSPACE, "%20"));
			}
		}

		return new ModelAndView("nexusFiles", "nexusFileList", nexusFileNamesList);
	}
}
