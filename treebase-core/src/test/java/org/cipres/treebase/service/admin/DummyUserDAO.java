
package org.cipres.treebase.service.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole;

/**
 * Mock object for unit testing.
 * 
 * Created on October 14, 2005
 * 
 * @author Jin Ruan
 */
public class DummyUserDAO extends AbstractDAO implements UserHome {

	private User mStoredUser;
	private User mNewUser;

	/**
	 * Constructor.
	 */
	public DummyUserDAO() {
		super();
	}

	/**
	 * Return the NewUser field. Uses lazy initialization.
	 * 
	 * @return User mNewUser
	 */
	public User getNewUser() {
		if (mNewUser == null) {
			mNewUser = new User();

			mNewUser.setUsername("newUser");
			mNewUser.setFirstName("New");
			mNewUser.setLastName("User");
			mNewUser.setPassword("dummyPasswd");
			mNewUser.setEmailAddressString("dummy@treebase.org");
		}
		return mNewUser;
	}

	/**
	 * Return the DummyUser field. Uses lazy initialization.
	 * 
	 * @return User mDummyUser
	 */
	public User getStoredUser() {
		if (mStoredUser == null) {
			mStoredUser = new User();

			mStoredUser.setUsername("dummyUser");
			mStoredUser.setFirstName("Dummy");
			mStoredUser.setLastName("User");
			mStoredUser.setPassword("dummyPasswd");
			mStoredUser.setEmailAddressString("dummy@treebase.org");
		}
		return mStoredUser;
	}

	/**
	 * @param pDummyUser The dummyUser to set.
	 */
	@SuppressWarnings("unused")
	private void setStoredUser(User pDummyUser) {
		mStoredUser = pDummyUser;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserName(java.lang.String)
	 */
	public User findByUserName(String pUserName) {
		if (getStoredUser().getUsername().equalsIgnoreCase(pUserName)) {
			return getStoredUser();
		}
		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#delete(org.cipres.treebase.domain.admin.User)
	 */
	public void delete(User pUser) {
		if (pUser != null && !getStoredUser().equals(pUser)) {
			// not good. I cannot handle other user:
			throw new RuntimeException("I cannot delete user:" + pUser.getUsername());
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refresh(org.cipres.treebase.domain.TBPersistable)
	 */
	@Override
	public void refresh(TBPersistable pPersistable) {
		if (!getStoredUser().equals(pPersistable)) {
			// not good. I cannot handle other user:
			throw new RuntimeException("I cannot handle refresh user:" + pPersistable);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refreshAll(java.util.Collection)
	 */
	@Override
	public void refreshAll(Collection<? extends TBPersistable> pDomainObjects) {}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#store(org.cipres.treebase.domain.TBPersistable)
	 */
	@Override
	public void store(TBPersistable pPersistable) {
		if (!getNewUser().equals(pPersistable)) {
			if (getStoredUser().equals(pPersistable)) {
				// not good. it is already stored.
				throw new RuntimeException("The user is already stored:" + pPersistable);
			} else {
				// not good. I cannot handle other user:
				throw new RuntimeException("I cannot handle store user:" + pPersistable);
			}
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#flush()
	 */
	@Override
	public void flush() {}

	/**
	 * 
	 * @see org.cipres.treebase.dao.AbstractDAO#merge(T)
	 */
	@Override
	public <T extends TBPersistable> T merge(T pPersistable) {
		return pPersistable;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByEmail(java.lang.String)
	 */
	public List<User> findByEmail(String pEmailAddress, boolean pExactMatch) {

		if (getStoredUser().getEmailAddressString().equalsIgnoreCase(pEmailAddress)) {
			List<User> result = new ArrayList<User>();
			result.add(getStoredUser());
			return result;
		}

		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#getUserRole()
	 */
	public UserRole getUserRole() {
		UserRole role = new UserRole();
		role.setAuthority("User");
		return role;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findUserByLastName(java.lang.String)
	 */
	public List<User> findUserByLastName(String pLastName) {
		List<User> users = new ArrayList<User>();
		
		if (getStoredUser().getLastName().equalsIgnoreCase(pLastName)) {
			users.add(getStoredUser());
		}
		
		return users;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserNameLike(java.lang.String)
	 */
	public List<User> findByUserNameLike(String pUserName) {
		if (getStoredUser().getUsername().equalsIgnoreCase(pUserName)) {
			List<User> result = new ArrayList<User>();
			result.add(getStoredUser());
			return result;
		}

		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findUserRole(java.lang.String)
	 */
	public UserRole findUserRole(String pUserRole) {
		if (UserRole.ROLE_USER.equals(pUserRole)) {
			UserRole role = new UserRole();
			role.setAuthority("User");
			return role;
		}
		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findbyUserRole(java.lang.String)
	 */
	public List<User> findbyUserRole(String pUserRole) {
		List<User> users = new ArrayList<User>();
		
		if (UserRole.ROLE_USER.equals(pUserRole)) {
			users.add(getStoredUser());
		}
		return users;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByPerson(org.cipres.treebase.domain.admin.Person)
	 */
	public User findByPerson(Person pPerson) {
		if (pPerson == null) {
			return null;
		}
		
		if (getStoredUser().getFirstName().equals(pPerson.getFirstName())
			&& getStoredUser().getLastName().equals(pPerson.getLastName())
			&& getStoredUser().getEmailAddressString().equals(pPerson.getEmailAddressString())) {
			
			return getStoredUser();
		}
		
		return null;
	}

}
