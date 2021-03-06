
package org.cipres.treebase.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashSet;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.auxdata.AuxiliaryDataParser;
import org.cipres.treebase.auxdata.CompleteStudyAction;
import org.cipres.treebase.auxdata.Generator;
import org.cipres.treebase.auxdata.LazyList;
import org.cipres.treebase.auxdata.RDParserResult;
import org.cipres.treebase.auxdata.Token;
import org.cipres.treebase.auxdata.TokenReader;


class AuxiliaryDataImporter extends AbstractStandalone implements AuxiliaryDataImporterInterface {
	
	PrintStream output = System.out;
	
	public static void main(String[] args) {
		setupContext();
		AuxiliaryDataImporterInterface me = (AuxiliaryDataImporterInterface) ContextManager.getBean("auxiliaryDataImporter");
		me.doIt(args);
	}
	
	public void doIt(String[] args) {
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("s:tnl:a:"));
		UnixOptions opts = go.getOpts(args);
		args = go.getArgs();

		if (args.length == 0) {
			args  = new String[1];
			args[0] = "/home/mjd/trunk/treebase-data/Dump.txt";
		}
		
		if (opts.hasOpt("l")) {
			String logFileName = opts.getStringOpt("l");
			try {
				setOutput(new PrintStream(new File(logFileName)));
			} catch (FileNotFoundException e) {
				die("File not found: " + logFileName);
			}
		}
			
		String actionName = opts.getOptWithDefault("a", "commitStudy");
		CompleteStudyAction action = (CompleteStudyAction) ContextManager.getBean(actionName + "Action");
		action.setDisabled(opts.getBoolOpt("n"));
		action.setIsForTesting(opts.getBoolOpt("t"));
		if (opts.hasOpt("s")) {
			action.setInterestingLegacyIDs(new HashSet());
			
			for (String legacyID : opts.getStringOpt("s").split(",\\s*"))
				action.getInterestingLegacyIDs().add(legacyID);
		}


		AuxiliaryDataParser parser = new AuxiliaryDataParser(action);

		boolean allSucceeded = true;
		for (String fileName : args) {
			try {
				if (! doFile(parser, new File(fileName))) 
					allSucceeded = false;
			} catch (IOException e) {
				warn("I/O exception processing file '" + fileName + "': " + e.getMessage());
				allSucceeded = false;
			}
		}

		System.exit(allSucceeded ? 1 : 0);
	}

	public boolean doFile(AuxiliaryDataParser p, File f) throws IOException {		
		BufferedReader fh = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		skipBOM(fh);
		
		Generator<Token> gen = new TokenReader(fh);
		LazyList<Token> toks = new LazyList<Token>(gen);

		RDParserResult result = p.Parse(toks);
		p.reportResults(this.getOutput(), result);
		return result.success();
	}
	
	// Skip the next character in the input if it is a UTF-8 byte-order marker
	private static void skipBOM(Reader fh) throws IOException {
		fh.mark(1); // Mark current position
		int i = fh.read();
		if (i != 0xfeff && i != 0xffef) fh.reset();  // return to position, if next char was not a BOM
	}

	public PrintStream getOutput() {
		return output;
	}

	public void setOutput(PrintStream output) {
		this.output = output;
	}
}