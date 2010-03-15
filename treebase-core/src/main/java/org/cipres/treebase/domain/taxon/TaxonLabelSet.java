package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

/**
 * TaxonLabelSet.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TaxonLabelSET")
@AttributeOverride(name = "id", column = @Column(name = "TaxonLabelSET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
public class TaxonLabelSet extends AbstractPersistedObject {

	private static final long serialVersionUID = 5373942900819725860L;

	private String mTitle;
	private boolean mTaxa;

	private Study mStudy;

	private List<TaxonLabel> mTaxonLabelList;

	/**
	 * Constructor.
	 */
	public TaxonLabelSet() {
		super();

		mTaxonLabelList = new ArrayList<TaxonLabel>();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		if ( ! TreebaseUtil.isEmpty(mTitle) ) {
			return mTitle;
		}
		else {
			return "TaxonLabelSet" + getId();
		}
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Indicate whether the taxonlabelset represents a "taxa".
	 * 
	 * @return boolean
	 */
	@Column(name = "Taxa")
	public boolean isTaxa() {
		return mTaxa;
	}

	/**
	 * Set the Taxa field.
	 */
	public void setTaxa(boolean pNewTaxa) {
		mTaxa = pNewTaxa;
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "STUDY_ID", nullable = true)
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
	 * Append a new TaxonLabel to the end of the list.
	 * 
	 * <p>
	 * <b>Warning:</b> does not check to see if the new TaxonLabel is already in the set.
	 * </p>
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabel TaxonLabel
	 */
	public void addPhyloTaxonLabel(TaxonLabel pTaxonLabel) {
		if (pTaxonLabel != null) {
			getTaxonLabelList().add(pTaxonLabel);
		}
	}

	/**
	 * Clear taxon label list.
	 * 
	 * Creation date: Mar 14, 2006
	 */
	public void clearTaxonLabelList() {
		getTaxonLabelList().clear();
	}

	/**
	 * Remove the TaxonLabel.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabel TaxonLabel
	 */
	public void removePhyloTaxonLabel(TaxonLabel pTaxonLabel) {
		if (pTaxonLabel != null) {
			getTaxonLabelList().remove(pTaxonLabel);
		}
	}

	/**
	 * Return the TaxonLabelList field.
	 * 
	 * @return List<PhyloTaxonLabel>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "TaxonLabelSET_TaxonLabel", joinColumns = {@JoinColumn(name = "TaxonLabelSET_ID")}, inverseJoinColumns = @JoinColumn(name = "TaxonLabel_ID"))
	@IndexColumn(name = "TaxonLabel_ORDER")
	protected List<TaxonLabel> getTaxonLabelList() {
		return mTaxonLabelList;
	}

	/**
	 * Return a read only list of the taxon labels.
	 * 
	 * @return
	 */
	@Transient
	public List<TaxonLabel> getTaxonLabelsReadOnly() {
		return Collections.unmodifiableList(mTaxonLabelList);
	}

	/**
	 * Set the TaxonLabelList field.
	 */
	protected void setTaxonLabelList(List<TaxonLabel> pTaxonLabelList) {
		mTaxonLabelList = pTaxonLabelList;
	}

	/**
	 * Build the nexus block for the taxon label set.
	 * 
	 * @param pBuilder
	 * @param pOnePerLine whether to output in one taxon label per line
	 * @param pLineNumber in one taxon label per line, whether to add line number (like the TREES
	 *            TRANSLATE section)
	 */
	public void buildNexusBlockTaxa(StringBuilder pBuilder, boolean pOnePerLine, boolean pLineNumber) {

		List<TaxonLabel> txnlbllist = getTaxonLabelsReadOnly();
		int numoftxnlbls = txnlbllist.size();

		pBuilder.append("BEGIN TAXA;\n");
		pBuilder.append("        TITLE  " + StringUtil.tokenize(getTitle()) + ";\n");
		pBuilder.append("        DIMENSIONS NTAX=" + numoftxnlbls + ";\n");
		pBuilder.append("        TAXLABELS\n");
		pBuilder.append("            ");

		if (pOnePerLine) {
			if (pLineNumber) {
				buildTaxonWithLineNumber(pBuilder);
			} else {
				for (int z = 0; z < numoftxnlbls; z++) {
					pBuilder.append(StringUtil.tokenize(txnlbllist.get(z).getTaxonLabel()));
					pBuilder.append("\n            ");
					// out.write(txnlbllist.get(z).getTaxonLabel().replaceAll(" ", "_") + " ");
				}
			}
		} else {
			for (int z = 0; z < numoftxnlbls; z++) {
				pBuilder.append(StringUtil.tokenize(txnlbllist.get(z).getTaxonLabel()));
				pBuilder.append(" ");
			}
		}

		pBuilder.append(";\nEND;\n\n");
	}

	/**
	 * Build the taxon labels. One label per line, with line number.
	 *  
	 * @param pBuilder
	 */
	public void buildTaxonWithLineNumber(StringBuilder pBuilder) {

		List<TaxonLabel> txnlbllist = getTaxonLabelList();
		int numoftxnlbls = txnlbllist.size();
		for (int z = 0; z < numoftxnlbls; z++) {
			pBuilder.append("            ");
			int p = z + 1;
			pBuilder.append(String.valueOf(p));
			if (p < 10) {
				pBuilder.append("    ");
			} else if (p >= 10 && p < 100) {
				pBuilder.append("   ");
			} else if (p >= 100 && p < 1000) {
				pBuilder.append("  ");
			} else {
				pBuilder.append(" ");
			}
			pBuilder.append(StringUtil.tokenize(txnlbllist.get(z).getTaxonLabel()));
			if (p == numoftxnlbls) {
				pBuilder.append(";");
			} else {
				pBuilder.append(",");
			}
			pBuilder.append("\n");
		}
	}
	
	@Transient
	public String getLabel() {
		return getTitle();
	}

}
