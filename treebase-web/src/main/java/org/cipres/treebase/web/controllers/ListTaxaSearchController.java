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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


public class ListTaxaSearchController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(ListTaxaSearchController.class);

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Map<String,Object> params = request.getParameterMap();
		Collection<String> newTaxonLabels = new ArrayList<String> ();
		for (int i=0; params.containsKey("taxonLabel" + i); i++) {
			newTaxonLabels.add((String) params.get("taxonLabel" + i));
		}
		request.setAttribute("taxonLabelStatus", newTaxonLabels);
		return getReturnLocation(request);
	}

	private ModelAndView getReturnLocation(HttpServletRequest request) {
		return (ModelAndView) request.getAttribute("returnLocation");
	}

	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Map<String,String> fbo =  (Map<String, String>) request.getAttribute("taxonLabelStatus");
		if (fbo != null) { return fbo; }
		fbo = new LinkedHashMap<String,String>();
		
		fbo.put("potato", null);
		fbo.put("octoopus", "octopus");
		return fbo;
	}
}
