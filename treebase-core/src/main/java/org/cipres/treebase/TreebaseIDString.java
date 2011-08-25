package org.cipres.treebase;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.PhyloChar;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;

public class TreebaseIDString {
	
	public class MalformedTreebaseIDString extends Exception {
		private static final long serialVersionUID = 1L;
		public MalformedTreebaseIDString() {
			super();
		}
		public MalformedTreebaseIDString(String message) {
			super(message);
		}	
	}
	

	
	private static final Map<Class<?>,String> prefixFor = initializePrefixForMap();
	private static final Map<String,Class<?>> classFor = initializeClassForMap();
	private String mTypePrefix;
	private Long mId;		
	
	// These are the canonical prefixes
	private static Map<Class<?>, String> initializePrefixForMap() {
		Map<Class<?>, String> val = new HashMap<Class<?>, String> ();
		val.put(Study.class, "S");
		val.put(Matrix.class, "M");
		val.put(PhyloChar.class, "C");
		val.put(MatrixColumn.class, "Col");
		val.put(DiscreteCharState.class, "Dcs");
		val.put(DiscreteMatrixElement.class, "Dme");
		val.put(PhyloTree.class, "Tr");
		val.put(PhyloTreeNode.class, "Tn");
		val.put(TreeBlock.class, "Tb");
		val.put(Taxon.class, "Tx");
		val.put(TaxonLabel.class, "Tl");
		val.put(TaxonLabelSet.class, "Tls");
		val.put(TaxonVariant.class, "Tv");
		val.put(Analysis.class, "A");
		val.put(AnalysisStep.class, "As");
		val.put(AnalyzedData.class, "Ad");
		val.put(Algorithm.class, "Al");
		val.put(Software.class, "So");
		return val;
	}

	// This includes all the mappings from prefixFor, but in reverse
	// and also some aliases.  
	// For example, the application will always use "Tr1000" as the ID string for PhyloTree 1000
	// but it will accept "T1000" as an alias. 
	private static Map<String, Class<?>> initializeClassForMap() {
		Map<String, Class<?>> val = new HashMap<String, Class<?>> ();
		for (Map.Entry<Class<?>, String> e : prefixFor.entrySet()) {
			val.put(e.getValue(), e.getKey());
		}
		val.put("T", PhyloTree.class);
		return val;
	}
	
	public TreebaseIDString(String typePrefix, Long id) {
		mTypePrefix = typePrefix;
		mId = id;
	}
	
	public TreebaseIDString(Class<?> typeClass, Long id) {
		mTypePrefix = getPrefixForClass(typeClass);
		mId = id;
	}
	
	public TreebaseIDString(TBPersistable tbObj) {
		mTypePrefix = getPrefixForClass(tbObj.getClass());
		mId = tbObj.getId();
	}
	
	/**
	 * Parse the specified ID string, with default implicit prefix.
	 * 
	 * <p>Parse a treebase ID string, such as "M1234".  Normally the initial prefix is mandatory, as with
	 * {@link #TreebaseIDString(String ids)}.  But if the prefix is missing, and the ID string is otherwise
	 * acceptable, assume that the unadorned id number represents an object of the specified class, instead of
	 * throwing {@link MalformedTreebaseIDString}.
	 * 
	 * <p>It is not an error for the type prefix to be present but to diasgree with the specified class,
	 * unless the "mustMatch" flag is set.  The mustMatch flag is optional, and defaults to false.
	 * 
	 * <p>If the type prefix is present but unacceptable, throw {@link MalformedTreebaseIDString}.
	 * 
	 * 
	 * @author mjd 20090312
	 * @param ids - the string to parse
	 * @param defaultClass - if the string has no prefix, assume that the string represents an object in this class
	 * @param mustMatch - if set, an exception is thrown if the type prefix in ids does not match the defaultClass
	 * @throws MalformedTreebaseIDString
	 */
	public TreebaseIDString(String ids, Class<?> defaultClass, boolean mustMatch) throws MalformedTreebaseIDString {
		try {
			parseWithoutSemanticChecks(ids);
		} catch (NumberFormatException e) {
			throw new MalformedTreebaseIDString(ids);
		}
		if (getId() == null) throw new MalformedTreebaseIDString(ids);
		
		// If there was no prefix, try to infer one
		if (getTypePrefix() == null || getTypePrefix().equals("")) {
			mTypePrefix = getPrefixForClass(defaultClass);
			if (getTypePrefix() == null)
				throw new MalformedTreebaseIDString(ids);			
		}
		else {
			// also validate as usual
			validateTypePrefix();
			// If requested, make sure the prefix matches the requested class
			if ( mustMatch ) { 
				if (getClassForPrefix(getTypePrefix()) != defaultClass) {
					throw new MalformedTreebaseIDString(ids + " has wrong prefix; should be " + getPrefixForClass(defaultClass));
				}
			}
		}
	}
	
	public String toString() {
		return mTypePrefix + mId;
	}
	
	public TreebaseIDString(String string, Class<?> class1) throws MalformedTreebaseIDString {
		this(string, class1, false);
	}

	
	public TreebaseIDString(String ids) throws MalformedTreebaseIDString {
		try {
			parseWithoutSemanticChecks(ids);
		} catch (NumberFormatException e) {
			throw new MalformedTreebaseIDString(ids);
		}		
		validateTypePrefix();
	}
	

	private void validateTypePrefix() throws MalformedTreebaseIDString {
		if (isValidPrefix(this.getTypePrefix())) return;
		throw new MalformedTreebaseIDString(this.getTypePrefix());
	}

