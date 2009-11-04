


package org.cipres.treebase.web.controllers;

import java.sql.Clob;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * 
 * Created on 14th August, 2007
 * 
 * Modified on April 29, 2008 to accommodate blank spaces in the file name. Blank space are replaced
 * by an "_"
 * 
 * @author madhu
 * 
 */
public class DownloadANexusFileController extends AbstractDownloadController implements Controller {

	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
	private MatrixService mMatrixService;

	private static final String org = "org";

	/**
	 * Return the MatrixService field.
	 * 
	 * @return MatrixService mMatrixService
	 */
	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	/**
	 * Set the MatrixService field.
	 */
	public void setMatrixService(MatrixService pNewMatrixService) {
		mMatrixService = pNewMatrixService;
	}

	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		generateAFileDynamically(request,response,0L);
		return null;
	}

	@Override
	protected String getFileName(long objectId,HttpServletRequest req) {
		String treeId = req.getParameter("treeid");
		String matrixId = req.getParameter("matrixid");
		String nexusFile = req.getParameter("nexusfile");

		if (treeId == null && matrixId == null && nexusFile == null) {
			return null;
		}
		long clickedParameterId = 0L;
		String nexusFileName = null;
		if (treeId != null) {
			clickedParameterId = Long.parseLong(treeId);
			nexusFileName = getPhyloTreeService().findByID(clickedParameterId).getNexusFileName();
		} else if (matrixId != null) {
			clickedParameterId = Long.parseLong(matrixId);
			nexusFileName = getMatrixService().findByID(clickedParameterId).getNexusFileName();
		} else if (nexusFile != null) {
			nexusFileName = (nexusFile).replaceAll("%20", "_");
		}
		return ( nexusFileName + org ).replaceAll(TreebaseUtil.ANEMPTYSPACE, "_");
	}

	@Override
	protected String getFileNamePrefix() {
		// Not necessary because we override getFileName
		return null;
	}

	@Override
	protected String getFileContent(long objectId, HttpServletRequest req) {
		String nexusFileName = null;
		String treeId = req.getParameter("treeid");
		String matrixId = req.getParameter("matrixid");
		String nexusFile = req.getParameter("nexusfile");		
		long clickedParameterId = 0L;
		if (treeId != null) {
			clickedParameterId = Long.parseLong(treeId);
			nexusFileName = getPhyloTreeService().findByID(clickedParameterId).getNexusFileName();
		} else if (matrixId != null) {
			clickedParameterId = Long.parseLong(matrixId);
			nexusFileName = getMatrixService().findByID(clickedParameterId).getNexusFileName();
		} else if (nexusFile != null) {
			nexusFileName = (nexusFile).replaceAll("%20", "_");
		}
		if (nexusFileName == null) {
			return null;
		}
		Study study = ControllerUtil.findStudy(req, mStudyService);
		Map<String, Clob> nexusMap = study.getNexusFiles();		
		Clob clob = nexusMap.get(nexusFileName);
		String clobStr = "File Not Found. File Name is: " + nexusFileName;
		if (clob != null) {
			try {
				int clobLength = (int) clob.length();
				char[] clobchars = new char[clobLength];
				clob.getCharacterStream().read(clobchars);
				clobStr = new String(clobchars);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return clobStr;
	}

}
