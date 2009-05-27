/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
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

package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Study;

/**
 * StudyCommand.java
 * 
 * Created on Aug 17, 2006
 * @author lcchan
 *
 */
public class StudyCommand {
	
	private Long submission_id;
	private Study study;
	private List<Person> authors;
	private List<AnalysisCommand> analysisCommandList = new ArrayList<AnalysisCommand>();

	public List<AnalysisCommand> getAnalysisCommandList() {
		return analysisCommandList;
	}

	public void setAnalysisCommandList(List<AnalysisCommand> analysisCommandList) {
		this.analysisCommandList = analysisCommandList;
	}

	public List<Person> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Person> authors) {
		this.authors = authors;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Long getSubmission_id() {
		return submission_id;
	}

	public void setSubmission_id(Long submission_id) {
		this.submission_id = submission_id;
	}
	

}
