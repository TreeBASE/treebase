package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.web.Constants;

public abstract class PhyloWSController implements Controller {
	private static final long serialVersionUID = 1L;
	private static String searchBase = "/treebase-web/search/";
    
	/**
	 * 
	 * @return
	 */
	abstract String getDisplayPage();
	
	/**
	 * Given an object ID and a servlet request object, constructs
	 * a relative URL to an HTML page about that object.
	 * @param objectId is the primary key for the requested object
	 * @param request is the servlet request, which we need to check for reviewer access
	 * @return a relative URL to an HTML page
	 * @throws ObjectNotFoundException 
	 */
    private String createDisplayUrl(Long objectId,HttpServletRequest request) throws ObjectNotFoundException {
    	StringBuffer base = new StringBuffer(searchBase + "study/");
    	base.append(getDisplayPage());
    	Map<String,String> params = getObjectQueryParameters(objectId);
    	return createUrl(base, params, request);
    }
    
    /**
     * 
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {        
        String url = null; 
        String pathInfo = req.getPathInfo();
        
        // url has "find" in it, re-direct to search services
        if ( pathInfo.indexOf("find") != -1 ) {
        	url = createSearchUrl(req);
        }
        else {
        	String substr = pathInfo.replaceAll(".*/", "");
            try {
	            NamespacedGUID namespacedGUID = new NamespacedGUID(substr);
	            TreebaseIDString tbID = namespacedGUID.getTreebaseIDString();
	        	String serializationFormat = createSerializationFormat(req);	            
	            if ( hasWebPage(tbID.getTypePrefix()) && ! TreebaseUtil.isEmpty(serializationFormat) ) {
	            	
	            	// output format is html, re-direct to display services
	            	if ( serializationFormat.equals("html") ) {
	            		url = createDisplayUrl(tbID.getId(),req);
	            	}
	            	
	            	// output format is something else, re-direct to download services
	            	else {
	            		url = createDownloadUrl(tbID.getId(),serializationFormat,req);
	            	}
	            }
	            
	            // no output format, re-direct to base resource url
	            else {
	            	url = createResourceUrl(namespacedGUID, req);
	            }
            } catch ( MalformedTreebaseIDString e ) {
            	res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad ID string from path info '" + substr + "' message: " + e.getMessage());
            } catch ( ObjectNotFoundException e ) {        	        	
            	res.sendError(HttpServletResponse.SC_NOT_FOUND, "Object not found: " + e.getMessage());
            }	            
        }

        if ( ! TreebaseUtil.isEmpty(url) ) {
        	res.setContentType("text/plain");
        	res.setStatus(HttpServletResponse.SC_SEE_OTHER);        
        	res.setHeader("Location", url);        
        }
        return null;
	}
    
    /**
     * 
     * @param guid
     * @param req
     * @return
     */
    private String createResourceUrl(NamespacedGUID guid,HttpServletRequest req) {
    	StringBuffer base = new StringBuffer(searchBase + "study/anyObjectAsRDF.rdf");
    	Map<String,String> params = new HashMap<String,String>();
    	params.put("namespacedGUID", guid.toString());
    	return createUrl(base,params,req);
    }
	
	/**
	 * This is a placeholder method that might parse accept headers for content-negotiation
	 * one day
	 * @param request
	 * @return
	 */
	private String createSerializationFormat(HttpServletRequest request) {
		return request.getParameter("format");
	}
	
	/**
	 * 
	 * @return
	 */
	abstract String getSearchPage();
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private String createSearchUrl(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer(searchBase);
		sb.append(getSearchPage());
		Map<String,String> params = new HashMap<String,String>();
		String query = request.getParameter("query");
		if ( ! TreebaseUtil.isEmpty(query) ) {
			params.put("query", query);
		}
		String format = createSerializationFormat(request);
		if ( ! TreebaseUtil.isEmpty(format) ) {
			params.put("format", format);
		}
		String recordSchema = request.getParameter("recordSchema");
		if ( ! TreebaseUtil.isEmpty(recordSchema) ) {
			params.put("recordSchema", recordSchema);	
		}
		return createUrl(sb,params,request);
	}

	/**
	 * Given an array of path fragments, returns whether an HTML web page
	 * exists for the type of object the fragments map onto
	 * @param pathComponents
	 * @return true of web page exists, false otherwise
	 */
	abstract boolean hasWebPage(String prefix);
	
	/**
	 * 
	 * @param objectId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	abstract Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException;

	/**
	 * 
	 * @param base
	 * @param params
	 * @return
	 */
	private String createUrl(StringBuffer base,Map<String,String> params,HttpServletRequest req) {
		if ( ! TreebaseUtil.isEmpty(req.getParameter(Constants.X_ACCESS_CODE))) {
			req.setAttribute("initialReviewerRedirect", true);
			params.put(Constants.X_ACCESS_CODE, req.getParameter(Constants.X_ACCESS_CODE));
			req.getSession().setAttribute(Constants.X_ACCESS_CODE,req.getParameter(Constants.X_ACCESS_CODE));			
		}
		if ( ! params.isEmpty() ) {
			base.append('?');
		}
		for ( String key : params.keySet() ) {
			base.append(key).append('=').append(params.get(key)).append('&');
		}
		String url = base.toString();
		return url.substring(0, url.length() - 1 );
	}
	
	
	/**
	 * 
	 * @return
	 */
	abstract String getDownloadPage();	
	
	/**
	 * 
	 * @param objectId
	 * @param format
	 * @return
	 * @throws ObjectNotFoundException 
	 */
	private String createDownloadUrl(Long objectId, String format,HttpServletRequest req) throws ObjectNotFoundException {
    	StringBuffer base = new StringBuffer(searchBase);
    	base.append(getDownloadPage());
    	Map<String,String> params = getObjectQueryParameters(objectId);
    	params.put("format", format);
    	return createUrl(base,params,req);
	}
    
	/**
	 * 
	 * @author rvosa
	 *
	 */
	class ObjectNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		public ObjectNotFoundException(String message) {
			super(message);
		}
	}

}
