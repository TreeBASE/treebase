package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author rvosa
 *
 */
public class AnyObjectAsRDFController implements Controller {

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
			if ( null != treebaseIDString.getTypePrefix() ) {
				Class<?> theClass = TreebaseIDString.getClassForPrefix(treebaseIDString.getTypePrefix());
				if ( null != theClass ) {
					if ( theClass.isAssignableFrom(PhyloTree.class) 
							|| theClass.isAssignableFrom(Matrix.class) 
							|| theClass.isAssignableFrom(Study.class) ) {
						request.setAttribute("hasWebPage", true);
						request.setAttribute("hasNexus", true);
						request.setAttribute("hasNeXML", true);
						request.setAttribute("hasRdf", true);
					}
					phyloWSPath = new PhyloWSPath(theClass.getPackage(),namespacedGUID);
//					if ( theClass.isAssignableFrom(Annotatable.class) ) {
//						request.setAttribute(arg0, arg1)
//					}
				}
			}
		}
		request.getSession().setAttribute("namespacedGUID", namespacedGUID.toString());
		// <c:set var="baseURL" value="http://localhost:8080/treebase-web/PhyloWS"/>
		// treebase.purl.domain=http://purl.org/phylo/treebase/phylows/
		request.getSession().setAttribute("baseURL", TreebaseUtil.getPurlDomain());
		request.getSession().setAttribute("phyloWSPath", phyloWSPath);
		return new ModelAndView("anyObjectAsRDF");
	}

}
