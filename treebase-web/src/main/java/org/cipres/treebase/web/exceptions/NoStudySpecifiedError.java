
package org.cipres.treebase.web.exceptions;

/**
 * EmptySubmissionException.java
 * 
 * Created on Jun 7, 2006
 * @author lcchan
 *
 */
public class NoStudySpecifiedError extends RuntimeException {

	/**
	 * Constructor.
	 */
	public NoStudySpecifiedError(String s) {
		super(s);
	}
}
