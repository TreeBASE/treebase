
package org.cipres.treebase.domain;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;

/**
 * A tagging interface for all persisted treebase objects.
 * 
 * Created on Sep 27, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface TBPersistable extends NexmlWritable {

	/**
	 * Default column length for the String type in the database.
	 */
	public static final int COLUMN_LENGTH_30 = 30;
	public static final int COLUMN_LENGTH_50 = 50;
	public static final int COLUMN_LENGTH_100 = 100;
	public static final int COLUMN_LENGTH_STRING = 255;
	public static final int COLUMN_LENGTH_500 = 500;
	public static final int COLUMN_LENGTH_STRING_1K = 1000;
	public static final int COLUMN_LENGTH_STRING_NOTES = 2000;
	public static final int COLUMN_LENGTH_STRING_MAX = 5000;
	
	// Needed to make these bigger.  MJD 20090420
	public static final int CITATION_TITLE_COLUMN_LENGTH = 500;
	public static final int CITATION_ABSTRACT_COLUMN_LENGTH = 10000;

	/**
	 * Return the id.
	 * 
	 * @return
	 */
	public Long getId();
	
	public TreebaseIDString getTreebaseIDString ();
	
	public NamespacedGUID getNamespacedGUID ();
	
	public PhyloWSPath getPhyloWSPath ();
	
	public TBPersistable getContext ();

}
