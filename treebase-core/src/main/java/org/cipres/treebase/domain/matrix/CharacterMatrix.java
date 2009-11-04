
package org.cipres.treebase.domain.matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * CharacterMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("C")
public abstract class CharacterMatrix extends Matrix {

	protected List<MatrixRow> mRows = new ArrayList<MatrixRow>();

	private AncStateSet mDefaultAncSet;
	private ExcludedCharSet mDefaultExcludedSet;
	private CharWeightSet mDefaultWeightSet;
	private CodonPositionSet mDefaultCodonPosSet;
	private TypeSet mDefaultTypeSet;

	private List<MatrixColumn> mColumns = new ArrayList<MatrixColumn>();
	private Set<AncStateSet> mAncStateSets = new HashSet<AncStateSet>();
	private Set mCharSets = new HashSet();
	private Collection<CharWeightSet> mWeightSets = new ArrayList<CharWeightSet>();
	private Collection<CodonPositionSet> mCodonPosSets = new ArrayList<CodonPositionSet>();
	private Collection<GeneticCodeSet> mCodeSets = new ArrayList<GeneticCodeSet>();
	private Collection<TypeSet> mTypeSets = new ArrayList<TypeSet>();
	private Collection<CharPartition> mCharPartitions = new ArrayList<CharPartition>();

	private Integer mNtax; // 20081204 mjd
	private Integer mNchar; // 20081204

	//transient
	private int mMaxTaxonLabelLength;
	
	/**
	 * Constructor.
	 */
	public CharacterMatrix() {
		super();
	}

	/**
	 * Return the DefaultCodonPosSet field.
	 * 
	 * @return CodonPositionSet
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CODONPOSITIONSET_ID", nullable = true)
	public CodonPositionSet getDefaultCodonPosSet() {
		return mDefaultCodonPosSet;
	}

	/**
	 * Return true if the codon set is the default.
	 * 
	 * @param pCodonSet
	 * @return
	 */
	private boolean isDefaultCodonSet(CodonPositionSet pDefaultCodonPosSet) {
		return (pDefaultCodonPosSet == getDefaultCodonPosSet());
	}
	
	/**
	 * Set the DefaultCodonPosSet field.
	 */
	public void setDefaultCodonPosSet(CodonPositionSet pNewDefaultCodonPosSet) {
		mDefaultCodonPosSet = pNewDefaultCodonPosSet;
	}

	/**
	 * Return the DefaultWeightSet field.
	 * 
	 * @return CharWeightSet
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CHARWEIGHTSET_ID", nullable = true)
	public CharWeightSet getDefaultWeightSet() {
		return mDefaultWeightSet;
	}

	/**
	 * Return true if the wtset is the default.
	 * 
	 * @param pWtSet
	 * @return
	 */
	private boolean isDefaultWeightSet(CharWeightSet pWtSet) {
		return (pWtSet == getDefaultWeightSet());
	}
	
	/**
	 * Set the DefaultWeightSet field.
	 */
	public void setDefaultWeightSet(CharWeightSet pNewDefaultWeightSet) {
		mDefaultWeightSet = pNewDefaultWeightSet;
	}

	/**
	 * Return the DefaultExcludedSet field.
	 * 
	 * @return ExcludedCharSet
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CHARSET_ID", nullable = true)
	public ExcludedCharSet getDefaultExcludedSet() {
		return mDefaultExcludedSet;
	}

	/**
	 * Set the DefaultExcludedSet field.
	 */
	public void setDefaultExcludedSet(ExcludedCharSet pNewDefaultExcludedSet) {
		mDefaultExcludedSet = pNewDefaultExcludedSet;
	}

	/**
	 * Return true if the excluded set is the default.
	 * 
	 * @param pExSet
	 * @return
	 */
	private boolean isDefaultExcludedSet(ExcludedCharSet pExSet) {
		return (pExSet == getDefaultExcludedSet());
	}
	
	/**
	 * Return the DefaultTypeSet field.
	 * 
	 * @return TypeSet mDefaultTypeSet
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "TYPESET_ID", nullable = true)
	public TypeSet getDefaultTypeSet() {
		return mDefaultTypeSet;
	}

	/**
	 * Set the DefaultTypeSet field.
	 */
	public void setDefaultTypeSet(TypeSet pNewDefaultTypeSet) {
		mDefaultTypeSet = pNewDefaultTypeSet;
	}

