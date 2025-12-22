
package org.cipres.treebase.dao;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

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
public abstract class AbstractDAOTest extends AbstractTransactionalDataSourceSpringContextTests {

	static protected HibernateTemplate hibernateTemplate;

	private static final String PARSER_STUDY_NAME = "MesquiteTestStudy";

	/**
	 * constructor.
	 */
	public AbstractDAOTest() {
		super();
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
		// ++this.transactionsStarted;
		// this.complete = !this.defaultRollback;

		if (logger.isInfoEnabled()) {
			// logger.info("Began transaction (" + this.transactionsStarted + "): transaction
			// manager [" +
			// this.transactionManager + "]; default rollback = " + this.defaultRollback);
		}
	}

	/**
	 * 
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpBeforeTransaction()
	 */
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		// SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		// Session s = SessionFactoryUtils.getSession(sessionFactory, true);
		// TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
	}

	/**
	 * 
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onTearDownAfterTransaction()
	 */
	@Override
	protected void onTearDownAfterTransaction() throws Exception {
		super.onTearDownAfterTransaction();

		// SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		// SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
		// .getResource(sessionFactory);
		// Session s = holder.getSession();
		// s.flush();
		// TransactionSynchronizationManager.unbindResource(sessionFactory);
		// SessionFactoryUtils.releaseSession(s, sessionFactory);
	}

	/**
	 * 
	 * 
	 * @param applicationContext
	 */
	private void initializeHibernateTemplate(ConfigurableApplicationContext applicationContext) {
		if (hibernateTemplate == null) {

			SessionFactory sessionFactory = (SessionFactory) applicationContext
				.getBean("sessionFactory");

			hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}

	/**
	 * Override to initialize the hibernate template.
	 * 
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#loadContextLocations(java.lang.String[])
	 */
	@Override
	protected ConfigurableApplicationContext loadContextLocations(String[] pLocations) throws Exception {
		ConfigurableApplicationContext context = super.loadContextLocations(pLocations);

		initializeHibernateTemplate(context);

		return context;
	}

	/**
	 * 
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	@Override
	protected String[] getConfigLocations() {
		return CoreServiceLauncher.getSpringConfigurations();
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
