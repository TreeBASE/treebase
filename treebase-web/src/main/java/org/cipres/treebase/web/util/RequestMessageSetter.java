package org.cipres.treebase.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.cipres.treebase.web.Constants;

public class RequestMessageSetter implements SearchMessageSetter {
	private static final Logger LOGGER = Logger.getLogger(SearchMessageSetter.class);

	HttpServletRequest req;
	
	public RequestMessageSetter(HttpServletRequest req) {
		this.req = req;
	}

	public void addMessage(String msg) {
		@SuppressWarnings("unchecked")
		List<String> s = (List<String>) req.getAttribute(Constants.SEARCH_MESSAGES);
		if (s == null) { s = new ArrayList<String> (); }
		LOGGER.info("Adding message '" + msg + "' to searchMessage variable");
		s.add(msg);
		req.setAttribute("searchMessage", s);	}

	public void addMessages(List<String> msgs) {
		for (String msg : msgs) addMessage(msg);
	}

	public void clearMessages() {
		req.removeAttribute(Constants.SEARCH_MESSAGES);
	}

}