	/**
	 * Return true if the type is the default.
	 * 
	 * @param pTypeSet
	 * @return
	 */
	private boolean isDefaultTypeSet(TypeSet pTypeSet) {
		return (pTypeSet == getDefaultTypeSet());
	}
	
	/**
	 * Return the AncStateSets field.
	 * 
	 * @return Set<AncStateSet>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<AncStateSet> getAncStateSets() {
		return mAncStateSets;
	}

	/**
	 * Set the AncStateSets field.
	 */
	public void setAncStateSets(Set<AncStateSet> pNewAncStateSets) {
		mAncStateSets = pNewAncStateSets;
	}

	/**
	 * Return the WeightSets field.
	 * 
	 * @return Collection<CharWeightSet>
	 */
	//@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<CharWeightSet> getWeightSets() {
		return mWeightSets;
	}

	/**
	 * Set the WeightSets field.
	 */
	public void setWeightSets(Collection<CharWeightSet> pNewWeightSets) {
		mWeightSets = pNewWeightSets;
	}

	/**
	 * Return the CharPartitions field.
	 * 
	 * @return Collection<CharPartition> mCharPartitions
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<CharPartition> getCharPartitions() {
		return mCharPartitions;
	}

	/**
	 * Set the CharPartitions field.
	 */
	public void setCharPartitions(Collection<CharPartition> pNewCharPartitions) {
		mCharPartitions = pNewCharPartitions;
	}

	/**
	 * Return the CharSets field.
	 * 
	 * @return Set<CharSet>
	 */
	// @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @JoinColumn(name = "MATRIX_ID", nullable = true)
	@OneToMany(mappedBy = "matrix", cascade = {CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REMOVE}, targetEntity = CharSet.class)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set getCharSets() {
		return mCharSets;
	}

	/**
	 * Set the CharSets field.
	 */
	public void setCharSets(Set pNewCharSets) {
		mCharSets = pNewCharSets;
	}

	/**
	 * Return the CodeSets field.
	 * 
	 * @return Collection<GeneticCodeSet>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<GeneticCodeSet> getCodeSets() {
		return mCodeSets;
	}

	/**
	 * Set the CodeSets field.
	 */
	public void setCodeSets(Collection<GeneticCodeSet> pNewCodeSets) {
		mCodeSets = pNewCodeSets;
	}

	/**
	 * Return the CodonPosSets field.
	 * 
	 * @return Collection<CodonPositionSet>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<CodonPositionSet> getCodonPosSets() {
		return mCodonPosSets;
	}

	/**
	 * Set the CodonPosSets field.
	 */
	public void setCodonPosSets(Collection<CodonPositionSet> pNewCodonPosSets) {
		mCodonPosSets = pNewCodonPosSets;
	}

	/**
	 * Return the TypeSets field.
	 * 
	 * @return Collection<TypeSet> mTypeSets
	 */
	//@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "MATRIX_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<TypeSet> getTypeSets() {
		return mTypeSets;
	}

	/**
	 * Set the TypeSets field.
	 */
	public void setTypeSets(Collection<TypeSet> pNewTypeSets) {
		mTypeSets = pNewTypeSets;
	}

	/**
	 * Return the DefaultAncState field.
	 * 
	 * @return AncStateSet
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "ANCSTATESET_ID", nullable = true)
	public AncStateSet getDefaultAncSet() {
		return mDefaultAncSet;
	}

	/**
	 * Set the DefaultAncState field.
	 */
	public void setDefaultAncSet(AncStateSet pNewDefaultAncState) {
		mDefaultAncSet = pNewDefaultAncState;
	}

	/**
	 * Return the Columns field.
	 * 
	 * Note: used internally, not intended to be used as an API. 
	 * 
	 * @return List<MatrixColumn>
	 */
	// Note: for the true index to work,
	// the one side has to be the owner side, cannot use "mappedby"
	// also need to duplicate specify the JoinColumn here.
	//@OneToMany(cascade = CascadeType.ALL)
	// no cascade merge since we do not modify a column ?? Need test.
	//@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@OneToMany
	@JoinColumn(name = "MATRIX_ID")
	@IndexColumn(name = "COLUMN_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	@BatchSize(size=40)
	@LazyCollection(LazyCollectionOption.EXTRA)
	public List<MatrixColumn> getColumns() {
		return mColumns;
	}

	/**
	 * Set the Columns field.
	 */
	protected void setColumns(List<MatrixColumn> pNewColumns) {
		mColumns = pNewColumns;
//		setnChar(pNewColumns.size());
	}

	/**
	 * Return the phylo character for the specified column.
	 * 
	 * @param pColIndex
	 * @return
	 */
	public PhyloChar getCharacter(int pColIndex) {
		List<MatrixColumn> columns = getColumns();

		if (pColIndex < 0 || pColIndex >= columns.size()) {
			return null;
		}

		return columns.get(pColIndex).getCharacter();
	}

	/**
	 * Append a column to the end of the list. Manage bi-directional relationship.
	 * 
	 * Creation date: February 22, 2006 12:06:25 PM
	 * 
	 * @param pColumn MatrixColumn
	 */
	public void addColumn(MatrixColumn pColumn) {
		if (pColumn != null && !getColumns().contains(pColumn)) {
			getColumns().add(pColumn);
			pColumn.setMatrix(this);
		}
	}

	/**
	 * Remove a column. Manage bi-directional relationship.
	 * 
	 * Creation date: February 22, 2006 12:06:25 PM
	 * 
	 * @param pColumn MatrixColumn
	 */
	public void removeColumn(MatrixColumn pColumn) {
		if (pColumn != null && getColumns().contains(pColumn)) {
			getColumns().remove(pColumn);
			pColumn.setMatrix(null);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#cascadePersist(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	@Override
	public void cascadePersist(MatrixHome pMatrixHome) {

		pMatrixHome.cascadePersistElements(this);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#cascadeDelete(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	@Override
	public void cascadeDelete(MatrixHome pMatrixHome) {

		// Cascade Delete following:
		// private List<MatrixColumn> mColumns;
		// private Set<AncStateSet> mAncStateSets;
		// private Set<UserDefinedCharSet> mCharSets;
		// private Set<CharWeightSet> mWeightSets;
		// private Set<CodonPositionSet> mCodonPosSets;
		// private Set<GeneticCodeSet> mCodeSets;
		// private Set<TypeSet> mTypeSets;

		pMatrixHome.cascadeDeleteElements(this);
		pMatrixHome.cascadeDeleteRows(getRows());
		pMatrixHome.cascadeDeleteColumns(this);
		
		// pMatrixHome.cascadeDeleteAncStateSet(getAncStateSets());
		// pMatrixHome.cascadeDeleteCharWeightSet(getWeightSets());
		// pMatrixHome.cascadeDeleteCodeSet(getCodeSets());
		// pMatrixHome.cascadeDeleteTypeSet(getTypeSets());

		// pMatrixHome.deleteAll(getCodonPosSets());
		// pMatrixHome.deleteAll(getCharSets());

	}

	/**
	 * Returns a read only list of matrix columns.
	 * 
	 * @return
	 */
	@Transient
	public List<MatrixColumn> getColumnsReadOnly() {
		return Collections.unmodifiableList(getColumns());
	}

	/**
	 * Add a matrix row. Manage bi-directional relationship.
	 * 
	 * Creation date: February 22, 2006 12:06:25 PM
	 * 
	 * @param pRow
	 */
	public void addRow(MatrixRow pRow) {
		if (pRow != null && !getRows().contains(pRow)) {
			getRows().add(pRow);
			pRow.setMatrix(this);

		}
	}

	/**
	 * Remove a matrix row. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pRow
	 */
	public void removeRow(MatrixRow pRow) {
		if (pRow != null && getRows().contains(pRow)) {
			getRows().remove(pRow);
			pRow.setMatrix(null);
		}
	}

	/**
	 * Return the Rows field.
	 * 
	 * @return List<MatrixRow>
	 */
	// Note: for the true index to work,
	// the one side has to be the owner side, cannot use "mappedby"
	// also need to duplicate specify the JoinColumn here.
	// @OneToMany(mappedBy = "matrix", cascade = CascadeType.ALL)
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "MATRIX_ID")
	@IndexColumn(name = "ROW_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.EXTRA)
    //@BatchSize(size=40)
	protected List<MatrixRow> getRows() {
		return mRows;
	}

	/**
	 * Set the Rows field.
	 */
	protected void setRows(List<MatrixRow> pNewRows) {
		mRows = pNewRows;
//		setnTax(pNewRows.size());
	}

	/**
	 * Returns a list of matrix rows. Access is restricted to read only.
	 * 
	 * @return
	 */
	@Transient
	public List<MatrixRow> getRowsReadOnly() {
		return Collections.unmodifiableList(mRows);
	}

	/**
	 * Returns a map of taxonlabel as string to rows. 
	 * 
	 * @return
	 */
	@Transient
	public Map<String, MatrixRow> buildTaxonLabelRowMap() {
		Map<String, MatrixRow> taxonLabelRowMap = new HashMap<String, MatrixRow>();
		
		for (MatrixRow aRow : getRows()) {
			taxonLabelRowMap.put(aRow.getTaxonLabel().getTaxonLabel(), aRow);
		}
		
		return taxonLabelRowMap;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#getAllTaxonLabels()
	 */
	@Override
	@Transient
	public List<TaxonLabel> getAllTaxonLabels() {
		List<TaxonLabel> allTaxonLabels = new ArrayList<TaxonLabel>();

		for (MatrixRow aRow : getRows()) {
			allTaxonLabels.add(aRow.getTaxonLabel());
		}

		return allTaxonLabels;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#getDimensionsInfo()
	 */
	@Override
	@Transient
	public String getDimensionsInfo() {
		// generateSymbols();
		StringBuilder sb = new StringBuilder();
		sb.append("\tDIMENSIONS NCHAR=").append(getnChar()).append(";");

		return sb.toString();
	}

	// protected abstract Collection<StateSet> getStateSets();
	/*
	 * @Transient public void generateSymbols() { Collection<StateSet> astatesetcoll =
	 * getStateSets(); int i = 0; for (StateSet stateset : astatesetcoll) { ++i; }
	 * System.out.println("TOTAL # OF SYMBOLS: " + i); }
	 */
	
	
	/** 
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#generateMatrixTable(java.lang.StringBuilder)
	 */
	@Override
	@Transient
	public void generateMatrixTable(StringBuilder pBuilder) {

		// MATRIX section:
		List<MatrixRow> matrixRows = getRows();
		Iterator<MatrixRow> iterator = matrixRows.iterator();

		ArrayList<String> tLabel = new ArrayList<String>();
		ArrayList<String> tElementAsString = new ArrayList<String>();
		int sizeTaxonLabelMax = 0;
		int sizeElementAsStringMax = 0;
		while (iterator.hasNext()) {

			MatrixRow aRow = iterator.next();
			int sizeTaxonLabel = aRow.getTaxonLabel().getTaxonLabel().length();
			int sizeElementAsString = aRow.buildElementAsString().length();

			if (sizeTaxonLabel > sizeTaxonLabelMax) {
				sizeTaxonLabelMax = sizeTaxonLabel;
			}
			if (sizeElementAsString > sizeElementAsStringMax) {
				sizeElementAsStringMax = sizeElementAsString;
			}
			tLabel.add(aRow.getTaxonLabel().getTaxonLabel());
			tElementAsString.add(aRow.buildElementAsString());
		}

		setMaxTaxonLabelLength(sizeTaxonLabelMax);
		pBuilder.append("MATRIX").append(TreebaseUtil.LINESEP);
		addMatrixColumnLabelInformation(pBuilder, sizeElementAsStringMax);

		formatTaxonAndMatrix(pBuilder, sizeTaxonLabelMax, tLabel, tElementAsString);
		pBuilder.append(";").append(TreebaseUtil.LINESEP);
	}

	@Transient
	private void addMatrixColumnLabelInformation(StringBuilder pBldr, int rowlength) {

		if (rowlength < 20) // ONLY ADD THIS INFORMATION WHEN Max Length for Matrix Row > 20
			return;

		int spacesrequired = getMaxTaxonLabelLength() + 5 - 1;
		// -1 is required because of "[" being added in the very beginning of the StringBuilder
		int ntens = spacesrequired / 10;
		int nones = spacesrequired % 10;

		pBldr.append("[");
		addSpaces(pBldr, ntens, nones);
		for (int i = 0; i < rowlength / 10; i++) {

			if (i < 10) {
				addSpaces(pBldr, 0, 8);
			} else {
				addSpaces(pBldr, 0, 7);
			}

			pBldr.append((i + 1) * 10);
		}
		addSpaces(pBldr, 0, rowlength % 10);
		pBldr.append("]").append(TreebaseUtil.LINESEP);

		pBldr.append("[");
		addSpaces(pBldr, ntens, nones);

		for (int i = 0; i < rowlength / 10; i++) {
			addSpaces(pBldr, 0, 9);
			pBldr.append(".");
		}
		addSpaces(pBldr, 0, rowlength % 10);
		pBldr.append("]").append(TreebaseUtil.LINESEP);

	}

	/**
	 * Adding the Taxon Labels (formated) and Matrix Rows to the file All the Taxon Labels and
	 * Matrix Rows are passed as ArrayLists to this method. I iterate through the list and Taxon is
	 * passed as a StringBuilder to formatTaxonSpaces method, which is responsible for formatting.
	 * Subsequently,I add a Matrix Row to the StringBuilder. This procedure is repeated for each
	 * record.
	 * 
	 * @param pBldr
	 * @param maxlen Maximum length of the Taxon Label in the Matrix
	 * @param taxonLabel ArrayList for all Taxon Labels in the Matrix
	 * @param tElementAsString ArrayList for all the Matrix rows
	 * @throws IOException
	 */
	@Transient
	public void formatTaxonAndMatrix(
		StringBuilder pBldr,
		int maxlen,
		ArrayList<String> taxonLabel,
		ArrayList<String> tElementAsString) {

		int strsize = maxlen + 5;
		// idea is that there should be 5 blanks more than the max length

		for (int j = 0; j < taxonLabel.size(); j++) {
			String tmpTaxon = StringUtil.tokenize(taxonLabel.get(j));
			StringBuilder lineBldr = new StringBuilder(tmpTaxon);
			int diff = strsize - tmpTaxon.length();
			// Here I am finding out how many spaces need to be added after Taxon Label.
			int ntens = diff / 10;
			int nones = diff % 10;
			addSpaces(lineBldr, ntens, nones);
			pBldr.append(lineBldr.toString());
			pBldr.append(tElementAsString.get(j)).append(TreebaseUtil.LINESEP);
		}

	}

	/**
	 * 
	 * Appends number of 10 & 1 empty spaces after taxon depending upon the argument values.
	 * <p>
	 * This method takes in the Taxon Label and number of spaces to be appended as input as 10s and
	 * 1s and these spaces are appended to the StringBuilder, which is returned.
	 * 
	 * @param stbldr
	 * @param tens
	 * @param ones
	 */
	public void addSpaces(StringBuilder stbldr, int tens, int ones) {

		stbldr.append(TreebaseUtil.getSpaces(tens * 10));
		stbldr.append(TreebaseUtil.getSpaces(ones));
	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#generateSetsNexusBlock(java.lang.StringBuilder)
	 */
	public void generateSetsNexusBlock(StringBuilder pBuilder) {

		StringBuilder tmpBldr = new StringBuilder("BEGIN SETS;").append(TreebaseUtil.LINESEP);
		boolean test = false;

		for (CharSet aset : (Set<CharSet>) getCharSets()) {
			test = true;
			tmpBldr.append("\t").append(aset.getNexusString()).append(";").append(TreebaseUtil.LINESEP);
		}
		tmpBldr.append("END;").append(TreebaseUtil.LINESEP);

		if (test) {
			pBuilder.append(tmpBldr.toString());
		}
	}

	/**
	 * 
	 * 
	 * @return maximum length of Taxon Label
	 */
	@Transient
	public int getMaxTaxonLabelLength() {
		return mMaxTaxonLabelLength;
	}

	/**
	 * pMaxTaxonLabelLength is maximum length for a taxon label in a particular Matrix. This is not
	 * a fixed number; it depends on the Taxon Label Length.
	 */
	@Transient
	public void setMaxTaxonLabelLength(int pMaxTaxonLabelLength) {
		mMaxTaxonLabelLength = pMaxTaxonLabelLength;
	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#generateAssumptionsNexusBlock(java.lang.StringBuilder)
	 */
	public void generateAssumptionsNexusBlock(StringBuilder pStngbldr) {
		
		//Let's generate CodonPosSet if it is defined:
		generateCodonsNexusBlock(pStngbldr);
		
		boolean test = false;
		StringBuilder tmpBldr = new StringBuilder("BEGIN ASSUMPTIONS;")
			.append(TreebaseUtil.LINESEP);

		Iterator<CharWeightSet> itercharwtset = getWeightSets().iterator();
		while (itercharwtset.hasNext()) {// I could have used the for loop as well
			tmpBldr.append("\tWTSET ");
			CharWeightSet acharwtset = itercharwtset.next();
			if (isDefaultWeightSet(acharwtset)) {
				tmpBldr.append('*');
			}
			tmpBldr.append(" ").append(acharwtset.getTitle()).append(" = ");
			Set<? extends CharWeight> charwtlot = acharwtset.getCharWeights();

			boolean isFirst = true;
			for (CharWeight acharwt : charwtlot) {
				test = true;// It means there is at least one record.
				if (!isFirst) {
					tmpBldr.append(", ");
				} else {
					isFirst = false;
				}
				tmpBldr.append(acharwt.getWeightAndColumnAsString());
			}
			tmpBldr.append(";").append(TreebaseUtil.LINESEP);
		}

		Iterator<TypeSet> itertypeset = getTypeSets().iterator();
		while (itertypeset.hasNext()) {
			tmpBldr.append("\tTYPESET ");
			TypeSet atypeset = itertypeset.next();
			if (isDefaultTypeSet(atypeset)) {
				tmpBldr.append('*');
			}			
			tmpBldr.append(' ').append(atypeset.getTitle()).append(" = ");
			Set<UserTypeRecord> usertyperecordset = atypeset.getTypeRecords();

			boolean isFirst = true;
			for (UserTypeRecord arecord : usertyperecordset) {
				test = true;// It means there is at least one record.
				if (!isFirst) {
					tmpBldr.append(", ");
				} else {
					isFirst = false;
				}
				tmpBldr.append(arecord.getWeightAndColumnAsString());
			}
			tmpBldr.append(TreebaseUtil.LINESEP);
		}
		tmpBldr.append("END;").append(TreebaseUtil.LINESEP);

		if (test) {
			pStngbldr.append(tmpBldr.toString());
		}
	}

	/**
	 * Generate CODONS nexus block.
	 * 
	 * @param pStrBuilder
	 */
	@Transient
	private StringBuilder generateCodonsNexusBlock(StringBuilder pStrBuilder) {

		if (getCodonPosSets().isEmpty() && getCodeSets().isEmpty()) {
			//nothing defined. 
			return null;
		}
		
		pStrBuilder.append("BEGIN CODONS;").append(TreebaseUtil.LINESEP);

		Iterator<CodonPositionSet> iterCodonPosSet = getCodonPosSets().iterator();
		while (iterCodonPosSet.hasNext()) {
			pStrBuilder.append("CODONPOSSET ");
			CodonPositionSet codonSet = iterCodonPosSet.next();
			if (isDefaultCodonSet(codonSet)) {
				pStrBuilder.append('*');
			}
			pStrBuilder.append(" ").append(codonSet.getTitle()).append(" = ").append(TreebaseUtil.LINESEP);			
			codonSet.generateNexusString(pStrBuilder);
		}

		//TODO: generate CODESET here
				
		pStrBuilder.append("END;").append(TreebaseUtil.LINESEP);
		return pStrBuilder;
	}

	/**
	 * @return a list of all the columns which have a character that has a description
	 * @author mjd 20080724
	 */
	@Transient
	public List<MatrixColumn> getDescribedColumns() {
		List<MatrixColumn> result = new ArrayList<MatrixColumn> ();
		for (MatrixColumn mc : getColumnsReadOnly()) {
			PhyloChar character = mc.getCharacter();
			if (character != null && character.isInteresting() && character.getDescription() != null) {
				result.add(mc);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#getnChar()
	 */
	public Integer getnChar() {
		// Previously, mNchar was an int, which meant it was
		// impossible to distinguish between "unassigned" and truly
		// "0". I changed mNchar to the boxed Integer, so we can test
		// whether or not it has been assigned, and if not we
		// compute and store it on the fly. rav 20090228
		if ( mNchar == null || mNchar == 0 ) { // I don't trust "0"
			mNchar = this.getColumnsReadOnly().size();
		}
		return mNchar.intValue();
//		return mNchar;
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#setnChar(java.lang.Integer)
	 */
	public void setnChar(Integer nChar) {
		mNchar = nChar;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#getnTax()
	 */
	public Integer getnTax() {
		// Previously, mNtax was an int, which meant it was
		// impossible to distinguish between "unassigned" and truly
		// "0". I changed mNtax to the boxed Integer, so we can test
		// whether or not it has been assigned, and if not we
		// compute and store it on the fly. rav 20090228
//		if ( mNtax == null || mNtax == 0 ) {
//			mNtax = this.getRowsReadOnly().size();
//		}
//		return mNtax.intValue();
		return mNtax;
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.Matrix#setnTax(java.lang.Integer)
	 */
	public void setnTax(Integer nTax) {
		mNtax = nTax;
	}
	
	@Override
	@Transient
	public void setDimensions() {
		setnChar(getColumnsReadOnly().size());
		setnTax(getRowsReadOnly().size());
	}
	
	@Override
	@Transient
	public boolean isCharacterMatrix() { return true; }
}
