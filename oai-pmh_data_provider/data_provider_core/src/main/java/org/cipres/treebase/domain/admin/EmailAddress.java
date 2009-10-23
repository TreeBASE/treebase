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
	 */
	public void setEmailAddressString(String pNewEmailAddreessString) {
		mEmailAddressString = pNewEmailAddreessString;
	}

}
