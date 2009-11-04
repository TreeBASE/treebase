
package org.cipres.treebase.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.auxdata.AuxData;
import org.cipres.treebase.auxdata.AuxiliaryDataParser;
import org.cipres.treebase.auxdata.Filter;
import org.cipres.treebase.auxdata.RDParserResult;
import org.cipres.treebase.auxdata.Value;
import org.cipres.treebase.auxdata.ValueNone;
import org.cipres.treebase.auxdata.ValueSection;
import org.cipres.treebase.auxdata.ValueStudy;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.Session;
import org.hibernate.Transaction;

class RepatriateStudyAuthors implements Filter {
	Session session;
	PrintStream out;

	public RepatriateStudyAuthors(Session session, PrintStream out) {
		this.session = session;
		this.out = out;
	}

	public Value perform(Value v) {
		AuxData aux = new AuxData((ValueStudy) v);
		Study s;
		{
			String studyId = aux.getStudyID();
			Collection<Study> studies = ContextManager.getStudyService().findByTBStudyID(studyId);
			if (studies.size() == 0) {
				out.println("Couldn't locate study " + studyId + "; skipping");
				return new ValueNone();
			} else if (studies.size() > 1) {
				out.println("Multiple studies with ID " + studyId + "; skipping");
				return new ValueNone();
			}
			
			Study[] tmp = { null };
			studies.toArray(tmp);
			s = tmp[0];

			out.println("Processing study " + studyId);
		}
		
		Transaction t = session.beginTransaction();
		
		Citation cit = s.getCitation();
		cit.setAuthors(new ArrayList<Person>());
		ContextManager.getCitationHome().flush();
		
		for (ValueSection authorSection : aux.getSections("AUTHOR")) {
			Person theAuthor = new Person();
			theAuthor.setFirstName(authorSection.getsval("first_name"));
			theAuthor.setLastName(authorSection.getsval("last_name"));
			Person oldAuthor = ContextManager.getPersonService().findByExactMatch(theAuthor);
			if (oldAuthor == null) {
				out.println("Can't find person " + theAuthor.getFullName() + "; skipping");
				t.rollback();
				return new ValueNone();
			}
			out.println("  Adding author " + oldAuthor.getFullName() + "(" + oldAuthor.getId() + ")");
			cit.addAuthor(oldAuthor);
		}
		t.commit();
		return v;
	}
	
}

class RepatriateAuthors extends AbstractStandalone {
	
	public static void main(String[] args) throws IOException {
		File f;
		RepatriateAuthors me = new RepatriateAuthors();
		me.setupContext();

		switch (args.length) {
		case 0:
			f = new File("/home/mjd/trunk/treebase-data/Dump.txt");
			break;
		case 1:
			f = new File(args[0]);
			break;
		default:
			throw new RuntimeException("Usage: program [inputFilename]");
		}
		
		Filter repatriateStudyAuthors = new RepatriateStudyAuthors(me.getSession(), System.out);
		AuxiliaryDataParser parser = new AuxiliaryDataParser(repatriateStudyAuthors);
		RDParserResult result = parser.parseFile(f);
		parser.reportResults(System.err, result);
		System.exit(result.success() ? 0 : 1);
	}
	
}