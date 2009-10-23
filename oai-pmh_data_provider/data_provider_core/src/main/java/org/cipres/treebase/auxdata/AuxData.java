/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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



package org.cipres.treebase.auxdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;


/**
 * Façade class providing convenient access to frequently-used components of
 * a {@see ValueStudy} value.
 * 
 * For example:
 * <pre>
 *     AuxData aux = new AuxData(studyValue);
 *     Study s = aux.study();
 * </pre>
 * 
 * Examples in the following documentation will suppose that
 * the study data in question is as follows:
 * 
 * <pre>
 * >STUDY [59]
 *       study_id = 'S1x28x96c14c14c15'
 *       citation = 'Anderberg, A. A., and P. Eldenas. 1991. A cladistic analysis of Anigozanthos and Macropidia (Haemodoraceae). Australian Systematic Botany 4:655-664.'
 *       abstract = 'Using morphological data and a computerised parsimony program, the Australian genera Anigozanthos and Macropidia (Haemodoraceae) have been analysed cladistically in order to formulate a hypothesis of their phylogenetic relationship. The results are illustrated with two cladograms and indicate that Anigozanthos together with Macropidia form a monophyletic group diagnosed by a string of synapomorphies, with Macropidia fuliginosa being a derived relative of the species of Anigozanthos subgenus Anigozanthos. Consequently, Macropidia fuliginosa is here treated as a section Macropidia of Anigozanthos.'
 *       >AUTHOR [1]
 *               author_id = 'A5'
 *               first_name = 'Arne A. '
 *               last_name = 'Anderberg'
 *               email = 'arne.anderberg@nrm.se'
 *       >AUTHOR [2]
 *               author_id = 'A89'
 *               first_name = 'Pia'
 *               last_name = 'Eldens'
 *               email = 'Pia.Eldenas@nrm.se'
 *       >HISTORY [1]
 *               date = '1/28/96'
 *               time = '14:14:16'
 *               person = 'W. Piel'
 *               event = 'New Study Added'
 *       >MATRIX [1]
 *               matrix_id = 'M62c1x28x96c14c23c23'
 *               matrix_name = 'Table 1'
 *               data_type = 'Morphological'
 *               nchar = '23'
 *       >ANALYSIS [1]
 *               analysis_id = 'A64c1x28x96c14c17c27'
 *               analysis_name = ''
 *               software = 'Hennig86'
 *               algorithm = 'Parsimony'
 *               >INPUT_MATRIX [1]
 *                       matrix_id = 'M62c1x28x96c14c23c23'
 *               >OUTPUT_TREE [1]
 *                       tree_id = 'T201c1x28x96c14c18c33'
 *                       tree_label = 'Fig. 1'
 *                       tree_title = 'Haemodoraceae'
 *                       tree_type = 'Single Tree'
 *</pre>
 * 
 * @author mjd 20080229
 *
 */
public class AuxData {
	ValueStudy studyData;
	Study theStudy = null;

	/**
	 * Constructor returns a façade object for the specified study value
	 * 
	 * @param pStudyData
	 */
	public AuxData(ValueStudy pStudyData) {
		studyData = pStudyData;
	}
	
