package org.cipres.treebase.domain.study;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * Algorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ALGORITHM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "ALGORITHM_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public abstract class Algorithm extends AbstractPersistedObject {

	private String mDescription;

	private String mPropertyName;
	private String mPropertyValue;

	/**
	 * Constructor.
	 */
	public Algorithm() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
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
	 * Return the PropertyValue field.
	 * 
	 * @return String mPropertyValue
	 */
	@Column(name = "PropertyValue", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPropertyValue() {
		return mPropertyValue;
	}

	/**
	 * Set the PropertyValue field.
	 */
	public void setPropertyValue(String pNewPropertyValue) {
		mPropertyValue = pNewPropertyValue;
	}

	/**
	 * Return the PropertyName field.
	 * 
	 * @return String mPropertyName
	 */
	@Column(name = "PropertyName", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPropertyName() {
		return mPropertyName;
	}

	/**
	 * Set the PropertyName field.
	 */
	public void setPropertyName(String pNewPropertyName) {
		mPropertyName = pNewPropertyName;
	}
	
	/* constants returned by getAlgorithmType */
	public static final String LikelihoodAlgorithm = "maximum likelihood";	
	public static final String OtherAlgorithm = "other algorithm";
	public static final String ParsimonyAlgorithm = "parsimony";
	public static final String BayesianAlgorithm ="bayesian inference";	
	public static final String EvolutionAlgorithm ="minimum evolution";
	public static final String JoiningAlgorithm ="neighbor joining";
	public static final String UPGMAAlgorithm = "UPGMA";
	
	
	/**
	 * @return a string describing the algorithm type (for example, "parsimony")
	 * @author mjd 20080723
	 */
	@Transient
	public abstract String getAlgorithmType();
}
