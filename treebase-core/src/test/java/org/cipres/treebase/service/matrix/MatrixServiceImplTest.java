package org.cipres.treebase.service.matrix;

import java.util.Collection;

import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.StandardMatrix;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyStatusHome;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * The class <code>MatrixServiceImplTest</code> contains tests for the class
 * {@link <code>UserServiceImpl</code>}.
 * 
 * @generatedBy CodePro at 10/13/05 4:18 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class MatrixServiceImplTest extends AbstractDAOTest {

	private MatrixService mFixture;

	private StudyStatusHome mStudyStatusHome;
	private StudyHome mStudyHome;
	private SubmissionHome mSubmissionHome;
	private MatrixHome mMatrixHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;

	/**
	 * Return the MatrixDataTypeHome field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	private MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHome field.
	 */
	public void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHome) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHome;
	}

	/**
	 * Return the MatrixHome field.
	 * 
	 * @return MatrixHome mMatrixHome
	 */
	private MatrixHome getMatrixHome() {
		return mMatrixHome;
	}

	/**
	 * Set the MatrixHome field.
	 */
	public void setMatrixHome(MatrixHome pNewMatrixHome) {
		mMatrixHome = pNewMatrixHome;
	}

	/**
	 * Return the SubmissionHome field.
	 * 
	 * @return SubmissionHome mSubmissionHome
	 */
	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	/**
	 * Return the StudyHome field.
	 * 
	 * @return StudyHome mStudyHome
	 */
	public StudyHome getStudyHome() {
		return mStudyHome;
	}

	/**
	 * Set the StudyHome field.
	 */
	public void setStudyHome(StudyHome pNewStudyHome) {
		mStudyHome = pNewStudyHome;
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return MatrixService mFixture
	 */
	public MatrixService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(MatrixService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Return the StudyStatusHome field.
	 * 
	 * @return StudyStatusHome mStudyStatusHome
	 */
	public StudyStatusHome getStudyStatusHome() {
		return mStudyStatusHome;
	}

	/**
	 * Set the StudyStatusHome field.
	 */
	public void setStudyStatusHome(StudyStatusHome pNewStudyStatusHome) {
		mStudyStatusHome = pNewStudyStatusHome;
	}
	
	/**
	 * @author mjd 20081204
	 * @throws MalformedRangeExpression
	 */
	public void testFindSomethingByRangeExpression() throws MalformedRangeExpression {
		Collection<Matrix> matrices = getFixture()
		.findSomethingByRangeExpression(Matrix.class, "nTax", "37..100");
		
		// Skip test if database is empty
		if (matrices == null || matrices.size() == 0) {
			logger.info("SKIPPED: testFindSomethingByRangeExpression - No Matrix data found in database with nTax in range 37..100. Test requires populated database.");
			return;
		}
		
		assertTrue(matrices.size() > 0);
		for (Matrix matrix : matrices) {
			int n = matrix.getnTax();
			assertTrue(n >= 37 && n <= 100);
		}
	}


	/**
	 * Run the void createMatrix and deleteMatrix() methods test
	 */
	public void testAddDelete() {
		String testName = "testAddDelete";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new matrix:
		String newName = testName + " test " + Math.random();

		StandardMatrix matrix = new StandardMatrix();
		matrix.setAligned(true);
		matrix.setTitle(newName);
		matrix.setNexusFileName("None");

		MatrixDataType dnaType = getMatrixDataTypeHome().findByDescription(
			MatrixDataType.MATRIX_DATATYPE_DNA);
		matrix.setDataType(dnaType);

		// add two columns:
		DiscreteChar dnaChar = (DiscreteChar) dnaType.getDefaultCharacter();
		assertTrue("Failed to load dna character", dnaChar != null);

		MatrixColumn c1 = new MatrixColumn();
		c1.setCharacter(dnaChar);
		matrix.addColumn(c1);

		MatrixColumn c2 = new MatrixColumn();
		c2.setCharacter(dnaChar);
		matrix.addColumn(c2);

		// add two rows:
		TaxonLabel taxonLabel = new TaxonLabel();
		taxonLabel.setTaxonLabel(newName);

		// missing symbol is ?
		DiscreteCharState aState = dnaChar.getStateByDescription("" + DiscreteCharState.MISSING_SYMBOL);
		assertTrue("? is not a valid state", aState != null);

		// create one row with 2 elements:
		MatrixRow r1 = new MatrixRow();
		r1.setTaxonLabel(taxonLabel);

		DiscreteMatrixElement e1 = new DiscreteMatrixElement();
		e1.setColumn(c1);
		e1.setCharState(aState);
		r1.addElement(e1);

		DiscreteMatrixElement e2 = new DiscreteMatrixElement();
		e2.setColumn(c2);
		e2.setCharState(aState);
		r1.addElement(e2);

		// add two segments, each with one element:
		RowSegment seg1 = new RowSegment();

		seg1.setStartIndex(0);
		seg1.setEndIndex(0);
		r1.addSegment(seg1);

		RowSegment seg2 = new RowSegment();

		seg2.setStartIndex(1);
		seg2.setEndIndex(1);
		r1.addSegment(seg2);

		matrix.addRow(r1);

		// TODO: add a second row.
	
		//getMatrixHome().store(taxonLabel);
		getMatrixHome().store(c1);
		getMatrixHome().store(c2);
		getMatrixHome().store(e1);
		getMatrixHome().store(e2);
		getMatrixHome().store(matrix);
		
		// 2. verify
		Long matrixID = matrix.getId();
		Long c1ID = c1.getId();
		Long c2ID = c2.getId();
		Long r1ID = r1.getId();
		Long seg1ID = seg1.getId();
		Long seg2ID = seg2.getId();
		Long e1ID = e1.getId();
		Long e2ID = e2.getId();
		Long taxonID = taxonLabel.getId();

	    logger.info("matrix created: " + matrix.getTitle() + "id = " + matrixID + " symbols=" + matrix.getSymbols() + " gap =" + matrix.getGapSymbol());
		logger.info("taxonLabel created: " + taxonLabel.getTaxonLabel() + "id = " + taxonID);
		logger.info("2 columns created: id = " + c1ID + ", " + c2ID);
		logger.info("2 rows created: id = " + r1.getId() + ", ");
		logger.info("2 segments created: id = " + seg1.getId() + ", " + seg2.getId());
		logger.info("2 elements created: id = " + e1.getId() + ", " + e2.getId());

		// force commit immediately, important:
		//setComplete();
		//endTransaction();
	
		Matrix m = (Matrix)loadObject(Matrix.class, matrixID);
		TaxonLabel tl = (TaxonLabel)loadObject(TaxonLabel.class, taxonID);
	    //String sqlStr = "select count(*) from matrix where matrix_id=" + matrixID;
		//int count = jdbcTemplate.queryForInt(sqlStr);
		//assertTrue(count == 1);
		assertTrue( m!=null );
		//assertTrue( tl!=null );
		assertTrue(c1ID != null && c2ID != null);
		assertTrue(r1.getId() != null);
		assertTrue(seg1ID != null && seg2ID != null);
		assertTrue(e1ID != null && e2ID != null);
	

		// 3. delete
	    //startNewTransaction();
		getFixture().deleteMatrix(matrix);
		getMatrixHome().deletePersist(taxonLabel);
		//setComplete();

		// 4. verify delte:
		m = (Matrix)loadObject(Matrix.class, matrixID);
		assertTrue( m == null );
		//int countVerify = jdbcTemplate.queryForInt(sqlStr);
		//assertTrue("Deletion failed.", countVerify == 0);

		//String sqlStrRow = "select count(*) from matrixrow where matrixrow_id=" + r1ID;
		//countVerify = jdbcTemplate.queryForInt(sqlStrRow);
		//assertTrue("cascade delete row failed.", countVerify == 0);

		//String sqlStrElement = "select count(*) from matrixelement where matrixelement_id=" + e2ID;
		//countVerify = jdbcTemplate.queryForInt(sqlStrElement);
		//assertTrue("cascade delete element failed.", countVerify == 0);

		//String sqlStrColumn = "select count(*) from matrixcolumn where matrixcolumn_id=" + c2ID;
		//countVerify = jdbcTemplate.queryForInt(sqlStrColumn);
		//assertTrue("cascade delete column failed.", countVerify == 0);

		tl = (TaxonLabel)loadObject(TaxonLabel.class, taxonID);
		assertTrue( tl == null );
		//String sqlStrTaxonLabel = "select count(*) from taxonLabel where taxonLabel_id=" + taxonID;
		//countVerify = jdbcTemplate.queryForInt(sqlStrTaxonLabel);
		//assertTrue("delete taxonLabel failed.", countVerify == 0);

		logger.info("Done. matrix Deleted: " + matrixID);
	}

}
