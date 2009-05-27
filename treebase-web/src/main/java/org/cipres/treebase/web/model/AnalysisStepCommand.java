/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.matrix.GapMode;
import org.cipres.treebase.domain.matrix.PolyTCount;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.DistanceAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.web.Constants;

/**
 * AnalysisStepCommand.java
 * 
 * Created on June 6, 2006
 * 
 * @author lcchan
 * 
 */
@SuppressWarnings("serial")
public class AnalysisStepCommand extends AnalysisStep {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(AnalysisStepCommand.class);

	private Long id;
	private int order;
	private String algorithmType;
	private Map<String, Object> algorithmMap = new HashMap<String, Object>();
	private Software softwareInfo = new Software();
	private GapMode gapMode = new GapMode();
	private PolyTCount polyTCount = new PolyTCount();
	// private UserType userType = new UserType();
	private List<AnalyzedDataCommand> analyzedDataCommandList = new ArrayList<AnalyzedDataCommand>();

	private List<String> mUniqueAlgorithmDescriptions;

	public AnalysisStepCommand() {
		algorithmMap.put(Constants.ALGORITHM_DISTANCE, new DistanceAlgorithm());
		algorithmMap.put(Constants.ALGORITHM_LIKELIHOOD, new LikelihoodAlgorithm());
		algorithmMap.put(Constants.ALGORITHM_OTHER, new OtherAlgorithm());
		// ParsimonyAlgorithm parsimonyAlgorithm = new ParsimonyAlgorithm();
		// parsimonyAlgorithm.setGapMode(new GapMode());
		// parsimonyAlgorithm.setPolyTCount(new PolyTCount());
		algorithmMap.put(Constants.ALGORITHM_PARSIMONY, new ParsimonyAlgorithm());
	}

	public Algorithm getAlgorithmMap(String key) {
		return (Algorithm) algorithmMap.get(key);
	}

	public void setAlgorithmMap(String key, Object object) {
		algorithmMap.put(key, object);
	}

	public Map<String, Object> getAlgorithmMap() {
		return algorithmMap;
	}

	public void setAlgorithmMap(Map<String, Object> algorithmMap) {
		this.algorithmMap = algorithmMap;
	}

	public String getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}

	public GapMode getGapMode() {
		return gapMode;
	}

	public void setGapMode(GapMode gapMode) {
		this.gapMode = gapMode;
	}

	public PolyTCount getPolyTCount() {
		return polyTCount;
	}

	public void setPolyTCount(PolyTCount polyTCount) {
		this.polyTCount = polyTCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Software getSoftwareInfo() {
		return softwareInfo;
	}

	public void setSoftwareInfo(Software softwareInfo) {
		//ALERT: make sure to handle null case. 
		// a better way is to get rid of software here, but defines softwareName, 
		//softwareVersion etc. properties to display in jsp page!!!
		if (softwareInfo == null) {
			this.softwareInfo = new Software();
		}
		else {
			this.softwareInfo = softwareInfo;
		}
	}

	public List<AnalyzedDataCommand> getAnalyzedDataCommandList() {
		return analyzedDataCommandList;
	}

	public void setAnalyzedDataCommandList(List<AnalyzedDataCommand> analyzedDataCommandList) {
		this.analyzedDataCommandList = analyzedDataCommandList;
	}

	public List<String> getUniqueAlgorithmDescriptions() {

		return mUniqueAlgorithmDescriptions;
	}

	public void setUniqueAlgorithmDescriptions(List<String> pUniqueAlgorithmDescriptions) {
		mUniqueAlgorithmDescriptions = pUniqueAlgorithmDescriptions;
	}

}