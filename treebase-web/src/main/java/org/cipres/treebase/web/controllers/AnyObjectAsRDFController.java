package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author rvosa
 *
 */
public class AnyObjectAsRDFController implements Controller {
	private static final Logger LOGGER = Logger.getLogger(AnyObjectAsRDFController.class);

	private StudyService mStudyService;

	public StudyService getStudyService() {
		return mStudyService;
	}

	public void setStudyService(StudyService studyService) {
		mStudyService = studyService;
	}	
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NamespacedGUID namespacedGUID = new NamespacedGUID(request.getParameter("namespacedGUID"));
		TreebaseIDString treebaseIDString = null;
		PhyloWSPath phyloWSPath = null;
		request.setAttribute("hasWebPage", false);
		request.setAttribute("hasNexus", false);
		request.setAttribute("hasNeXML", false);
		request.setAttribute("hasRdf", false);
		try {
			treebaseIDString = namespacedGUID.getTreebaseIDString();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		if ( null != treebaseIDString ) {
			LOGGER.info("TreeBASE ID String: "+treebaseIDString);
			
			String typePrefix = treebaseIDString.getTypePrefix();
			if ( null != typePrefix ) {
				LOGGER.info("Type prefix: "+typePrefix);
				
				Class<?> theClass = TreebaseIDString.getClassForPrefix(typePrefix);
				if ( null != theClass ) {
					LOGGER.info("Class: "+theClass);
					
					if ( theClass.isAssignableFrom(PhyloTree.class) 
							|| theClass.isAssignableFrom(Matrix.class) 
							|| theClass.isAssignableFrom(Study.class) ) {
						request.setAttribute("hasWebPage", true);
						request.setAttribute("hasNexus", true);
						request.setAttribute("hasNeXML", true);
						request.setAttribute("hasRdf", true);
					}
					phyloWSPath = new PhyloWSPath(theClass.getPackage(),namespacedGUID);
					
					TBPersistable theObject = getStudyService().findByID(theClass, treebaseIDString.getId());
					if ( null != theObject ) {
						if (! getStudyService().findByID(treebaseIDString.getId()).isPublished()) {
				        	response.setContentType("text/plain");
				        	response.setStatus(HttpServletResponse.SC_SEE_OTHER);        
				        	response.setHeader("Location", "/treebase-web/accessviolation.html");
				        	return null;
				        }
						request.getSession().setAttribute("theObject", theObject);
						LOGGER.info("Object: "+theObject);
					}
					else {
						LOGGER.info("Couldn't instantiate object of type "+theClass+" with ID "+treebaseIDString.getId());
					}
				}
				else {
					LOGGER.info("Couldn't get class for prefix "+typePrefix);
				}
			}
			else {
				LOGGER.info("Couldn't get prefix for ID string" + treebaseIDString);
			}
		}
		else {
			LOGGER.info("Couldn't get ID String");
		}
		request.getSession().setAttribute("namespacedGUID", namespacedGUID.toString());
		// <c:set var="baseURL" value="http://localhost:8080/treebase-web/PhyloWS"/>
		// treebase.purl.domain=http://purl.org/phylo/treebase/phylows/
		request.getSession().setAttribute("baseURL", TreebaseUtil.getPurlBase());
		request.getSession().setAttribute("phyloWSPath", phyloWSPath);
		return new ModelAndView("anyObjectAsRDF");
	}

}
