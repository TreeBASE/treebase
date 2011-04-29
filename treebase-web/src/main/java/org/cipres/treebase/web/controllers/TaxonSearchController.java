package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.SearchService;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.web.model.SearchCommand;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.z3950.zing.cql.CQLAndNode;
import org.z3950.zing.cql.CQLBooleanNode;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLNotNode;
import org.z3950.zing.cql.CQLOrNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLParser;
import org.z3950.zing.cql.CQLTermNode;

public class TaxonSearchController extends SearchController {
	static final Logger LOGGER = Logger.getLogger(TaxonSearchController.class);
	
	private TaxonHome mTaxonHome;
	private TaxonLabelHome mTaxonLabelHome;
	private enum SearchIndex { LABEL, ID };
	private enum SearchTable { TAXON, TAXONLABEL, TAXONVARIANT };
	private enum NamingAuthority { TREEBASE, UBIO, NCBI };
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object searchCommand, BindException errors)
	throws Exception {
		clearMessages(request);
		String formName = request.getParameter("formName");
		String query = request.getParameter("query");
		if ( ! TreebaseUtil.isEmpty(query) ) {
			return this.handleQueryRequest(request, response, errors, query);
		}
		if (formName.equals("searchByTaxonLabel")) {
			SearchCommand newSearchCommand = (SearchCommand)searchCommand;
			String searchOn = request.getParameter("searchOn");
			String searchTerm = convertStars(request.getParameter("searchTerm"));
			String[] searchTerms = searchTerm.split("\\r\\n");	
			Collection<Taxon> taxa = new HashSet<Taxon>();
			for ( int i = 0; i < searchTerms.length; i++ ) {
				if ( searchOn.equals("TextSearch") ) {
					taxa.addAll(doTaxonSearch(request, newSearchCommand, searchTerms[i], SearchIndex.LABEL, null));
				}
				else if ( searchOn.equals("Identifiers") ) {
					String objectIdentifier = request.getParameter("objectIdentifier");
					if ( objectIdentifier.equals("TreeBASE") ) {
						taxa.addAll(doTaxonSearch(request,newSearchCommand,searchTerms[i],SearchIndex.ID,NamingAuthority.TREEBASE));
					}
					else if ( objectIdentifier.equals("NCBI") ) {
						taxa.addAll(doTaxonSearch(request,newSearchCommand,searchTerms[i],SearchIndex.ID,NamingAuthority.NCBI));
					}
					else if ( objectIdentifier.equals("uBio") ) {
						taxa.addAll(doTaxonSearch(request,newSearchCommand,searchTerms[i],SearchIndex.ID,NamingAuthority.UBIO));
					}								
				}	
			}
			TaxonSearchResults tsr = new TaxonSearchResults(taxa);
			saveSearchResults(request, tsr);
			if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {
				return samePage(request);
			}
			else {
				return this.searchResultsAsRDF(tsr, request, null,"taxon","taxon");
			}			
			
		} else if (formName.equals("taxonResultsAction")) {
			return modifySearchResults(request, response, errors);
		} else {
			return super.onSubmit(request, response, (SearchCommand) searchCommand, errors);
		}
	}
	
	protected Set<Taxon> doCQLQuery(CQLNode node, Set<Taxon> results, HttpServletRequest request) {
		if ( node instanceof CQLBooleanNode ) {
			Set<Taxon> resultsLeft = doCQLQuery(((CQLBooleanNode)node).left,results, request);
			Set<Taxon> resultsRight = doCQLQuery(((CQLBooleanNode)node).right,results, request);
			if ( node instanceof CQLNotNode ) {
				for ( Taxon rightTaxon : resultsRight ) {
					if ( resultsLeft.contains(rightTaxon) ) {
						resultsLeft.remove(rightTaxon);
					}
				}
			}
			else if ( node instanceof CQLOrNode ) {
				resultsLeft.addAll(resultsRight);
			}
			else if ( node instanceof CQLAndNode ) {
				Set<Taxon> resultsUnion = new HashSet<Taxon>();
				for ( Taxon leftTaxon : resultsLeft ) {
					if ( resultsRight.contains(leftTaxon) ) {
						resultsUnion.add(leftTaxon);
					}
				}				
				resultsLeft = resultsUnion;
			}
			results = resultsLeft;
		}
		else if ( node instanceof CQLTermNode ) {
			CQLTermNode term = (CQLTermNode)node;
			String index = term.getIndex();
			
			// CQL predicate is a title, will do a text search
			if ( index.startsWith("tb.title") ) {
				boolean exactMatch = term.getRelation().getBase().equals("==");
				SearchTable searchTable = null;
				if ( index.endsWith("taxonLabel") ) {
					searchTable = SearchTable.TAXONLABEL;
				}
				else if ( index.endsWith("taxonVariant") ) {
					searchTable = SearchTable.TAXONVARIANT;
				}
				else if ( index.endsWith("taxon") ) {
					searchTable = SearchTable.TAXON;
				}
				boolean caseSensitive = true;
				if ( ! term.getRelation().getModifiers().isEmpty() ) {
					caseSensitive = ! term.getRelation().getModifiers().firstElement().getType().equalsIgnoreCase("ignoreCase");
				}
				results.addAll(doTextSearch(term.getTerm(), caseSensitive, exactMatch, searchTable));
			}
			
			// CQL predicate is an identifier, will do id search
			else if ( index.startsWith("tb.identifier") ) {
				NamingAuthority namingAuthority = null;
				if ( index.endsWith("ncbi") ) {
					namingAuthority = NamingAuthority.NCBI;
				}
				else if ( index.endsWith("ubio") ) {
					namingAuthority = NamingAuthority.UBIO;
				}
				else {
					namingAuthority = NamingAuthority.TREEBASE;
				}
				results.addAll(doIdentifierSearch(request, term.getTerm(), namingAuthority, index));
			} else {
				// issue warnings
				addMessage(request, "Unsupported index: " + index);
			}
			
		}
		logger.debug(node);
		return results;		
	}
	
	private ModelAndView modifySearchResults(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws InstantiationException {
		TaxonSearchResults results = searchResults(request).convertToTaxa();
		
		String buttonName = request.getParameter("taxonResultsaction");
		if (buttonName.equals("addCheckedToResults")) {
//			Map<String,String> parameterMap = request.getParameterMap();
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

	protected Collection<Taxon> doTaxonSearch(HttpServletRequest request,
			SearchCommand searchCommand, String searchTerm, SearchIndex searchIndex, NamingAuthority namingAuthority) throws Exception {
		
		Collection<Taxon> taxa = new HashSet<Taxon> ();
		switch(searchIndex) {
			case LABEL:
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
				for ( SearchTable searchTable : createSearchTableEnum(request) ) {
					taxa.addAll(doTextSearch(searchTerm, caseSensitive, exactMatch, searchTable));
				}
				break;
			case ID:
				taxa.addAll(doIdentifierSearch(request,searchTerm, namingAuthority, null));
				break;
		}
		
		getTaxonLabelService().resurrectAll(taxa);
		LOGGER.debug("Found " + taxa.size() + " taxa");
		return taxa;
//		TaxonSearchResults tsr = new TaxonSearchResults(taxa);
//		saveSearchResults(request, tsr);
//		return samePage(request);	
	}
	
	/**
	 * 
	 * 
	 * @param request
	 * @param results
	 */
	private Collection<Taxon> doIdentifierSearch(HttpServletRequest request, String identifier, NamingAuthority namingAuthority, String index) {		
		Collection<Taxon> taxaFound = new ArrayList<Taxon>();
		switch(namingAuthority) {
			case TREEBASE :
				LOGGER.debug("Going to search for TreeBASE IDs");	
				if ( ! index.endsWith(".tb1") ) {
					TreebaseIDString idstr;
					try {
						idstr = new TreebaseIDString(identifier, Taxon.class, true);
						Taxon match = getTaxonHome().findPersistedObjectByID(idstr.getTBClass(), idstr.getId());
						if ( match != null ) {
							taxaFound.add(match);
						}					
					} catch (MalformedTreebaseIDString e) {
						addMessage(request, "Ignoring malformed taxon ID string '" + identifier + "'; try 'Tx####' or just a number");
					}
				}
				// looking up by legacy IDs, which we might have for Taxon and TaxonVariant
				else {
					LOGGER.debug("Searching for legacy " + index);
					Integer tb1LegacyId = null;
					try {
						tb1LegacyId = Integer.parseInt(identifier);
					} catch ( NumberFormatException e ) {
						addMessage(request, "Ignoring malformed TreeBASE1 ID string '" + identifier + "', because: " + e.getMessage());
						LOGGER.error("Couldn't parse legacy ID: "+e.getMessage());
					}
					if ( null != tb1LegacyId && index.matches(".*taxonVariant.*") ) {
						TaxonVariant tv = getTaxonHome().findVariantByTB1LegacyId(tb1LegacyId);
						LOGGER.debug("Found taxon variant: " + tv.getId());
						if ( null != tv.getTaxon() ) {
							taxaFound.add(tv.getTaxon());
						}
					}
					else if ( null != tb1LegacyId ){
						Taxon taxon = getTaxonHome().findByTB1LegacyId(tb1LegacyId);
						LOGGER.debug("Found taxon: " + taxon.getId());
						if ( null != taxon ) {
							taxaFound.add(taxon);
						}
					}					
				}
				break;
			case NCBI :
				LOGGER.debug("Going to search for NCBI taxon ids");	
				Taxon match = getTaxonHome().findByNcbiTaxId(Integer.parseInt(identifier));
				if ( match != null ) {
					taxaFound.add(match);
				}
				break;
			case UBIO :
				LOGGER.debug("Going to search for uBio nameBankIDs");
				Taxon match1 = getTaxonHome().findByUBIOTaxId(Long.parseLong(identifier));
				if ( match1 != null ) {
					taxaFound.add(match1);
				}
				break;
		}
		return taxaFound;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private Set<SearchTable> createSearchTableEnum(HttpServletRequest request) {
		Set<SearchTable> results = new HashSet<SearchTable>();
		String[] stringProperties = request.getParameterValues("stringProperty");	
		for ( int i = 0; i < stringProperties.length; i++ ) {
			if ( stringProperties[i].equals("taxonLabel") ) {
				results.add(SearchTable.TAXONLABEL);
			}
			else if ( stringProperties[i].equals("taxonVariant") ) {
				results.add(SearchTable.TAXONVARIANT);
			}
			else if ( stringProperties[i].equals("taxon") ) {
				results.add(SearchTable.TAXON);
			}			
		}
		return results;
	}
	
	/**
	 * 
	 * 
	 * @param request
	 * @param results
	 */
	private Collection<Taxon> doTextSearch(String pattern, boolean caseSensitive, boolean exactMatch, SearchTable searchTable) {
		LOGGER.debug("Going to search for strings");
		LOGGER.debug("Case sensitive? " + caseSensitive);
		LOGGER.debug("Exact match? " + exactMatch);		
		TaxonLabelService tls = getTaxonLabelService();
		Collection<Taxon> taxaFound = new ArrayList<Taxon>();
		switch(searchTable) {
			case TAXONLABEL :
				LOGGER.debug("Will search taxon labels");
				Collection<TaxonLabel> labelsFound = exactMatch 
					? tls.findByExactString(pattern) 
					: tls.findBySubstring(pattern, caseSensitive);
				for ( TaxonLabel label : labelsFound ) {
					if ( label.getTaxonVariant() != null && label.getTaxonVariant().getTaxon() != null ) {
						taxaFound.add(label.getTaxonVariant().getTaxon());
					}
				}
				break;
			case TAXONVARIANT :
				LOGGER.debug("Will search taxon variants");
				Collection<TaxonVariant> variantsFound = exactMatch 
					? tls.findTaxonVariantByFullName(pattern) 
					: tls.findTaxonVariantWithSubstring(pattern, caseSensitive);
				for ( TaxonVariant variant : variantsFound ) {
					if ( variant.getTaxon() != null ) {
						taxaFound.add(variant.getTaxon());
					}
				}
				break;
			case TAXON :
				LOGGER.debug("Will search taxa");
				Collection<Taxon> tmpTaxaFound = exactMatch 
					? tls.findTaxaByName(pattern) 
					: tls.findTaxaBySubstring(pattern, caseSensitive);
				taxaFound.addAll(tmpTaxaFound);
				break;
		}
		return taxaFound;
	}

//	private void saveTempSearchResults(HttpServletRequest request,
//			TaxonSearchResults taxonSearchResults) {
//		request.getSession().setAttribute("taxonSearchResults", taxonSearchResults);
//	}

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

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors, String query)
			throws CQLParseException, IOException, InstantiationException {
		//String query = request.getParameter("query");
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(query);
		root = normalizeParseTree(root);
		Set<Taxon> queryResults = doCQLQuery(root, new HashSet<Taxon>(),request);
		TaxonSearchResults tsr = new TaxonSearchResults(queryResults);
		saveSearchResults(request, tsr);
		if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {
			return samePage(request);
		}
		else {
			SearchResults<?> res = tsr;
			String schema = null;
			if ( ! TreebaseUtil.isEmpty(request.getParameter("recordSchema")) ) {
				schema = request.getParameter("recordSchema");
				if ( schema.equals("tree" ) ) {
					res = tsr.convertToTrees();
				}
				else if ( schema.equals("matrix") ) {
					res = tsr.convertToMatrices();
				}
				else if ( schema.equals("study") ) {
					res = tsr.convertToStudies();
				}
			}
			this.saveSearchResults(request, res);
			return this.searchResultsAsRDF(res, request, root,schema,"taxon");
		}
	}

	@Override
	protected Map<String, String> getPredicateMapping() {
		Map<String,String> mapping = new HashMap<String,String>();
		mapping.put("dcterms.title", "tb.title.taxon");
		mapping.put("dcterms.identifier", "tb.identifier.taxon");		
		return mapping;
	}
}
