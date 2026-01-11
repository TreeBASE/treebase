package org.cipres.treebase.dao.admin;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import org.cipres.treebase.domain.admin.PasswordResetToken;
import org.cipres.treebase.domain.admin.PasswordResetTokenHome;
import org.cipres.treebase.domain.admin.User;

/**
 * PasswordResetTokenDAO.java
 * 
 * Data access object for PasswordResetToken entity.
 * 
 * @author Security Migration
 */
public class PasswordResetTokenDAO extends HibernateDaoSupport implements PasswordResetTokenHome {

	private static final Logger LOGGER = LogManager.getLogger(PasswordResetTokenDAO.class);

	/**
	 * Constructor.
	 */
	public PasswordResetTokenDAO() {
		super();
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#store(PasswordResetToken)
	 */
	@Override
	public void store(PasswordResetToken token) {
		getHibernateTemplate().saveOrUpdate(token);
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#findByToken(String)
	 */
	@Override
	public PasswordResetToken findByToken(String tokenString) {
		if (tokenString == null || tokenString.isEmpty()) {
			return null;
		}

		String query = "from PasswordResetToken where token = :token";
		List<?> result = getHibernateTemplate().findByNamedParam(query, "token", tokenString);
		
		if (result == null || result.isEmpty()) {
			return null;
		}

		return (PasswordResetToken) result.get(0);
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#findByUser(User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PasswordResetToken> findByUser(User user) {
		if (user == null) {
			return null;
		}

		String query = "from PasswordResetToken where user = :user";
		return (List<PasswordResetToken>) getHibernateTemplate().findByNamedParam(query, "user", user);
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#delete(PasswordResetToken)
	 */
	@Override
	public void delete(PasswordResetToken token) {
		if (token != null) {
			getHibernateTemplate().delete(token);
		}
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#deleteExpiredTokens()
	 */
	@Override
	public int deleteExpiredTokens() {
		String query = "delete from PasswordResetToken where expiryDate < ?";
		return getHibernateTemplate().bulkUpdate(query, new Date());
	}

	/**
	 * @see org.cipres.treebase.domain.admin.PasswordResetTokenHome#invalidateTokensForUser(User)
	 */
	@Override
	public void invalidateTokensForUser(User user) {
		if (user == null) {
			return;
		}

		String query = "update PasswordResetToken set used = true where user = ? and used = false";
		getHibernateTemplate().bulkUpdate(query, user);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Invalidated existing tokens for user: " + user.getUsername());
		}
	}
}
