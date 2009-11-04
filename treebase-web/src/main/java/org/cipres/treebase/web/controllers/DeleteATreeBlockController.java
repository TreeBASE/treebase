
package org.cipres.treebase.web.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * 
 * @author madhu
 * 
 */

public class DeleteATreeBlockController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(DeleteATreeBlockController.class);

	private PhyloTreeHome mPhyloTreeHome;
	private SubmissionHome mSubmissionHome;
	private SubmissionService mSubmissionService;
	private boolean mTestVariable;

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	public PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeHome(PhyloTreeHome pPhyloTreeHome) {
		mPhyloTreeHome = pPhyloTreeHome;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		boolean test = false;

		String username = request.getRemoteUser();
		TreeBlock atreeblock = (TreeBlock) command;

		if (request.getParameter("treeblockid") != null) {

			Long submission_id = getSubmissionHome().findByTreeBlock(atreeblock).getId();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("IN DELETEATREEBLOCKCONTROLLER-SUBMISSION ID: " + submission_id);
			}
			if (submission_id == null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("IN DELETEATREEBLOCKCONTROLLER-SORRY NO SUBMISSION ID--!!!!!!!!");
				}
				return null;
			}
			TBPermission perm2 = getSubmissionService().getPermission(username, submission_id);

			if (perm2 == TBPermission.WRITE) {
				getPhyloTreeHome().deleteTreeBlock(atreeblock);
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Sorry Authorization Failure, you cannot delete this tree block.");
				}

				return setAttributeAndShowForm(
					request,
					response,
					bindExp,
					"errors",
					"Sorry Authorization Failure, you cannot delete this tree block.");

			}

		}

		return new ModelAndView(new RedirectView("treeBlockList.html"));
		// IMPORTANT: in handle request RedirectView one cannot use /user/treeBlockList.html
		// However, one can use "redirect:/user/treeBlockList.html" as an argument for the
		// ModelAndView object.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) {

		if (request.getParameter("treeblockid") != null) {

			Long treeBlockID = Long.parseLong(request.getParameter("treeblockid"));
			TreeBlock pTreeBlock = getPhyloTreeHome().findTreeBlockById(treeBlockID);

			return pTreeBlock;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map<String,String> referenceData(HttpServletRequest request) {

		Map<String,String> referenceMap = new HashMap<String,String>();
		referenceMap.put("deleteelementtype", "Delete this particular TreeBlock");

		Long treeBlockID = Long.parseLong(request.getParameter("treeblockid"));
		TreeBlock pTreeBlock = getPhyloTreeHome().findTreeBlockById(treeBlockID);

		Collection<PhyloTree> pTreeCollection = pTreeBlock.getTreeList();

		for (PhyloTree pTree : pTreeCollection) {
			if (pTree.getStudy() != null) { // It means this tree is tied to analysis step
				setTestVariable(true);
				referenceMap
					.put(
						"generalmessage",
						"Either this TreeBlock or atleast one of the Tree in this Block is tied to an Analysis Step.");
				break; // Even if one tree of the block is ties to Analysis step, we can exit the
				// for loop.
			}
		}

		referenceMap.put("deleteid", "TreeBlock ID : " + treeBlockID);
		referenceMap.put("objectname", "TreeBlock Title : " + pTreeBlock.getTitle());

		return referenceMap;
	}

	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}

	/**
	 * @return the test
	 */
	public boolean getTestVariable() {
		return mTestVariable;
	}

	/**
	 * @param pTest the test to set
	 */
	public void setTestVariable(boolean pTestVariable) {
		mTestVariable = pTestVariable;
	}
}
