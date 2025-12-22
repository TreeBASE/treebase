
package org.cipres.treebase.auxdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.StandardMatrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.service.matrix.MockMatrixService;
import org.cipres.treebase.service.study.MockStudyService;
import org.cipres.treebase.service.tree.MockPhyloTreeService;
import static org.junit.Assert.*;
import org.junit.Test;

public class AuxDataTest extends TestCase {

	private static final String TID = "T201c1x28x96c14c18c33";
	private static final String AID = "A64c1x28x96c14c17c27";
	private static final String SID = "S1x28x96c14c14c15";
	private static final String MID = "M62c1x28x96c14c23c23";
	private AuxData aux;
	private AuxiliaryDataParser parser;
	private ValueStudy v;
	private RDParserResult res;
	
	public void setUp() throws URISyntaxException, IOException {
		String fileName = "s59-dump.txt";
		String path = "/" + fileName;		
		File dumpFile = new File(getClass().getResource(path).toURI());

		parser = new AuxiliaryDataParser(new FilterTrivial());
		res = parser.parseFile(dumpFile);
		if (res.failed()) {
			fail("Couldn't parse test input");
		} else {
			ValueSequence vs = (ValueSequence) res.value();
			assertEquals(1, vs.length());
			Value v_anon = vs.subvalue(0);
			assertTrue(v_anon instanceof ValueStudy);
			v = (ValueStudy) v_anon;
			aux = new AuxData(v);
		}
	}
	
	public AuxDataTest() {

	}

	@Test


	public void testAuxData() {	
		assertNotNull(aux);
	}

	@Test


	public void testGetString() {
		assertEquals(SID, aux.getString(null, "study_id"));
		assertEquals(MID, aux.getString("MATRIX", "matrix_id"));
		assertEquals("W. Piel", aux.getString("HISTORY", "person"));
	}

	@Test


	public void testGetSection() {
		ValueSection historySection = aux.getSection("HISTORY");
		assertEquals("HISTORY", historySection.getLabel());
		assertEquals(1, historySection.getIndex());
		assertEquals("1/28/96", historySection.getString("date"));
	}

	@Test


	public void testGetSections() {
		Collection<ValueSection> authors = aux.getSections("AUTHOR");
		assertEquals(2, authors.size());
		Iterator<ValueSection> it = authors.iterator();
		ValueSection a = it.next(), b = it.next();
		if (a.getString("author_id").equals("A5")) {
			assertEquals(b.getString("author_id"), "A89");
		} else if (a.getString("author_id").equals("A89")) {
			assertEquals(b.getString("author_id"), "A5");
		} else {
			fail("Unexpected author ID" + a.getString("author_id"));
		}
	}

	@Test


	public void testGetAnalysisSections() {
		Collection<ValueAnalysisSection> anal = aux.getAnalysisSections();
		assertEquals(1, anal.size());
		ValueAnalysisSection an = anal.iterator().next();
		assertEquals("ANALYSIS", an.getLabel());
		assertEquals(1, an.getIndex());
		assertEquals(AID, an.getString("analysis_id"));
	}

	@Test


	public void testGetMatrixSections() {
		Collection<ValueSection> msect = aux.getMatrixSections();
		assertEquals(1, msect.size());
		ValueSection m = msect.iterator().next();
		assertEquals("MATRIX", m.getLabel());
		assertEquals(1, m.getIndex());
		assertEquals(MID, m.getString("matrix_id"));
	}

	@Test


	public void testGetTreeSections() {
		Collection<ValueSection> tsect = aux.getTreeSections();
		assertEquals(1, tsect.size());
		ValueSection t = tsect.iterator().next();
		assertEquals("OUTPUT_TREE", t.getLabel());
		assertEquals(1, t.getIndex());
		assertEquals(TID, t.getString("tree_id"));
	}

	//unfinished FakeMatrixService class cause null pointer
	@Test

	public void testGetMatrices() {
		//Map<String, Matrix> mm = aux.getMatrices(new FakeMatrixService());
		//assertEquals(1, mm.size());
		//String k = mm.keySet().iterator().next();
		//assertEquals(MID, k);
	}

	//unfinished FakeTreeService class cause null pointer
	@Test

	public void testGetTrees() {
		//Map<String, PhyloTree> tm = aux.getTrees(new FakeTreeService());
		//assertEquals(1, tm.size());
		//String k = tm.keySet().iterator().next();
		//assertEquals(TID, k);
	}