	/**
	 * Get a string value with the specified key from the specified section.
	 * 
	 * <p>If the <code>sectionTitle</code> is <b>null</b>, the main section is used.
	 * If there is more than one section with the specified title, one is chosen
	 * arbitrarily.  If the key does not exist in the specified section, <b>null</b> is returned.</p>
	 * 
	 * <p>For example, given the sample data above, <code>getString(null, "study_id")</code>
	 * would return "S1x28x96c14c14c15", and <code>getString("MATRIX", "matrix_name")</code>
	 * would return "Table 1".  <code>getString("AUTHOR", "first_name")</code>
	 * might return either "Arne A. " or "Pia".</p>
	 * 
	 * @param sectionTitle - the title of the section to fetch, or <b>null</b>
	 * @param key
	 * @return - the corresponding string
	 * @author mjd
	 */
	public String getString(String sectionTitle, String key) {
		if (sectionTitle == null) {
			return studyData.props.getString(key);
		} else {
			return getSection(sectionTitle).getString(key);
		}
	}

	
	/**
	 * Return the simple section with the specified label
	 * 
	 * <p>Return the {@see ValueSection} that represents a simple section
	 * with a particular label.  If there are multiple sections with the same 
	 * label, one is chosen arbitrarily.  If there is no section the specified label,
	 * <b>null</b> is returned.</p>
	 * 
	 * <p>It is not clear that this method is actually useful for anything.
	 * It should probably be removed.  {@see getSections} should be used instead.</p>
	 * 
	 * @param sectionName - the label of the desired section
	 * @return a section with that label, if there is one
	 * @author mjd
	 */
	public ValueSection getSection(String sectionName) {
		for (ValueSection sect : studyData.items) {
			if (sect.label.equals(sectionName)) {
				return sect;
			}
		}
		return null;
	}
	
	
	/**
	 * Get all the simple sections with a particular label
	 * 
	 * @param sectionName - the desired label
	 * @return - a collection of all the sections with that label
	 * @author mjd
	 */
	public Collection<ValueSection> getSections(String sectionName) {
		Collection<ValueSection> res = new LinkedList<ValueSection>();
		for (ValueSection sect : studyData.items) {
			if (sect.label.equals(sectionName)) {
				res.add(sect);
			}
		}
		return res;
	}

	
	/**
	 * return a list of the analysis sections associated with this study
	 * 
	 * @return - the list of {@see ValueAnalysisSection}
	 * @author mjd
	 */
	public List<ValueAnalysisSection> getAnalysisSections() {
		return studyData.analyses;
	}

	
	/**
	 * return the matrix sections associated with this study
	 * 
	 * @return a collection of <tt>MATRIX</tt> sections
	 * @author mjd
	 */
	public Collection<ValueSection> getMatrixSections() {
		return getSections("MATRIX");
	}
	
	/**
	 * return the matrix sections associated with this study
	 * 
	 * <p>Extracts the <tt>OUTPUT_TREE</tt> sections from the 
	 * analyses and returns them in a {@see Collection}.</p>
	 *
	 * @return a collection of tree sections
	 * @author mjd
	 */
	public Collection<ValueSection> getTreeSections() {
		Collection<ValueSection> treeSections = new LinkedList<ValueSection>();
		for (ValueAnalysisSection an : getAnalysisSections()) {
			treeSections.addAll(an.getTreeSections());
		}
		return treeSections;
	}
	
	/**
	 * Check for and return all the matrices for this study
	 * 
	 * <p>For each matrix mentioned in the description, look for it in the database.
	 * If every matrix is present, make a {@see Map} that maps TB1 matrix ID strings
	 * to {@see Matrix} objects and return the Map.  </p>
	 * 
	 * <p>If any of the study's matrices are absent from the matrix, return <b>null</b>.  
	 * If any matrix queries produce an ambiguous result, throw {@see MultipleMatchError}.</p>
	 * 
	 * 
	 * @param matrixService - a {@see MatrixService} object that can query the database
	 * @return - the Map described above
	 * @author mjd
	 */
	public Map<String,Matrix> getMatrices(MatrixService matrixService) {
		Map<String,Matrix> results = new HashMap<String,Matrix> ();
		for (ValueSection mSection : getMatrixSections()) {
			String title = mSection.getsval("matrix_id");
			Set<Matrix> matrices = new HashSet<Matrix> ();
			{
				Collection<TBPersistable> ms = matrixService.findSomethingByString(Matrix.class, "TB1MatrixID", title, true);
				for (TBPersistable tbp : ms) matrices.add((Matrix) tbp);
			}
			// Couldn't find it by legacy ID; try this other method
			if (matrices.isEmpty()) matrices = matrixService.findMatricesByTitle(title);
			
			if (matrices.isEmpty()) {
				return null;
			} else  {
				results.put(title, oneItem(matrices,
										new MultipleMatchError("Search for matrix with title " + title + " produced multiple results")));
			}
		}
		return results;
	}
	
