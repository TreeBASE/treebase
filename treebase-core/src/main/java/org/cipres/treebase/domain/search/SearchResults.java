package org.cipres.treebase.domain.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.service.AbstractService;

/**
 * Represents a collection of studies that result from some sort of search process.
 * <tt>mResults</tt> holds the studies in the collection.  If it is null, the collection notionally
 * includes every study in the database.<p>
 * 
 * <b>XXX 20080717</b> The representation of "every study" as a null search results list
 * was not a wise choice.  Rewrite it with a more explicit representation, say with a <tt>Boolean</tt>.
 * <p>
 *
 * @author mjd 20070509
 *
 * @param <E> - the results are a {@link java.util.Set set} of type <i>E</i>.
 */
public abstract class SearchResults<E extends TBPersistable> {
	static SearchService searchService = null;
	Set<E> mResults = null;  

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(StudySearchResults.class);
	
	/* Makes a FULL search result set, not am empty one */
	public SearchResults () { 
		mResults = null; 
	}

	// Make an empty set if isEmpty is true, a full set otherwise
	public SearchResults (boolean isEmpty) {
		this();
		if (isEmpty) {
			makeEmpty();
		}
	}
	
	public SearchResults (Collection<E> pResults) {
		super();
		setResults(pResults);
	}
	
	public SearchResults<E> clone() {
		@SuppressWarnings("unchecked")
		SearchResults<E> clone = (SearchResults<E>) SearchResults.fullResultsFactory(this.getResultType());
		clone.makeEmpty();
		clone.getResults().addAll(this.getResults());
		return clone;
	}
			
	public boolean isAll() { return mResults == null; }
	public boolean isEmpty() { return mResults != null && mResults.isEmpty(); }
	public void setAll() { mResults = null; }
	public void makeEmpty() { mResults = new HashSet<E> (); }
	public boolean hasResultsList() { return ! (isEmpty() || isAll()); }
	public SearchResultsType resultType() { return SearchResultsType.NONE; }
	public SearchResultsType getResultType() { return resultType(); }
	public abstract Class getResultClass();
	public SearchResults<?> convertToType(SearchResultsType toType) {
		if (toType == resultType()) { return this; }
		
		switch(toType) {
		case STUDY:
			return convertToStudies();
		case MATRIX:
			return convertToMatrices();
		case TREE:
			return convertToTrees();
		case TAXON:
			return convertToTaxa();
		default:
			throw new Error("Unknown search results type conversion: " + toType);
		}	
	}
	
	// XXX Fix this 142857 idiocy
	public int size() { return mResults == null ? 142857 : mResults.size(); }
	
	public String sizeString() {
		if (isAll()) {
			return "all";
		} else if (isEmpty()) {
			return "no";
		} else if (mResults.size() == 1) {
			return "one";
		} else {
			Integer s = mResults.size();
			return s.toString();
		}
	}
	
	public void intersect(Collection<E> pItems) {
		if (isAll()) {
			setResults(pItems);
		} else {
			mResults.retainAll(pItems);
		}
	}
	
	public void intersect(SearchResults<E> pResults) {
		if (!pResults.isAll()) {
			this.intersect(pResults.getResults());
		}
	}
	
	public void union(Collection<E> pItems) {
		if (! isAll()) {
			mResults.addAll(pItems);
		} 
	}
	
	public void union(SearchResults<E> pResults) {
		if (pResults.isAll()) {
			this.setAll();
		} else {
			this.union(pResults.getResults());
		}
	}
	
	public Integer getCount() { return size(); }
	public void scrub() { mResults = null; }

	public String nItems() { 
		return sizeString() + " " + this.items(this.size()); 
	}  
	
	public String description() {
		return this.getClass() + " " + this + " contains " + nItems();
	}
	
	public String items(int n) {
		return n == 1 ? "item" : "items";
	}
	
	public String getDescription() {
		return this.description();
	}
	
	public void announceSize(Logger L) { 
	    L.info(this.description()); 
	}
	
	
	// Aliases for JSP access
	// TODO: Is there a better way to do this?
	public boolean getIsAll() { return isAll(); }
	public boolean getIsEmpty() { return isEmpty(); }
	public boolean getHasResultsList() { return hasResultsList(); }
	public boolean getIsTrivial() { return ! hasResultsList(); }


	public String getItems() { return nItems(); }
	
	public abstract StudySearchResults convertToStudies() ;
	public abstract TreeSearchResults convertToTrees() ;
	public abstract MatrixSearchResults convertToMatrices() ;	
	public abstract TaxonSearchResults convertToTaxa() ;

	public StudyService getStudyService() {
		return getSearchService().getStudyService();
	}
	
	public PhyloTreeService getPhyloTreeService() {
		return getSearchService().getPhyloTreeService();
	}

	public MatrixService getMatrixService() {
		return getSearchService().getMatrixService();
	}
	
	public TaxonLabelService getTaxonLabelService() {
		return getSearchService().getTaxonLabelService();
	}
	
	public abstract AbstractService getService();

	public static SearchResults<? extends TBPersistable> fullResultsFactory(SearchResultsType searchResultsType) {
		switch (searchResultsType) {
		case MATRIX:
			return new MatrixSearchResults();
		case STUDY:
			return new StudySearchResults();
		case TREE:
			return new TreeSearchResults();
		case TAXON:
			return new TaxonSearchResults();
		default:
			throw new Error("Can't happen");
		}
	}
	
	public Set<E> getResults() { return mResults; }
	public void setResults(Collection<E> pResults) {
		if (pResults == null) {
			mResults = null;
		} else if (pResults instanceof Set) {
			mResults = (Set<E>) pResults;
		} else {
			mResults = new HashSet<E>(pResults);
		}
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService mSearchService) {
		searchService = mSearchService;
	}
	
	public SearchResultsFrozen freeze() {
		return new SearchResultsFrozen(this);
	}
	
	public E getItemByID(Long id) {
		for (E item : getResults()) 
			if (item.getId().equals(id))
				return item;
		return null;
	}

	@SuppressWarnings("unchecked")
	public void setResultIDs(Collection<Long> ids) {
		AbstractService service = getService();
		makeEmpty();
		for (Long id : ids) {
			E obj = (E) service.findByID(getResultClass(), id);
			getResults().add(obj);
		}
	}
}


