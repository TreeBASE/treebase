


package org.cipres.treebase.auxdata;

import org.cipres.treebase.auxdata.Value;

interface SideCondition {
	abstract boolean okay(Value v);
}
