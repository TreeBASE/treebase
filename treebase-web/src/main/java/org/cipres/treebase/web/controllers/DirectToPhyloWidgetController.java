


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
