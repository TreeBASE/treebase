package org.cipres.treebase.service.admin;

import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * The class <code>UserServiceImplTest</code> contains tests for the class
 * {@link <code>UserServiceImpl</code>}.
 * 
 * @generatedBy CodePro at 10/13/05 4:18 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class UserServiceImplTest extends TestCase {
	private static final Logger LOGGER = Logger.getLogger(UserServiceImplTest.class);

	private UserServiceImpl mFixture;
	private User mStoredUser;
	private User mNewUser;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see UserServiceImpl
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public UserServiceImpl getFixture() throws Exception {
		if (mFixture == null) {
			mFixture = new UserServiceImpl();

			DummyUserDAO dummyDAO = new DummyUserDAO();
			mFixture.setUserHome(dummyDAO);

			setStoredUser(dummyDAO.getStoredUser());
			setNewUser(dummyDAO.getNewUser());
		}
		return mFixture;
	}

	/**
	 * Return the NewUser field.
	 * 
	 * @return User mNewUser
	 */
	private User getNewUser() {
		return mNewUser;
	}

	/**
	 * Set the NewUser field.
	 */
	private void setNewUser(User pNewNewUser) {
		mNewUser = pNewNewUser;
	}

	/**
	 * Return the DummyUser field.
	 * 
	 * @return User
	 */
	private User getStoredUser() {
		return mStoredUser;
	}

	/**
	 * Set the DummyUser field.
	 */
	private void setStoredUser(User pNewDummyUser) {
		mStoredUser = pNewDummyUser;
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception if the initialization fails for some reason
	 * 
	 * @see TestCase#setUp()
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception if the clean-up fails for some reason
	 * 
	 * @see TestCase#tearDown()
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// Add additional tear down code here
	}

	public void testDomainHome() throws Exception {
		assertNotNull(getFixture().getDomainHome());
	}
	
	/**
	 * Run the UserServiceImpl() constructor test.
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public void testConstructor() throws Exception {
		String testName = "Constructor";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n\t\tRunning Test: " + testName);
		}

		UserServiceImpl result = new UserServiceImpl();
		assertNotNull(result);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void createUser(User) method test.
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public void testCreateUser() throws Exception {
		String testName = "CreateUser";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n\t\tRunning Test: " + testName);
		}

		UserServiceImpl fixture = getFixture();

		// 1. test do not create existing user:
		User user = getStoredUser();
		ExecutionResult result = fixture.createUser(user);
		assertTrue("user already exists.", result.isSuccessful() == false);

		// 2. test create a new user with the same email address:
		User user2 = getNewUser();
		ExecutionResult result2 = fixture.createUser(user2);
		assertTrue("user with the same email address exists.", result2.isSuccessful() == false);

		// 3. test create a new user:
		User user3 = getNewUser();
		user3.setEmailAddressString("newEmail454545");
		ExecutionResult result3 = fixture.createUser(user3);
		assertTrue("user created.", result3.isSuccessful());

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void updateUser(User) method test.
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public void testUpdateUser() throws Exception {
		String testName = "UpdateUser";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n\t\tRunning Test: " + testName);
		}

		UserServiceImpl fixture = getFixture();
		User user = getStoredUser();
		fixture.update(user);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteUser(String) method test.
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public void testDeleteUser() throws Exception {
		String testName = "deleteUser";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n\t\tRunning Test: " + testName);
		}

		UserServiceImpl fixture = getFixture();
		User user = getStoredUser();
		fixture.deleteUser(user.getUsername());

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteUser(String) method test.
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public void testDeleteUser_negative() throws Exception {
		String testName = "deleteUser_negative";
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n\t\tRunning Test: " + testName);
		}

		UserServiceImpl fixture = getFixture();
		String username = "not exist";
		boolean caughtException = false;
		try {
			fixture.deleteUser(username);
		} catch (EntityNotFoundException ex) {
			// good.
			caughtException = true;
		}

		if (!caughtException) {
			fail("UserService.deleteUser() does not throw exception for non exist user.");
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
