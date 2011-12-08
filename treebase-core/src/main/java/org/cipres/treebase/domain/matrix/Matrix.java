
package org.cipres.treebase.domain.matrix;

import java.util.Comparator;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;

/**
 * Matrix.java
 * 
 * Created on February 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "MATRIX")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MATRIXTYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "MATRIX_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@DiscriminatorValue("-")
public abstract class Matrix extends AbstractPersistedObject {

	private String mTitle;
	private char mGapSymbol = DiscreteCharState.GAP_SYMBOL;
	private char mMissingSymbol = DiscreteCharState.MISSING_SYMBOL;
	private String mDescription;
	private String mNexusFileName;
	private String mSymbols;
	private String mTB1MatrixID;

	private boolean mPublished;

	private MatrixDataType mDataType;
	private MatrixKind mMatrixKind;
	private Study mStudy;
	private TaxonLabelSet mTaxa;

	//transient
	//Help to display/capture the string from interface:
	private String mKindDescription;
	private boolean mAnalyzed;
	
	private Integer mNtax; // 20081204 mjd
	private Integer mNchar; // 20081204	

	/**
	 * Constructor.
	 */
	public Matrix() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Override
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
	 * Return the MissingSymbol field. getAnalysisStepReadOnly().)
	 * 
	 * @return Character
	 */
	// @Column(name = "MissingSymbol", length = TBPersistable.COLUMN_LENGTH_STRING)
	@Column(name = "MissingSymbol")
	public char getMissingSymbol() {
		return mMissingSymbol;
	}

	/**
	 * Set the MissingSymbol field.
	 */
	public void setMissingSymbol(char pNewMissingSymbol) {
		mMissingSymbol = pNewMissingSymbol;
	}

	/**
	 * Return the GapSymbol field.
	 * 
	 * @return char
	 */
	// @Column(name = "GapSymbol", length = TBPersistable.COLUMN_LENGTH_STRING)
	@Column(name = "GapSymbol")
	public char getGapSymbol() {
		return mGapSymbol;
	}

	/**
	 * Set the GapSymbol field.
	 */
	public void setGapSymbol(char pNewGapSymbol) {
		mGapSymbol = pNewGapSymbol;
	}

	/**
	 * The defined symbols for the matrix, delimited by one empty space. One symbol is one
	 * character.
	 * 
	 * @return String
	 */
	@Column(name = "Symbols", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getSymbols() {
		return mSymbols;
	}

	/**
	 * Set the Symbols field.
	 */
	public void setSymbols(String pNewSymbols) {
		mSymbols = pNewSymbols;
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Return the NexusFileName field.
	 * 
	 * @return String mNexusFileName
	 */
	@Column(name = "NexusFileName", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getNexusFileName() {
		return mNexusFileName;
	}

	/**
	 * Set the NexusFileName field.
	 */
	public void setNexusFileName(String pNewNexusFileName) {
		mNexusFileName = pNewNexusFileName;
	}

	/**
	 * Return the TB1 MatrixID field.
	 * 
	 * @return String 
	 */
	@Column(name = "TB_MatrixID", length = TBPersistable.COLUMN_LENGTH_30)
	public String getTB1MatrixID() {
		return mTB1MatrixID;
	}

	/**
	 * Set the TB1MatrixID field.
	 */
	public void setTB1MatrixID(String pNewTB1MatrixID) {
		mTB1MatrixID = pNewTB1MatrixID;
	}
	
	/**
	 * Return the Analyzed field.
	 * 
	 * @return boolean mAnalyzed
	 */
	@Transient
	public boolean isAnalyzed() {
		return mAnalyzed;
	}
	
	/**
	 * Set the Analyzed field.
	 */	
	public void setAnalyzed(boolean pNewAnalyzed) {
		mAnalyzed = pNewAnalyzed;
	}
	
	
	/**
	 * Return the Published field.
	 * 
	 * @return boolean mPublished
	 */
	@Column(name = "Published")
	public boolean isPublished() {
		return mPublished;
	}

	/**
	 * Set the Published field.
	 */
	public void setPublished(boolean pNewPublished) {
		mPublished = pNewPublished;
	}

	/**
	 * Return the DataType field.
	 * 
	 * @return MatrixDataType
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "MATRIXDATATYPE_ID", nullable = true)
	public MatrixDataType getDataType() {
		return mDataType;
	}

	/**
	 * Set the DataType field.
	 */
	public void setDataType(MatrixDataType pNewDataType) {
		mDataType = pNewDataType;
	}

	/**
	 * Return the MatrixKind.
	 * 
	 * @return MatrixKind 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATRIXKIND_ID")
	public MatrixKind getMatrixKind() {
		return mMatrixKind;
	}

	/**
	 * Set the MatrixKind field.
	 */
	public void setMatrixKind(MatrixKind pNewMatrixKind) {
		mMatrixKind = pNewMatrixKind;
	}
	
	/**
	 * Returns the matrix kind description. Uses a transient variable to enable
	 * setting the matrix kind as a string from the GUI. 
	 * 
	 * @return String 
	 */
	@Transient
	public String getKindDescription() {
		if (mKindDescription == null) {
			if (getMatrixKind() != null) {
				mKindDescription = getMatrixKind().getDescription();
			}
		}
		return mKindDescription;
	}

	/**
	 * Set the KindDescription field.
	 */
	public void setKindDescription(String pNewKindDescription) {
		mKindDescription = pNewKindDescription;
	}
	
	/**
	 * Use taxon label set to represent a "taxa". 
	 * 
	 * @return TaxonLabelSet 
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "TAXONLABELSET_ID")
	public TaxonLabelSet getTaxa() {
		return mTaxa;
	}

	/**
	 * Set the TaxonLabelSet field.
	 */
	public void setTaxa(TaxonLabelSet pNewTaxonLabelSet) {
		mTaxa = pNewTaxonLabelSet;
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study mStudy
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	@Fetch(FetchMode.JOIN)
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	public void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}

	/**
	 * Return a string describe the dimension info.
	 * 
	 * <p>This is used by the nexus file generator, so don't change the format.  20090420 MJD
	 * 
	 * @return <tt>"NCHAR=1234"</tt>
	 */
	@Transient
	public abstract String getDimensionsInfo();

	/**
	 * Use double dispatch pattern for the subclasses to handle cascade persist.
	 * 
	 * @param pMatrixHome
	 */
	public abstract void cascadePersist(MatrixHome pMatrixHome);

	/**
	 * Use double dispatch pattern for the subclasses to handle cascade delete.
	 * 
	 * @param pMatrixHome
	 */
	public abstract void cascadeDelete(MatrixHome pMatrixHome);

	/**
	 * Note: Do not use this method. It is only for data migration from TreeBASE I.
	 * 
	 * @param pID
	 */
	@Transient
	public void setMatrixID(Long pID) {
		super.setId(pID);
	}

	/**
	 * Return a string describe the format info, including the charStateLabels.
	 * 
	 * @return
	 */
	@Transient
	public String getFormatInfo() {
		StringBuilder builder = new StringBuilder();
		return generateFormatInfo(builder).toString();
	}

	/**
	 * Generate the format info and append to the builder.
	 * 
	 * @return
	 */
	@Transient
	protected StringBuilder generateFormatInfo(StringBuilder pBuilder) {
		pBuilder.append("\t");

		if (getDataType() != null) {
			pBuilder.append("FORMAT DATATYPE=").append(getDataType().getDescription()).append(
				TreebaseUtil.ANEMPTYSPACE);
		}

		if (!TreebaseUtil.isEmpty(getSymbols())) {
			pBuilder.append("SYMBOLS= \"");

			for (int i = 0; i < getSymbols().length(); i++) {
				if (i > 0) {
					pBuilder.append(' ');
				}
				pBuilder.append(getSymbols().charAt(i));
			}

			pBuilder.append("\" ");
		}

		pBuilder.append("MISSING=").append(getMissingSymbol()).append(" GAP= ").append(
			getGapSymbol()).append(";").append(TreebaseUtil.LINESEP);

		return pBuilder;
	}

	/**
	 * Generate the nexus block and append to the string builder.
	 * 
	 * @param pBuilder
	 * 
	 * @return
	 */
	@Transient
	public void generateNexusBlock(StringBuilder pBuilder) {

		pBuilder.append("BEGIN CHARACTERS;").append(TreebaseUtil.LINESEP);
		pBuilder
			.append("[! TreeBASE Matrix URI: ").append(getPhyloWSPath().getPurl()).append("]").append(TreebaseUtil.LINESEP)
			.append(TreebaseUtil.LINESEP);

		pBuilder.append("\tTITLE ").append(StringUtil.tokenize(getTitle())).append(";").append(
			TreebaseUtil.LINESEP);
		
		//If BEGIN CHARACTERS is used, there must be at least one "BEGIN TAXA" block 
		String taxaTitle = "?";
		if (getTaxa() != null) {
			taxaTitle = getTaxa().getTitle();
		} else {
			taxaTitle = "No taxa found for this matrix. Old data. Need to import this matrix again.";
		}
		pBuilder.append("\tLINK TAXA = " + StringUtil.tokenize(taxaTitle.replaceAll("Input|Output", "")) + ";").append(
			TreebaseUtil.LINESEP);
		
		pBuilder.append(getDimensionsInfo()).append(TreebaseUtil.LINESEP);

		generateFormatInfo(pBuilder).append(TreebaseUtil.LINESEP);

		generateMatrixTable(pBuilder);

		pBuilder.append("END;").append(TreebaseUtil.getLineSeparators(2));

		generateSetsNexusBlock(pBuilder);
		generateAssumptionsNexusBlock(pBuilder);

	}

	/**
	 * Return all taxon labels contained in this matrix.
	 * 
	 * @return
	 */
	@Transient
	public abstract List<TaxonLabel> getAllTaxonLabels();

	/**
	 * Override this method to generate nexus strings for the matrix table.
	 * 
	 * @param pBuilder
	 */
	protected abstract void generateSetsNexusBlock(StringBuilder pBuilder);

	protected abstract void generateMatrixTable(StringBuilder pBuilder);

	protected abstract void generateAssumptionsNexusBlock(StringBuilder pBuilder);
	
	/**
	 * Return the number of taxa in this matrix
	 * 
	 * @return number of taxa in this matrix
	 * @author mjd 20081202
	 */
	@Transient
	public Integer getnTax() {
		if ( mNtax == null ) {
			mNtax = getAllTaxonLabels().size();
		}
		return mNtax;
	}
	public void setnTax(Integer nTax) {
		mNtax = nTax;
	}
	
	/**
	 * Return the number of characters in this matrix
	 * 
	 * @return number of characters in this matrix
	 * @author mjd 20081202
	 */	
	@Transient
	public Integer getnChar() {
		// yes, this is evil. But the cardinal sin was to assume
		// that distance matrices are somehow a conceptually 
		// related to character state matrices. For other stop
		// gap measures to deal with that mismatch, I refer you
		// to the "implementation" of getnChar() and setnChar()
		// in DistanceMatrix. No, I didn't write those.
		if ( mNchar == null && this instanceof CharacterMatrix ) {
			mNchar = ((CharacterMatrix)this).getColumns().size();
		}
		return mNchar;
	}
	public abstract void setnChar(Integer nChar);
	
	@Transient
	public boolean isCharacterMatrix() { return false; }

	/**
	 * initial setting of nTAX and nCHAR attributes
	 * 
	 * @author mjd 20090420
	 */
	public void setDimensions() {
		// By default, do nothing
	}
	
	protected void sortRowByTaxonLabel(List<MatrixRow> rList)
	{
		java.util.Collections.sort(rList, new Comparator<MatrixRow>() {
			
			public int compare(MatrixRow pObject1, MatrixRow pObject2) {
				String id1 = pObject1.getTaxonLabel().getTaxonLabel();
				String id2 = pObject2.getTaxonLabel().getTaxonLabel();
				return id1.compareTo(id2);
			}
		
		});
		
	}
}
