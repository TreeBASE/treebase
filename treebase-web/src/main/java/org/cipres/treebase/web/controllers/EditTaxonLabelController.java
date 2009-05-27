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
import java.util.List;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;

/**
 * EditTaxonLabelController.java
 * 
 * Created on July 31, 2007
 * 
 * @author Madhu
 * 
 */

public class EditTaxonLabelController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(EditTaxonLabelController.class);
	private TaxonHome mTaxonHome;
	private TaxonLabelService mTaxonLabelService;
	private StudyService mStudyService;
	
	/**
	 * Return the TaxonHome field
	 * 
	 * @return TaxonHome mTaxonHome
	 */
	public TaxonHome getTaxonHome() {
		return mTaxonHome;
	}

	/**
	 * Set the TaxonHome field.
	 * 
	 * @param pNewTaxonHome
	 */
	public void setTaxonHome(TaxonHome pNewTaxonHome) {
		mTaxonHome = pNewTaxonHome;
	}
	
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

	@Override
	protected ModelAndView onCancel(
		HttpServletRequest request,
		HttpServletResponse response,
		Object pCommand) throws Exception {

		processRedirection(request, "onCancel");
		return new ModelAndView(getCancelView());
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 * @throws Exception
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {
		List<String> ubioResultErrors = new ArrayList<String>();
		TaxonLabel taxonLabel = (TaxonLabel) command;
		taxonLabel.setTaxonVariant(null);
		processRedirection(request, "onSubmit");
		TaxonVariant variant = getTaxonVariant(request, taxonLabel, ubioResultErrors);
		if ( variant != null ) {
			Map<Long,String> homonyms = (Map<Long,String>)request.getSession().getAttribute("homonyms");
			if ( homonyms != null ) {
				homonyms.remove(taxonLabel.getId());
				request.getSession().setAttribute("homonyms", homonyms);
			}
			taxonLabel.setAttemptedLinking(true);
			taxonLabel.setTaxonVariant(variant);
			getTaxonLabelService().update(taxonLabel);
			getTaxonLabelService().updateAndRegenerateNewick(taxonLabel);// Trees still need to be updated
			return new ModelAndView(getSuccessView());
		}

		long starttime = System.currentTimeMillis();
		variant = getTaxonLabelService().createFromUBIOService(taxonLabel);
		if ( variant == null ) {
			// For now user is not forced to enter the valid Taxon Label.
			// String message = "Taxon label: " + alabel + " is invalid.";
			// request.setAttribute("errors", message);
			// return showForm(request, response, bindExp);
		} 
		else {
			taxonLabel.setAttemptedLinking(true);
			taxonLabel.setTaxonVariant(variant);
			getTaxonLabelService().update(taxonLabel);
		}

		if ( LOGGER.isInfoEnabled() ) {
			long endtime = System.currentTimeMillis();
			LOGGER.info("TIME USED IN ACCESSING UBIO SERVICE: " + (endtime - starttime));
		}

		if ( request.getParameter(ACTION_UPDATE) != null ) {
			getTaxonLabelService().updateAndRegenerateNewick(taxonLabel);
		}
		if (ubioResultErrors.size() > 0) {
			request.setAttribute("errors", ubioResultErrors);
			return showForm(request, response, bindExp);
		}
		
		return new ModelAndView(getSuccessView());

	}

	/* 
	 * This method attempts to find a TaxonVariant based on user input. Several 
	 * scenarios are possible:
	 * 1. the user clicks one of the radio buttons, in which case we:
	 *    1a. have an an existing taxon variant in the database (which we return), or
	 *    1b. the user selected the '(no association)' radio button (which returns null)
	 * 2. the user enters a uBio taxon id, in which case we:
	 * 	  2a. check if we have a taxon by that ID, and we return the first taxon variant
	 *        that refers to that variant, or
	 *    2b. we don't have a taxon by that ID, so we must first fetch the corresponding
	 *        NCBI taxon ID, then create a new taxon and taxon variant de novo
	 */
	private TaxonVariant getTaxonVariant(
		HttpServletRequest request,
		TaxonLabel taxonLabel,
		List<String> ubioResultErrors) {
		String tvId = request.getParameter("taxonvariantid");
		String manualIdString = request.getParameter("manualid");
		Long manualId = null;
		TaxonVariant variant = null;
		if ( ! manualIdString.equals("") ) {
			try {
				manualId = Long.parseLong(manualIdString);
			} catch ( NumberFormatException e ) {
				ubioResultErrors.add("Not a valid ID: "+manualIdString);
				return variant;
			}
		}	
		if ( manualId == null ) {
			// 1a or 1b depending whether findTaxonVariantByID returns a variant or null
			try {
				variant = getTaxonLabelService().findTaxonVariantByID(Long.parseLong(tvId));
			} catch ( NumberFormatException e ) {
				LOGGER.info("NumberFormatException, user supplied taxon variant id was probably null: "+tvId);
			}
		}
		else {
			Taxon taxon = null;
			taxon = getTaxonHome().findByUBIOTaxId(manualId);
			if ( taxon != null ) {
				// 2a
				Collection<TaxonVariant> variants = getTaxonHome().findVariantsByTaxon(taxon);
				variant = variants.iterator().next();
			}
			else {
				// 2b
				Integer ncbiId = getTaxonLabelService().findNcbiTaxIdByUBIOTaxId(manualId);
				String ncbiPreferredName = getTaxonLabelService().getNCBIPreferredName(ncbiId.toString());
				Taxon newTaxon = new Taxon(ncbiPreferredName,manualId,ncbiId);
				getTaxonHome().store(newTaxon);
				TaxonVariant newVariant = new TaxonVariant(manualId,taxonLabel.getTaxonLabel(),ncbiPreferredName,"canonical form");
				newVariant.setTaxon(newTaxon);
				getTaxonHome().store(newVariant);
				variant = newVariant;
			}
		}
		return variant;
	}

	/**
	 * @param request HttpServletRequest
	 * @param actionorigin This String has information regarding the origin of the request.
	 * 
	 */
	public void processRedirection(HttpServletRequest request, String actionOrigin) {
		List<String> sessionlst;
		if ((sessionlst = (List<String>) request.getSession().getAttribute("REQUEST_VIEW_URL")) != null) {
			StringBuilder redirect = new StringBuilder();
			if (sessionlst.get(0) != null
				&& sessionlst.get(0).endsWith("/user/matrixRowSegmentForm.html")) {

				redirect.append("redirect:/user/matrixRowSegmentForm.html");

				if (sessionlst.get(1) != null) {
					redirect.append("?id=").append(sessionlst.get(1));
				}

			} else if (sessionlst.get(0) != null
				&& sessionlst.get(0).endsWith("/user/taxaList.html")) {
				redirect.append("redirect:/user/taxaList.html");
			}

			if (actionOrigin.equals("onSubmit")) {
				setSuccessView(redirect.toString());
			} else {
				setCancelView(redirect.toString());
			}

			request.getSession().removeAttribute("REQUEST_VIEW_URL");
		}
	}

	protected boolean suppressBinding(HttpServletRequest request) {

		return isCancelRequest(request);
		/*
		 * if(request.getParameter("_cancel") != null) { return true; } else { return false; }
		 */
	}

	/**
	 * Retrieve TaxonLabel object we are working with.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 * 
	 * @throws ServletException
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		String taxonlabelid = ServletRequestUtils.getStringParameter(request, "taxonlabelid", null);
		TaxonLabel txnLabel = getTaxonLabelService().findByID(Long.parseLong(taxonlabelid));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("VALUE OF Taxon Label ID in FORM BACKING =" + taxonlabelid);
		}
		Collection<TaxonVariant> variants = getTaxonLabelService().findTaxonVariants(txnLabel);		
		TaxonVariant currentVariant = txnLabel.getTaxonVariant();
		if ( currentVariant != null ) {
			request.getSession().setAttribute("currentVariantId", currentVariant.getId());		
			if ( ! variants.contains(currentVariant) ) {
				variants.add(currentVariant);
			}
		}
		request.getSession().setAttribute("variants", variants);
		return txnLabel;
	}

}
