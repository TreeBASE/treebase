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

package org.cipres.treebase.framework;

/**
 * Indicates the execution result status. It can has an error message if the execution action fails.
 * 
 * Created on Nov 20, 2006
 * 
 * @author Jin Ruan
 */
public class ExecutionResult {

	private String mErrorMessage;
	private int mSuccessfulCount;
	private int mFailureCount;

	/**
	 * Constructor.
	 */
	public ExecutionResult() {
		super();
	}

	/**
	 * Return the error Message field.
	 * 
	 * @return String mErrorMessage
	 */
	public String getErrorMessage() {
		if (mErrorMessage == null) {
			mErrorMessage = new String();
		}
		return mErrorMessage;
	}

	/**
	 * Add an error Message.
	 * 
	 * @param String
	 */
	public void addErrorMessage(String pMessage) {
		String msg = getErrorMessage();
		if (msg.length() > 1) {
			msg += "/n";
		}
		msg += pMessage;

		setErrorMessage(msg);
	}

	/**
	 * Report the count of failures for the result.
	 * 
	 * @return int mFilureCount
	 */
	public int getFailureCount() {
		return mFailureCount;
	}

	/**
	 * Set the FilureCount field.
	 */
	public void setFailureCount(int pNewFilureCount) {
		mFailureCount = pNewFilureCount;
	}

	/**
	 * Report the count of success for the execution.
	 * 
	 * @return
	 */
	public int getSuccessfulCount() {
		return mSuccessfulCount;
	}

	/**
	 * Set the SuccessfulCount field.
	 */
	public void setSuccessfulCount(int pNewSuccessfulCount) {
		mSuccessfulCount = pNewSuccessfulCount;
	}

	/**
	 * Determine if the action was successful
	 * 
	 * @param boolean
	 */
	public boolean isSuccessful() {
		return getErrorMessage().length() == 0;
	}

	/**
	 * @param pErrorMessage the errorMessage to set
	 */
	private void setErrorMessage(String pErrorMessage) {
		mErrorMessage = pErrorMessage;
	}

}
