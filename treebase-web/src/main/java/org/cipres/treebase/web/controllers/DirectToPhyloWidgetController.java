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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

/**
 * 
 * @author madhu
 * 
 * Created on, October 11, 2007
 * 
 */

public class DirectToPhyloWidgetController implements Controller {

	private PhyloTreeService mPhyloTreeService;
	protected String mDefaultView;

	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}
	

	public String getDefaultView() {
		return mDefaultView;
	}

	public void setDefaultView(String defaultView) {
		this.mDefaultView = defaultView;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		if (request.getParameter("treeid") != null) {

			PhyloTree pt = getPhyloTreeService().findByID(
				Long.parseLong(request.getParameter("treeid")));

			Map<String, String> treemap = new HashMap<String, String>();
			treemap.put("TreeId:" + pt.getId(), pt.getNewickString());
			request.getSession().setAttribute("NEWICKSTRINGSMAP", treemap);

		}

		return new ModelAndView(getDefaultView());

	}

}
