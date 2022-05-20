package org.cipres.treebase.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import org.cipres.treebase.ContextManager;

/**
 * The class <code>CoreServiceLauncherTest</code> contains tests for the class
 * {@link <code>CoreServiceLauncher</code>}.
 * 
 * @generatedBy CodePro at 9/27/05 9:50 AM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class CoreServiceLauncherTest extends TestCase {
	private static final Logger LOGGER = LogManager.getLogger(CoreServiceLauncherTest.class);

	/**
	 * An instance of the class being tested.
	 * 
	 * @see CoreServiceLauncher
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	private CoreServiceLauncher mFixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see CoreServiceLauncher
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	public CoreServiceLauncher getFixture() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getFixture() - start"); //$NON-NLS-1$
		}

		if (mFixture == null) {
			mFixture = new CoreServiceLauncher();
			ContextManager.createSpringContext(CoreServiceLauncher.getSpringConfigurations());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getFixture() - end"); //$NON-NLS-1$
		}
		return mFixture;
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception if the initialization fails for some reason
	 * 
	 * @see TestCase#setUp()
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	@Override
	protected void setUp() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setUp() - start"); //$NON-NLS-1$
		}

		super.setUp();
		// Add additional set up code here

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setUp() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception if the clean-up fails for some reason
	 * 
	 * @see TestCase#tearDown()
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	@Override
	protected void tearDown() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("tearDown() - start"); //$NON-NLS-1$
		}

		super.tearDown();
		// Add additional tear down code here

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("tearDown() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Launch the test.
	 * 
	 * @param args the command line arguments
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		junit.textui.TestRunner.run(CoreServiceLauncherTest.class);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Run the CoreServiceLauncher() constructor test.
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	public void testCoreServiceLauncher_1() throws Exception {

		String testName = "CoreServiceLauncher_1";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\nRunning Test: " + testName);
		}

		CoreServiceLauncher result = new CoreServiceLauncher();
		// add test code here
		assertNotNull(result);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the CoreServiceLauncher() constructor test.
	 * 
	 * @generatedBy CodePro at 9/27/05 10:24 AM
	 */
	public void testJDBCConnection() throws Exception {
		String testName = "JDBCConnection";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\nRunning Test: " + testName);
		}

		// initialize the spring context
		getFixture();

		SessionFactory sessionFactory = (SessionFactory) ContextManager.getBean("sessionFactory");
		DataSource ds = SessionFactoryUtils.getDataSource(sessionFactory);
		Connection conn = ds.getConnection();

		DatabaseMetaData dmd = conn.getMetaData();
		String conInfo = dmd.getDriverName() + " " + dmd.getDriverVersion();

		LOGGER.info("Connection: " + conInfo);

		// important: Close sessionFactory:
		// otherwise all tests in dao package will fail.
		sessionFactory.close();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the String[] getSpringConfigurations() method test.
	 * 
	 * @generatedBy CodePro at 9/27/05 9:50 AM
	 */
	public void xxtestGetSpringConfigurations_fixture_1() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("testGetSpringConfigurations_fixture_1() - start"); //$NON-NLS-1$
		}

		// String[] result = CoreServiceLauncher.getSpringConfigurations();
		// add test code here
		// TODO assertNotNull(result);
		// assertEquals(3, result.length);
		// assertEquals("applicationContext.xml", result[0]);
		// assertEquals("applicationContext-dao.xml", result[1]);
		// assertEquals("applicationContext-service.xml", result[2]);
		// fail("unverified");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("testGetSpringConfigurations_fixture_1() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Run the String[] getSpringConfigurations() method test.
	 * 
	 * @generatedBy CodePro at 9/27/05 10:24 AM
	 */
	public void xxtestGetSpringConfigurations_fixture_2() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("testGetSpringConfigurations_fixture_2() - start"); //$NON-NLS-1$
		}

		// String[] result = fixture.getSpringConfigurations();
		// add test code here
		// TODO assertNotNull(result);
		// assertEquals(3, result.length);
		// assertEquals("applicationContext.xml", result[0]);
		// assertEquals("applicationContext-dao.xml", result[1]);
		// assertEquals("applicationContext-service.xml", result[2]);
		// fail("unverified");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("testGetSpringConfigurations_fixture_2() - end"); //$NON-NLS-1$
		}
	}
}
