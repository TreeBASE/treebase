
package org.cipres.treebase.domain.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;

/**
 * Class for "freezing" search results lists between user requests
 * 
 * <p>Idea: to save a SearchResults at  the end of a request, first
 * use searchResults.freeze() to turn it into a SearchResultsFrozen, then
 * stick the SearchResultsFrozen object somewhere, such as in the server session object.
 * 
 * <p>To recover saved search state, use {@link searchResultsFrozen#thaw()}.
 * 
 * @author mjd 20090306
 *
 */
public class SearchResultsFrozen {

	Set<Long> ids;
	SearchResultsType type;
	boolean isAll;
	
	public <E extends TBPersistable> SearchResultsFrozen(SearchResults<E> searchResults) {
		setType(searchResults.getResultType());
		if (searchResults.isAll()) {
			setAll(true);
		} else {
			setAll(false);
			setIds(extractIds(searchResults.getResults()));
		}
	}

	private Set<Long> extractIds(Collection<? extends TBPersistable> items) {
		Set<Long> ids = new HashSet<Long> ();
		for (TBPersistable item : items)
			ids.add(item.getId());
		return ids;
	}

	@SuppressWarnings("unchecked")
	public <E extends TBPersistable> SearchResults<E> thaw() {
		SearchResults<E> result = (SearchResults<E>) SearchResults.fullResultsFactory(type);
		if (this.isAll()) {
			// No further action required
		} else if (this.isEmpty()) {
			result.makeEmpty();
		} else {
			result.setResultIDs(this.getIds());
		}
		return result;
	}
	
	public boolean isEmpty() {
		return !isAll() && getIds().isEmpty();
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}

	public SearchResultsType getType() {
		return type;
	}

	public void setType(SearchResultsType type) {
		this.type = type;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
}
