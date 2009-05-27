/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

/**
 * UserFormValidator.java
 * 
 * Created on Nov 2, 2005
 * @author lcchan
 *
 */

package org.cipres.treebase.web.validators;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import org.cipres.treebase.domain.admin.User;

/**
 * UserFormValidator.java
 * 
 * Created on Nov 2, 2005
 * 
 * @author lcchan
 * 
 */
public class UserFormValidator implements Validator {

	/**
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return clazz.equals(User.class);
	}

	/**
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object object, Errors errors) {
		User user = (User) object;

		// ValidationUtils.rejectIfEmptyOrWhitespace(errors,
		// "firstName","required.firstName","First Name is required");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors,
		// "lastName","required.lastName","Last Name is required");
		validateEmail(user.getEmailAddressString(), errors);
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors,
		// "userName","required.userName", "User Name is required");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors,
		// "password","required.password","Password is required");
	}

	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^\\w+([_\\.-]\\w+)*@(\\w+([_\\.-]\\w+)*)");

	private void validateEmail(String emailAddressString, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddressString",
				"required.email", "Email address is required");
		if (!EMAIL_PATTERN.matcher(emailAddressString).matches()) {
			errors.rejectValue("emailAddressString", "invalid.email",
					"Email address is invalid");
		}
	}

}
