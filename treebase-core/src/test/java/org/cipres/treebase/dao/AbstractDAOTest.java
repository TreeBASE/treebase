/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.dao;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * AbstractDAOTest.java
 * 
 * Created on Oct 7, 2005
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("unchecked")
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
			obj = (TBPersistable) hibernateTemplate.load(pClass, pID);
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
