


package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


public class ListTaxaSearchController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(ListTaxaSearchController.class);

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
