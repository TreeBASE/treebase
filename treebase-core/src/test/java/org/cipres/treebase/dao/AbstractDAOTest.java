
package org.cipres.treebase.dao;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * AbstractDAOTest.java
 * 
 * Created on Oct 7, 2005
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:applicationContext-db-standalone.xml",
	"classpath:applicationContext-dao.xml",
	"classpath:applicationContext-service.xml"
})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractDAOTest {

	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	protected PlatformTransactionManager transactionManager;
	
	@Autowired
	protected ConfigurableApplicationContext applicationContext;
	
	static protected HibernateTemplate hibernateTemplate;
	protected TransactionStatus transactionStatus;

	private static final String PARSER_STUDY_NAME = "MesquiteTestStudy";
	
	protected org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());


	/**
	 * constructor.
	 */
	public AbstractDAOTest() {
		super();
	}
	
	/**
	 * JUnit 3 compatibility method - override in subclasses if needed for test setup
	 * In JUnit 4, use @Before annotated methods instead
	 */
	protected void onSetUp() throws Exception {
		// Empty implementation for backward compatibility
		// Subclasses can override if needed
	}
	
	/**
	 * JUnit 3 compatibility method - override in subclasses if needed for test setup within a transaction
	 * In JUnit 4, use @Before annotated methods instead (transaction is automatic with @Transactional)
	 */
	protected void onSetUpInTransaction() throws Exception {
		// Empty implementation for backward compatibility
		// Subclasses can override if needed
	}
	
	/**
	 * Initialize hibernate template after Spring dependency injection
	 */
	@Autowired
	public void initializeHibernateTemplate(SessionFactory sessionFactory) {
		if (hibernateTemplate == null) {
			hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	
	/**
	 * Initialize JDBC template from DataSource after Spring dependency injection
	 */
	@Autowired
	public void initializeJdbcTemplate(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Mark transaction for commit instead of rollback
	 */
	protected void setComplete() {
		// In Spring 3.2 with @Transactional, we don't manually manage this
		// The transaction will rollback by default unless we explicitly commit
		// This method is kept for compatibility but does nothing
	}
	
	/**
	 * End the current transaction
	 */
	protected void endTransaction() {
		if (transactionStatus != null && !transactionStatus.isCompleted()) {
			transactionManager.commit(transactionStatus);
			transactionStatus = null;
		}
	}

	/**
	 * Start a new transaction. Only call this method if <code>endTransaction()</code> has been
	 * called. <code>setComplete()</code> can be used again in the new transaction. The fate of
	 * the new transaction, by default, will be the usual rollback.
	 * 
	 * @see #endTransaction()
	 * @see #setComplete()
	 */
	protected void startNewTransaction() {
		if (this.transactionStatus != null) {
			throw new IllegalStateException(
				"Cannot start new transaction without ending existing transaction:"
					+ "Invoke endTransaction() before startNewTransaction()");
		}

		this.transactionStatus = this.transactionManager
			.getTransaction(new DefaultTransactionDefinition());

		if (logger.isInfoEnabled()) {
			logger.info("Started new transaction");
		}
	}

	/**
	 * For test only.
	 * 
	 * Load a persisted object for a given id. Returns null if not found.
	 * 
	 * @param pClass
	 * @param pID
	 * @return
	 */
	public TBPersistable loadObject(Class pClass, Long pID) {

		TBPersistable obj = null;
		try {
			obj = (TBPersistable) hibernateTemplate.get(pClass, pID);
		} catch (DataAccessException ex) {
			obj = null;
		}
		return obj;
	}

	/**
	 * For test only.
	 * 
	 * Load a persisted object. Returns null if not none is found.
	 * 
	 * @param pClass
	 * @return
	 */
	public TBPersistable loadObject(Class pClass) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<? extends TBPersistable> firstObject = session.createCriteria(pClass).setMaxResults(1).list();
		return firstObject.isEmpty() ? null : firstObject.get(0);
	}

	/**
	 * For test only.
	 * 
	 * Load a random persisted object. Returns null if not none is found.
	 * 
	 * @param pClass
	 * @return
	 */
	public TBPersistable loadRandomObject(Class pClass) {

		List<? extends TBPersistable> allObjects = loadAllObject(pClass);
		if (!allObjects.isEmpty()) {
			int size = allObjects.size();
			int randomIndex = (int) Math.random() * size;
			return allObjects.get(randomIndex);
		}
		return null;
	}

	/**
	 * For test only.
	 * 
	 * Load all persisted object for a given class. Return an empty list if none is found.
	 * 
	 * @param pClass
	 * @return
	 */
	public List<? extends TBPersistable> loadAllObject(Class pClass) {

		List obj = new ArrayList();
		obj = hibernateTemplate.loadAll(pClass);

		return obj;
	}

	/**
	 * 
	 * @return
	 */
	protected Study getTestStudy() {
		//Study name: PARSER_STUDY_NAME
		// study id: 1
		return (Study) loadObject(Study.class, 1l);		
	}
}
