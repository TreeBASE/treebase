
package org.cipres.treebase.util;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.cipres.treebase.auxdata.AuxiliaryDataParser;
import org.cipres.treebase.auxdata.CompleteStudyAction;

/**
 * @author mjd 20090223
 *
 */
public interface AuxiliaryDataImporterInterface extends Standalone {
	public boolean doFile(AuxiliaryDataParser parser, File file) throws IOException;
	public void setOutput(PrintStream printStream);
	public PrintStream getOutput();
}
