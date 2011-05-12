package org.cipres.treebase.web.controllers;

import java.util.HashMap;

import org.apache.log4j.Logger;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * @author madhu
 * 
 * Created on January 28th, 2008
 */
public class DirectMapToPhyloWidgetController implements Controller {

	private static final Logger LOGGER = Logger.getLogger(DirectMapToPhyloWidgetController.class);

	private PhyloTreeService mPhyloTreeService;
	private PhyloTreeHome mPhyloTreeHome;
	protected String mDefaultView;

	public PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeHome(PhyloTreeHome pPhyloTreeHome) {
		mPhyloTreeHome = pPhyloTreeHome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest pRequest, HttpServletResponse pResponse)
		throws Exception {
		String defaultNewick = "";
		String separator = "@";
		Map<String, String> treeMap = new HashMap<String, String>();
		Map<String,String> phylowsMap = new HashMap<String,String>();
		String newickStringName = null;

		if (pRequest.getParameter("treeblockid") != null) {

			String TBID = pRequest.getParameter("treeblockid");
			Long value = Long.parseLong(TBID);

			TreeBlock TB = getPhyloTreeHome().findTreeBlockById(value);

			if (TB.getTitle().startsWith("Tree")) {
				newickStringName = TB.getTitle();
			} else {
				newickStringName = "Tree Block: " + TB.getTitle();
			}

			for (PhyloTree aTree : TB.getTreeList()) {
				treeMap.put(getMapKey(aTree), aTree.getId() + separator + aTree.getNewickString()
					+ separator + "T");
				phylowsMap.put(getMapKey(aTree), aTree.getPhyloWSPath().getPath());
				if ( defaultNewick.equals("") ) {
					defaultNewick = aTree.getNewickString();
				}
			}					
			// XXX ok, so this mess started when we somehow combined phylowidget for searching
			// with phylowidget for submission (why???) -- rvosa
			if ( TB.getTreeListIterator().hasNext() ) {
				Study study = TB.getTreeListIterator().next().getStudy();
				if ( study != null && study.isPublished() ) {
					pRequest.getSession().setAttribute("treeBlockID", TB.getId());
					if ( TB.getTreeListIterator().hasNext() ) {
						pRequest.getSession().setAttribute("studyID", study.getId());
					}
				}
			}
		}

		if (pRequest.getParameter("treeid") != null) {

			String TreeId = pRequest.getParameter("treeid");
			PhyloTree aTree = getPhyloTreeService().findByID(Long.parseLong(TreeId));

			// use "T" to enable the edit menu items.
			treeMap.put(getMapKey(aTree), TreeId + separator + aTree.getNewickString() + separator
				+ "T");
			phylowsMap.put(getMapKey(aTree), aTree.getPhyloWSPath().getPath());
			newickStringName = aTree.getTitle();
			Study study = aTree.getStudy();
			if ( study != null && study.isPublished() ) {
				pRequest.getSession().setAttribute("treeBlockID", aTree.getTreeBlock().getId());
				pRequest.getSession().setAttribute("studyID", study.getId());
			}
			defaultNewick = aTree.getNewickString();
		}
		
		pRequest.getSession().setAttribute("DEFAULTNEWICK", defaultNewick);
		pRequest.getSession().setAttribute("NEWICKSTRINGSMAP", treeMap);
		pRequest.getSession().setAttribute("PHYLOWSMAP", phylowsMap);
		pRequest.getSession().setAttribute("NEWICKSTRINGNAME", newickStringName);

		return new ModelAndView(getDefaultView());
	}

	/**
	 * @param pTree
	 * @return String with a complete tree's information
	 */
	private String getMapKey(PhyloTree pTree) {
		StringBuilder key = new StringBuilder("TreeId: " + pTree.getId());
		if (!TreebaseUtil.isEmpty(pTree.getLabel())) {
			key.append("|").append(pTree.getLabel());
		}
		if (!TreebaseUtil.isEmpty(pTree.getTitle())) {
			key.append("|").append(pTree.getTitle());
		}
		return key.toString();
	}

	/**
	 * @return the phyloTreeService
	 */
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * @param pPhyloTreeService the phyloTreeService to set
	 */
	public void setPhyloTreeService(PhyloTreeService pPhyloTreeService) {
		mPhyloTreeService = pPhyloTreeService;
	}

	public String getDefaultView() {
		return mDefaultView;
	}

	public void setDefaultView(String defaultView) {
		mDefaultView = defaultView;
	}

}
