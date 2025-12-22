package org.cipres.treebase.dao.admin;

import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole;
import org.junit.Assume;

/**
 * The class <code>UserDAOTest</code> contains tests for the class {@link <code>UserDAO</code>}.
 * 
 * @generatedBy CodePro at 10/6/05 1:26 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unchecked")
public class UserDAOTest extends AbstractDAOTest {

	private UserHome mUserHome;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * @see UserDAO
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public UserHome getUserHome() {
		// if (mUserHome == null) {
		// TransactionProxyFactoryBean proxy = (TransactionProxyFactoryBean) applicationContext
		// .getBean("&userHome");
		// Object bean = proxy.getObject();
		// logger.info("bean=" + bean.getClass());
		// mUserHome = (UserHome) bean;
		// }
		return mUserHome;
	}

	/**
	 * Injected by the super class based on autowiring by type.
	 * 
	 * @param pUserDAO
	 */
	public void setUserHome(UserHome pUserDAO) {
		mUserHome = pUserDAO;
	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User delete(User) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testCreateDelete() throws Exception {
		String testName = "CreateAndDelete";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new user:
		String newName = testName + " test " + Math.random();
		User user = new User();
		user.setFirstName("testF");
		user.setLastName("testL");
		user.setUsername(newName);
		user.setPassword("testPassword");

		String email = "test@cipres.treebase.org";
		user.setEmailAddressString(email);

		UserRole testRole = new UserRole();
		testRole.setAuthority("TEST");
		user.setRole(testRole);

		UserHome fixture = getUserHome();
		fixture.store(testRole);
		fixture.store(user);

		// force commit immeidately, important:
		setComplete();
		endTransaction();
		logger.info("user created: " + user.getUsername() + "id = " + user.getId());

		// 2. verify
		String sqlStr = "select count(*) from public.user where user_id=" + user.getId();
		Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count == 1);

		// 3. delete
		// fixture.delete(testRole);
		fixture.delete(user);
		setComplete();

		// 4. verify delte:
		Integer countVerify = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User delete(User) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testDelete_Negative() throws Exception {
		String testName = "delete_negative";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new user:
		String newName = testName + " test " + Math.random();
		User user = new User();
		user.setFirstName("testF");
		user.setLastName("testL");
		user.setUsername(newName);
		user.setPassword("testPassword");

		String email = "test@cipres.treebase.org";
		user.setEmailAddressString(email);

		UserHome fixture = getUserHome();
		String sqlStr = "select count(*) from public.user where username='" + user.getUsername() + "'";

		// 3. delete
		fixture.delete(user);

		// 4. verify delte:
		Integer countVerify = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User findByUserName(String) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testFindByUserName() throws Exception {
		String testName = "findByUserName";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid username first:
		List<String> usernames = jdbcTemplate.queryForList(
			"select username from public.user",
			String.class);

		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", usernames);
		
		if (usernames.size() > 0) {
			// 3. find by user name:
			UserHome fixture = getUserHome();
			String userName = usernames.get(0);
			User result = fixture.findByUserName(userName);

			// 4. verify
			assertTrue(result != null);
			assertTrue(result.getUsername().equals(userName));
			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified username =" + userName);
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}

	}

	/**
	 * Run the List<User> findByUserRole(String) method test.
	 * 
	 */
	public void testFindByUserRole() throws Exception {
		String testName = "findByUserRole";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid role first:
		String testRole = UserRole.ROLE_USER; // UserRole.ROLE_ASSO_EDITOR;

		// 2. find by user name:
		UserHome fixture = getUserHome();
		List<User> result = fixture.findbyUserRole(testRole);

		// 3. verify correct handling of empty database
		assertNotNull("Query should return non-null list", result);
		
		if (logger.isInfoEnabled()) {
			logger.info("test role = " + testRole + " size =" + result.size());
		}

		// 4. verify results if data exists
		if (result.size() > 0) {
			for (User user : result) {
				assertTrue(user.getRoleDescription().equals(testRole));
			}
			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified role =" + testRole);
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

	/**
	 * Run the User findByPerson() method test.
	 * 
	 */
	public void testFindByPerson() throws Exception {
		String testName = "findByPerson";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid person first:
		User u = (User) loadObject(User.class);
		
		if (u != null) {
			Person p = u.getPerson();

			if (logger.isInfoEnabled()) {
				logger.info("username = " + u.getUsername() + " persopn =" + p.getFirstName() + " "
					+ p.getLastName());
			}

			// 2. test:
			UserHome fixture = getUserHome();
			User result = fixture.findByPerson(p);

			if (logger.isInfoEnabled()) {
				logger.info("u id = " + u.getId() + " result id =" + result.getId());
			}
			// 3. verify
			assertTrue(result != null);
			assertTrue(result.getId() == u.getId());

			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified result =");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User findByEmailAddress(String) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testFindByEmailAddress() throws Exception {
		String testName = "findByEmailAddress";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid email first:
		List<String> emails = jdbcTemplate.queryForList(
			"select p.email from person p, public.USER u where p.PERSON_ID = u.PERSON_ID",
			String.class);

		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", emails);
		
		if (emails.size() > 0) {
			// 3. find by user name:
			UserHome fixture = getUserHome();
			String email = emails.get(0);
			List<User> users = fixture.findByEmail(email.toUpperCase(), true);

			// 4. verify
			assertTrue(users != null && !users.isEmpty());
			User result = users.get(0);
			assertTrue(result.getEmailAddressString().equalsIgnoreCase(email));
			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified email =" + email + " num of matches:"
					+ users.size());
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}

	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User findByEmailAddress(String) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testFindByEmailAddressNegative() throws Exception {
		String testName = "findByEmailAddressNegative";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a unvalid email first:
		String email = "test" + Math.random() + "@test.com";

		// 2. find by email:
		UserHome fixture = getUserHome();
		List<User> users = fixture.findByEmail(email.toUpperCase(), true);

		// 3. verify
		assertTrue("Should not find a user", users == null || users.isEmpty());

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified email =" + email + " num of matches:" + users.size());
		}
	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User findByEmailAddress(String) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testFindByEmailAddressLike() throws Exception {
		String testName = "findByEmailAddressLike";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid email first:
		List<String> emails = jdbcTemplate.queryForList(
			"select p.email from person p, public.USER u where p.PERSON_ID = u.PERSON_ID",
			String.class);

		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", emails);
		
		if (emails.size() > 0) {
			// 3. find by user name:
			UserHome fixture = getUserHome();
			String email = emails.get(0);
			int emailSize = email.length();
			String emailTest = email;
			if (emailSize > 2) {
				emailTest = email.substring(0, emailSize - 2);
			}
			List<User> users = fixture.findByEmail(emailTest.toUpperCase(), false);

			if (logger.isInfoEnabled()) {
				logger.info("emailTest =" + emailTest + " num of matches:" + users.size());
			}
			// 4. verify
			assertTrue(users != null && !users.isEmpty());
			User result = users.get(0);
			assertTrue(result.getEmailAddressString().equalsIgnoreCase(email));
			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified email =" + email + " num of matches:"
					+ users.size());
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}

	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User merge(User) method test.
	 */
	public void testMerge() throws Exception {
		String testName = "merge";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a valid username first:
		List users = loadAllObject(User.class);
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", users);
		
		if (users.size() > 0) {
			// 3. update:
			// UserHome fixture = getUserHome();
			User result = (User) users.get(0);

			String oldUserName = result.getUsername();
			String newUserName = oldUserName + "unitTest";
			Long id = result.getId();

			result.setUsername(newUserName);

			// force commit immeidately, important:
			setComplete();
			endTransaction();
			logger.info("user updated: " + result.getUsername() + "id = " + result.getId());

			// 4. verify
			String sqlStr = "select username from public.user where user_id=" + result.getId();
			String verifyName = (String) jdbcTemplate.queryForObject(sqlStr, String.class);
			assertTrue("verify failed.", newUserName.equals(verifyName));

			// 5. undo update:
			// need to call onSetUp() to start a new session/Tx:
			onSetUp();
			result = (User) loadObject(User.class, id);
			result.setUsername(oldUserName);
			setComplete();
			endTransaction();

			// 6. verify undo:
			verifyName = (String) jdbcTemplate.queryForObject(sqlStr, String.class);
			assertTrue("Undo update failed.", oldUserName.equals(verifyName));

			if (logger.isInfoEnabled()) {
				logger.info(testName + " - end "); //$NON-NLS-1$
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}

	}

	/**
	 * Run the org.cipres.treebase.domain.admin.User findByUserName(String) method test.
	 * 
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public void testFindByUserName_Negative() throws Exception {
		String testName = "FindByUserName_Negative";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		UserHome fixture = getUserHome();

		// Test a non-existing username:
		String newName = testName + " test " + Math.random();
		User result = fixture.findByUserName(newName);
		assertEquals(null, result);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
