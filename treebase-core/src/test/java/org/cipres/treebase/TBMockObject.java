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
package org.cipres.treebase;

import org.cipres.treebase.domain.TBPersistable;

/**
 * A trivial, nonpersistent TBPersistable object, useful for incorporation into test classes.
 *  
 * @author mjd 20090414
 *
 */
public class TBMockObject extends Object implements TBPersistable {
	Long val;

	public TBMockObject(Long val) {
		super();
		this.val = val;
	}
	
	public TBMockObject(int val) {
		super();
		this.val = new Long(val);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof TBMockObject)) return false;
		TBMockObject o = (TBMockObject) obj;
		return this.getId().equals(o.getId());
	}

	public Long getId() {
		return val;
	}
	
	public String toString() {
		return "#" + getId().toString();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
