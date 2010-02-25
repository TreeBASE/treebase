
package org.cipres.treebase.util;

import java.io.BufferedReader;
import java.io.IOException;

import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.domain.study.CitationHome;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.StudyService;

/**
 * @author mjd 20090223
 *
 */
public interface CitationDataImporterInterface extends Standalone {
	public PersonHome getPersonHome();
	public void setPersonHome(PersonHome personHome);
	public StudyService getStudyService();
	public void setStudyService(StudyService studyService);
	public CitationService getCitationService();
	public void setCitationService(CitationService citationService);
	public CitationHome getCitationHome();
	public void setCitationHome(CitationHome citationHome);
	
	public void processCitationFile(BufferedReader in, boolean testMode) throws NumberFormatException, IOException;
	public void processAllCitations(BufferedReader in, boolean testMode) throws NumberFormatException, IOException;
    public void processOneCitation(String[] fields, boolean testMode) throws NumberFormatException, IOException;

}
