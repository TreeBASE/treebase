package org.cipres.treebase.domain.admin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * UserRole.java
 * 
 * Three user roles are defined:
 * <ul>
 * <li> TreeBASE Administrator/Editor
 * <li> Regular User/Submitter
 * <li> Journal Editor/Reviewer
 * </ul>
 * 
 * Created on Apr 6, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "USERROLE")
@AttributeOverride(name = "id", column = @Column(name = "USERROLE_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class UserRole extends AbstractPersistedObject implements GrantedAuthority {

	/**
	 * Define the access rights.
	 * <ul>
	 * <li>** none
	 * <li>** read_only
	 * <li>** Submitted_write: the submitter can change limited fields after the study is in
	 * "submitted" status.
	 * <li>** write
	 * </ul>
	 * 
	 */
	public enum TBPermission {
		NONE, READ_ONLY, SUBMITTED_WRITE, WRITE
	}

	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_USER = "User";
	public static final String ROLE_ASSO_EDITOR = "Associate Editor";

	private static final long serialVersionUID = -4663885536006782596L;

	private String mAuthority;

	/**
	 * Return an ordered list of all user roles as string.
	 * 
	 * @return
	 */
	public static List<String> allUserRoleList() {
		List<String> roleList = new ArrayList<String>();

		// order here!
		roleList.add(ROLE_USER);
		roleList.add(ROLE_ASSO_EDITOR);
		roleList.add(ROLE_ADMIN);

		return roleList;
	}

	/**
	 * Constructor.
	 */
	public UserRole() {
		super();
	}

	/**
	 * Return the Authority field.
	 * 
	 * @return String
	 */
	@Column(name = "Authority", nullable = false, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getAuthority() {
		return mAuthority;
	}

	/**
	 * Set the Authority field.
	 */
	public void setAuthority(String pNewAuthority) {
		mAuthority = pNewAuthority;
	}

	/**
	 * Return true if the user is an admin
	 * 
	 * @return
	 */
	@Transient
	public boolean isAdmin() {
		return (ROLE_ADMIN.compareToIgnoreCase(getAuthority()) == 0);
	}

	/**
	 * Return true if the user is an associate editor
	 * 
	 * @return
	 */
	@Transient
	public boolean isAssociateEditor() {
		return (ROLE_ASSO_EDITOR.compareToIgnoreCase(getAuthority()) == 0);
	}

	/**
	 * Return true if the user is a reviewer.
	 * 
	 * As per BP mail 20090109, there are no reviewer accounts. -- mjd 
	 * 
	 * @return
	 */
	@Transient
	@Deprecated
	public boolean isReviewer() {
		return false;
	}

	/**
	 * Return true if the user is a regular user.
	 * 
	 * @return
	 */
	@Transient
	public boolean isUser() {
		return (ROLE_USER.compareToIgnoreCase(getAuthority()) == 0);
	}

}
