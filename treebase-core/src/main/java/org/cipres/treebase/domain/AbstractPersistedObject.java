/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.domain;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;

/**
 * The abstract super class for all persisted domain objects.
 * 
 * Created on Sep 27, 2005
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractPersistedObject implements TBPersistable, Serializable {

	public static final Comparator<AbstractPersistedObject> COMPARATOR_ID = new Comparator<AbstractPersistedObject>() {
		
		public int compare(AbstractPersistedObject pObject1, AbstractPersistedObject pObject2) {
			Long id1 = pObject1.getId();
			Long id2 = pObject2.getId();
			return id1.compareTo(id2);
		}
	
	};
	
	private Long mId;
	private int mVersion;

	/**
	 * Constructor.
	 */
	public AbstractPersistedObject() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.TBPersistable#getId()
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return mId;
	}

	/**
	 * Set the Id field.
	 */
	public void setId(Long pNewId) {
		mId = pNewId;
	}

	/**
	 * Return the Version field.
	 * 
	 * @return int
	 */
	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return mVersion;
	}

	/**
	 * Set the Version field.
	 */
	@SuppressWarnings("unused")
	private void setVersion(int pNewVersion) {
		mVersion = pNewVersion;
	}
	
	@Transient
	public TreebaseIDString getTreebaseIDString () {
		return new TreebaseIDString(this);
	}
	
	@Transient
	public NamespacedGUID getNamespacedGUID () {
		return getTreebaseIDString().getNamespacedGUID();
	}
	
	@Transient
	public PhyloWSPath getPhyloWSPath() {
		return new PhyloWSPath(this);
	}

}
