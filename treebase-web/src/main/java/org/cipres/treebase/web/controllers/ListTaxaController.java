
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListTaxaController.java Generate Taxa
 * 
 * Created on July 27, 2007
 * 
 * @author Madhu
 * 
 */

public class ListTaxaController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(ListTaxaController.class);
	private static final String UBIOSEARCHTAXONLABEL = "http://www.ubio.org/browser/search.php?search_all=";
	private static final String UBIOTAXONRECORD = "http://www.ubio.org/browser/details.php?namebankID=";
	private static final String EDITTAXONLABEL = "/treebase-web/user/editTaxonLabel.html?taxonlabelid=";
	private static final String EMPTYSPACE = " ";

	private TaxonLabelService mTaxonLabelService;
	private StudyService mStudyService;
	private TaxonLabelHome mTaxonLabelHome;

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
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

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {
		
		Collection<TaxonLabel> taxonLabelCollection = (Collection<TaxonLabel>) command;

		// ACTION_UPDATE just means refresh the current list, without validation
		if (request.getParameter(ACTION_UPDATE) != null) {
			return new ModelAndView(getSuccessView());
		}

		// need to validate labels
		List<String> ubioResultMessages = new ArrayList<String>();
		List<String> ubioResultErrors = new ArrayList<String>();		
		String[] idsToValidate = request.getParameterValues("validate");
		Map<Long,String> homonyms = null;
		if ( request.getSession().getAttribute("homonyms") != null ) {
			homonyms = (Map<Long,String>)request.getSession().getAttribute("homonyms");
		}
		else {
			homonyms = new HashMap<Long,String>();
		}
		if ( idsToValidate != null ) {
			for ( TaxonLabel taxonlabel : taxonLabelCollection ) {
				boolean validateMe = false;
				FINDID: for ( int i = 0; i < idsToValidate.length; i++ ) {
					long idToValidate = Long.parseLong(idsToValidate[i]);
					if ( taxonlabel.getId() == idToValidate ) {
						validateMe = true;
						break FINDID;
					}
				}
				if ( validateMe ) {
					Collection<TaxonVariant> variants = getTaxonLabelService().findTaxonVariants(taxonlabel);
					TaxonVariant variant = null;
					
					// one variant, no homonyms
					if ( variants.size() == 1 ) {
						variant = variants.iterator().next();
					}
					
					// homonyms
					else if ( variants.size() > 1 ){
						homonyms.put(taxonlabel.getId(), taxonlabel.getTaxonLabel());
						variant = variants.iterator().next();			
					}
					
					// no variants were found locally in the db
					else {
						variant = getTaxonLabelService().createFromUBIOService(taxonlabel);
						
						// no variants were found by uBio
						if ( variant == null ) {
							ubioResultMessages.add(taxonlabel.getTaxonLabel() + " is **UNRECOGNIZED**.");
							ubioResultErrors.add(taxonlabel.getTaxonLabel()
								+ " is **UNRECOGNIZED** try <a href=" + UBIOSEARCHTAXONLABEL
								+ taxonlabel.getTaxonLabel().replace(EMPTYSPACE, "%20")
								+ " target=_blank>UBIO</a> site.");
						}
						
						// variants stored by uBio,TODO now check for homonyms
						else {
							ubioResultErrors.add(taxonlabel.getTaxonLabel()
								+ " was located in uBio, see: <a href=" + UBIOTAXONRECORD
								+ variant.getNamebankID()
								+ " target=_blank>UBIO</a> site.");							
						}
					}
					setStoreTaxonVariant(variant,taxonlabel);
					ubioResultMessages.add("Validated "+taxonlabel.getTaxonLabel());
				}
			}			
		}
		request.getSession().setAttribute("homonyms", homonyms);

		if (ubioResultErrors.size() > 0) {
			request.setAttribute("errors", ubioResultErrors);
			return showForm(request, response, myerrors);
		}
		
		return new ModelAndView("redirect:/user/taxaList.html");
	}
	
	/**
	 * Assigns the provided TaxonVariant (or null) to the provided TaxonLabel,
	 * flags that linking has been attempted, updates the TaxonLabel object.
	 * 
	 * @param variant the associated TaxonVariant or null
	 * @param taxonlabel the focal TaxonLabel
	 */
	private void setStoreTaxonVariant(TaxonVariant variant,TaxonLabel taxonlabel) {
		if ( variant != null ) {
			taxonlabel.setTaxonVariant(variant);
		}
		taxonlabel.setAttemptedLinking(true);
		getTaxonLabelService().update(taxonlabel);		
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException exc, Map map)
			throws Exception {
		request.setAttribute("editable", formIsEditable(request));		

		return super.showForm(request, response, exc, map);
	}

	// Formerly this was tested directly by JSP, but other parts of the
	// application (such as ListTaxaSearchController) need to make the labels
	// editable regardless of publicationState, or when there may not be a publicationState.
	// So editability is now signalled by an "editable" attribute, which the 
	// onSubmit handler will set as directed by this method.  MJD 20080626
	/**
	 * @param request the servlet request object containing the session
	 */
	protected boolean formIsEditable(HttpServletRequest request) {
		String publicationState = (String) request.getSession().getAttribute("publicationState");
		return publicationState != null && publicationState.equals("NotReady");		
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		// Setting the session variable "REQUEST_VIEW_URL
		List<String> sessionlist = new ArrayList<String>();
		sessionlist.add(request.getRequestURL().toString());
		sessionlist.add(null);
		request.getSession().setAttribute("REQUEST_VIEW_URL", sessionlist);

		Study study = ControllerUtil.findStudy(request, mStudyService);
		Collection<TaxonLabel> taxonLabels = getTaxonLabelHome().findByStudy(study);
		Iterator<TaxonLabel> taxonLabelsIterator = taxonLabels.iterator();
		List<String> errors = new ArrayList<String>();
		while ( taxonLabelsIterator.hasNext() ) {
			TaxonLabel label = taxonLabelsIterator.next();
			if ( ! label.getAttemptedLinking() ) {
				errors.add(label.getTaxonLabel() + " has not been linked to the external taxonomy");
			}
			else if ( label.getAttemptedLinking() && label.getNcbiTaxID() == null ) {
				errors.add(label.getTaxonLabel() + " - an unsuccesful attempt has been made to link this to the external taxonomy");
			}
		}
		
		Map<Long,String> homonyms = (Map<Long,String>)request.getSession().getAttribute("homonyms");
		if ( homonyms != null ) {
			for ( Long taxonLabelId : homonyms.keySet() ) {
				errors.add(homonyms.get(taxonLabelId) 
				+ " appears to be a homonym; defaulted to first hit, but please <a href=\"" + EDITTAXONLABEL
				+ taxonLabelId 
				+ "\">validate by hand</a>.");					
			}
		}	
		
		request.setAttribute("errors", errors);
		return taxonLabels;
	}

}
