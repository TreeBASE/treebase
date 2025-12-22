package org.cipres.treebase.domain.admin;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.dao.admin.UserDAO;
import org.cipres.treebase.domain.study.Submission;
import static org.junit.Assert.*;

/**
 * The class <code>UserTest</code> contains tests for the class {@link <code>User</code>}.
 * 
 * @generatedBy CodePro at 10/6/05 1:26 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class UserTest extends AbstractDAOTest {

	private UserHome mUserHome;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * @see UserDAO
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Injected by the super class based on autowiring by type.
	 * 
	 * @param pUserDAO
	 */
	@Autowired
	public void setUserHome(UserHome pUserDAO) {
		mUserHome = pUserDAO;
	}

	/**
	 *  TODO: Too slow.  20090528 MJD
	 */
	public void testgetInProgressSubmissions() throws Exception {
		String testName = "getInProgressSubmissions";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a user with submission:
		int inprogressSubCount = 0;
		List<Submission> subs = (List<Submission>) loadAllObject(Submission.class);
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", subs);
		
		if (!subs.isEmpty()) {
			for (Submission sub : subs) {
				if (sub.isInProgress()) {
					inprogressSubCount++;
					User user = sub.getSubmitter();

					Set<Submission> inprogressSubs = user.getInProgressSubmissions();
					assertTrue("in progress submission is null", !inprogressSubs.isEmpty());
				}
			}

			// 3. verify
			assertTrue("Failed to get any in progress submission.", inprogressSubCount > 0);
			logger.info("in progress submitter tested: " + inprogressSubCount);

			if (logger.isInfoEnabled()) {
				logger.info(testName + " - end "); //$NON-NLS-1$
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

}
