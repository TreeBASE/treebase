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

package org.cipres.treebase.domain.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Submission;

/**
 * User.java
 * 
 * Created on Sep 28, 2005
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "`user`")
@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "adminCache")
public class User extends AbstractPersistedObject implements UserDetails {

	private static final long serialVersionUID = 7663727479741176884L;

	private String mUserName;
	private String mPassword;

	private Person mPerson;
	private UserRole mRole;

	// Hibernate actually will use a Bag.
	private Collection<Submission> mSubmissions = new ArrayList<Submission>();

	//Transient
	private String mTmpEmailAddress;
	private String mTmpRoleDescription;

	/**
	 * Constructor.
	 */
	public User() {
		super();
	}

	/**
	 * Return the Roles field.
	 * 
	 * @return UserRole
	 */
	@ManyToOne
	@JoinColumn(name = "USERROLE_ID", nullable = false)
	public UserRole getRole() {
		return mRole;
	}

	/**
	 * Set the Roles field.
	 */
	public void setRole(UserRole pNewRole) {
		mRole = pNewRole;
	}

	/**
	 * Return the UserName field.
	 * 
	 * @return String userName
	 */
	@Column(name = "USERNAME", unique = true, nullable = false, length = TBPersistable.COLUMN_LENGTH_100)
	public String getUsername() {
		return mUserName;
	}

	/**
	 * Set the UserName field.
	 */
	public void setUsername(String pNewUserName) {
		mUserName = pNewUserName;
	}

	/**
	 * Return the Password field.
	 * 
	 * @return String password
	 */
	@Column(name = "PASSWORD", nullable = false, length = TBPersistable.COLUMN_LENGTH_100)
	public String getPassword() {
		return mPassword;
	}

	/**
	 * Set the Password field.
	 */
	public void setPassword(String pNewPassword) {
		mPassword = pNewPassword;
	}

	/**
	 * Return the EmailAddress field.
	 * 
	 * @return EmailAddress
	 */
	@Transient
	public String getEmailAddressString() {
		if (getPerson() == null) {
			return null;
		}
		return getPerson().getEmailAddressString();
	}

	/**
	 * A temporary field to hold the user input email address.
	 * 
	 * @return String 
	 */
	@Transient
	public String getTmpEmailAddress() {
		return mTmpEmailAddress;
	}

	/**
	 * Set the tmp email address field.
	 */
	public void setTmpEmailAddress(String pNewEmail) {
		mTmpEmailAddress = pNewEmail;
	}
	
	/**
	 * Return the role description.
	 * 
	 * @return
	 */
	@Transient
	public String getRoleDescription() {
		if (getRole() != null) {
			return getRole().getAuthority();
		}

		return "";
	}

	/**
	 * Save the changed role description.
	 * 
	 * @param pDesc
	 */
	public void setRoleDescription(String pDesc) {
		setTmpRoleDescription(pDesc);
	}

	/**
	 * Return the TmpRoleDescription field.
	 * 
	 * @return String mTmpRoleDescription
	 */
	@Transient
	public String getTmpRoleDescription() {
		return mTmpRoleDescription;
	}

	/**
	 * Set the TmpRoleDescription field.
	 */
	public void setTmpRoleDescription(String pNewTmpRoleDescription) {
		mTmpRoleDescription = pNewTmpRoleDescription;
	}
	
	/**
	 * Return the Person field.
	 * 
	 * @return Person
	 */
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "PERSON_ID", nullable = false)
	public Person getPerson() {
		return mPerson;
	}

	/**
	 * Get a non-null person, create one if it is null.
	 * 
	 * @return
	 */
	@Transient
	private Person getOrCreatePerson() {
		if (getPerson() == null) {
			setPerson(new Person());
		}
		return getPerson();
	}

	/**
	 * Set the Person field.
	 */
	public void setPerson(Person pNewPerson) {
		mPerson = pNewPerson;
	}

	/**
	 * Set the EmailAddress.
	 */
	public void setEmailAddressString(String pNewEmailAddress) {
		getOrCreatePerson().setEmailAddressString(pNewEmailAddress);
	}

	/**
	 * Return the LastName.
	 * 
	 * @return String
	 */
	@Transient
	public String getLastName() {
		if (getPerson() == null) {
			return null;
		}
		return getPerson().getLastName();
	}

	/**
	 * Set the LastName.
	 */
	public void setLastName(String pNewLastName) {
		getOrCreatePerson().setLastName(pNewLastName);
	}

	/**
	 * Return the phone number.
	 * 
	 * @return String
	 */
	@Transient
	public String getPhoneNumber() {
		if (getPerson() == null) {
			return null;
		}
		return getPerson().getPhoneNumber();
	}

	/**
	 * Set the phone number.
	 */
	public void setPhoneNumber(String pPhoneNumber) {
		getOrCreatePerson().setPhoneNumber(pPhoneNumber);
	}

	/**
	 * Return the middle Name.
	 * 
	 * @return String
	 */
	@Transient
	public String getMiddleName() {
		if (getPerson() == null) {
			return null;
		}
		return getPerson().getMiddleName();
	}

	/**
	 * Set the Middle Name.
	 */
	public void setMiddleName(String pMiddleName) {
		getOrCreatePerson().setMiddleName(pMiddleName);
	}

	/**
	 * Return the FirstName.
	 * 
	 * @return String firstName
	 */
	@Transient
	public String getFirstName() {
		if (getPerson() == null) {
			return null;
		}
		return getPerson().getFirstName();
	}

	/**
	 * Set the FirstName.
	 */
	public void setFirstName(String pNewFirstName) {
		getOrCreatePerson().setFirstName(pNewFirstName);
	}

	/**
	 * Return the Submissions field.
	 * 
	 * @return Set<Submission> mSubmissions
	 */
	@OneToMany(mappedBy = "submitter")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "adminCache")
	protected Collection<Submission> getSubmissions() {
		return mSubmissions;
	}

	/**
	 * Return true if the submission is owned by this user.
	 */
	public boolean hasSubmission(Submission pSubmission) {
		return getSubmissions().contains(pSubmission);
	}

	/**
	 * Set the Submissions field.
	 */
	protected void setSubmissions(Collection<Submission> pNewSubmissions) {
		mSubmissions = pNewSubmissions;
	}

	/**
	 * Return an iterator for submissions.
	 * 
	 * Creation date: May 8, 2006 4:11:15 PM
	 */
	@Transient
	public Iterator<Submission> getSubmissionIterator() {
		return getSubmissions().iterator();
	}

	/**
	 * Return a copy of submissions.
	 * 
	 * Creation date: May 8, 2006 4:11:15 PM
	 */
	@Transient
	public Collection<Submission> getSubmissionsCopy() {
		Collection<Submission> aCopy = new ArrayList<Submission>();
		aCopy.addAll(getSubmissions());

		return aCopy;
	}

	/**
	 * Return submission count.
	 * 
	 * Creation date: May 8, 2006 4:11:15 PM
	 */
	@Transient
	public int getSubmissionCount() {
		return getSubmissions().size();
	}

	/**
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pObj) {
		if (this == pObj) {
			return true;
		}

		if (!(pObj instanceof User)) {
			return false;
		}

		if (getUsername() != null) {
			User otherUser = (User) pObj;
			return getUsername().equals(otherUser.getUsername());
		}

		return super.equals(pObj);
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getUsername() != null) {
			return getUsername().hashCode();
		}

		return super.hashCode();
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
	 */
	@Transient
	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] roles = new GrantedAuthority[1];
		roles[0] = getRole();

		return roles;
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
	 */
	@Transient
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Return a set of in progress submissions.
	 * 
	 * Creation date: Apr 20, 2006 1:00:57 PM
	 */
	@Transient
	public Set<Submission> getInProgressSubmissions() {
		Set<Submission> returnVal = new HashSet<Submission>();

		for (Submission sub : getSubmissions()) {
			if (sub.isInProgress()) {
				returnVal.add(sub);
			}
		}
		return returnVal;
	}

	/**
	 * Return a set of ready submissions.
	 * 
	 * Creation date: Apr 20, 2006 1:00:57 PM
	 */
	@Transient
	public Set<Submission> getReadySubmissions() {
		Set<Submission> returnVal = new HashSet<Submission>();

		for (Submission sub : getSubmissions()) {
			if (sub.isReady()) {
				returnVal.add(sub);
			}
		}
		return returnVal;
	}

	/**
	 * Return a set of published submissions.
	 * 
	 * Creation date: Apr 20, 2006 1:00:57 PM
	 */
	@Transient
	public Set<Submission> getPublishedSubmissions() {
		Set<Submission> returnVal = new HashSet<Submission>();

		for (Submission sub : getSubmissions()) {
			if (sub.isPublished()) {
				returnVal.add(sub);
			}
		}
		return returnVal;
	}

	/**
	 * Append a new submission. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pSubmission Submission
	 */
	public void addSubmission(Submission pSubmission) {
		if (pSubmission != null) {
			getSubmissions().add(pSubmission);
			pSubmission.setSubmitter(this);
		}
	}

	/**
	 * Remove the Submission. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pSubmission Submission
	 */
	public void removeSubmission(Submission pSubmission) {
		if (pSubmission != null) {
			getSubmissions().remove(pSubmission);
			pSubmission.setSubmitter(null);
		}
	}

}
