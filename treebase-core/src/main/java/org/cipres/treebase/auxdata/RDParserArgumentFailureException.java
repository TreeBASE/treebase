
package org.cipres.treebase.auxdata;

@SuppressWarnings("serial")
public class RDParserArgumentFailureException extends Error {
	RDParserArgumentFailureException(String s) {
		super(s);
	}
}

// TODO: Seems like the collection type here is not handled in the best possible way
// Confer with Sam.
