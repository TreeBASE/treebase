
package org.cipres.treebase.domain.admin;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import org.hibernate.annotations.Index;

import org.cipres.treebase.domain.TBPersistable;

/**
 * Represents an Email Address.
 * 
 * Created on Sep 28, 2005
 * 
 * @author Jin Ruan
 * 
 */
@Embeddable
public class EmailAddress {

	private String mEmailAddressString;

	/**
	 * Constructor.
	 */
	public EmailAddress() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param pEmail
	 */
	public EmailAddress(String pEmail) {
		this();

		setEmailAddressString(pEmail);
	}

	/**
	 * Return the EmailAddreessString field.
	 * 
	 * @return String
	 */
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "EMAIL", length = TBPersistable.COLUMN_LENGTH_STRING)
	@Index(name="PERSON_EMAIL_IDX")
	public String getEmailAddressString() {
		return mEmailAddressString;
	}

	/**
	 * Set the EmailAddreessString field.
	 * Trims whitespace from the email address to prevent validation failures
	 * when users paste email addresses with leading/trailing spaces.
	 */
	public void setEmailAddressString(String pNewEmailAddreessString) {
		mEmailAddressString = pNewEmailAddreessString != null ? pNewEmailAddreessString.trim() : null;
	}

}
