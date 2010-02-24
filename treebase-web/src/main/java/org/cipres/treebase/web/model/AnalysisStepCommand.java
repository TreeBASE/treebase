
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
import org.cipres.treebase.domain.study.BayesianAlgorithm;
import org.cipres.treebase.domain.study.EvolutionAlgorithm;
import org.cipres.treebase.domain.study.JoiningAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.domain.study.UPGMAAlgorithm;
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
		algorithmMap.put(Constants.ALGORITHM_Bayesian, new BayesianAlgorithm());
		algorithmMap.put(Constants.ALGORITHM_Evolution, new EvolutionAlgorithm());
		algorithmMap.put(Constants.ALGORITHM_Joining, new JoiningAlgorithm());
		algorithmMap.put(Constants.ALGORITHM_UPGMA, new UPGMAAlgorithm());
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