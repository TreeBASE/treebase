
package org.cipres.treebase.domain.admin;

import java.beans.PropertyEditorSupport;

/**
 * EmailAddressPropertyEditor.java
 * 
 * Created on Nov 1, 2005
 * 
 * @author lcchan
 * 
 */
public class EmailAddressPropertyEditor extends PropertyEditorSupport {

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
