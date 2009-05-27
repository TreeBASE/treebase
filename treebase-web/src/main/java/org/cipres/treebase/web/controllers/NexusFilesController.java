/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

	private static final Logger LOGGER = Logger.getLogger(EditTaxonLabelController.class);

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
		Map<String, Clob> nexusFilesMap = study.getNexusFiles();

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
