
package org.cipres.treebase.domain.search;

/**
 * @author mjd 20090316
 *
 */
public class UnknownSearchResultsTypeError extends Error {

	public UnknownSearchResultsTypeError() {
		super();
	}

	public UnknownSearchResultsTypeError(String arg0) {
		super(arg0);
	}

	public UnknownSearchResultsTypeError(Throwable arg0) {
		super(arg0);
	}

	public UnknownSearchResultsTypeError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnknownSearchResultsTypeError(SearchResultsType srType) {
		super("'" + srType.toString() + "'");
	}

}
