package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataService;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.web.util.ControllerUtil;

public class AddAnalyzedDataController extends BaseFormController {
	private AnalysisService mAnalysisService;
	private StudyService mStudyService;
	private AnalysisStepService mAnalysisStepService;
	private MatrixService mMatrixService;
	private AnalyzedDataService mAnalyzedDataService;
	private PhyloTreeService mPhyloTreeService;
	
	/**
	 * Return the PhyloTreeService field.
	 * 
	 * @return PhyloTreeService mPhyloTreeService
	 */
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the PhyloTreeService field.
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}	
	
	/**
	 * Return the AnalyzedDataService field.
	 * 
	 * @return AnalyzedDataService mAnalyzedDataService
	 */
	public AnalyzedDataService getAnalyzedDataService() {
		return mAnalyzedDataService;
	}

	/**
	 * Set the AnalyzedDataService field.
	 */
	public void setAnalyzedDataService(AnalyzedDataService pNewAnalyzedDataService) {
		mAnalyzedDataService = pNewAnalyzedDataService;
	}
	
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
	
	/**
	 * Return the AnalysisStepService field.
	 * 
	 * @return AnalysisStepService mAnalysisStepService
	 */
	public AnalysisStepService getAnalysisStepService() {
		return mAnalysisStepService;
	}

	/**
	 * Set the AnalysisStepService field.
	 */
	public void setAnalysisStepService(AnalysisStepService pNewAnalysisStepService) {
		mAnalysisStepService = pNewAnalysisStepService;
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
	
	/**
	 * Return the AnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	public AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the AnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pNewAnalysisService) {
		mAnalysisService = pNewAnalysisService;
	}	
	
	@Override
	protected ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {
		String dataType = request.getParameter("dataType");
		String inputOutput = request.getParameter("inputOutput");
		Boolean input = inputOutput.equals("Input");
		String analysisStepId = request.getParameter("analysisStepId");
		AnalysisStep analysisStep = getAnalysisStepService().findByID(Long.parseLong(analysisStepId));
		String action = request.getParameter("action");
		
		// add a list of ids
		if ( action.equals("add") ) {
			String idString = request.getParameter("ids");
			String[] ids = idString.split(" +");			
			if ( dataType.equals("Matrices") ) {
				for ( int i = 0; i < ids.length; i++ ) {
					if ( ! TreebaseUtil.isEmpty(ids[i]) ) {
						Matrix matrix = getMatrixService().findByID(Long.parseLong(ids[i]));
						AnalyzedMatrix analyzedMatrix = new AnalyzedMatrix();
						analyzedMatrix.setInput(input);
						analyzedMatrix.setMatrix(matrix);
						analysisStep.addAnalyzedData(analyzedMatrix);
						getAnalysisStepService().update(analysisStep);					
					}
				}			
			}
			else if ( dataType.equals("Trees") ) {
				for ( int i = 0; i < ids.length; i++ ) {
					if ( ! TreebaseUtil.isEmpty(ids[i]) ) {
						PhyloTree phyloTree = getPhyloTreeService().findByID(Long.parseLong(ids[i]));	
						AnalyzedTree analyzedTree = new AnalyzedTree();
						analyzedTree.setInput(input);
						analyzedTree.setTree(phyloTree);
						analysisStep.addAnalyzedData(analyzedTree);
						getAnalysisStepService().update(analysisStep);
					}				
				}			
			}
			else if ( dataType.equals("TreeBlocks") ) {
				for ( int i = 0; i < ids.length; i++ ) {
					if ( ! TreebaseUtil.isEmpty(ids[i]) ) {
						TreeBlock treeBlock = getPhyloTreeService().findTreeBlockByID(Long.parseLong(ids[i]));
						for ( PhyloTree phyloTree : treeBlock.getTreeList() ) {
							AnalyzedTree analyzedTree = new AnalyzedTree();
							analyzedTree.setInput(input);
							analyzedTree.setTree(phyloTree);
							analysisStep.addAnalyzedData(analyzedTree);
							getAnalysisStepService().update(analysisStep);						
						}
					}				
				}			
			}
		}
		
		// remove an id
		else if ( action.equals("remove") ) {
			String deleteMe = request.getParameter("deleteMe");
			AnalyzedData datumToDelete = null;
			if ( dataType.equals("Matrices") ) {
				for ( AnalyzedData data : analysisStep.getDataSetReadOnly() ) {
					if ( data.isInputData() == input && data.getMatrixData() != null ) {
						if ( data.getId() == Long.parseLong(deleteMe) ) {
							datumToDelete = data;
							break;
						}
					}
				}
			}
			else if ( dataType.equals("Trees") ) {
				for ( AnalyzedData data : analysisStep.getDataSetReadOnly() ) {
					if ( data.isInputData() == input && data.getTreeData() != null ) {
						if ( data.getId() == Long.parseLong(deleteMe) ) {
							datumToDelete = data;
							break;
						}
					}
				}			
			}
			analysisStep.removeAnalyzedData(datumToDelete);
			getAnalysisStepService().update(analysisStep);
		}
		return new ModelAndView(new RedirectView("analyses.html"));		
	}
	
	private SubmissionService mSubmissionService;

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}	
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		String username = request.getRemoteUser();
		String submission_id = ServletRequestUtils.getStringParameter(request, "id", null);
		Study study;
		if (TreebaseUtil.isEmpty(submission_id)) {
			study = ControllerUtil.findStudy(request, mStudyService);
			setAuthorizationChecked(true);// This is needed in case one is clicking at Summary
			// link at the bottom of the menu list on the right hand
			// side.
		} else {
			study = mStudyService.findBySubmissionID(Long.parseLong(submission_id));
		}

		if (!TreebaseUtil.isEmpty(submission_id)) {

			TBPermission perm2 = getSubmissionService().getPermission(
				username,
				Long.parseLong(submission_id));
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.READ_ONLY
				|| perm2 == TBPermission.SUBMITTED_WRITE) {
				setAuthorizationChecked(true);
			} else {
				setAuthorizationChecked(false);
				return new ArticleCitation();
			}

			ControllerUtil.saveStudy(request, study); // user has made selection
			// Added by Madhu to set the session variables
			if (study.isReady()) {
				request.getSession().setAttribute("publicationState", "Ready");
			} else if (study.isPublished()) {
				request.getSession().setAttribute("publicationState", "Published");
			} else {
				request.getSession().setAttribute("publicationState", "NotReady");
			}

		} else {
			request.getSession().setAttribute("publicationState", "NotReady");
		}

		Citation citation = study.getCitation();
		if (citation == null) {
			return new ArticleCitation();
		} else {
			return citation;
		}

	}	
}
