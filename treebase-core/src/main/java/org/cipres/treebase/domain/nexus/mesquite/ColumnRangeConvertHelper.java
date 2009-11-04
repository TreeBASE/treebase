
package org.cipres.treebase.domain.nexus.mesquite;

import java.util.ArrayList;
import java.util.Collection;

import mesquite.lib.MesquiteNumber;

import org.cipres.treebase.domain.matrix.ColumnRange;

/**
 * A helper class for converting Mesquite CharNumSet object to a group of ColumnRange objects.
 * 
 * E.g. WTSET: one= 2:1-3, 6, 7 3:9-10
 * 
 * charset pos3 = 3-7\2 12-16\2 21-25\2 30 32-38\3;
 * 
 * taxset tbyname3 = t1 't2 the name';
 * 
 * charpartition part = one: 1-6, two: 7-12, three: 13-18;
 * 
 * 
 * Created on Jun 14, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class ColumnRangeConvertHelper {

	private MesquiteNumber mNumber;
	private String mName;
	private Collection<ColumnRange> mColRanges = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public ColumnRangeConvertHelper() {
		super();
	}

	/**
	 * Return the Name field.
	 * 
	 * @return String mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set the Name field.
	 */
	public void setName(String pNewName) {
		mName = pNewName;
	}

	/**
	 * @return the number
	 */
	public MesquiteNumber getNumber() {
		return mNumber;
	}

	/**
	 * @param pNumber the number to set
	 */
	public void setNumber(MesquiteNumber pNumber) {
		mNumber = pNumber;
	}

	/**
	 * @return the colRanges
	 */
	public Collection<ColumnRange> getColRanges() {
		return mColRanges;
	}

	/**
	 * @param pColRanges the colRanges to set
	 */
	public void setColRanges(Collection<ColumnRange> pColRanges) {
		mColRanges = pColRanges;
	}

}
