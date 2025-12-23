package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DoSQL extends AbstractStandalone {
	public static void main(String[] args) {
		setupContext();
		DoSQL me = new DoSQL();
		String sql = join(args, " ");
		
		HibernateTransactionManager htm = ContextManager.getTransactionManager();
		TransactionDefinition transDef = new DefaultTransactionDefinition();
		TransactionStatus transStatus = htm.getTransaction(transDef);
		SessionFactory sf = ContextManager.getSessionFactory();
		Session sess = sf.getCurrentSession();
		SQLQuery q = sess.createSQLQuery(sql);
		q.executeUpdate();
		sess.flush();
		sess.getTransaction().commit();
		sess.close();
	}
	
	static String join(String[] ss, String sep) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : ss) {
			if (! first) sb.append(sep);
			sb.append(s);
			first = false;
		}
		return sb.toString();
	}
}
