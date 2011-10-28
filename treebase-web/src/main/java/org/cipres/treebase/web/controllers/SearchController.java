


package org.cipres.treebase.web.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentWriter;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsFrozen;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.SearchService;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.service.AbstractService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.SearchCommand;
import org.cipres.treebase.web.util.SearchMessageSetter;
import org.cipres.treebase.web.util.WebUtil;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.z3950.zing.cql.CQLBooleanNode;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLRelation;
import org.z3950.zing.cql.CQLTermNode;


/**
 * @author mjd 20080701
 */
public abstract class SearchController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(SearchController.class);	
	protected SearchService searchService;
	protected String formView;
	private TaxonLabelService mTaxonLabelService;
	
	protected static final int FORMAT_NEXUS = 1;
	protected static final int FORMAT_NEXML = 2;
	private static String mNexmlContentType = "application/xml; charset=UTF-8";
	protected String mFormatParameter = "format";
	private TaxonLabelHome mTaxonLabelHome;	
	
	protected abstract ModelAndView handleQueryRequest(HttpServletRequest request,HttpServletResponse response,BindException errors,String query) throws CQLParseException, IOException, InstantiationException, ParseException;
	
	/**
	 * Recursively traverses a CQL parse tree (presumably starting from its
	 * root node), replacing the observed indices with ones suggested by the map
	 * returned by getPredicateMapping(). The function of this is to allow concrete
	 * subclasses to create specific mappings from more general ones, e.g. when a
	 * study search query refers to the index "dcterms.identifier", the StudySearchController
	 * can specify that within its context this index is understood to mean "tb.identifier.study",
	 * i.e. the study ID.
	 * @param node
	 * @return
	 */
	protected CQLNode normalizeParseTree(CQLNode node) {
		if ( node instanceof CQLBooleanNode ) {
			((CQLBooleanNode)node).left = normalizeParseTree(((CQLBooleanNode)node).left);
			((CQLBooleanNode)node).right = normalizeParseTree(((CQLBooleanNode)node).right);
			return node;
		}
		else if ( node instanceof CQLTermNode ) {
			String index = ((CQLTermNode)node).getIndex();
			String term = ((CQLTermNode)node).getTerm();
			CQLRelation relation = ((CQLTermNode)node).getRelation();
			Map<String,String> mapping = getPredicateMapping();
			for ( String key : mapping.keySet() ) {
				index = index.replaceAll(key, mapping.get(key));
			}
			return new CQLTermNode(index,relation,term);
		}
		logger.debug(node);
		return node;
	}	
	
	/**
	 * Returns a mapping between more general search indices (e.g. "dcterms.identifier")
	 * and the specific indices (e.g. "tb.identifier.study") that are understood to be used 
	 * by the concrete subclass
	 * @return
	 */
	protected abstract Map<String,String> getPredicateMapping();

	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {

		LOGGER.info("in generic SearchController.onSubmit");

		clearMessages(request);
		String formName = request.getParameter("formName");

		LOGGER.info("formName is '" + formName + "'");

		if (formName == null) {
			throw new Error("No form name supplied");
		} else if (formName.equals("resultsAction")) {
			if (request.getParameterMap().containsKey("refineSearch")) {
				// TODO
			} else if (request.getParameterMap().containsKey("discardResults")) {
				onSubmitDiscardResults(request, response, command, errors);
			} else if (request.getParameterMap().containsKey("action")) {
				String action = request.getParameter("action");
				if (action.equals("discardResults")) {
					onSubmitDiscardResults(request, response, command, errors);
				} else if ( action.equals("refineSearch") ){
					onSubmitRefineSearch(request,response,command,errors);					
				} else if ( action.equals("downloadAllTrees") ) {
					downloadAllTrees(request, response, errors);
					return null;
				}
				else {
					throw new Error("Unknown action '" + action + "'");
				}
			} else {
				throw new Error("Couldn't find button selection");	
			}
		} else if (formName.equals("searchResultsConversion")) {			
			onSubmitConversion(request, response, command, errors);
		} else if (formName.equals("discardResults")) {
			onSubmitDiscardResults(request, response, command, errors);
		} else {
			throw new Error("Unknown form name '" + formName + "'");
		}
		
		LOGGER.debug("using selectResultsView to determine view");
		return selectResultsView(request);
	}
	
	protected void downloadAllTrees(HttpServletRequest request,
			HttpServletResponse response, BindException errors) {
		// TODO Auto-generated method stub
		
		try {
			java.util.Date date= new java.util.Date();

			TreeSearchResults treeResults = searchResults(request).convertToTrees();

			String fileName = "TB" + date.getTime();
			StringBuilder builder = new StringBuilder();
			
			if ( getFormat(request) == FORMAT_NEXML ) {
				Document doc = DocumentFactory.safeCreateDocument();
				NexmlDocumentWriter ndw = new NexmlDocumentWriter(null,getTaxonLabelHome(), doc);		
				ndw.fromTreeBaseToXml(treeResults);
				builder.append(doc.getXmlString());
			}
			else {
				Set<PhyloTree> trees =  treeResults.getResults();
				TreeBlock treeBlock = new TreeBlock();
				TaxonLabelSet taxonLabelSet = new TaxonLabelSet();
				
				for (PhyloTree pTree : trees) {
					for (TaxonLabel pTaxLabel : pTree.getTreeBlock().getTaxonLabelList()) {
						if (! taxonLabelSet.getTaxonLabelsReadOnly().contains(pTaxLabel)) {
							taxonLabelSet.addPhyloTaxonLabel(pTaxLabel);	
						}
					}
					treeBlock.addPhyloTree(pTree);	
				}
				
				treeBlock.setTaxonLabelSet(taxonLabelSet);
				taxonLabelSet.setTitle("TB" + date.getTime());

				
				builder.append("#NEXUS\n\n");
			
				taxonLabelSet.buildNexusBlockTaxa(builder, true, false);
			
				treeBlock.generateAFileDynamically(builder);
				fileName += ".nex";
			}
			
				String downloadDirName = request.getSession().getServletContext().getRealPath(
					TreebaseUtil.FILESEP + "NexusFileDownload")
					+ TreebaseUtil.FILESEP
					+ "TreeDownload";
			
			
			File directory = new File(downloadDirName);
			directory.mkdir();
			
			File file = new File(downloadDirName + TreebaseUtil.FILESEP + fileName);
			FileWriter out = new FileWriter(file);
			out.write(builder.toString());
			out.close();
			
			if ( getFormat(request) == FORMAT_NEXML ) {
				WebUtil.downloadFile(response, downloadDirName, fileName, mNexmlContentType);
			}			
			else {
				WebUtil.downloadFile(response, downloadDirName, fileName);
			}	

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
		/**
		 * 
		 * @param request
		 * @return
		 */
	protected int getFormat (HttpServletRequest request) {
			String requestedFormat = request.getParameter(mFormatParameter);
			if ( null != requestedFormat ) {
				if ( requestedFormat.equalsIgnoreCase("nexml") ) {
					return FORMAT_NEXML;
				}
				else {
					return FORMAT_NEXUS; // default
				}
			}
			else {
				return FORMAT_NEXUS; // default
			}
	}
	
	protected Properties getDefaultProperties(HttpServletRequest request) {
		Properties properties = new Properties();

		properties.setProperty("nexml.uri.base", TreebaseUtil.getPurlBase());
		return properties;
	}
	
	/**
	 * 
	 * @return
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * 
	 * @param nexmlService
	 */
	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		mTaxonLabelHome = taxonLabelHome;
	}
	
	protected ModelAndView searchResultsAsRDF (SearchResults<?> searchResults,HttpServletRequest request,CQLNode root, String schema, String original) throws UnsupportedEncodingException {
		//${baseURL}
		//${phyloWSPath}
		//${normalizedCQLQuery}
		//${searchResults}
		//${domainAddress}
		String phyloWSPath = "find/" + original;
		request.getSession().setAttribute("searchResultsThawed", searchResults);		
		StringBuffer domainAddress = new StringBuffer("http://");
		domainAddress
			.append(request.getServerName())
			.append(':')
			.append(request.getServerPort());
		StringBuffer baseURL = new StringBuffer(domainAddress);
		baseURL.append("/treebase-web/phylows");
		request.getSession().setAttribute("recordSchema", schema);
		request.getSession().setAttribute("format", request.getParameter("format"));
		request.getSession().setAttribute("baseURL", TreebaseUtil.getPurlBase());
		request.getSession().setAttribute("domainAddress", domainAddress.toString());
		request.getSession().setAttribute("phyloWSPath", phyloWSPath);
		request.getSession().setAttribute("originalSection", original);
		if ( null != root ) {			
			request.getSession().setAttribute("normalizedCQLQuery", URLEncoder.encode(root.toCQL(),"UTF-8"));
		}
		return new ModelAndView("searchResultsAsRDF");
	}
	
	protected <E extends TBPersistable> SearchResults<E> intersectSearchResults(
			SearchResults<E> oldRes,
			SearchResults<E> newRes, 
			SearchMessageSetter searchMessages,
			String noResultsMessage) {
		if (noResultsMessage == null) noResultsMessage = "Your search produced no results";
		
		if (! newRes.isAll()) 
			LOGGER.info("Search returned " + newRes.nItems());
		
		if (oldRes.isAll() || oldRes.isEmpty()) 
			return newRes;
		else if (newRes.isAll()) 
			return oldRes;
		else {
			@SuppressWarnings("unchecked")
			SearchResults<E> intersection = oldRes.clone();
			intersection.intersect(newRes);
			
			if (intersection.isEmpty()) {
				if (searchMessages != null) searchMessages.addMessage(noResultsMessage);
				return oldRes;
			} else {
				return intersection;
			}
		}
		
	}
	
	protected ModelAndView onSubmitRefineSearch(HttpServletRequest request, HttpServletResponse response,
		Object command, BindException errors) throws InstantiationException {
		String[] selectedItems = request.getParameterValues("selection");
		SearchResults<? extends TBPersistable> sr = recoverSearchResults(request);
		if (sr == null) {
			LOGGER.info("Retrieved NULL old search results");
		} 
		else {
			sr.announceSize(LOGGER);
		}
		
		if (sr == null) {
			sr = initialSearchResults();
		} 
		else if (sr.hasResultsList()) {
			Object[] resultsArray = ((Set<TBPersistable>)((SearchResults)sr).getResults()).toArray();
			REFINE: for ( int i = 0; i < resultsArray.length; i++ ) {
				TBPersistable refineCandidate = (TBPersistable)resultsArray[i];	
				if ( selectedItems != null ) {
					for ( int j = 0; j < selectedItems.length; j++ ) {
						Long id = Long.parseLong(selectedItems[j]);
						if ( id.compareTo(refineCandidate.getId()) == 0 ) {
							LOGGER.debug("Keeping result "+refineCandidate.getId());
							continue REFINE;
						}
					}
				}
				LOGGER.debug("Removing result "+refineCandidate.getId());
				sr.getResults().remove(refineCandidate);
			}
			sr.announceSize(LOGGER);
			AbstractService service = getSearchService().getAppropriateService(sr);
			service.resurrectAll(sr.getResults());
			saveSearchResults(request, sr);
		}
		if ( sr.size() == 0 ) {
			return onSubmitDiscardResults(request,response,command,errors);
		}
		return new ModelAndView(getFormView(), Constants.RESULT_SET, sr);
	}	

	public SearchResults<? extends TBPersistable> recoverSearchResults(
			HttpServletRequest request) {
		SearchResultsFrozen srFrozen = searchResultsFrozen(request);
		if (srFrozen == null) return null;
		return srFrozen.thaw();
	}

	public ModelAndView selectResultsView(HttpServletRequest request) {
		SearchResultsFrozen srFrozen = searchResultsFrozen(request);
		LOGGER.debug("In selectResultsView with results = " + srFrozen);
		
		if (srFrozen == null) {
			LOGGER.debug("No results\n\tContextPath: " + request.getContextPath() +
					"\n\tPathInfo: " + request.getPathInfo() +
					"\n\tPathTranslated: " + request.getPathTranslated() +
					"\n\tRequestURI: " + request.getRequestURI() +
					"\n\tRequestURL: " + request.getRequestURL() +
					"\n");
			String URL = getDefaultViewURL();
			LOGGER.debug("Using default view URL=" + URL);
			return new ModelAndView(new RedirectView(URL));
		} else {
			switch (srFrozen.getType()) {
			case MATRIX:
				LOGGER.debug("Issuing new RedirectView pointing to matrixSearch.html");
				return new ModelAndView(new RedirectView("matrixSearch.html"));
			case STUDY:
				LOGGER.debug("Issuing new RedirectView pointing to studySearch.html");
				return new ModelAndView(new RedirectView("studySearch.html"));
			case TREE:			
				return new ModelAndView(new RedirectView("treeSearch.html"));
			case TAXON:
				return new ModelAndView(new RedirectView("taxonSearch.html"));
			case NONE: // XXX What then??
			}
			// NOTREACHED
			throw new Error();
		}
	}
	
	public abstract String getDefaultViewURL();

	@Override
	// Restore default behavior: Only POST requests are submissions
	protected boolean isFormSubmission(HttpServletRequest request) {
		return request.getMethod().equals("POST");
	}

	/**
	 * Retrieve the list of existing user for the citation and provide a brand new form each time
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected SearchCommand formBackingObject(HttpServletRequest request) throws ServletException {

		// retrieve SearchCommand object from request scope
		SearchCommand searchCommand = new SearchCommand (request);

		return searchCommand;
	}

	
	protected SearchResults<?> searchResults(HttpServletRequest request) throws InstantiationException {
		SearchResults<? extends TBPersistable> sr = recoverSearchResults(request);
		if (sr == null) {
			LOGGER.info("Retrieved NULL old search results");
		} else {
			sr.announceSize(LOGGER);
		}
		
		if (sr == null) {
			sr = initialSearchResults();
		} else if (sr.hasResultsList()) {
			AbstractService service = getSearchService().getAppropriateService(sr);
			service.resurrectAll(sr.getResults());
		}

		return sr;
	}

	private SearchResultsFrozen searchResultsFrozen(
			HttpServletRequest request) {
		return (SearchResultsFrozen) request.getSession().getAttribute("searchResults");
	}

	protected SearchResultsType searchResultsType(HttpServletRequest request) {
		SearchResultsFrozen sr = searchResultsFrozen(request);

		if (sr == null) {
			return initialSearchResults().getResultType();
		} else {
			return sr.getType();
		}
	}
	
	protected void convertSavedSearchResults(HttpServletRequest request) throws InstantiationException {
		SearchResults<?> savedResults = searchResults(request); 
		if (savedResults == null) { return; }
		if (savedResults.getResultType() != currentSearchType()) {
			// if we're converting, we probably don't want to carry over the search term from the 
			// previous page.
			request.getSession().removeAttribute("searchTerm");
			SearchResults<?> convertedResults = savedResults.convertToType(currentSearchType());
			saveSearchResults(request, savedResults.convertToType(currentSearchType()));
			if ( convertedResults.hasResultsList() )  {
				String msg = 
					"Note: the "
					+ currentSearchType().toString().toLowerCase()
					+ " records below are the ones associated with the "
					+ savedResults.getResultType().toString().toLowerCase() 
					+ " records from your previous tab.";
				this.addMessage(request, msg);
			}
			LOGGER.debug("Autoconverted " + convertedResults.size() + " search results to type " + currentSearchType());
		}	
	}
	
	protected void clearMessages(HttpServletRequest req) {
		req.removeAttribute("searchMessage");		
	}

	@SuppressWarnings("unchecked")
	protected void addMessage(HttpServletRequest req, String msg) {
		List<String> s = (List<String>) req.getAttribute("searchMessage");
		if (s == null) { s = new ArrayList<String> (); }
		LOGGER.info("Adding message '" + msg + "' to searchMessage variable");
		s.add(msg);
		req.setAttribute("searchMessage", s);
	}

	@SuppressWarnings("unchecked")
	protected void addMessages(HttpServletRequest req, List<String> msgs) {
		List<String> s = (List<String>) req.getAttribute("searchMessage");
		if (s == null) { s = new ArrayList<String> (); }
		for (String msg : msgs) s.add(msg);
		req.setAttribute("searchMessage", s);
	}

	protected void saveSearchResults(HttpServletRequest request, SearchResults<?> results) {
		request.getSession().setAttribute("searchResults", results == null ? null : results.freeze());
		request.getSession().setAttribute("searchTerm", request.getParameter("searchTerm"));
		request.getSession().setAttribute("searchButton", request.getParameter("searchButton"));
	}

	abstract SearchResultsType currentSearchType() ;
	
	protected void discardSearchResults(HttpServletRequest request) {
		request.getSession().setAttribute("searchResults", initialSearchResults().freeze());		
	}

	protected ModelAndView onSubmitDiscardResults(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws InstantiationException {
				discardSearchResults(request);
				return new ModelAndView(getFormView(), Constants.RESULT_SET, searchResults(request));
			}

	protected ModelAndView onSubmitConversion(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {
				LOGGER.info("In onSubmitConversion()");
				SearchResults<?> newRes, oldRes = searchResults(request);
				String conversionType = request.getParameter("conversion");
			
				if (oldRes == null) {
					// The choice of StudySearchResults here is arbitrary
					// Guaranteeing a non-null oldRes makes the later logic simpler
					oldRes = (SearchResults<?>) new StudySearchResults();
				}
			
				if (conversionType.equals("toStudies")) {
					newRes = oldRes.convertToStudies();
					saveSearchResults(request, newRes);
					if (! oldRes.isEmpty())
						addMessage(request,"These results are the studies associated with the "+newRes.getResultType()+" on the previous tab");
					// TODO: this should change to search/basicStudySearch when you create that
					return new ModelAndView(new RedirectView("studySearch.html"));
				} else if (conversionType.equals("toMatrices")) {
					newRes = oldRes.convertToMatrices();
					saveSearchResults(request, newRes);
					if (! oldRes.isEmpty())
						addMessage(request,"These results are the matrices associated with the "+newRes.getResultType()+" on the previous tab");
					return new ModelAndView(new RedirectView("matrixSearch.html"));
				} else if (conversionType.equals("toTrees")) {
					newRes = oldRes.convertToTrees();
					saveSearchResults(request, newRes);
					if (! oldRes.isEmpty())
						addMessage(request, "These results are the trees associated with the "+newRes.getResultType()+" on the previous tab");							
					return new ModelAndView("search/treeSearch", Constants.RESULT_SET, newRes);
				} else {
					throw new Error("Unknown conversion type '" + conversionType + "'");
				}
			}
	
	SearchResults<? extends TBPersistable> initialSearchResults() {
		SearchResults<? extends TBPersistable> sr = SearchResults.fullResultsFactory(currentSearchType());
		return sr;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException bindException, Map model)
			throws Exception {
		String query = request.getParameter("query");
		if ( query != null && ! TreebaseUtil.isEmpty(query) ) {
			LOGGER.info("query is '"+ query +"'");
			return this.handleQueryRequest(request, response, bindException, query);
		}
		String action = request.getParameter("action");
		if (action != null && action.equals("discard") && request.getMethod().equals("GET")) {
			discardSearchResults(request);
			return new ModelAndView(new RedirectView(request.getRequestURI()));
		} else {
			convertSavedSearchResults(request);
		}
		if (model == null) {
			LOGGER.debug("null model");
			model = new HashMap<String,Object> ();
		}
		
		model.put(Constants.RESULT_SET, searchResults(request));
		return super.showForm(request, response, bindException, model);
	}
	
	public Collection<? extends TBPersistable> doSearchByIDString(HttpServletRequest req, 
			AbstractService service, Class expectedClass, String idString) {
		Collection<TBPersistable> matches = new ArrayList<TBPersistable> ();
		TBPersistable t;
		try {
			TreebaseIDString ids = new TreebaseIDString(idString, expectedClass);
			if (ids.getTBClass() != expectedClass) {
				String expectedPrefix = TreebaseIDString.getPrefixForClass(expectedClass);
				addMessage(req, "ID number '" + idString + "' is not valid; try " + expectedPrefix + "#### or just ####");
				return null;
			}
			t = service.findByIDString(idString);
			if (t != null) matches.add(t);
		} catch (MalformedTreebaseIDString e) {
			addMessage(req, "Malformed ID number expression: '" + e.getMessage() + "'");
		}
		return matches;
	}
	
	protected Map<String,Object> referenceData(HttpServletRequest pRequest) throws Exception {
		LOGGER.info("in referenceData");
	
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("buttonName", pRequest.getSession().getAttribute("searchButton"));
		
		return resultMap;
		
	}
	
	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	protected String[] getTaxonStrings(String searchTerm) {
		return searchTerm.split("[\r\n]+");
	}

	protected String convertStars(String searchTerm) {
		return searchTerm.replace('*', '%');
	}
	
	protected ModelAndView samePage(HttpServletRequest request) {
		return new ModelAndView(new RedirectView(request.getRequestURI()));
	}

	public TaxonLabelService getTaxonLabelService() {
		return mTaxonLabelService;
	}

	public void setTaxonLabelService(TaxonLabelService taxonLabelService) {
		mTaxonLabelService = taxonLabelService;
	}

}
