
package org.cipres.treebase.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.BookCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationHome;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.CitationStatus;
import org.cipres.treebase.domain.study.CitationStatusHome;
import org.cipres.treebase.domain.study.InBookCitation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;

/**
 * 
 * CitationDataImporter.java
 * 
 * Created on Jan 27, 2009
 * @author rvosa
 *
 */
public class CitationDataImporter extends AbstractStandalone implements CitationDataImporterInterface {
	PersonHome personHome;
	StudyService studyService;
	CitationService citationService;
	CitationHome citationHome;
	CitationStatusHome citationStatusHome;
	
	
	/*
		The expected input data file is a tab-delimited file, with the following fields:
		
	    "type",      //0 ("Journal Article"/"Book"/"Book Section")
	    "author",    //1 (semicolon-separated list, each author formatted as "Doe, J. D.")
	    "year",      //2 (four digits)
	    "title",     //3 (String)
	    "editors",   //4 (same formatting as authors)
	    "journal",   //5 (String)
	    "city",      //6 (String)
	    "publisher", //7 (String)
	    "volume",    //8 (String - note: not necessarily parseable as Integer)
	    null,        //9
	    "number",    //10 (String)
	    "pages",     //11 (String)
	    null,        //12
	    null,        //13
	    null,        //14
	    "status",    //15 (String)
	    "keywords",  //16 (String)
	    "abstract",  //17 (String)
	    "accession", //18 (TB1 study ID)
	    "url",       //19 (String)
	    "doi",       //20 (String)
	*/
	
	private static void printUsage() {
		System.err.println("Usage: program [-testrun] /path/to/citationRecordsFile");
	}
	
	/**
	 * 
	 */
	private static void setAuthors (Citation citation, String authorNameString,
			PersonHome personHome, boolean authorsAreEditors) {
		List<Person> authors = new ArrayList<Person>();
		String[] authorNames = authorNameString.split("; ");

		for ( int i = 0; i < authorNames.length; i++ ) {
			String[] nameParts = authorNames[i].split(", ");
			if ( nameParts.length < 2 ) {
				warn("Can't split record on spaces: '"+authorNames[i]+"'");
			}
			String[] firstNames = nameParts[1].split(" ");
			Collection<Person> persons = personHome.findByLastName(nameParts[0]);
			Person person = null;
			// exactly one match
			if ( persons.size() == 1 ) {
				Person tempPerson = persons.iterator().next();
				if ( initialsMatch(tempPerson,firstNames) ) {
					person = tempPerson;
				}
			}
			// more than one match: find a person with matching initials
			else if ( persons.size() > 1 ) {
				Iterator<Person> personIterator = persons.iterator();
				while ( personIterator.hasNext() ) {
					Person tempPerson = personIterator.next();
					if ( initialsMatch(tempPerson,firstNames) ) {
						person = tempPerson;
						break;
					}
				}				
			}	
			if ( person == null ) {
				warn("Couldn't find person "+authorNames[i]+", creating new one");
				person = new Person();				
				person.setLastName(nameParts[0]);
				person.setFirstName(nameParts[1]);
				person.setMiddleName("");
				person.setEmailAddressString("");
				//personHome.save(person);   //-- VG 2010-02-24  These were messing up parent transactions by 
				//personHome.store(person);  //-- VG		     saving too early. 		
			}
			warn(person.getLastName());
			authors.add(person);
		}
		if ( authorsAreEditors ) {
			((BookCitation)citation).setEditors(authors);
		} else {
			citation.setAuthors(authors);
		}
	}
	
