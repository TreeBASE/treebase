
package org.cipres.treebase.service.study;

import java.util.Calendar;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationHome;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.CitationStatus;
import org.cipres.treebase.domain.study.CitationStatusHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * CitationServiceImpl.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class CitationServiceImpl extends AbstractServiceImpl implements CitationService {

	private CitationHome mCitationHome;
	private CitationStatusHome mCitationStatusHome;

	/**
	 * Constructor.
	 */
	public CitationServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getCitationHome();
	}

	/**
	 * Return the CitationHome field.
	 * 
	 * @return CitationHome mCitationHome
	 */
	private CitationHome getCitationHome() {
		return mCitationHome;
	}

	/**
	 * Set the CitationHome field.
	 */
	public void setCitationHome(CitationHome pNewCitationHome) {
		mCitationHome = pNewCitationHome;
	}

	/**
	 * Return the CitationStatusHome field.
	 * 
	 * @return CitationStatusHome mCitationStatusHome
	 */
	private CitationStatusHome getCitationStatusHome() {
		return mCitationStatusHome;
	}

	/**
	 * Set the CitationStatusHome field.
	 */
	public void setCitationStatusHome(CitationStatusHome pNewCitationStatusHome) {
		mCitationStatusHome = pNewCitationStatusHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationService#deleteCitation(org.cipres.treebase.domain.study.Citation)
	 */
	public void deleteCitation(Citation pCitation) {
		if (pCitation == null) {
			return;
		}

		// relationship managmenet:
		// * study: the study relatioinship should be handled by caller!
		Study study = pCitation.getStudy();
		if (study != null && study.getCitation() == pCitation) {
			// we might have a problem here, the caller better assign a new citation
			// before commit:
			study.setCitation(null);
		}
		pCitation.setStudy(null);

		getCitationHome().deletePersist(pCitation);

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationService#replaceCitation(org.cipres.treebase.domain.study.Study,
	 *      org.cipres.treebase.domain.study.Citation)
	 */
	public void replaceCitation(Study pStudy, Citation pCitation) {
		if (pCitation == null || pStudy == null) {
			return;
		}

		Citation oldCitation = pStudy.getCitation();
		if (!pCitation.isCitationTypeChanged() && pStudy.getCitation() == pCitation) {
			update(pCitation);
		} else {
			Citation newCitation = Citation.factory(pCitation);

			newCitation.setStudy(pStudy);
			pStudy.setCitation(newCitation);

			if (oldCitation != null) {
				oldCitation.setStudy(null);
				getCitationHome().deletePersist(oldCitation);
			}

			update(pStudy);
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationService#findCitationStatusByDescription(java.lang.String)
	 */
	public CitationStatus findCitationStatusByDescription(String pDescription) {
		return getCitationStatusHome().findStatusByDescription(pDescription);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationService#createCitation(java.lang.String)
	 */
	public Citation createCitation(String pCitationType) {
		Citation c = Citation.factory(pCitationType);

		// set default date
		int current_month = Calendar.getInstance().get(Calendar.MONTH);
		int current_year = Calendar.getInstance().get(Calendar.YEAR);
		int current_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (current_month >= 9 && current_day == 1) {
			current_year++;
		}

		// set default value for publication year
		c.setPublishYear(current_year);

		c.setCitationStatus(getCitationStatusHome().findStatusInPrep());

		return c;
	}

	@Override
	public Class defaultResultClass() {
		return Citation.class;
	}

}
