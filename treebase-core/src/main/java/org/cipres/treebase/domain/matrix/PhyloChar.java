package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * PhyloChar.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "PHYLOCHAR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "PHYLOCHAR_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@BatchSize(size = 10)
@DiscriminatorValue("C")
public class PhyloChar extends AbstractPersistedObject {

	private static final long serialVersionUID = 1556814295155923867L;

	private String mDescription;

	/**
	 * Constructor.
	 */
	public PhyloChar() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	@Override
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

	/**
	 * <p>Is this phylochar interesting?
	 * 
	 * <p>Some phylocharacters belong to many, many columns.  For example, the columns of a DNA matrix have
	 * phylochar #2 ("DNA") unless otherwise annotated.  Interesting phylochars should be mentioned on the 
	 * matrix display page; names of uninteresting phylochars should be suppressed.
	 * 
	 * <p>This function returns false for phylochars which should be omitted from the listing.
	 * 
	 * @author mjd 20090317
	 * @return whether the character is interesting enough to mention 
	 */
	@Transient
	public boolean isInteresting() {
		return getId() >= Constants.MIN_INTERESTING_PHYLOCHAR_ID;
	}
	
	@Transient
	public String getLabel() {
		return getDescription();
	}

}
