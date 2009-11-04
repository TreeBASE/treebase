
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
