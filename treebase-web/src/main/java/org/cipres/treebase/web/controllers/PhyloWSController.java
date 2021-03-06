package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.web.exceptions.NoStudySpecifiedError;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * This class re-directs clients to one of a number of other controllers depending
 * on the provided URL pattern. Here are the patterns. Where it says 'study' in the
 * example patterns, it can equally say 'matrix', 'tree' or 'taxon'. The part that
 * says 'TB2:S1787' is supposed to be a valid NamespacedGUID:
 * <ul>
 * <li>
 * - /phylows/study/TB2:S1787 - this is the general pattern for stable resource
 * URLs, without a query string. URLs of this kind are sent on to the AnyObjectAsRDFController.
 * If you want to work on content-negotiation, go there. The re-direct URL is generated by
 * the method createResourceUrl
 * </li>  
 * 
 * <li>
 * - /phylows/study/TB2:S1787?format=html - this is the pattern for the web page
 * version of a resource. URLs of this kind are sent on to the SearchSummaryController, except
 * for trees, which are sent to the SearchMapToPhyloWidgetController. The re-direct URL is
 * generated by the method createDisplayUrl
 * </li>
 * 
 * <li>
 * - /phylows/study/TB2:S1787?format=nexml - this is the pattern for any other available
 * serialization of the resource (i.e. nexml|nexus|rdf). URLs of this kind are sent to the
 * downloadA*Controller controller (e.g. Matrix|Study|Tree). If an output format isn't
 * working as it should, look in the download controllers (and the core services that ultimately
 * produce the output). The re-direct URL is generated by the method createDownloadUrl
 * </li>
 * 
 * <li>
 * - /phylows/study/find?query=... - this is the pattern for search queries. URLs of this
 * kind are sent to the *SearchController controllers (e.g. Matrix|Study|Tree). To debug
 * CQL queries, look in those controllers, as the class you are looking at now doesn't 
 * process CQL at all. The re-direct URL is generated by the method createSearchUrl
 * </li>
 * </ul>
 * @author rvosa
 *
 */

public abstract class PhyloWSController implements Controller {
	private static final long serialVersionUID = 1L;
	private static String searchBase = "/treebase-web/search/";
	
	private boolean redirectDownload = true;
	private Study TBStudy = null;
    
	/**
	 * Child classes return the display page for the focal type
	 * of objects, e.g. for matrices, this returns "matrix.html"
	 * @return
	 */
	abstract String getDisplayPage();
	
	/**
	 * Given an object ID and a servlet request object, constructs
	 * a relative URL to an HTML display page about that object.
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
     * Here the actual request is handled. See the class introduction at
     * the top of this file for an overview of the logic implemented here.
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
	                    if (!redirectDownload ) {
	                    	if (TBStudy != null) {
	                    		if (!ControllerUtil.isReviewerAccessGranted(req, TBStudy.getTreebaseIDString())) {
	                    			url = "/treebase-web/accessviolation.html";
	                    		}
	                    	}
	                    	else {
	                    		url = "/treebase-web/accessviolation.html";
	                    	}
	                    }
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
     * Given a NamespacedGUID, which is a wrapper around an ID such as TB2:S1787, and
     * a request object, returns a URL for the resource description of the object identified
     * by the NamespacedGUID.
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
	 * Child classes here return the search page; e.g. for matrices, returns "matrixSearch.html"
	 * @return
	 */
	abstract String getSearchPage();
	
	/**
	 * Constructs the URL that re-directs to the *SearchController classes. This involves:
	 * the getSearchPage() output (which identifies the seach controller), the CQL query,
	 * the output format (either rss1, or a web page in all other cases) and the recordSchema,
	 * which is used to project a search result set (e.g. a set of studies) into another class
	 * (e.g. the set of trees for those studies).
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
	 * Given an ID prefix (as per those of TreebaseIDString), returns whether an HTML web page
	 * exists for the type of object the prefix identifies
	 * @param pathComponents
	 * @return true of web page exists, false otherwise
	 */
	abstract boolean hasWebPage(String prefix);
	
	/**
	 * Child classes return a map of query string parameters and their values. Typically these
	 * are the study id (parameter name "id") and the id of an object in that study, e.g.
	 * a "matrixid" or a "treeid". In the latter cases where we look for objects in a study
	 * (not the study per se) we first need to look up those objects, and then fetch the study
	 * for them. Hence we need to interact with service objects, which potentially might not
	 * be able to find the object identified by objectId, hence we may throw an ObjectNotFoundException
	 * here.
	 * @param objectId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	abstract Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException;

	/**
	 * Creates a URL by concatenating the base + ? + the params as query string parameters, i.e.
	 * key1=value1&key2=value2 + potentially the x-access-code parameter, when dealing with
	 * embargoed objects
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
	 * Child classes return the focal download page here, e.g. for matrices, returns
	 * "downloadAMatrix.html"
	 * @return
	 */
	abstract String getDownloadPage();	
	
	/**
	 * Creates the URL that re-directs to the DownloadA*Controllers. This involves concatenating
	 * getDownloadPage() with the query string parameters to identify the object to download, the
	 * format in which to serialize it (and potentially the x-access-code parameter, for 
	 * embargoed objects).
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
	 * Exceptions of this kind are thrown when a non-existent objectId somehow ends up
	 * in a URL.
	 * @author rvosa
	 *
	 */
	class ObjectNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		public ObjectNotFoundException(String message) {
			super(message);
		}
	}
	
	public void setStudy(Study study) {
		TBStudy = study;
		redirectDownload = study.isPublished();
	}

}
