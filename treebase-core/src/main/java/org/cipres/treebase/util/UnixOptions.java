

package org.cipres.treebase.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Unix "getopts"-like interface.
 * Accepts specification string in the form "px:Y=" which means that p is a boolean option,
 * x is a string option, and Y is an integer option.
 * 
 * @author mjd 20090116
 *
 */
public class UnixOptions extends OptionSettings {
	Map<String,OptVal> opts = new HashMap<String, OptVal> ();
	Map<String,OptType> type = new HashMap<String, OptType> ();
	Map<String,Boolean> wasSet = new HashMap<String, Boolean> ();
	
	UnixOptions(String spec) {
		for (int i = 0; i < spec.length(); i++) {
			Character c = spec.charAt(i);
			OptType t;
			if (i+1 == spec.length()) {
				t = OptType.BOOLEAN;
			} else {
				char typeSpec = spec.charAt(i+1);
				if (typeSpec == ':') {
					t = OptType.STRING; i++;
				} else if (typeSpec == '=') {
					t = OptType.INTEGER; i++;
				} else {
					t = OptType.BOOLEAN;
				}
			}
			type.put(c.toString(), t);
			
			OptVal ov = null;
			switch (t) {
			case BOOLEAN: ov = new BoolOptVal(); break;	
			case INTEGER: ov = new IntOptVal(); break;	
			case STRING: ov = new StringOptVal(); break;
			default:
				throw new Error("Unknown option type " + t);
			}
			
			opts.put(c.toString(), ov);
		}
	}
	
	@Override
	void initialize() {
		for (Map.Entry<String,OptType> entry : type.entrySet()) {
			switch(entry.getValue()) {
			case BOOLEAN:
				((BoolOptVal) opts.get(entry.getKey())).setB(false); break;
			case INTEGER:
				((IntOptVal) opts.get(entry.getKey())).setI(0); break;
			case STRING:
				((StringOptVal) opts.get(entry.getKey())).setS(""); break;
			default:
				throw new Error("Unknown option type " + entry.getValue());
			}
			wasSet.put(entry.getKey(), false);
		}
	}
	
	private enum OptType { STRING, BOOLEAN, INTEGER };
	
	@Override
	OptVal getOpt(String f) {
		return opts.get(withoutLeadingHyphen(f));
	}

	@Override
	boolean getsArgument(String f) {
		return type.get(withoutLeadingHyphen(f)) != OptType.BOOLEAN;
	}

	@Override
	boolean acceptsOpt(String f) {
		return opts.containsKey(withoutLeadingHyphen(f));
	}

	@Override
	void setOpt(String f, String v) {
		f = withoutLeadingHyphen(f);
		wasSet.put(f, true);
		OptVal o = opts.get(f);
		switch(type.get(f)) {
		case BOOLEAN:
			if (v.equals("") || v.equals("0")) ((BoolOptVal) o).setB(false);
			else ((BoolOptVal) o).setB(true);
			break;
		case INTEGER:
			((IntOptVal) o).setI(Integer.parseInt(v));
			break;
		case STRING:
			((StringOptVal) o).setS(v);
			break;
		default:
			throw new Error("Unknown option type " + type.get(f));
		}
	}
	
	boolean getBoolOpt(String f) {
		return ((BoolOptVal) opts.get(withoutLeadingHyphen(f))).getB();
	}
	
	int getIntOpt(String f) {
		return ((IntOptVal) opts.get(withoutLeadingHyphen(f))).getI();
	}
	
	String getStringOpt(String f) {
		return ((StringOptVal) opts.get(withoutLeadingHyphen(f))).getS();
	}
	
	private abstract class OptVal {
	}
	
	private class StringOptVal extends OptVal {
		String s;
		public StringOptVal() {
			super();
		}
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
	}

	private class BoolOptVal extends OptVal {
		boolean b;
		public BoolOptVal() {
			super();
		}
		public boolean getB() {
			return b;
		}
		public void setB(boolean b) {
			this.b = b;
		}
	}

	private class IntOptVal extends OptVal {
		int i;

		public IntOptVal() {
			super();
		}
		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}
	}

	@Override
	boolean hasOpt(String f) {
		return wasSet.get(withoutLeadingHyphen(f));
	}

	int getOptWithDefault(String f, int i) {
		return hasOpt(f) ? getIntOpt(f) : i;
	}
	
	String getOptWithDefault(String f, String s) {
		return hasOpt(f) ? getStringOpt(f) : s;
	}
	
	boolean getOptWithDefault(String f, boolean b) {
		return hasOpt(f) ? getBoolOpt(f) : b;
	}

}