	/**
	 * 
	 * 
	 * @param person
	 * @param firstNames
	 * @return
	 */
	private static boolean initialsMatch (Person person, String[] firstNames) {
		if ( TreebaseUtil.isEmpty(person.getMiddleName()) && firstNames.length == 1 && firstNames[0].charAt(0) == person.getFirstName().charAt(0) ) {
			return true;
		}
		else if ( TreebaseUtil.isEmpty(person.getMiddleName()) && firstNames.length > 1 ) {
			String[] personFirstNames = person.getFirstName().split(" ");
			if ( personFirstNames.length != firstNames.length ) {
				return false;
			}
			else {
				for ( int i = 0; i < personFirstNames.length; i++ ) {
					if ( personFirstNames[i].charAt(0) != firstNames[i].charAt(0) ) {
						return false;
					}
				}
				return true;
			}
		}
		else if ( ! TreebaseUtil.isEmpty(person.getMiddleName()) ) {
			StringBuilder fullName = new StringBuilder(person.getFirstName());
			fullName.append(" ").append(person.getMiddleName());
			String[] personFirstNames = fullName.toString().split(" ");
			if ( personFirstNames.length != firstNames.length ) {
				return false;
			}
			else {
				for ( int i = 0; i < personFirstNames.length; i++ ) {
					if ( personFirstNames[i].charAt(0) != firstNames[i].charAt(0) ) {
						return false;
					}
				}
				return true;
			}			
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * 
	 * @param citationType
	 * @return
	 */
	private Citation createCitation (String citationType) {
    	Citation citation;
    	if ( citationType.equals("Journal Article") ) {
    		citation = Citation.factory(ArticleCitation.CITATION_TYPE_ARTICLE);
    	}
    	else if ( citationType.equals("Book Section") ) {
    		citation = Citation.factory(InBookCitation.CITATION_TYPE_BOOKSECTION);
    		  	}
    	else if ( citationType.equals("Book") ) {
    		citation = Citation.factory(BookCitation.CITATION_TYPE_BOOK);
    	}
    	else {
    		warn("Unrecognized citation type: " + citationType);
    		return null;
    	}
    	
    	citation.setCitationStatus(getCitationStatusHome().findStatusInPrep());
    	citation.setId(getCitationService().save(citation));
    	getCurrentSession().flush();

    	return citation;
	}

	/**
	 * 
	 * 
	 * @param studyService
	 * @param tb1StudyId
	 * @return
	 */
	private Study findStudy(String tb1StudyId) {
		Study study = null;
		Iterator<Study> studyIterator = getStudyService().findByTBStudyID(tb1StudyId).iterator();
		if ( studyIterator.hasNext() ) {
			study = studyIterator.next();
		}                			
		return study;
	}
	
	/**
	 * 
	 * 
	 * @param citationAbstract
	 * @return
	 */
	private static String checkAbstractLength(String citationAbstract) {
		int maxLen = TBPersistable.CITATION_ABSTRACT_COLUMN_LENGTH;
		if ( citationAbstract.length() > maxLen ) {
			warn("Abstract is too long, can be only " + maxLen + " characters, but is "+citationAbstract.length());
			citationAbstract = citationAbstract.substring(0, maxLen-3) + "...";
		}
		return citationAbstract;
	}
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {	
		String inFileName = null;
		boolean testmode = false;
		
		if (args.length == 1) {
			inFileName = args[0];
		} else if (args.length == 2) {
			if (args[0].startsWith("-test")) {
				inFileName = args[1];
				testmode = true;
			} else {
				printUsage();
				System.exit(1);
			}
		} else {
			printUsage();
			System.exit(1);
		}
		
		setupContext();
		CitationDataImporterInterface me = (CitationDataImporterInterface) ContextManager.getBean("citationDataImporter");
		BufferedReader in;

		try {
			in = new BufferedReader(
					new InputStreamReader(new FileInputStream(inFileName), "UTF8"));

			//me.processCitationFile(in, testmode);  //VG-2010-02-25 now deprecated
			me.processAllCitations(in, testmode);
		} 
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}	
	
	/** Go through the open citation file and transactionally save each successfully processed citation. 
	 *  Processing stops on error, with prior transactions committed. 
	 *  Derived from the deprecated processCitationsFile by factoring out most of the while-loop code
	 *  into its own method, so that Spring can make it transactional. 
	 */
	public void processAllCitations(BufferedReader in, boolean testMode) throws NumberFormatException, IOException {
		String line;
        while ( ( line = in.readLine()) != null ) {
        	String[] fields = line.split("\t"); 
        	processOneCitation(fields, testMode); 
        }
        in.close();
	}	

	/** Process one citation from the file and save the result transactionally.  
	 *  This method is declared PROPAGATION_REQUIRES_NEW in the bean descriptor.
	 */
	public void processOneCitation(String[] fields, boolean testMode) throws NumberFormatException, IOException {
    	if ( fields[0].startsWith("*") ) {
    		warn("Line is commented out, skipping");
    		return;
    	}
    	String tb1StudyId = fields[18];
    	Study study = findStudy(tb1StudyId);
    	if ( study == null ) {
    		warn("No such study (TB1 accession): " + tb1StudyId);
    		return;
    	}
    	Citation citationStub = study.getCitation();
    	Citation citation = null;
    	if ( citationStub != null ) {
    		warn("Study "+tb1StudyId+" already associated with a citation "+citationStub.getId());
//    		 In general, citation stubs already exist for each study. However, these
//    		 stubs are all of type ArticleCitation, which means that, in the case of
//    		 BookCitations (for example) we'll get a class cast exception when we try
//    		 to cast the citation to a book in order to set the editors.
    		//getCitationService().deleteCitation(citationStub);
    		//citation = createCitation(fields[0]);
    		citation = citationStub;
    	}		
    	else {
    		warn("No citation stub association with study (TB1 accession) " + tb1StudyId);
    		citation = createCitation(fields[0]);
    	}
    	if ( citation == null ) {
    		warn("No citation was instantiated, skipping this record (TB1 accession) "+tb1StudyId);
    		return;
    	}
    	for ( int i = 0; i < fields.length; i++ ) {
    		if ( ! TreebaseUtil.isEmpty(fields[i]) ) {
    			switch(i) {
    				case 0: citation.setCitationType(fields[i]); break;
    				case 1: setAuthors(citation,fields[i],personHome,false); break;
    				case 2: citation.setPublishYear(Integer.parseInt(fields[i])); break;
    				case 3: if ( citation instanceof BookCitation ) { 
    					((BookCitation)citation).setBookTitle(fields[i]);
    					citation.setTitle(fields[i]);
    				} else {
    					citation.setTitle(fields[i]);
    				} break;
    				case 4: setAuthors(citation,fields[i],personHome,true); break;
    				case 5: if ( citation instanceof ArticleCitation ) {
    					((ArticleCitation)citation).setJournal(fields[i]);
    					warn("Setting journal name "+fields[i]);
    				} else {
    					warn("Setting book title "+fields[i]);
    					((BookCitation)citation).setBookTitle(fields[i]);
    				} break;
    				case 6: if ( citation instanceof BookCitation ) {                 					
    					((BookCitation)citation).setCity(fields[i]); 
    				} else {
    					warn("Record has an entry for city, yet it's not a book (TB1 accession) "+tb1StudyId);
    				} break;
    				case 7: if ( citation instanceof BookCitation ) {                 					
    					((BookCitation)citation).setPublisher(fields[i]);
    				} else {
    					warn("Record has an entry for publisher, yet it's not a book (TB1 accession) "+tb1StudyId);
    				} break;
    				case 8: if ( citation instanceof ArticleCitation ) { 
    					((ArticleCitation)citation).setVolume(fields[i]);
    				} else {
    					warn("Record has an entry for volume, yet it's not an article (TB1 accession) "+tb1StudyId);
    				} break;
    				case 10: if ( citation instanceof ArticleCitation ) { 
    					((ArticleCitation)citation).setIssue(fields[i]);
    				} else {
    					warn("Record has an entry for issue, yet it's not an article (TB1 accession)"+tb1StudyId);
    				} break;
    				case 11: citation.setPages(fields[i]); break;
    				case 15: if ( fields[i].equals("in press") ) {
    					citation.setCitationStatusDescription(CitationStatus.INPRESS);                					
    				}
    				case 16: citation.setKeywords(fields[i]); break;
    				case 17: citation.setAbstract(checkAbstractLength(fields[i])); break;
    				case 18: citation.setStudy(study); break;
    				case 19: citation.setURL(fields[i]); break;
    				case 20: citation.setDoi(fields[i]); break;
    				default: warn("Empty field expected, found: " + fields[i] + " for line: " + /*was: line */fields.toString()); break;
    			}
    		}
    	}
    	warn("Storing citation "+citation.getId());
    	warn(citation.getAuthorsCitationStyleWithoutHtml());   
    	if ( ! testMode ) {
        	study.setCitation(citation);
        	getStudyService().update(study);
        	Long id = study.getCitation().getId();
        	warn("Stored citation at ID " + id); 
    	}
    	else {
    		warn("Test run, not saving");
    	}		
	}
	
	/**
	 * Deprecated, use processAllCitations() instead. 
	 * The programmatic transaction handling via the commit(); beginTransaction(); pair at the end of this method
	 * was leaving an open transaction, leading to an exception. Also, the "-test" command line switch had no effect. 
	 * Instead, fine-grained transactions (for each citation) are now achieved by embracing Spring's declarative 
	 * transactions: transactions are at boundaries of designated methods.  VG-2010-02-25
	 */
	@Deprecated
	public void processCitationFile(BufferedReader in, boolean testMode) throws NumberFormatException, IOException {
		String line;
		Long maxId = new Long(0);
        LINE: while ( ( line = in.readLine()) != null ) {
        	String[] fields = line.split("\t"); 
        	if ( fields[0].startsWith("*") ) {
        		warn("Line is commented out, skipping");
        		continue LINE;
        	}
        	String tb1StudyId = fields[18];
        	Study study = findStudy(tb1StudyId);
        	if ( study == null ) {
        		warn("No such study (TB1 accession): " + tb1StudyId);
        		continue LINE;
        	}
        	Citation citationStub = study.getCitation();
        	Citation citation = null;
        	if ( citationStub != null ) {
        		warn("Study "+tb1StudyId+" already associated with a citation "+citationStub.getId());
//        		 In general, citation stubs already exist for each study. However, these
//        		 stubs are all of type ArticleCitation, which means that, in the case of
//        		 BookCitations (for example) we'll get a class cast exception when we try
//        		 to cast the citation to a book in order to set the editors.
        		//getCitationService().deleteCitation(citationStub);
        		//citation = createCitation(fields[0]);
        		citation = citationStub;
        	}		
        	else {
        		warn("No citation stub association with study (TB1 accession) " + tb1StudyId);
        		citation = createCitation(fields[0]);
        	}
        	if ( citation == null ) {
        		warn("No citation was instantiated, skipping this record (TB1 accession) "+tb1StudyId);
        		continue LINE;
        	}
        	for ( int i = 0; i < fields.length; i++ ) {
        		if ( ! TreebaseUtil.isEmpty(fields[i]) ) {
        			switch(i) {
        				case 0: citation.setCitationType(fields[i]); break;
        				case 1: setAuthors(citation,fields[i],personHome,false); break;
        				case 2: citation.setPublishYear(Integer.parseInt(fields[i])); break;
        				case 3: if ( citation instanceof BookCitation ) { 
        					((BookCitation)citation).setBookTitle(fields[i]);
        					citation.setTitle(fields[i]);
        				} else {
        					citation.setTitle(fields[i]);
        				} break;
        				case 4: setAuthors(citation,fields[i],personHome,true); break;
        				case 5: if ( citation instanceof ArticleCitation ) {
        					((ArticleCitation)citation).setJournal(fields[i]);
        					warn("Setting journal name "+fields[i]);
        				} else {
        					warn("Setting book title "+fields[i]);
        					((BookCitation)citation).setBookTitle(fields[i]);
        				} break;
        				case 6: if ( citation instanceof BookCitation ) {                 					
        					((BookCitation)citation).setCity(fields[i]); 
        				} else {
        					warn("Record has an entry for city, yet it's not a book (TB1 accession) "+tb1StudyId);
        				} break;
        				case 7: if ( citation instanceof BookCitation ) {                 					
        					((BookCitation)citation).setPublisher(fields[i]);
        				} else {
        					warn("Record has an entry for publisher, yet it's not a book (TB1 accession) "+tb1StudyId);
        				} break;
        				case 8: if ( citation instanceof ArticleCitation ) { 
        					((ArticleCitation)citation).setVolume(fields[i]);
        				} else {
        					warn("Record has an entry for volume, yet it's not an article (TB1 accession) "+tb1StudyId);
        				} break;
        				case 10: if ( citation instanceof ArticleCitation ) { 
        					((ArticleCitation)citation).setIssue(fields[i]);
        				} else {
        					warn("Record has an entry for issue, yet it's not an article (TB1 accession)"+tb1StudyId);
        				} break;
        				case 11: citation.setPages(fields[i]); break;
        				case 15: if ( fields[i].equals("in press") ) {
        					citation.setCitationStatusDescription(CitationStatus.INPRESS);                					
        				}
        				case 16: citation.setKeywords(fields[i]); break;
        				case 17: citation.setAbstract(checkAbstractLength(fields[i])); break;
        				case 18: citation.setStudy(study); break;
        				case 19: citation.setURL(fields[i]); break;
        				case 20: citation.setDoi(fields[i]); break;
        				default: warn("Empty field expected, found: " + fields[i] + " for line: " + line); break;
        			}
        		}
        	}
        	warn("Storing citation "+citation.getId());
        	warn(citation.getAuthorsCitationStyleWithoutHtml());   
        	if ( ! testMode ) {
	        	study.setCitation(citation);
	        	getStudyService().update(study);
	        	Long id = study.getCitation().getId();
	        	if ( id > maxId ) {
	        		maxId = id;
	        	}
	        	getCurrentSession().getTransaction().commit();
	        	getCurrentSession().beginTransaction();
        	}
        	else {
        		warn("Test run, not saving");
        	}
        }
        in.close();
        //deleteOrphanedCitations(maxId);
	}	
	
	/**
	 * This method was here because in an earlier iteration I had accidentally saved
	 * every record twice - with of each pair one record ending up as an orphan w.r.t.
	 * study objects. Hence, I had to remove the orphans (and I did so by brute force -
	 * but it did the trick).
	 * 
	 * @param maxId
	 */
	private void deleteOrphanedCitations(Long maxId) {
		int duplicates = 0;
		for ( int i = 0; i < maxId.intValue(); i++ ) {
			Long id = new Long(i);
			Citation citation = (Citation)getCitationHome().findPersistedObjectByID(Citation.class, id);
			//warn("Checking id "+id);
			if ( citation != null ) {
				Study study = citation.getStudy();
				if ( study == null ) {
					warn("Removing orphan: "+citation.getId()+" "+citation.getAuthorsCitationStyleWithoutHtml());
					duplicates++;
					getCitationService().deleteCitation(citation);
				}
				else if ( study.getCitation().getId() != citation.getId() ) {
					warn("Orphan: "+citation.getId()+" "+citation.getAuthorsCitationStyle());
				}
				else {
					warn("Makes sense: citation "+citation.getId()+" -study-> "+study.getId());
				}
			}
		}
		warn("I removed "+duplicates+" orphans");
	}

	public PersonHome getPersonHome() {
		return personHome;
	}

	public void setPersonHome(PersonHome personHome) {
		this.personHome = personHome;
	}

	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}
	
	public CitationService getCitationService () {
		return citationService;
	}
	
	public void setCitationService(CitationService citationService) {
		this.citationService = citationService;
	}
	
	public CitationHome getCitationHome() {
		return citationHome;
	}
	
	public void setCitationHome(CitationHome citationHome) {
		this.citationHome = citationHome;
	}
	
	public CitationStatusHome getCitationStatusHome() {
		return citationStatusHome;
	}
	
	public void setCitationStatusHome(CitationStatusHome citationStatusHome) {
		this.citationStatusHome = citationStatusHome;
	}
}
