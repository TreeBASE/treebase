
package org.cipres.treebase.domain.admin;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

/**
 * EmailAddressPropertyEditor.java
 * 
 * Created on Nov 1, 2005
 * 
 * @author lcchan
 * 
 */
public class EmailAddressPropertyEditor extends PropertyEditorSupport {
	private static final Logger LOGGER = Logger.getLogger(EmailAddressPropertyEditor.class);

	@Override
	public String getAsText() {
		Object object = getValue();
		return (object == null) ? "" : ((EmailAddress) object).getEmailAddressString().toString();
	}

	@Override
	public void setAsText(String string) {
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmailAddressString(string);
		setValue(emailAddress);
	}

}
