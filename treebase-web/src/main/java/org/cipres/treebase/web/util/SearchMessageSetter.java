package org.cipres.treebase.web.util;

import java.util.List;

/**
 * @author mjd 20090315
 *
 */
public interface SearchMessageSetter {
	void addMessage(String msg);
	void addMessages(List<String> msgs);
	void clearMessages();
}
