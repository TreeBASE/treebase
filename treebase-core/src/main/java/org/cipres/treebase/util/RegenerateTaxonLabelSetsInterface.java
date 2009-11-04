
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
