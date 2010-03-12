package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;

public class PhyloWSController implements Controller {
	private static String ncbiBaseUrl = "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=";
	private static String uBioBaseUrl = "http://www.ubio.org/browser/details.php?namebankID=";
	private static final long serialVersionUID = 1L;
	private MatrixService mMatrixService;
	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
	private static String format = "format";
	
	/* 
	 * this array is used to identify whether a path fragment maps to an object
	 * for which web pages are available. Maybe this should be an enum or a map
	 * instead.
	 */
	private static String[] classesWithPages = { "tree", "study", "matrix" };
    
	/**
	 * Given an object prefix ("tree", "study" or "matrix") and an object ID, constructs
	 * a relative URL to an HTML page about that object.
	 * @param prefix identifies either studies, matrices or trees
	 * @param objectId is the primary key for the requested object
	 * @param request is the servlet request, which we need to check for reviewer access
	 * @return a relative URL to an HTML page, or an empty string
	 * @throws Exception
	 */
    private String createUrl(String prefix, Long objectId,HttpServletRequest request) throws Exception {
    	String url = "";
    	StringBuffer base = new StringBuffer("/treebase-web/search/study");
    	
    	// requested object is a study
    	if ( prefix.equals(TreebaseIDString.getPrefixForClass(Study.class)) ) {
			Study study = getStudyService().findByID(objectId);
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study " + objectId);
			}    
			if ( ! TreebaseUtil.isEmpty(request.getParameter(Constants.X_ACCESS_CODE))) {
				request.setAttribute("initialReviewerRedirect", true);
				return base
					.append("/summary.html?id=")
					.append(objectId)
					.append('&')
					.append(Constants.X_ACCESS_CODE)
					.append('=')
					.append(request.getParameter(Constants.X_ACCESS_CODE))
					.toString();
			}
			else {
				return base
					.append("/summary.html?id=")
					.append(objectId)
					.toString();
			}
    	}   
    	
