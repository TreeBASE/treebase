/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.util;

import java.util.Set;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;

/**
 * @author mjd 20090223
 *
 */
public interface RegenerateTaxonLabelSetsInterface extends Standalone {
	public TaxonLabelService getTlService();
	public void setTlService(TaxonLabelService tlService);
	public TaxonLabelHome getTlHome();
	public void setTlHome(TaxonLabelHome tlHome);
	public StudyService getStudyService();
	public void setStudyService(StudyService studyService);
	public StudyHome getStudyHome();
	public void setStudyHome(StudyHome studyHome);
		
	public Set<TaxonLabelSet> createTaxonLabelSets(Study s,  boolean justTrees, boolean justMatrices);
	public Set<TaxonLabelSet> createTaxonLabelSets(Long sid, boolean justTrees, boolean justMatrices);

	public Set<TaxonLabelSet> createTaxonLabelSets(Study s);
	public Set<TaxonLabelSet> createTaxonLabelSets(Long sid);
}
