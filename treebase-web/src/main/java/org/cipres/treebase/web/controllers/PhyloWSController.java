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

public class PhyloWSController implements Controller {
	private static final long serialVersionUID = 1L;
	private MatrixService mMatrixService;
	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
    
    private String createUrl(String prefix, Long objectId) throws Exception {
    	String url = "";
    	String base = "/treebase-web/search/study";
    	if ( prefix.equals(TreebaseIDString.getPrefixForClass(Study.class)) ) {
			Study study = getStudyService().findByID(objectId);
			if ( study == null ) {
				throw new ObjectNotFoundException("Can't find study " + objectId);
			}    			    		
    		return base + "/summary.html?id=" + objectId;
    	}   
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(Matrix.class)) ) {
    		Matrix matrix = getMatrixService().findByID(objectId);
    		if ( matrix == null ) {
    			throw new ObjectNotFoundException("Can't find matrix " + objectId);
    		}
    		Study study = matrix.getStudy();
    		return base + "/matrix.html?id=" + study.getId() + "&matrixid=" + objectId;
    	}
    	else if ( prefix.equals(TreebaseIDString.getPrefixForClass(PhyloTree.class)) ) {
    		PhyloTree phyloTree = getPhyloTreeService().findByID(objectId);
    		if ( phyloTree == null ) {
    			throw new ObjectNotFoundException("Can't find tree " + objectId);
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
            		+ "', should be /${NamespacedGUID}");
            }
            String[] pathComponents = pathInfo.split("/");
            if ( pathComponents.length != 2 ) {
            	throw new InvalidRequestException(
            		"Invalid request '" 
            		+ pathInfo 
            		+ "', should be /${NamespacedGUID}");
            }
            String[] idComponents = pathComponents[1].split("\\.");
            NamespacedGUID namespacedGUID = new NamespacedGUID(idComponents[0]);
            TreebaseIDString tbID = namespacedGUID.getTreebaseIDString();
            if ( idComponents.length >= 2 && idComponents[1].equals("html") ) {
            	url = createUrl(tbID.getTypePrefix(),tbID.getId());
            }
            else if ( idComponents.length >= 2 ) {
            	url = createDownloadUrl(tbID.getTypePrefix(),tbID.getId(),idComponents[1]);
            }
            else {
            	url = "/treebase-web/search/study/anyObjectAsRDF.html?namespacedGUID=" + namespacedGUID.toString();
            }
        } catch ( MalformedTreebaseIDString e ) {
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
