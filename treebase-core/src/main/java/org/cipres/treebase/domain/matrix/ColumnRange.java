package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * ColumnRange.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "COLUMNRANGE")
@AttributeOverride(name = "id", column = @Column(name = "COLUMNRANGE_ID"))
public class ColumnRange extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private Integer mStartColIndex;
	private Integer mEndColIndex;
	private Integer mRepeatInterval;

	/**
	 * Constructor.
	 */
	public ColumnRange() {
		super();
	}

	/**
	 * Return the RepeatInterval field.
	 * 
	 * @return Integer
	 */
	@Column(name = "RepeatInterval", nullable = true)
	public Integer getRepeatInterval() {
		return mRepeatInterval;
	}

	/**
	 * Set the RepeatInterval field.
	 */
	public void setRepeatInterval(Integer pNewRepeatInterval) {
		mRepeatInterval = pNewRepeatInterval;
	}

	/**
	 * Return the EndColIndex field.
	 * 
	 * @return Integer
	 */
	@Column(name = "EndColIndex", nullable = true)
	public Integer getEndColIndex() {
		return mEndColIndex;
	}

	/**
	 * Set the EndColIndex field.
	 */
	public void setEndColIndex(Integer pNewEndColIndex) {
		mEndColIndex = pNewEndColIndex;
	}

	/**
	 * Return the StartColIndex field.
	 * 
	 * @return Integer
	 */
	@Column(name = "StartColIndex", nullable = true)
	public Integer getStartColIndex() {
		return mStartColIndex;
	}

	/**
	 * Set the StartColIndex field.
	 */
	public void setStartColIndex(Integer pNewStartColIndex) {
		mStartColIndex = pNewStartColIndex;
	}

	/**
	 * Print the column range to the string buffer. Return the string buffer for call chaining.
	 */
	public StringBuilder appendRange(StringBuilder pBuilder) {

		// Note: convert 0 based index to 1 based column index when printing:

		if (getStartColIndex() != null) {
			pBuilder.append(getStartColIndex() + 1);
			if (!getStartColIndex().equals(getEndColIndex())) {
				pBuilder.append("-").append(getEndColIndex() + 1);
			}

			if (getRepeatInterval() != null && getRepeatInterval() != 0) {
				pBuilder.append("\\").append(getRepeatInterval());
			}
		}

		return pBuilder;
	}

}
