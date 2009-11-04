
package org.cipres.treebase.exception;

/**
 * @author mjd 20090415
 *
 */
public class UnimplementedMethodError extends Error {

	private static final long serialVersionUID = 1L;

	public UnimplementedMethodError() {
		this("Method is unimplemented");
	}

	public UnimplementedMethodError(String arg0) {
		super(arg0);
	}

	public UnimplementedMethodError(Throwable arg0) {
		super(arg0);
	}

	public UnimplementedMethodError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
