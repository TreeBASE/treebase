
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
