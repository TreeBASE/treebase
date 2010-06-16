
package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeQuality.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREEQUALITY")
@AttributeOverride(name = "id", column = @Column(name = "TREEQUALITY_ID"))
public class TreeQuality extends AbstractPersistedObject {

	//default: is unrated. 
	private static final String QUALITY_UNRATED = "Unrated";
	private static final String QUALITY_PREFERRED = "Preferred Tree";
	private static final String QUALITY_ALTERNATIVE = "Alternative Tree";
	private static final String QUALITY_SUBOPTIMAL = "Suboptimal Tree";

	private String mDescription;

	/**
	 * Return a list of all predefined instances as String. 
	 * 
	 * @return
	 */
	public static List<String> allInstanceDescriptions() {
		List<String> allInsts = new ArrayList<String>();
		allInsts.add(QUALITY_UNRATED);
		allInsts.add(QUALITY_PREFERRED);
		allInsts.add(QUALITY_ALTERNATIVE);
		allInsts.add(QUALITY_SUBOPTIMAL);

		return allInsts;
	}
	
	/**
	 * Constructor.
	 */
	public TreeQuality() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Override
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if unrated tree.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isQualityUnrated() {
		return QUALITY_UNRATED.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
