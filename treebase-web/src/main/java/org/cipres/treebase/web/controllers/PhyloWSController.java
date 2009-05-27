package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

public class PhyloWSController implements Controller {
	private static final long serialVersionUID = 1L;
	private MatrixService mMatrixService;
	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
    
    private String createUrl(String prefix, String objectId) throws Exception {
    	String url = "";
    	String base = "/treebase-web/search/study";
    	if ( prefix.equalsIgnoreCase("study") ) {
			Long studyId = Long.parseLong(objectId);
			Study study = getStudyService().findByID(studyId);
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study " + studyId);
			}    			    		
    		return base + "/summary.html?id=" + objectId;
    	}   
    	else if ( prefix.equalsIgnoreCase("matrix") ) {
    		Long matrixId = Long.parseLong(objectId);
    		Matrix matrix = getMatrixService().findByID(matrixId);
    		if ( matrix == null ) {
    			throw new ObjectNotFoundException("Can't find matrix " + matrixId);
    		}
    		Study study = matrix.getStudy();
    		return base + "/matrix.html?id=" + study.getId() + "&matrixid=" + objectId;
    	}
    	else if ( prefix.equalsIgnoreCase("tree") ) {
    		Long treeId = Long.parseLong(objectId);
    		PhyloTree phyloTree = getPhyloTreeService().findByID(treeId);
    		if ( phyloTree == null ) {
    			throw new ObjectNotFoundException("Can't find tree " + treeId);
    		}
    		Study study = phyloTree.getStudy();
    		return base + "/tree.html?id=" + study.getId() + "&treeid=" + objectId;
    	}
    	return url;
    }

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
        res.setContentType("text/plain");
        String url = null;
        try { 
            String pathInfo = req.getPathInfo();
            if ( TreebaseUtil.isEmpty(pathInfo) ) {
            	throw new InvalidRequestException(
            		"Invalid request '" 
            		+ pathInfo 
            		+ "', should be /${class}/${id}");
            }
            String[] pathComponents = pathInfo.split("/");
            if ( pathComponents.length != 3 ) {
            	throw new InvalidRequestException(
            		"Invalid request '" 
            		+ pathInfo 
            		+ "', should be /${class}/${id}");
            }
            String prefix = pathComponents[1];
            String fullyQualifiedId = pathComponents[2];
            String id = fullyQualifiedId.replaceAll(".*:[a-zA-Z]*","");        	
        	url = createUrl(prefix,id);
        } catch ( NumberFormatException e ) {
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad ID string: " + e.getMessage());
        } catch ( ObjectNotFoundException e ) {        	        	
        	res.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch ( InvalidRequestException e ) {
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        if ( ! TreebaseUtil.isEmpty(url) ) {
        	res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);        
        	res.setHeader("Location", url);        
        }
        return null;
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