	private void parseWithoutSemanticChecks(String ids) throws MalformedTreebaseIDString {
		if (ids == null) return;
		StringBuilder prefix = new StringBuilder("");
		StringBuilder digits = new StringBuilder("");

		int i;
		
		// Skip leading white space
		for (i = 0; i < ids.length(); i++) {
			if (! Character.isWhitespace(ids.charAt(i))) break;
		}
		
		// Copy prefix, up to first digit
		for ( ; i < ids.length(); i++) {
			if (Character.isDigit((ids.charAt(i))))
				break;
			else 
				prefix.append(ids.charAt(i));	
		}
		
		// Copy digits, up to end or some non-digits
		for ( ; i < ids.length(); i++) {
			if (Character.isDigit((ids.charAt(i))))
				digits.append(ids.charAt(i));
			else 
				break;
		}
		
		// Skip trailing white space
		for (; i < ids.length(); i++) {
			if (! Character.isWhitespace(ids.charAt(i))) break;
		}
		
		if (i < ids.length()) throw new MalformedTreebaseIDString("..." + ids.substring(i));

		mId = Long.parseLong(digits.toString());
		mTypePrefix = prefix.toString();
	}

	String getIDString() {
		return getTypePrefix() + getId();
	}
	
	/**
	 * Given a string that might contain a treebase object ID, try to extract the ID number
	 * 
	 * This means trimming of leading and trailing white space, trimming a leading entity type
	 * indicator (such as "S" for "study" or "Tr" for "tree") and validating the result
	 * 
	 * @param target the string to parse
	 * @param prefix an optional indicator prefix
	 * @return the ID number, or null if the input was invalid
	 * @author mjd 20081121
	 */
	public static Long trimPrefix(String target, String prefix) {
		if (target == null) return null;
		String s = target.trim();
		if (prefix != null && s.startsWith(prefix)) s = s.substring(prefix.length());
		return TreebaseUtil.parseLong(s, null);
	}

	public static boolean isValidPrefix(String prefix) {
		if (prefix == null) return false;
		return classFor.containsKey(prefix);
	}

	/**
	 * Given a prefix string, say what class of objects it denotes.
	 * 
	 * <p>For example, "M" is Matrix; "Tl" is Taxonlabels.
	 * 
	 * @author mjd 20090312
	 * @param prefix
	 * @return
	 */
	public static Class<?> getClassForPrefix(String prefix) {
		return classFor.get(prefix);
	}
	
	/**
	 * Given an object class, return the prefix string that represents that class.
	 * 
	 * <p>For example, Matrix is represented by "M", and TaxonLabel by "Tl"
	 * 
	 * @author mjd 20090312
	 * @param cl - the class
	 * @return
	 */
	public static String getPrefixForClass(Class<?> cl) {
		if ( prefixFor.containsKey(cl) ) {
			return prefixFor.get(cl);
		}
		else {
			for ( Class<?> knownClass : prefixFor.keySet() ) {
				if ( knownClass.isAssignableFrom(cl) ) {
					return prefixFor.get(knownClass);
				}
			}
			return null;
		}
	}
	
	/**
	 * Given a TB object, return an ID string for the object.
	 * 
	 * <p>For example, given matrix 1234, return "M1234"; given taxon label 567,
	 * return "Tl567". 
	 * @author mjd 20090312
	 * @param obj
	 * @return
	 */
	public static String getIDString(TBPersistable obj) {
		String prefix = getPrefixForClass(obj.getClass());
		if (prefix == null) prefix = obj.getClass().toString();  // XXX Is this good behavior?
		return prefix + obj.getId();
	}
	
	/**
	 * Given an ID string, return the type prefix of the string.
	 * 
	 * <p>For example, given "M1234", return "M", and given "Tl789", return "Tl"
	 * 
	 * @author mjd 20090312
	 * @param idString
	 * @return
	 */
	public static String getPrefixOf(String idString) {
		StringBuilder prefix = new StringBuilder("");
		for (int i = 0; i < idString.length(); i++) {
			if (! Character.isDigit((idString.charAt(i))))
				prefix.append(idString.charAt(i));
			else
				break;
		}
		
		return prefix.toString();
	}

	/**
	 * Given an ID string, return the class of the object that the string represents.
	 * 
	 * <p>For example, given "M1234", return Matrix.class, and given "Tl789", return TaxonLabel.class
	 * 
	 * @author mjd 20090312
	 * @param idString
	 * @return
	 */
	public static Class<?> getClassForIDString(String idString) {
		return getClassForPrefix(getPrefixOf(idString));
	}

	public String getTypePrefix() {
		return mTypePrefix;
	}

	public Long getId() {
		return mId;
	}
	
	public Class<?> getTBClass() {
		return getClassForPrefix(getTypePrefix());
	}
	
	/**
	 * This creates a GUID of the form TB2:${mTypePrefix}${mId}, which is the
	 * minimal recommendation for, for example, dc:identifier annotations. 
	 * Additional blogging by Rod Page and others about this suggests there 
	 * should be a delimiter between mTypePrefix and mId so that we'd end up
	 * with id's that look like TB2:Tr:2131, which could then be mapped onto
	 * purls. But doing that would break our backward compatibility with older
	 * records, files, etc.
	 *   
	 * @return
	 */
	public NamespacedGUID getNamespacedGUID() {
		StringBuilder sb = new StringBuilder();
		sb
			.append(NamespacedGUID.getDefaultGUIDPrefix())
			.append(getTypePrefix())
			.append(getId());
		return new NamespacedGUID(sb.toString());
	}
	

}