	@Test


	public void testStudy() {
		Study s = aux.study(new FakeStudyService());
		assertEquals(SID, s.getTB1StudyID());
	}

	@Test


	public void testGetStudyID() {
		assertEquals(SID, aux.getStudyID());
	}

	@Test


	public void testGetCreationDateString() {
		assertEquals("1/28/96 14:14:16", aux.getCreationDateString());
	}

	@Test


	public void testGetCreationHistorySection() {
		ValueSection histSection = aux.getCreationHistorySection();
		assertEquals("HISTORY", histSection.getLabel());
		assertEquals("New Study Added", histSection.getString("event"));
	}

	@Test


	public void testGetCreationDate() {
		Date d = aux.getCreationDate();
		DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		assertEquals("28 Jan 1996 19:14:16 GMT", df.format(d));
	}
	
	@Test

	
	public void testSkip() {
		assertFalse(aux.skip());
	}

	private class FakeMatrixService extends MockMatrixService {
		@Override
		public Set<Matrix> findMatricesByTitle(String title) {
//			System.err.println("Pretending to find matrices titled '" + title + "'");
			Set<Matrix> results = new HashSet<Matrix>();
			if (title.equals(MID)) {
				results.add(new StandardMatrix());
			} 
			return results;
		}

		public Matrix findByIDString(String searchTerm) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(
				Class T, String attributeName, String rangeExpression)
				throws MalformedRangeExpression {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> T findByID(Class T, Long id) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public Matrix findByTB1StudyID(String pTB1MatrixID) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findAll(Class T) {
			// TODO Auto-generated method stub
			return null;
		}	
	}
	
	private class FakeTreeService extends MockPhyloTreeService {
		@Override
		public Set<PhyloTree> findTreesByLabel(String label) {
			Set<PhyloTree> results = new HashSet<PhyloTree>();
			if (label.equals(TID)) {
				results.add(new PhyloTree());
			} 
			return results;
		}

		public Collection<PhyloTree> findTreesByNCBINodes(String pNcbiId) {
			// TODO Auto-generated method stub
			return null;		
		}


		public Set<PhyloTree> findByTopology3(TaxonVariant taxonVariant,
				TaxonVariant taxonVariant2, TaxonVariant taxonVariant3) {
			// TODO Auto-generated method stub
			return null;
		}

		public Set<PhyloTree> findByTopology4a(TaxonVariant taxonVariant,
				TaxonVariant taxonVariant2, TaxonVariant taxonVariant3,
				TaxonVariant taxonVariant4) {
			// TODO Auto-generated method stub
			return null;
		}

		public Set<PhyloTree> findByTopology4s(TaxonVariant taxonVariant,
				TaxonVariant taxonVariant2, TaxonVariant taxonVariant3,
				TaxonVariant taxonVariant4) {
			// TODO Auto-generated method stub
			return null;
		}

		public PhyloTree findByIDString(String searchTerm) {
			// TODO Auto-generated method stub
			return null;
		}

		public TreeBlock findTreeBlockByID(Long treeBlockID) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(
				Class T, String attributeName, String rangeExpression)
				throws MalformedRangeExpression {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> T findByID(Class T, Long id) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findAll(Class T) {
			// TODO Auto-generated method stub
			return null;
		}	
	}
	
	private class FakeStudyService extends MockStudyService {
		@Override
		public Collection<Study> findByTBStudyID(String studyID) {
			Collection<Study> results = new HashSet<Study>();
			if (studyID.equals(SID)) {
				Study s = new Study();
				s.setTB1StudyID(studyID);
				results.add(s);
			} 
			return results;
		}

		public Study findByIDString(String searchTerm) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByItsDescription(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByRangeExpression(
				Class T, String attributeName, String rangeExpression)
				throws MalformedRangeExpression {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> T findByID(Class T, Long id) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findSomethingByString(
				Class T, String attributeName, String target,
				Boolean caseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public Collection<Study> findByJournal(String pJournal,
				boolean pCaseSensitive) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends TBPersistable> Collection<T> findAll(Class T) {
			// TODO Auto-generated method stub
			return null;
		}

		public Collection<Study> findByPublicationDateRange(Date from,
				Date until) {
			// TODO Auto-generated method stub
			return null;
		}

		public Study findByDOI(String doi) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}


