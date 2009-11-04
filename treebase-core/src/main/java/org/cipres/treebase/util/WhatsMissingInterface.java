
package org.cipres.treebase.util;

public interface WhatsMissingInterface extends Standalone {
	boolean doCheck(String matrixPropertyName, WhatsMissing.ObjectCounter objectCounter);
}
