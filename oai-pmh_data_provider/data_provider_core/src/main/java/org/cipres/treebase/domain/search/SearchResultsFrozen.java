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
