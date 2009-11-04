
package org.cipres.treebase.auxdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Class for parsing an entire file in Bill Piel's <tt>Dump.txt</tt> format
 * 
 * <p>The main method, <code>parse</code>, is an {@link RDParser} class parser as 
 * usual.  It expects to receive an input that is a sequence of study objects.
 * As each study in the input is recognized, it is passed to an action callback which can
 * insert it into a database or whatever.</p>
 * 
 * <p>There are convenience methods for parsing a complete file and for
 * printing a report afterward.</p>
 * 
 * @author mjd 20080731
 *
 */
public class AuxiliaryDataParser extends RDParser {
	RDParser parser = null;
	
	/**
	 * Manufacture a parser which executes an action for each study in the input
	 * The action executed is specified by <code>completeStudyAction</code>, which should expect
	 * to be invoked repeatedly, and passed a {@link ValueStudy} each time. 
	 * 
	 * @param completeStudyAction
	 */
	public AuxiliaryDataParser(Filter completeStudyAction) {
		super();
		parser = new RepeatedParser(
				new FilterParser(
						new StudyParser(),
						completeStudyAction
				),
				false);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.auxdata.RDParser#Parse(org.cipres.treebase.auxdata.LazyList)
	 */
	public RDParserResult Parse(LazyList<Token> source) {		
		return parser.Parse(source);
	}

	/**
	 * Utility function that opens a file and invokes the parser on it
	 * 
	 * @param filename - the file to parse
	 * @return the results of Parsing
	 * @see	RDParser.Parse 
	 * @author mjd 20080731
	 * @throws IOException 
	 */
	public RDParserResult parseFile(String filename) throws IOException {
		return parseFile(new File(filename));
	}
	
	public RDParserResult parseFile(File f) throws IOException {
		BufferedReader fh = new BufferedReader(new FileReader(f));
		Generator<Token> gen = new TokenReader(fh);
		LazyList<Token> toks = new LazyList<Token>(gen);
		return Parse(toks);
	}
	
	
	/**
	 * Print a report about the results of parsing to the <code>PrintStream</code> <code>out</code>
	 * 
	 * @param out
	 * @param result - the result returned from the parser
	 * @author mjd 20080731
	 */
	public void reportResults(PrintStream out,
			RDParserResult result) {
		LazyList<Token> remainingTokens = result.remainingTokens();
		if (result.failed()) {
			out.println("Parsing failed; no studies parsed");
			out.println("Parsing stopped at:");
			print_toks(out, remainingTokens.lastNode(), 3);
		} else {
			if (result.remainingTokens() == null || result.remainingTokens().head().hasType(TT_EOF)) {
				out.println("Parsing completed normally");
			} else {
				out.println("Parsing completed prematurely at:");
				print_toks(out, remainingTokens.lastNode(), 7);
			}

			{ 
				Value[] results = ((ValueSequence) result.value()).values;
				int nStudies = results.length;
				int nImported = 0;
				for (Value v : results) {
					if (! (v instanceof ValueNone)) {
						nImported += 1;
					}
				}
				String studies = nImported == 1 ? "study" : "studies";
				out.println(nImported + "/" + nStudies + " " + studies + " processed");
			}
		}
	}

	
	/**
	 * Utility function to print out the first few tokens in a token stream
	 * 
	 * @param out - where to print the tokens
	 * @param toks - the token stream
	 * @param n - how many to print
	 * @author mjd
	 */
	static void print_toks(PrintStream out, LazyList<Token> toks, int n) {
		int i = 0;
		while (i < n && toks != null) {
			out.println("\t" + toks.head());
			toks = toks.tail();
			i += 1;
		}
	}
}

