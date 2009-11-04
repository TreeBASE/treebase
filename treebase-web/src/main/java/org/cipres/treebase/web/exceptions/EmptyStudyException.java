
package org.cipres.treebase.web.exceptions;

/**
 * EmptySubmissionException.java
 * 
 * Created on Jun 7, 2006
 * @author lcchan
 *
 */
public class EmptyStudyException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public EmptyStudyException(String s) {
		super(s);
	}
}