	/**
	 * Check for and return all the trees for this study
	 * 
	 * <p>Just like {@see getMatrices}, but for trees instead of matrices.</p>
	 * 
	 * @param treeService - a {@see PhyloTreeService} object that can query the database
	 * @return - a Map that maps tree ID strings to {@see PhyloTree} objects
	 * @author mjd
	 */
	public Map<String,PhyloTree> getTrees(PhyloTreeService treeService) {
		Map<String,PhyloTree> results = new HashMap<String,PhyloTree> ();
		for (ValueSection mSection : getTreeSections()) {
			String label = mSection.getsval("tree_id");

			Set<PhyloTree> trees = new HashSet<PhyloTree> ();
			{
				Collection<TBPersistable> ts = treeService.findSomethingByString(PhyloTree.class, "TB1TreeID", label, true);
				for (TBPersistable tbp : ts) trees.add((PhyloTree) tbp);
			}
			// Couldn't find it by legacy ID; try this other method
			if (trees.isEmpty()) trees =  treeService.findTreesByLabel(label);

			if (trees.isEmpty()) {
				return null;
			} else  {
				results.put(label, oneItem(trees,
										new MultipleMatchError("Search for tree with label " + label + " produced multiple results")));
			}
		}
		return results;
	}
			
	
	/**
	 * Fetch the {@see Study} object to which this data refers
	 * 
	 * <p>Search in the database for a {@see Study} object with the appropriate
	 * TB1 study ID string.  If a unique match is found, return it.  Otherwise,
	 * return <b>null</b>.</p>
	 * 
	 * @param studyService -a {@see StudyService} object that can query the database
	 * @return the {@see Study} object with correspnding ID, or <b>null</b>
	 * @author mjd
	 */
	public Study study(StudyService studyService) {
		if (theStudy != null) {
			return theStudy;
		}
		
		Collection<Study> studies =	studyService.findByTBStudyID(getStudyID());
		if (studies.size() == 1) {
			theStudy = studies.iterator().next();
			return theStudy;
		} else {
			return null;
		}
	}
	
	
	/**
	 * Return the TB1 study ID string for this data
	 * 
	 * <p>For the example above, this is <code>"S1x28x96c14c14c15"</code>.</p>
	 * 
	 * <p>This is equivalent to <code>getString(null, "study_id")</code>.</p>
	 * 
	 * @return The study ID string
	 * @author mjd
	 */
	public String getStudyID() {
		return getString(null, "study_id");
	}
	
	/**
	 * Extract the unique element of a singleton {@see Set}.
	 * 
	 * <p>If the supplied set has a single element, that element is returned.
	 * Otherwise, if an error object <i>e</i> is supplied, the error is thrown.
	 * Otherwise, the function returns <b>null</b>.</p>
	 * 
	 * @param set - set of type T
	 * @param e - an error to be thrown if the set fails to have exactly one element
	 * @return The single element of type T from the set, or <b>null</b>
	 * 
	 */
	private <T> T oneItem(Set<T> set, Error e) {
		if (set.size() == 1) {
			return set.iterator().next();
		} else if (e != null) {
			throw e;
		} else {
			return null;
		}
	}

	/**
	 * Returns a string describing the date and time the study was submitted to TB1
	 * 
	 * <p>In the example above, the string is <code>"1/28/96 14:14:16"</code>.</p>
	 * 
	 * @return the date-and-time string
	 * @author mjd 20080804
	 */
	public String getCreationDateString() {
		ValueSection creationSection = getCreationHistorySection();
		if (creationSection == null) { return null; }
		return getString("HISTORY", "date") + " "
		+ getString("HISTORY", "time");
	}
	
	/**
	 * Return the <tt>HISTORY</tt> section that describes the creation of this study
	 * 
	 * @author mjd 20080804
	 */
	public ValueSection getCreationHistorySection() {
		for ( ValueSection sect : getSections("HISTORY")) {
			if (sect.getString("event").equals("New Study Added")) {
				return sect;
			}
		}
		return null;
	}

	/**
	 * Returns the date that the study was created in TB1
	 * 
	 * @return the date, as a Date object
	 * @author mjd 20080804
	 */
	public Date getCreationDate() {
		String creationDateString = getCreationDateString();
		if (creationDateString == null) {
			return null;
		}
		try {
			SimpleDateFormat ds = new SimpleDateFormat("M/d/yy KK:mm:ss");
			return ds.parse(creationDateString);
		}  catch (ParseException e) { /* ignore it */ }
		return null;
	}

	
	/**
	 * Is this study marked to be skipped for some reason?
	 * 
	 * <p>If the data objects main section contains a value of the form
	 * <pre>
	 *   skip = <i>anything</i>
	 * </pre>
	 * 
	 * then it should be skipped.  This feature can be used to skip over 
	 * broken or malformed sections of the data.  The method returns true if
	 * and only if such an item is present.</p>
	 * 
	 * @return - true if there is a "skip" marker
	 * @author mjd 20080805
	 */
	public boolean skip() {
		return getString(null, "skip") != null;
	}
}

