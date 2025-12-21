package org.cipres.treebase.dao.study;

import java.util.Collection;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * The class <code>AnalyzedDataDAOTest</code> contains tests for the class
 * {@link <code>AnalyzedDataDAO</code>}.
 * 
 * @generatedBy CodePro at 6/29/07 3:28 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class AnalyzedDataDAOTest extends AbstractDAOTest {
	private AnalyzedDataHome mFixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @generatedBy CodePro at 6/29/07 3:28 PM
	 */
	private AnalyzedDataHome getFixture() {
		return mFixture;
	}

	/**
	 * 
	 * 
	 * @param pHome
	 */
	public void setFixture(AnalyzedDataHome pHome) {
		mFixture = pHome;
	}

	/**
	 * Run the AnalyzedDataDAO() constructor test.
	 * 
	 * @generatedBy CodePro at 6/29/07 3:28 PM
	 */
	public void testAnalyzedDataDAO_1() throws Exception {

		String testName = "---AnalyzedDataDAOTest---";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		AnalyzedDataDAO result = new AnalyzedDataDAO();
		// add test code here
		assertNotNull(result);
		assertEquals(null, result.getHibernateTemplate());
		assertEquals(null, result.getSessionFactory());
	}

	/**
	 * Run the Collection<AnalyzedData> findByMatrix(Matrix) method test.
	 * 
	 * @generatedBy CodePro at 6/29/07 3:28 PM
	 */
	public void testFindByMatrix_fixture_1() throws Exception {

		String testName = "testFindByMatrix";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a matrix in a analyzed data:
		String matrixStr = "select matrix_id from analyzedData where matrix_id is not null fetch first rows only";
		long matrixId = jdbcTemplate.queryForLong(matrixStr);
		logger.info("matrix id: " + matrixId);
		assertTrue(matrixId > 0);

		// 2. query
		Matrix m = (Matrix) loadObject(Matrix.class, matrixId);
		assertTrue(m != null);

		Collection<AnalyzedData> data = getFixture().findByMatrix(m);
		assertTrue(data != null && !data.isEmpty());

		// 3. verify
		long dataId = data.iterator().next().getId();
		String sqlStr = "select count(*) from analyzedData where AnalyzedData_id = " + dataId
			+ " and matrix_id = " + m.getId();
		int count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count > 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the Collection<AnalyzedData> findByTree(PhyloTree) method test.
	 * 
	 * @generatedBy CodePro at 6/29/07 3:28 PM
	 */
	public void testFindByTree_fixture_1() throws Exception {
		String testName = "testFindByTree";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a matrix in a analyzed data:
		String treeStr = "select Phylotree_id from analyzedData where Phylotree_id is not null fetch first rows only";
		long treeId = jdbcTemplate.queryForLong(treeStr);
		logger.info("tree id: " + treeId);
		assertTrue(treeId > 0);

		// 2. query
		PhyloTree m = (PhyloTree) loadObject(PhyloTree.class, treeId);
		assertTrue(m != null);

		Collection<AnalyzedData> data = getFixture().findByTree(m);
		assertTrue(data != null && !data.isEmpty());

		// 3. verify
		long dataId = data.iterator().next().getId();
		String sqlStr = "select count(*) from analyzedData where AnalyzedData_id = " + dataId
			+ " and phylotree_id = " + m.getId();
		int count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count > 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

}