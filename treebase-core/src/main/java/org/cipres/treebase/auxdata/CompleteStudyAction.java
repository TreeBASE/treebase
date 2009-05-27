package org.cipres.treebase.auxdata;

import java.util.Set;

import org.hibernate.SessionFactory;


public interface CompleteStudyAction extends Action {
	public void setIsForTesting(boolean ift);
	public boolean isForTesting();
	public SessionFactory getSessionFactory();
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public Set<String> getInterestingLegacyIDs();
	public void setInterestingLegacyIDs(Set<String> interestingLegacyIDs);
	public void clearInterestingLegacyIDs();
}
