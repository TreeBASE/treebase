/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
