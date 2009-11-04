


package org.cipres.treebase.auxdata;

/* A regular parser, but performs an action before returning */
class ActionParser extends FilterParser { 
	ActionParser(RDParser parser, GenericAction action) {
		super(parser, action);
	}
}

