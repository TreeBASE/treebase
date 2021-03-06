
package org.cipres.treebase.domain.study;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;

/**
 * BookCitation.java
 * 
 * Created on Feb 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("B")
public class BookCitation extends Citation {

	private static final long serialVersionUID = -8850249636294610488L;

	public static final String CITATION_TYPE_BOOK = "Book";

	private String mBookTitle;
	private String mPublisher;
	private String mCity;
	private String mISBN;

	private List<Person> mEditors = new ArrayList<Person>();

	/**
	 * Constructor.
	 */
	public BookCitation() {
		super();
	}

	/**
	 * Return the Editors field.
	 * 
	 * @return List<Person> mEditors
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "CITATION_EDITOR", joinColumns = {@JoinColumn(name = "CITATION_ID")})
	@IndexColumn(name = "EDITOR_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "studyCache")
	public List<Person> getEditors() {
		return mEditors;
	}

	/**
	 * Set the Editors field.
	 */
	public void setEditors(List<Person> pNewEditors) {
		mEditors = pNewEditors;
	}

	/**
	 * Return the ISBN field.
	 * 
	 * @return String
	 */
	@Column(name = "ISBN", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getISBN() {
		return mISBN;
	}

	/**
	 * Set the ISBN field.
	 */
	public void setISBN(String pNewISBN) {
		mISBN = pNewISBN;
	}

	/**
	 * Return the City field.
	 * 
	 * @return String
	 */
	@Column(name = "City", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getCity() {
		return mCity;
	}

	/**
	 * Set the City field.
	 */
	public void setCity(String pNewCity) {
		mCity = pNewCity;
	}

	/**
	 * Return the Publisher field.
	 * 
	 * @return String
	 */
	@Column(name = "Publisher", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPublisher() {
		return mPublisher;
	}

	/**
	 * Set the Publisher field.
	 */
	public void setPublisher(String pNewPublisher) {
		mPublisher = pNewPublisher;
	}

	/**
	 * Return the BookTitle field.
	 * 
	 * @return String
	 */
	@Column(name = "BookTitle", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getBookTitle() {
		return mBookTitle;
	}
	
	/**
	 * Return the BookTitle
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getTitle()
	 * @return String
	 */
	@Override
	@Transient
	public String getTitle () {
		/*
		 * We always want getTitle() to return something.
		 * For ArticleCitations, this field would normally
		 * be set with the name of the article. For InBookCitation,
		 * this field is populated with the section title, so
		 * InBookCitation.getSectionTitle() calls the getTitle()
		 * method defined here, from whence we need to go up
		 * the inheritance tree to the parent Citation class. For
		 * BookCitations, the title field is unset, so we return
		 * the BookTitle field instead.
		 */
		if ( this instanceof InBookCitation ) {
			return super.getTitle();
		}
		return getBookTitle();
	}

	/**
	 * Set the BookTitle field.
	 */
	public void setBookTitle(String pNewBookTitle) {
		mBookTitle = pNewBookTitle;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getRealCitationType()
	 */
	@Override
	@Transient
	protected String getRealCitationType() {
		return CITATION_TYPE_BOOK;
	}

	@Transient
	@Override
	public void getDetailedPublicationInformation(StringBuilder pBuilder, boolean pGenerateHtml) {
		pBuilder
			.append(". ").append(getBookTitleInformation()).append(", ").append(getCity()).append(
				", ").append(getPublisher()).append(".");
	}

	/**
	 * @author madhu September, 2007
	 * @return
	 */
	@Transient
	public String getEditorsAsString(boolean pGenerateHtml) {
		StringBuilder editorsCitationStyle = new StringBuilder();
		List<Person> editors = getEditors();
		int size = editors.size();

		if (size > 0) {

			for (int i = 0; i < size; i++) {
				if (pGenerateHtml) {
					editorsCitationStyle.append("<a href='mailto:");
					editorsCitationStyle.append(editors.get(i).getEmailAddressString());
					editorsCitationStyle.append("?subject=From Treebase-2 Community'>");
				}
				editorsCitationStyle.append(editors.get(i).getFullNameCitationStyle());
				if (pGenerateHtml) {
					editorsCitationStyle.append("</a>");
				}

				if (size > 1 && i == size - 2) {
					editorsCitationStyle.append(", & ");
				} else if (size > 1 && i < size - 2) {
					editorsCitationStyle.append(", ");
				}
			}

		}

		return editorsCitationStyle.toString();

	}

	/**
	 * @return book title with a dot at the end, if the dot does not previously exist.
	 */

	@Transient
	public String getBookTitleInformation() {
		String bookTitle = getBookTitle().trim();
		if (bookTitle.endsWith(".")) {
			return bookTitle;
		} else {
			return bookTitle + ".";
		}
	}

}
