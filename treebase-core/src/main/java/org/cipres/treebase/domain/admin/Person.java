
package org.cipres.treebase.domain.admin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * User.java
 * 
 * Created on September 28, 2005
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
// Notes: the unique constraint needs not null fields
// @Table(name = "PERSON", uniqueConstraints ={@UniqueConstraint(columnNames={"LASTNAME",
// "FIRSTNAME", "EMAIL"})})
@Table(name = "PERSON")
@AttributeOverride(name = "id", column = @Column(name = "PERSON_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "adminCache")
public class Person extends AbstractPersistedObject {

	private String mFirstName;
	private String mLastName;
	private String mMiddleName;
	private String mPhoneNumber;

	private EmailAddress mEmailAddress;

	/**
	 * Constructor.
	 */
	public Person() {
		super();
	}

	/**
	 * Return the LastName field.
	 * 
	 * @return String
	 */
	@Column(name = "LASTNAME", nullable = false, length = TBPersistable.COLUMN_LENGTH_STRING)
	@Index(name = "person_lname_idx")
	public String getLastName() {
		return mLastName;
	}

	/**
	 * Set the LastName field.
	 */
	public void setLastName(String pNewLastName) {
		mLastName = pNewLastName;
	}

	/**
	 * Return the FirstName field.
	 * 
	 * @return String firstName
	 */
	@Column(name = "FIRSTNAME", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * Set the FirstName field.
	 */
	public void setFirstName(String pNewFirstName) {
		mFirstName = pNewFirstName;
	}

	/**
	 * Return the MiddleName field.
	 * 
	 * @return String
	 */
	@Column(name = "MNAME", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getMiddleName() {
		return mMiddleName;
	}

	/**
	 * Set the MiddleName field.
	 */
	public void setMiddleName(String pNewMiddleName) {
		mMiddleName = pNewMiddleName;
	}

	/**
	 * Return the PhoneNumber field.
	 * 
	 * @return String mPhoneNumber
	 */
	@Column(name = "PHONE", length = TBPersistable.COLUMN_LENGTH_50)
	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	/**
	 * Set the PhoneNumber field.
	 */
	public void setPhoneNumber(String pNewPhoneNumber) {
		mPhoneNumber = pNewPhoneNumber;
	}

	/**
	 * Return the EmailAddress field.
	 * 
	 * @return EmailAddress
	 */
	@Transient
	public String getEmailAddressString() {
		if (getEmailAddress() != null) {
			return getEmailAddress().getEmailAddressString();
		}
		return "";
	}
	
	@Transient
	public List<Integer> getEmailAddressCodePoints() {
		List<Integer> points = new ArrayList<Integer>();
		String email = getEmailAddressString();
		for ( int i = 0; i < email.length(); i++ ) {
			points.add(new Integer(email.codePointAt(i)));
		}
		return points;
	}

	/**
	 * Return the EmailAddress field.
	 * 
	 * @return EmailAddress
	 */
	@Embedded
	private EmailAddress getEmailAddress() {
		return mEmailAddress;
	}

	/**
	 * Set the EmailAddress field.
	 */
	public void setEmailAddressString(String pNewEmailAddress) {
		if (pNewEmailAddress == null) {
			setEmailAddress(null);
		} else {
			setEmailAddress(new EmailAddress(pNewEmailAddress));
		}
	}

	/**
	 * Set the EmailAddress field.
	 */
	private void setEmailAddress(EmailAddress pNewEmailAddress) {
		mEmailAddress = pNewEmailAddress;
	}

	@Transient
	public String getFullName() {
		StringBuilder fullName = new StringBuilder(getFirstName());
		fullName.append(" ").append(getMiddleName()).append(" ").append(getLastName());
		return fullName.toString();

	}

	@Transient
	/**
	 * author Madhu
	 */
	public String getFullNameCitationStyle() {

		String lastname = getLastName().trim().substring(0, 1).toUpperCase()
			+ getLastName().trim().toLowerCase().substring(1);
		// It brings the first letter of the last
		// name to the upper case and remaining
		// letters in lower case.

		StringBuilder fullNameCitationStyle = new StringBuilder(lastname);
		fullNameCitationStyle.append(" ");

		fullNameCitationStyle.append(getFirstName().trim().substring(0, 1).toUpperCase()).append(
			".");

		if (getMiddleName().trim() != null && getMiddleName().trim().length() > 0) {

			fullNameCitationStyle
				.append(getMiddleName().trim().substring(0, 1).toUpperCase()).append(".");
		}

		return fullNameCitationStyle.toString();

	}

	/**
	 * Compare lastname, firstname, and email address
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

		if (getLastName() != null) {
			Person otherUser = (Person) pObj;
			boolean isSame = getLastName().equalsIgnoreCase(otherUser.getLastName());

			if (isSame) {
				isSame = getFirstName().equalsIgnoreCase(otherUser.getFirstName());
			}

			if (isSame) {
				isSame = getEmailAddressString()
					.equalsIgnoreCase(otherUser.getEmailAddressString());
			}

			return isSame;
		}

		return super.equals(pObj);
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getLastName() != null) {
			return TreebaseUtil.getHashCode(getLastName(), getFirstName(), getEmailAddressString());
		}

		return super.hashCode();
	}

}
