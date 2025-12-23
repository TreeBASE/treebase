
package org.cipres.treebase.web.controllers;

import org.springframework.web.servlet.mvc.UrlFilenameViewController;

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
        return extractFilenameFromUrlPath(pUri);		
	}

	/**
	 * Extract the filename from the given URL path.
	 * This replaces the removed WebUtils.extractFilenameFromUrlPath method.
	 */
	private String extractFilenameFromUrlPath(String urlPath) {
		if (urlPath == null || urlPath.isEmpty()) {
			return "";
		}
		
		int begin = urlPath.lastIndexOf('/') + 1;
		int end = urlPath.indexOf(';');
		if (end == -1) {
			end = urlPath.indexOf('?');
			if (end == -1) {
				end = urlPath.length();
			}
		}
		
		// Ensure valid bounds
		if (begin < 0 || begin >= urlPath.length() || end > urlPath.length() || begin >= end) {
			// Return the path as-is if we can't extract a valid filename
			return urlPath.startsWith("/") ? urlPath.substring(1) : urlPath;
		}
		
		String filename = urlPath.substring(begin, end);
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex != -1) {
			filename = filename.substring(0, dotIndex);
		}
		return filename;
	}

}
