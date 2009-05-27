/*
 * CIPRES Copyright (c) 2005- 2008, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author madhu
 * 
 * Created on January 8th, 2008
 * 
 */
public class AdminDeletingUserStepOneController extends AdminAndUserGenericController {

	private static final Logger LOGGER = Logger.getLogger(AdminDeletingUserStepOneController.class);

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String username = request.getParameter("username").trim();

		if (username.equals(request.getRemoteUser())) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Self destruction is against the law.");
		}

		if (super.onSubmit(request, response, command, bindExp) != null) {
			return super.onSubmit(request, response, command, bindExp);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USERNAME: " + username);
		}

		StringBuilder aMessage = new StringBuilder("Are you sure you want to delete the user '");
		aMessage.append(username).append("'?");

		request.getSession().setAttribute("ADMIN_TEST_CONDITION", aMessage.toString());
		request.getSession().setAttribute("ADMIN_DELETING_USERNAME", username);

		return new ModelAndView(getSuccessView());

	}

}
