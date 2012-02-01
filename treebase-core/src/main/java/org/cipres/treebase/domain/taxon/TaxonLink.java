package org.cipres.treebase.domain.taxon;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * TaxonLink.java
 * 
 * Created on Mar 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TAXONLINK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LINKTYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "TAXONLINK_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
public abstract class TaxonLink extends AbstractPersistedObject {

	private static final long serialVersionUID = -7574323206006350976L;
	private TaxonAuthority mAuthority;

	/**
	 * Constructor.
	 */
	public TaxonLink() {
		super();
	}

	/**
	 * Return the Authority field.
	 * 
	 * @return TaxonAuthority
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "TAXONAUTHORITY_ID", nullable = false)
	public TaxonAuthority getAuthority() {
		return mAuthority;
	}

	/**
	 * Set the Authority field.
	 */
	public void setAuthority(TaxonAuthority pNewAuthority) {
		mAuthority = pNewAuthority;
	}

}
