
package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.domain.study.Analysis;

/**
 * AnalysisCommand.java
 * 
 * Created on August 16, 2006
 * 
 * @author lcchan
 * 
 */
@SuppressWarnings("serial")
public class AnalysisCommand extends Analysis {

	private Long id;
	private List<AnalysisStepCommand> analysisStepCommandList = new ArrayList<AnalysisStepCommand>();

	public List<AnalysisStepCommand> getAnalysisStepCommandList() {
		return analysisStepCommandList;
	}

	public void setAnalysisStepCommandList(List<AnalysisStepCommand> analysisStepCommandList) {
		this.analysisStepCommandList = analysisStepCommandList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
