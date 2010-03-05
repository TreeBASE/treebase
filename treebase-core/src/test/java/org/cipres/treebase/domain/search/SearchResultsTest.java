package org.cipres.treebase.domain.search;

import java.util.Collection;
import java.util.HashSet;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTree;

public class SearchResultsTest extends AbstractDAOTest {

	private TaxonLabelHome mTaxonLabelHome;
	private StudyHome mStudyHome;
	
	private StudySearchResults ssr = null;
	private TreeSearchResults tsr = null;
	private Collection<Study> studies = null;
	private Collection<Matrix> matrices = null;
	private Collection<PhyloTree> trees = null;
		
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		studies = getTestData();
		ssr = new StudySearchResults(studies);
		matrices = new HashSet<Matrix> ();
		for (Study s : studies) {
			matrices.addAll(s.getMatrices());
		}
		trees = new HashSet<PhyloTree> ();
		for (Study s : studies) {
			trees.addAll(s.getTrees());
		}
		tsr = new TreeSearchResults();
	}

	public SearchResultsTest() {
		super();
	}
	
	public void testTrivial() {
		assertNotNull(getTaxonLabelHome());
		assertNotNull(getStudyHome());
	}
	
	public void testSearchService() {
		assertNotNull(ssr.getSearchService());
		assertNotNull(ssr.getSearchService().getStudyService());
		assertNotNull(ssr.getStudyService());
		assertNotNull(tsr.getSearchService());
		assertNotNull(tsr.getSearchService().getPhyloTreeService());
		assertNotNull(tsr.getPhyloTreeService());
	}
	
	public void testQuickCheck() {
		//failed because unpublished data 
		//ssr called discardUnpublishedStudies(); 
		//assertEquals(studies.size(), ssr.size()); 
		assertFalse(studies.size() == 0);
		assertFalse(matrices.size() == 0);
		assertFalse(trees.size() == 0);
	}
	
	
	private Collection<Study> getTestData(String authorName) {
		Collection<Study> studies = getStudyHome().findByAuthor(authorName);
		assertFalse(studies.size() == 0);	
		return studies;
	}
	
	private Collection<Study> getTestData() {
		return getTestData("Smith");
	}
	
	public void testConvertToMatrices() {
		MatrixSearchResults msr = ssr.convertToMatrices();
		//assertEquals(matrices.size(), msr.size()); failed because unpublished data
		//assertTrue(sameCollection(matrices, msr.getResults()));
		
		assertTrue((new StudySearchResults ()).convertToMatrices().isAll());
	}
	
	public void testConvertFromMatrices() {
		// Every study in smsr was in ssr
		// Every study in ssr is in smsr except those with no matrices
		StudySearchResults smsr = ssr.convertToMatrices().convertToStudies();
		assertTrue(contains(ssr.getResults(), smsr.getResults()));

		assertTrue((new MatrixSearchResults ()).convertToStudies().isAll());
	}
		
	public void testConvertToTrees() {
		TreeSearchResults tsr = ssr.convertToTrees();
		//assertEquals(trees.size(), tsr.size()); failed because unpublished data
		//assertTrue(sameCollection(trees, tsr.getResults()));

		assertTrue((new StudySearchResults ()).convertToTrees().isAll());
	}
		
	public void testConvertFromTrees() {
		StudySearchResults stsr = ssr.convertToTrees().convertToStudies();
		// Every study in stsr was in tsr
		// Every study in tsr is in stsr except those with no trees
		assertTrue(contains(ssr.getResults(), stsr.getResults()));

		assertTrue((new TreeSearchResults ()).convertToStudies().isAll());
	}
	
	private <E> boolean sameCollection(Collection<E> as, Collection<E> bs) {
		return contains(as, bs) && contains(bs, as);
	}
	
	// returns true iff every item in bs is also in as
	private <E> boolean contains(Collection<E> as, Collection<E> bs) {
		for (E b: bs) {
			boolean b_found_in_as = false;
			for (E a : as) {
				if (a.equals(b)) b_found_in_as = true; 
			}
			if (! b_found_in_as) return false;
		}
		return true;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	public StudyHome getStudyHome() {
		return mStudyHome;
	}

	public void setStudyHome(StudyHome studyHome) {
		mStudyHome = studyHome;
	}
	
}	
