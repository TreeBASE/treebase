/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.web.model.AGenericList;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * Created on August, 2007
 * 
 * @author madhu
 * 
 */

public class EditSetTaxonLabelController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(EditSetTaxonLabelController.class);

	private StudyService mStudyService;
	private TaxonLabelService mTaxonLabelService;

	/**
	 * Return the TaxonLabelService field.
	 * 
	 * @return TaxonLabelService mTaxonLabelService
	 */
	public TaxonLabelService getTaxonLabelService() {
		return mTaxonLabelService;
	}

	/**
	 * Set the TaxonLabelService field.
	 */
	public void setTaxonLabelService(TaxonLabelService pNewTaxonLabelService) {
		mTaxonLabelService = pNewTaxonLabelService;
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
	 * 
	 * @param StudyService
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 * @throws Exception
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		AGenericList<List<TaxonLabel>> taxonLabelList = (AGenericList<List<TaxonLabel>>) command;
		List<TaxonLabel> txnlblList = (List<TaxonLabel>) taxonLabelList.getMyList();

		if (request.getParameter(ACTION_CANCEL) != null) {
			return new ModelAndView(getSuccessView());
		} else if (request.getParameter(ACTION_UPDATE) != null) {

			List<TaxonLabel> diffList = compareLists((Map<Long, String>) request
				.getSession().getAttribute("testListMap"), txnlblList);
			List<Object> testList = null;
			if (diffList.size() > 0) {
				testList = validateValues(txnlblList);
			} else {
				return new ModelAndView(getCancelView());
			}

			if ((Boolean) testList.get(0)) {
				// Validation successful
				// Only Taxon Labels, which have changed will be updated.
				if (diffList.size() > 0) {
					getTaxonLabelService().updateChanged(diffList);
				}

			} else {
				testList.remove(0);
				request.setAttribute("errors", testList);
				return showForm(request, response, myerrors);
			}

		}
		return new ModelAndView(getSuccessView());

	}

	/**
	 * This method checks for empty, int values, over sized, and duplicate Taxon Labels
	 * 
	 * @param ataxonlabelGroup
	 * @return a List containing a boolean value and various errors messages
	 */

	private List<Object> validateValues(List<TaxonLabel> ataxonlabelGroup) {

		String emptyrows = "Please fix, following rows are empty:  ";
		String introws = "Please fix, following rows have integer values:  ";
		String duplicaterows = "Please fix, following rows are being duplicated:  ";
		String oversizerows = "Please fix, following rows have over "
			+ TBPersistable.COLUMN_LENGTH_STRING + " characters:  ";

		Boolean check = true;

		List<Object> returnList = new ArrayList<Object>();
		Map<String, String> testMap = new HashMap<String, String>();

		StringBuilder emptyrowsbldr = new StringBuilder("");
		StringBuilder introwsbldr = new StringBuilder("");
		StringBuilder duplicaterowsbldr = new StringBuilder("");
		StringBuilder oversizerowsbldr = new StringBuilder("");

		for (int i = 0; i < ataxonlabelGroup.size(); i++) {

			String txnlbl = ataxonlabelGroup.get(i).getTaxonLabel().trim();
			if (txnlbl.length() == 0) {
				check = false;
				emptyrowsbldr.append(String.valueOf(i + 1) + ",");
			} else if (txnlbl.length() > TBPersistable.COLUMN_LENGTH_STRING) {
				check = false;
				oversizerowsbldr.append(String.valueOf(i + 1) + ",");
			} else {
				if (!testMap.containsKey(txnlbl.toUpperCase())) {
					testMap.put(txnlbl.toUpperCase(), String.valueOf(i + 1));
				} else {
					check = false;
					duplicaterowsbldr.append(testMap.get(txnlbl.toUpperCase()) + " & "
						+ String.valueOf(i + 1) + "; ");
				}

			}

		}
		/*
		 * for (int n = 0; n < diffGroup.size(); n++) { String taxonlbl =
		 * diffGroup.get(n).getTaxonLabel().trim(); if (taxonlbl.length() == 0) { check = false;
		 * emptyrowsbldr.append(String.valueOf(n + 1) + ","); } else if (taxonlbl.length() >
		 * TBPersistable.COLUMN_LENGTH_STRING) { check = false;
		 * oversizerowsbldr.append(String.valueOf(n + 1) + ","); } else { String[] splitlabel =
		 * taxonlbl.split(" "); if (!resultsFromUBIOService(splitlabel[0])) { check = false;
		 * notfoundatubiobldr .append(" ").append(taxonlbl).append(" row: ").append(n +
		 * 1).append(";"); } } }
		 */

		returnList.add(check);

		if (emptyrowsbldr.toString().trim().length() > 0) {
			returnList.add(emptyrows + emptyrowsbldr.toString());
		}
		if (introwsbldr.toString().trim().length() > 0) {
			returnList.add(introws + introwsbldr.toString());
		}
		if (duplicaterowsbldr.toString().trim().length() > 0) {
			returnList.add(duplicaterows + duplicaterowsbldr.toString());
		}
		if (oversizerowsbldr.toString().trim().length() > 0) {
			returnList.add(oversizerows + oversizerowsbldr.toString());
		}

		return returnList;
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		Study study = ControllerUtil.findStudy(request, mStudyService);
		List<TaxonLabel> taxonLabelList = study.getSubmission().getSubmittedTaxonLabelsReadOnly();
		Map<Long, String> testList = new HashMap<Long, String>();

		for (TaxonLabel tLabel : taxonLabelList) {
			testList.put(tLabel.getId(), tLabel.getTaxonLabel());
		}

		request.getSession().setAttribute("testListMap", testList);
		// testListMap session variable is used in onSubmit method

		return new AGenericList<List<TaxonLabel>>(taxonLabelList);
	}

	// Only the Taxon Labels, which have changed will be updated in the database.

	public List<TaxonLabel> compareLists(Map<Long, String> initialList, List<TaxonLabel> changedList) {

		List<TaxonLabel> differenceList = new ArrayList<TaxonLabel>();

		int size = initialList.size();
		for (int i = 0; i < size; i++) {
			Long id = changedList.get(i).getId();
			String changedLabel = changedList.get(i).getTaxonLabel();
			String initialLabel = initialList.get(id);

			if (!TreebaseUtil.isEqual(initialLabel, changedLabel)) {
				differenceList.add(changedList.get(i));
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of Taxon Labels that have changed: " + differenceList.size()); //$NON-NLS-1$
		}
		return differenceList;
	}

}
