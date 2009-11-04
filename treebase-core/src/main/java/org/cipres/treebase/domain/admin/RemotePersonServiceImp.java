package org.cipres.treebase.domain.admin;

import java.util.List;
import java.util.ArrayList;

/**
 * @author madhu
 * 
 */
public class RemotePersonServiceImp {

	public RemotePersonServiceImp() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.admin.RemotePersonService#findCompleteEmailAddress(java.lang.String)
	 */
	public List<String> findCompleteEmailAddress(String pPartEmailAddress) {
		List<String> alist = new ArrayList<String>();

		// go through PersonDAO

		alist.add("t@gmail.com");
		alist.add("t@gotmail.com");
		alist.add("t@fotmail.com");
		alist.add("t@botmail.com");
		alist.add("t@aotmail.com");
		alist.add("t@cotmail.com");
		alist.add("t@dotmail.com");
		alist.add("t@eotmail.com");
		return alist;
	}

}
