package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.SearchService;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.web.model.SearchCommand;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class TaxonSearchController extends SearchController {
	static final Logger LOGGER = Logger.getLogger(TreeSearchController.class);
	
	private TaxonHome mTaxonHome;
	private TaxonLabelHome mTaxonLabelHome;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object searchCommand, BindException errors)
	throws Exception {
		clearMessages(request);
		String formName = request.getParameter("formName");
		if (formName.equals("searchByTaxonLabel")) {
			SearchCommand newSearchCommand = (SearchCommand)searchCommand;
			ModelAndView modelAndView = doTaxonSearch(request, response, newSearchCommand, errors);			
			return modelAndView;	
		} else if (formName.equals("taxonResultsAction")) {
			return modifySearchResults(request, response, errors);
		} else {
			return super.onSubmit(request, response, (SearchCommand) searchCommand, errors);
		}
	}
	
	private ModelAndView modifySearchResults(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws InstantiationException {
		TaxonSearchResults results = searchResults(request).convertToTaxa();
		
		String buttonName = request.getParameter("taxonResultsaction");
		if (buttonName.equals("addCheckedToResults")) {
			Map<String,String> parameterMap = request.getParameterMap();
			Collection<Taxon> newTaxa = new HashSet<Taxon> ();
			for (String taxonIdString : request.getParameterValues("selection")) {
				Taxon tx;
				try {
					tx = getTaxonLabelService().findTaxonByIDString(taxonIdString);
				} catch (MalformedTreebaseIDString e) {
					// This can only occur if the ID numbers in our web form are
					// malformed, so it represents a programming error.  20090312 MJD
					throw new Error(e);
				}
				if (tx != null) newTaxa.add(tx);
			}
			results.union(newTaxa);
			saveSearchResults(request, results);
		}
		return samePage(request);
	}

	protected ModelAndView doTaxonSearch(HttpServletRequest request,
			HttpServletResponse response,
			SearchCommand searchCommand, BindException errors) throws Exception {
		String searchTerm = convertStars(request.getParameter("searchTerm"));
		String[] searchTerms = searchTerm.split("\\r\\n");
		String searchOn = request.getParameter("searchOn");
		//Collection<TaxonVariant> tvs = new ArrayList<TaxonVariant>();
		Collection<Taxon> taxa = new HashSet<Taxon> ();
		if ( searchOn.equals("TextSearch") ) {
			taxa = doTextSearch(request,searchTerms);
		}
		else if ( searchOn.equals("Identifiers") ) {
			taxa = doIdentifierSearch(request,searchTerms);			
		}
		
		/*
		tvs = getTaxonLabelService().findTaxonVariantByFullName(searchTerm);
		LOGGER.debug("Found " + tvs.size() + " tvs");
		for (TaxonVariant tv : tvs) {
			if (tv.getTaxon() != null) taxa.add(tv.getTaxon());
		}
		*/
		
		getTaxonLabelService().resurrectAll(taxa);
		LOGGER.debug("Found " + taxa.size() + " taxa");

		TaxonSearchResults tsr = new TaxonSearchResults(taxa);
		//saveTempSearchResults(request, tsr);
		saveSearchResults(request, tsr);
		return samePage(request);	
	}
	
	/**
	 * 
	 * 
	 * @param request
	 * @param results
	 */
	private Collection<Taxon> doIdentifierSearch(HttpServletRequest request,String[] identifiers) {
		String objectIdentifier = request.getParameter("objectIdentifier");
		Collection<Taxon> taxaFound = new ArrayList<Taxon>();
		if ( objectIdentifier.equals("TreeBASE") ) {
			LOGGER.debug("Going to search for TreeBASE IDs");	
			for ( int i = 0; i < identifiers.length; i++ ) {
				TreebaseIDString idstr;
				try {
					idstr = new TreebaseIDString(identifiers[i], Taxon.class, true);
				} catch (MalformedTreebaseIDString e) {
					addMessage(request, "Ignoring malformed taxon ID string '" + identifiers[i] + "'; try 'Tx####' or just a number");
					continue;
				}
				Taxon match = getTaxonHome().findPersistedObjectByID(idstr.getTBClass(), idstr.getId());
				if ( match != null ) {
					taxaFound.add(match);
				}
			}			
		}
		else if ( objectIdentifier.equals("NCBI") ) {
			LOGGER.debug("Going to search for NCBI taxon ids");	
			for ( int i = 0; i < identifiers.length; i++ ) {
				Taxon match = getTaxonHome().findByNcbiTaxId(Integer.parseInt(identifiers[i]));
				if ( match != null ) {
					taxaFound.add(match);
				}
			}
		}
		else if ( objectIdentifier.equals("uBio") ) {
			LOGGER.debug("Going to search for uBio nameBankIDs");
			for ( int i = 0; i < identifiers.length; i++ ) {
				Taxon match = getTaxonHome().findByUBIOTaxId(Long.parseLong(identifiers[i]));
				if ( match != null ) {
					taxaFound.add(match);
				}
			}			
		}
		return taxaFound;
	}
	
	/**
	 * 
	 * 
	 * @param request
	 * @param results
	 */
	private Collection<Taxon> doTextSearch(HttpServletRequest request, String[] stringPatterns) {
		LOGGER.debug("Going to search for strings");
		Collection<Taxon> taxaFound = new ArrayList<Taxon>();
		String[] stringProperties = request.getParameterValues("stringProperty");	
		String[] stringModifiers = request.getParameterValues("stringModifier");
		boolean caseSensitive = false;
		boolean exactMatch = false;
		if ( stringModifiers != null ) {
			for ( int i = 0; i < stringModifiers.length; i++ ) {
				if ( stringModifiers[i].equals("caseSensitive") ) {
					caseSensitive = true;
				}
				if ( stringModifiers[i].equals("exactMatch") ) {
					exactMatch = true;
				}
			}
		}
		LOGGER.debug("Case sensitive? " + caseSensitive);
		LOGGER.debug("Exact match? " + exactMatch);
		if ( stringProperties != null ) {
			for ( int i = 0; i < stringProperties.length; i++ ) {
				for ( int j = 0; j < stringPatterns.length; j++ ) {
					if ( stringProperties[i].equals("taxonLabel") ) {
						LOGGER.debug("Will search taxon labels");
						Collection<TaxonLabel> labelsFound = new ArrayList<TaxonLabel>();
						if ( exactMatch ) {
							labelsFound = getTaxonLabelService().findByExactString(stringPatterns[j]);
						}
						else {
							labelsFound = getTaxonLabelService().findBySubstring(stringPatterns[j], caseSensitive);							
						}
						Iterator<TaxonLabel> labelsIterator = labelsFound.iterator();
						while ( labelsIterator.hasNext() ) {
							TaxonLabel label = labelsIterator.next();
							TaxonVariant variant = label.getTaxonVariant();
							if ( variant != null ) {
								Taxon taxon = variant.getTaxon();
								if ( taxon != null ) {
									taxaFound.add(taxon);
								}
							}
						}
					}
					if ( stringProperties[i].equals("taxonVariant") ) {
						LOGGER.debug("Will search taxon variants");
						Collection<TaxonVariant> variantsFound = new ArrayList<TaxonVariant>();
						if ( exactMatch ) {
							variantsFound = getTaxonLabelService().findTaxonVariantByFullName(stringPatterns[j]);
						}
						else {
							variantsFound = getTaxonLabelService().findTaxonVariantWithSubstring(stringPatterns[j], caseSensitive);
						}		
						Iterator<TaxonVariant> variantsIterator = variantsFound.iterator();
						while ( variantsIterator.hasNext() ) {
							TaxonVariant variant = variantsIterator.next();
							Taxon taxon = variant.getTaxon();
							if ( taxon != null ) {
								taxaFound.add(taxon);
							}
						}
					}
					if ( stringProperties[i].equals("taxon") ) {
						LOGGER.debug("Will search taxa");
						Collection<Taxon> tmpTaxaFound = new ArrayList<Taxon>();
						if ( exactMatch ) {
							tmpTaxaFound = getTaxonLabelService().findTaxaByName(stringPatterns[j]);
						}
						else {
							tmpTaxaFound = getTaxonLabelService().findTaxaBySubstring(stringPatterns[j], caseSensitive);
						}
						taxaFound.addAll(tmpTaxaFound);
					}
				}
			}
		}
		return taxaFound;
	}

	private void saveTempSearchResults(HttpServletRequest request,
			TaxonSearchResults taxonSearchResults) {
		request.getSession().setAttribute("taxonSearchResults", taxonSearchResults);
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	
	@Override
	SearchResultsType currentSearchType() {
		return SearchResultsType.TAXON;
	}

	@Override
	public String getDefaultViewURL() {
		return "taxonSearch.html";
	}

	public TaxonHome getTaxonHome() {
		return mTaxonHome;
	}

	public void setTaxonHome(TaxonHome taxonHome) {
		mTaxonHome = taxonHome;
	}

	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		mTaxonLabelHome = taxonLabelHome;
	}
}
