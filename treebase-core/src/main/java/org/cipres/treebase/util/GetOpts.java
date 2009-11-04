
package org.cipres.treebase.util;


public class GetOpts<OS extends OptionSettings> {
	OS opt;
	String[] args;
	int argsOffset;
	
	GetOpts(OS optionSettings) {
		opt = optionSettings;
	}
	
	OS getOpts(String... args) {
		reInitialize(args);
		
		int i;
		for (i=0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				if (arg.equals("--")) { i++; break; }
				if (opt.acceptsOpt(arg)) {
					if (opt.getsArgument(arg)) {
						opt.setOpt(arg, args[i+1]);
						i++;
					} else {
						opt.setOpt(arg, "1");
					}
				} else {
					throw new Error("Unrecognized option flag " + arg);
				}
			} else { break; }
		}
		argsOffset = i;
		return opt;
	}
	
	public void reInitialize(String[] args) {
		this.args = args;
		argsOffset = 0;
		opt.initialize();
	}
	
	String[] getArgs() {
		int nElts = args.length - argsOffset;
		String[] newArgs = new String[nElts];
		System.arraycopy(args, argsOffset, newArgs, 0, nElts);
		return newArgs;
	}
}