    	// requested object is a matrix
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(Matrix.class)) ) {
    		Matrix matrix = getMatrixService().findByID(objectId);
    		if ( matrix == null ) {
    			throw new ObjectNotFoundException("Can't find matrix " + objectId);
    		}
    		Study study = matrix.getStudy();
    		return base
    			.append("/matrix.html?id=")
    			.append(study.getId())
    			.append("&matrixid=")
    			.append(objectId)
    			.toString();
    	}
    	
    	// requested object is a tree
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(PhyloTree.class)) ) {
    		PhyloTree phyloTree = getPhyloTreeService().findByID(objectId);
    		if ( phyloTree == null ) {
    			throw new ObjectNotFoundException("Can't find tree " + objectId);
    		}
    		Study study = phyloTree.getStudy();
    		return base
    			.append("/tree.html?id=")
    			.append(study.getId())
    			.append("&treeid=")
    			.append(objectId)
    			.toString();
    	}
    	return url;
    }

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
        res.setContentType("text/plain");
        String url = null;
        //String domain = "http://" + req.getServerName() + ":" + req.getServerPort();
		if ( ! TreebaseUtil.isEmpty(req.getParameter(Constants.X_ACCESS_CODE)) ) {
			req.getSession().setAttribute(
				Constants.X_ACCESS_CODE, 
				req.getParameter(Constants.X_ACCESS_CODE));
		}        
        try { 
            String pathInfo = req.getPathInfo();
            if ( TreebaseUtil.isEmpty(pathInfo) ) {
            	throw new InvalidRequestException(
            		"Invalid request '" 
            		+ pathInfo 
            		+ "', should be /${class}/${NamespacedGUID}");
            }
            String[] pathComponents = pathInfo.split("/");
            if ( pathComponents[pathComponents.length-1].equals("find") ) {
            	//url = domain + createSearchUrl(pathComponents[pathComponents.length-2],req);
            	url = createSearchUrl(pathComponents[pathComponents.length-2],req);
            } else {
	            String rawNamespacedGUID = pathComponents[pathComponents.length-1];
	            if ( rawNamespacedGUID.startsWith("uBio:") ) { // XXX be polite, use real URL
	            	url = uBioBaseUrl + rawNamespacedGUID.substring("uBio:".length());
	            }
	            else if ( rawNamespacedGUID.startsWith("NCBI:") ) { // XXX be polite, use real URL
	            	url = ncbiBaseUrl + rawNamespacedGUID.substring("NCBI:".length());
	            }            
	            else {
		            NamespacedGUID namespacedGUID = new NamespacedGUID(rawNamespacedGUID);
		            TreebaseIDString tbID = namespacedGUID.getTreebaseIDString();
		            if ( hasWebPage(pathComponents) ) {
		            	String serializationFormat = createSerializationFormat(req);
		            	if ( TreebaseUtil.isEmpty(serializationFormat) ) {
		            		url = "/treebase-web/search/study/anyObjectAsRDF.rdf?namespacedGUID=" + namespacedGUID.toString();
		            	}
		            	else if ( serializationFormat.equals("html") ) {
		            		url = createUrl(tbID.getTypePrefix(),tbID.getId(),req);
		            	}
		            	else {
		            		url = createDownloadUrl(tbID.getTypePrefix(),tbID.getId(),serializationFormat);
		            	}
		            }
		            else {
		            	url = "/treebase-web/search/study/anyObjectAsRDF.rdf?namespacedGUID=" + namespacedGUID.toString();
		            }
	            }
            }
        } catch ( MalformedTreebaseIDString e ) {
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad ID string: " + e.getMessage());
        } catch ( ObjectNotFoundException e ) {        	        	
        	res.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch ( InvalidRequestException e ) {
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        if ( ! TreebaseUtil.isEmpty(url) ) {
        	res.setStatus(HttpServletResponse.SC_SEE_OTHER);        
        	res.setHeader("Location", url);        
        }
        return null;
	}
	
	/**
	 * This is a placeholder method that might parse accept headers for content-negotiation
	 * one day
	 * @param request
	 * @return
	 */
	private String createSerializationFormat(HttpServletRequest request) {
		return request.getParameter(format);
	}
	
	private String createSearchUrl(String path,HttpServletRequest request) {
		StringBuilder sb = new StringBuilder("/treebase-web/search/");
		sb
			.append(path)
			.append("Search.html?query=")
			.append(request.getParameter("query"))
			.append("&format=")
			.append(request.getParameter("format"))
			.append("&recordSchema=")
			.append(request.getParameter("recordSchema"));
		return sb.toString();
	}

	/**
	 * Given an array of path fragments, returns whether an HTML web page
	 * exists for the type of object the fragments map onto
	 * @param pathComponents
	 * @return true of web page exists, false otherwise
	 */
	private boolean hasWebPage(String[] pathComponents) {
		for ( int i = ( pathComponents.length - 1 ); i >= 0; i-- ) {
			for ( int j = 0; j < classesWithPages.length; j++ ) {
				if ( pathComponents[i].equals(classesWithPages[j]) ) {
					return true;
				}
			}
		}
		return false;
	}

	private String createDownloadUrl(String prefix, Long objectId, String format) throws Exception {
		if ( format.equals("xml") ) {
			format = "nexml";
		}
    	String url = "";
    	StringBuffer base = new StringBuffer("/treebase-web/search");
    	if ( prefix.equals(TreebaseIDString.getPrefixForClass(Study.class)) ) {
			Study study = getStudyService().findByID(objectId);
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study " + objectId);
			}    			    		
    		return base
				.append("/downloadAStudy.html?id=")
				.append(objectId)
				.append("&format=")
				.append(format)
					.toString();  			
    	}   
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(Matrix.class)) ) {
    		Matrix matrix = getMatrixService().findByID(objectId);
    		if ( matrix == null ) {
    			throw new ObjectNotFoundException("Can't find matrix " + objectId);
    		}
    		Study study = matrix.getStudy();
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study for matrix "+objectId);
			}    		
    		return base
				.append("/downloadAMatrix.html?id=")
				.append(study.getId())
				.append("&matrixid=")
				.append(objectId)
				.append("&format=")
				.append(format)
					.toString();    		
    	}
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(PhyloTree.class)) ) {
    		PhyloTree phyloTree = getPhyloTreeService().findByID(objectId);
    		if ( phyloTree == null ) {
    			throw new ObjectNotFoundException("Can't find tree " + objectId);
    		}
    		Study study = phyloTree.getStudy();
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study for tree "+objectId);
			}     		
    		return base
    			.append("/downloadATree.html?id=")
    			.append(study.getId())
    			.append("&treeid=")
    			.append(objectId)
    			.append("&format=")
    			.append(format)
    				.toString();
    	}
    	return url;
	}

	public MatrixService getMatrixService() {
		return mMatrixService;
	}
	
	public void setMatrixService(MatrixService matrixService) {
		mMatrixService = matrixService;
	}
	
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}
	
	public void setPhyloTreeService(PhyloTreeService phyloTreeService) {
		mPhyloTreeService = phyloTreeService;
	}
	
	public StudyService getStudyService() {
		return mStudyService;
	}
	
	public void setStudyService(StudyService studyService) {
		mStudyService = studyService;
	}
    
	class ObjectNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		public ObjectNotFoundException(String message) {
			super(message);
		}
	}
	
	class InvalidRequestException extends Exception {
		private static final long serialVersionUID = 1L;
		public InvalidRequestException(String message) {
			super(message);
		}
	}

}
