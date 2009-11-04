
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
