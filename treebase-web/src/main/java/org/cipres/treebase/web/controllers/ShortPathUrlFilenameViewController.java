
package org.cipres.treebase.web.controllers;

import org.springframework.web.servlet.mvc.UrlFilenameViewController;

/**
 * A subclass of Spring UrlFilenameViewController. This class now uses Spring 2.0 behavior which
 * preserves the path structure, allowing URLs like /admin/page.html to resolve to admin/page view.
 * 
 * See Spring bug SPR-2789.
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
	 * Use Spring 2.0 behavior: maps the url "/admin/view.html" to "admin/view".
	 * This allows JSPs to be organized in subdirectories matching URL paths.
	 * 
	 * @see org.springframework.web.servlet.mvc.UrlFilenameViewController#extractViewNameFromUrlPath(java.lang.String)
	 */
	@Override
	protected String extractViewNameFromUrlPath(String pUri) {
		// Use parent class method which preserves path structure
		return super.extractViewNameFromUrlPath(pUri);
	}
}
