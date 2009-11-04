
package org.cipres.treebase.util;

public abstract class OptionSettings {
	abstract boolean acceptsOpt(String f) ;
	abstract void setOpt(String f, String v);
	abstract Object getOpt(String f);
	abstract boolean hasOpt(String f);
	abstract boolean getsArgument(String f);
	abstract void initialize();
	String withoutLeadingHyphen(String s) {
		if (s.startsWith("-")) return s.substring(1);
		else return s;
	}
	Object getOptWithDefault(String f, Object defaultVal) {
		return hasOpt(f) ? getOpt(f) : defaultVal;
	}
}
