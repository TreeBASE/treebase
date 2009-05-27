/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import org.springframework.web.servlet.mvc.UrlFilenameViewController;
import org.springframework.web.util.WebUtils;

/**
 * A subclass of Spring UrlFilenameViewController. The UrlFilenameViewController class in Spring 2
 * is not backward compatible with Spring 1.2.8. This class restores the old Spring 1.2.8 behavior.
 * 
 * See Spring bug SPR-2789.
 * 
 * ALERT Revisit this issue if Spring fixes this!
 * 
 * Created on Nov 8, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class ShortPathUrlFilenameViewController extends UrlFilenameViewController {

	/**
	 * Constructor.
	 */
	public ShortPathUrlFilenameViewController() {
		super();
	}

	/**
	 * Restore the old Spring 1.2.8 behavior: In version 1.2.8 UrlFilenameViewController maps the
	 * url "/products/view.html" to "view" while version 2.0 maps the same url to "products/view".
	 * 
	 * @see org.springframework.web.servlet.mvc.UrlFilenameViewController#extractViewNameFromUrlPath(java.lang.String)
	 */
	@Override
	protected String extractViewNameFromUrlPath(String pUri) {
        return WebUtils.extractFilenameFromUrlPath(pUri);		
	}

}
