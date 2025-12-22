
package org.cipres.treebase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.Query;
import org.hibernate.impl.SessionImpl;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * EnvrionmentTest.java
 * 
 * Unit test cases for the dev envrionment.
 * 
 * Created on Jun 8, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class EnvironmentTest extends AbstractDAOTest {

	/**
	 * Constructor.
	 */
	public EnvironmentTest() {
		super();
	}

	/**
	 * Test connection pool c3p0 settings.
	 */
	public void testC3p0() throws Exception {
		String testName = "Test C3p0";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		Object obj = applicationContext.getBean("dataSource");
		ComboPooledDataSource ds = (ComboPooledDataSource) obj;

		if (logger.isInfoEnabled()) {
			logger.info("num of connections=" + ds.getNumConnectionsDefaultUser());
			logger.info("num of busy connections=" + ds.getNumBusyConnections());
			logger.info("acquire increment =" + ds.getAcquireIncrement());
			logger.info("driver class=" + ds.getDriverClass());
			logger.info("initial pool size=" + ds.getInitialPoolSize());
			logger.info("max pool size=" + ds.getMaxPoolSize());
			logger.info("min pool size=" + ds.getMinPoolSize());
			logger.info("thread pool size=" + ds.getThreadPoolSize());
			logger.info("max statemetn size=" + ds.getMaxStatements());
			logger.info("num helper threads=" + ds.getNumHelperThreads());

			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test connection pool c3p0 settings.
	 */
	public void testProperty() throws Exception {
		String testName = "Test Setting property in jdbc.properties";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		String prop = System.getenv("mesquite.folder_dir");

		System.out.println("    mesquite.folder_dir= " + prop);

		// assertTrue(prop != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test hql statement whether it is valid.
	 */
	public void testHibernateQueryStatement() throws Exception {
		String testName = "Test hql statement ";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		StringBuffer query = new StringBuffer(
//			"delete from RowSegment r where r in (select r2 from RowSegment r2 join r2.matrixRow row " +
//			"where row.matrix = :matrixId and r2.startIndex between :start and :endIndex and r2.endIndex between :start and :endIndex)");
			"delete from RowSegment where id in (select r2.id from RowSegment r2 join r2.matrixRow row " +
			"where row.matrix = :matrixId and r2.startIndex between :start and :endIndex and r2.endIndex between :start and :endIndex)");

		Query q =  hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query.toString());

		// Important: use setLong() instead of setParameter() !!
		q.setLong("matrixId", 1205);
		q.setInteger("start", 5);
		q.setInteger("endIndex", 30);

		int count = q.executeUpdate();
		
		logger.debug(" delete count=" + count);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
	
	/**
	 * Test hql statement whether it is valid.
	 * postgresql does't support RETURN_GENERATED_KEYS
	 */
	
	public void testGetGeneratedKey() throws Exception {
		String testName = "Test get generated key ";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		StringBuffer query = new StringBuffer(
			"INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?)");
			
		// Note: Session.connection() is deprecated in Hibernate 3.x. We cast to SessionImpl
		// to access the connection() method. While Session.doReturningWork() would be better,
		// it would require restructuring the test. For test code, this is acceptable.
		Connection con =  ((SessionImpl)hibernateTemplate.getSessionFactory().getCurrentSession()).connection();
		String queryBuf = "INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?)";
		// String idQuery = "identity_val_local()";

		long t1 = System.currentTimeMillis();
		
		//PreparedStatement ps = con.prepareStatement(queryBuf, Statement.RETURN_GENERATED_KEYS);
		PreparedStatement ps = con.prepareStatement(queryBuf, Statement.NO_GENERATED_KEYS);
		
		// Important: use setLong() instead of setParameter() !!
		ps.setString(1, "testTestPhylo");

		int count = ps.executeUpdate();
		
		logger.debug(" execute count=" + count);
		
		con.commit();
		
		ResultSet rs = ps.getGeneratedKeys();
		long phyloCharId = -1;
		if (rs != null && rs.next()) {

			phyloCharId = rs.getLong(1); // phylochar_id
		}
		
		long t2 = System.currentTimeMillis();
		rs.close();
		ps.close();
		
		if (logger.isDebugEnabled()) {
			logger.debug("phylocharId =" + phyloCharId + " time=" + (t2-t1));
		}
		assertTrue(phyloCharId <=0);
		
		//delete:
		String deleteStr = "delete from phylochar where phylochar_id = ?";
		ps = con.prepareStatement(deleteStr);
		ps.setLong(1, phyloCharId);
		
		ps.executeUpdate();
		con.commit();
		
		ps.close();
		con.close();
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
	
	/**
	 * Test hql statement whether it is valid.
	 * 
	 * select ... from (insert into...) is more efficient than JDBC getGeneratedKeys in db2. 
	 * (save 50% time)
	 *  
	 *  ** select ... insert uses one sql statement for insert and select. 
	 *  ** JDBC Statement.RETURN_GENERATED_KEYS uses identity_val_local(), needs two statements
	 *   
	 */
	public void testSelectFromInsert() throws Exception {
		String testName = "TestSelectFromInsert";
		//It is the replacement for identity_val_local() in db2 v8.
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		StringBuffer query = new StringBuffer(
			"INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?) RETURNING phylochar_id");
			
		// Note: Session.connection() is deprecated in Hibernate 3.x. We cast to SessionImpl
		// to access the connection() method. While Session.doReturningWork() would be better,
		// it would require restructuring the test. For test code, this is acceptable.
		Connection con =  ((SessionImpl)hibernateTemplate.getSessionFactory().getCurrentSession()).connection();
		//String queryBuf = "INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?)";
		// String idQuery = "identity_val_local()";

		long t1 = System.currentTimeMillis();
		PreparedStatement ps = con.prepareStatement(query.toString());
		
		// Important: use setLong() instead of setParameter() !!
		ps.setString(1, "testTestPhylo");

		ResultSet rs = ps.executeQuery();
		
		//logger.debug(" execute count=" + count);
		
		con.commit();
		
		//ResultSet rs = ps.getGeneratedKeys();
		long phyloCharId = -1;
		if (rs != null && rs.next()) {

			phyloCharId = rs.getLong(1); // phylochar_id
		}
		
		long t2 = System.currentTimeMillis();
		rs.close();
		ps.close();
		
		if (logger.isDebugEnabled()) {
			logger.debug("phylocharId =" + phyloCharId + " time=" + (t2-t1));
		}
		//assertTrue(phyloCharId > 0);
		
		//delete:
		String deleteStr = "delete from phylochar where phylochar_id = ?";
		ps = con.prepareStatement(deleteStr);
		ps.setLong(1, phyloCharId);
		
		ps.executeUpdate();
		con.commit();
		
		ps.close();
		con.close();
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
	
}
