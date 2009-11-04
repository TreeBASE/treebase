
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixKind;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.model.AGenericList;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListMatrixController.java
 * 
 * Created on June 23, 2006
 * 
 * @author lcchan
 * 
 * Modified by
 * @author Madhu on June 27, 2007 Now individual Matrices can be deleted/downloaded
 * 
 */
public class ListMatrixController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(ListMatrixController.class);

	private StudyService mStudyService;
	private MatrixService mMatrixService;
	private MatrixHome mMatrixHome;

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

	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	public void setMatrixService(MatrixService pMatrixService) {
		mMatrixService = pMatrixService;
	}

	/**
	 * Return the MatrixHome field.
	 * 
	 * @return MatrixHome mMatrixHome
	 */
	private MatrixHome getMatrixHome() {
		return mMatrixHome;
	}

	/**
	 * Set the MatrixHome field.
	 */
	public void setMatrixHome(MatrixHome pNewMatrixHome) {
		mMatrixHome = pNewMatrixHome;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		// AMatrixList aMatrixList = (AMatrixList) command;
		AGenericList<Collection<Matrix>> aMatCollection = (AGenericList<Collection<Matrix>>) command;
		
		if (request.getParameter(ACTION_UPDATE) != null) {

			Collection<Matrix> matrices = (Collection<Matrix>) aMatCollection.getMyList();
			List<Object> testList = checkLength(matrices, TBPersistable.COLUMN_LENGTH_STRING);
			Boolean check = (Boolean) testList.get(0);

			if (!check) {
				testList.remove(0);
				request.setAttribute("errors", testList);
				return showForm(request, response, myerrors);
			} else {
				for (Matrix matrix : matrices) {
					MatrixKind kind = getMatrixHome().findKindByDescription(matrix.getKindDescription());
					matrix.setMatrixKind(kind);
				}
				getMatrixService().updateCollection(matrices);
			}
		}

		return new ModelAndView(getSuccessView());
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		Study study = ControllerUtil.findStudy(request, mStudyService);
		Collection<Matrix> matrixList = study.getSubmission().getSubmittedMatricesReadOnly();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of matrices: " + matrixList.size());
		}
		
		checkAnalyzed(matrixList,study);

		// return new AMatrixList(matrixList);
		return new AGenericList<Collection<Matrix>>(matrixList);
	}

	private void checkAnalyzed(Collection<Matrix> mylist, Study study) {	
		Iterator<Matrix> matrixIterator = mylist.iterator();
		while ( matrixIterator.hasNext() ) {
			Matrix matrix = matrixIterator.next();
			if ( matrix.isPublished() ) {
				matrix.setAnalyzed(true);
			}
			else {
				if ( study != null ) {
					Iterator<Matrix> analyzedMatrixIterator = study.getMatrices().iterator();
					ANALYZED: while ( analyzedMatrixIterator.hasNext() ) {
						Matrix analyzedMatrix = analyzedMatrixIterator.next();
						if ( analyzedMatrix.getId() == matrix.getId() ) {
							matrix.setAnalyzed(true);
							break ANALYZED;
						}
					}
				}
				else {
					matrix.setAnalyzed(false);
				}
			}			
		}		
	}
	
	private List<Object> checkLength(Collection<Matrix> mylist, int permittedlength) {

		String oversizerows = "Please fix, following rows have over " + permittedlength
			+ " characters in the Title:  ";

		List<Object> returnedList = new ArrayList<Object>();
		Boolean check = true;
		StringBuilder oversizerowsbldr = new StringBuilder("");
		Iterator<Matrix> matrices = mylist.iterator();

		int i = 0;
		while (matrices.hasNext()) {
			Matrix amat = matrices.next();
			if (amat.getTitle().trim().length() > permittedlength) {
				check = false;
				oversizerowsbldr.append(String.valueOf(i + 1));
				oversizerowsbldr.append(",");
			}
			i++;
		}

		returnedList.add(check);

		if (oversizerowsbldr.toString().trim().length() > 0) {
			returnedList.add(oversizerows + oversizerowsbldr.toString());
		}

		return returnedList;
	}
}
