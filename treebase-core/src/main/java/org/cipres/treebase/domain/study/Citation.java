
package org.cipres.treebase.domain.study;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.springframework.beans.BeanUtils;

import org.cipres.treebase.Constants;
import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.Annotation;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;

/**
 * Citation.java
 * 
 * Created on February 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CITATION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "CITATION_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "studyCache")
public class Citation extends AbstractPersistedObject {

	private static final long serialVersionUID = 6723537853962129546L;

	private String mTitle;
	private Integer mPublishYear;
	private String mKeywords;
	private String mPages;
	private String mURL; // TODO: change to a URL class.
	private String mDoi;
	private String mPMID;
	private String mAbstract;
	private boolean mPublished;

	// transient
	private String mCitationType;
	private String mStatusDescriptionUpdate;

	private Study mStudy;
	private CitationStatus mCitationStatus;

	private List<Person> mAuthors = new ArrayList<Person>();

	/**
	 * create a new citation. Copy appropriate properties.
	 * 
	 * @param pCitation
	 * @return
	 */
	public static Citation factory(Citation pCitation) {
		if (!pCitation.isCitationTypeChanged()) {
			return pCitation;
		}

		String newType = pCitation.getCitationType();
		Citation newCitation = factory(newType);

		// copy all matching properties:
		BeanUtils.copyProperties(pCitation, newCitation);

		newCitation.setCitationStatus(pCitation.getCitationStatus());

		return newCitation;
	}

	/**
	 * Create a new citation by type. Return an ArticleCitation if the type parameter is null.
	 * 
	 * @param pCitationType
	 * @return
	 */
	public static Citation factory(String pCitationType) {

		if (pCitationType == null) {
			return new ArticleCitation();
		}

		Citation newCitation = null;
		if (BookCitation.CITATION_TYPE_BOOK.compareToIgnoreCase(pCitationType) == 0) {
			newCitation = new BookCitation();
		} else if (InBookCitation.CITATION_TYPE_BOOKSECTION.compareToIgnoreCase(pCitationType) == 0) {
			newCitation = new InBookCitation();
		} else if (ArticleCitation.CITATION_TYPE_ARTICLE.compareToIgnoreCase(pCitationType) == 0) {
			newCitation = new ArticleCitation();
		} else {
			// default:
			newCitation = new ArticleCitation();
		}

		return newCitation;
	}

	/**
	 * Constructor.
	 */
	public Citation() {
		super();

		mPublishYear = Calendar.getInstance().get(Calendar.YEAR);

		setCitationType(getRealCitationType());
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.CITATION_TITLE_COLUMN_LENGTH)
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
	 * Return the URL field.
	 * 
	 * @return String
	 */
	@Column(name = "URL", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getURL() {
		return mURL;
	}

	/**
	 * Set the URL field.
	 */
	public void setURL(String pNewURL) {
		mURL = pNewURL;
	}

	/**
	 * Return the Pages field.
	 * 
	 * @return String
	 */
	@Column(name = "Pages", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPages() {
		return mPages;
	}

	/**
	 * Set the Pages field.
	 */
	public void setPages(String pNewPages) {
		mPages = pNewPages;
	}

	/**
	 * Return the Keywords field.
	 * 
	 * @return String
	 */
	@Column(name = "Keywords", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getKeywords() {
		return mKeywords;
	}

	/**
	 * Set the Keywords field.
	 */
	public void setKeywords(String pNewKeywords) {
		mKeywords = pNewKeywords;
	}

	/**
	 * Return the PublishYear field.
	 * 
	 * @return Integer
	 */
	@Column(name = "PublishYear")
	public Integer getPublishYear() {
		return mPublishYear;
	}

	/**
	 * Set the PublishYear field.
	 */
	public void setPublishYear(Integer pNewPublishYear) {
		mPublishYear = pNewPublishYear;
	}

	/**
	 * Return the PMID field.
	 * 
	 * @return String
	 */
	@Column(name = "PMID", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPMID() {
		return mPMID;
	}

	/**
	 * Set the PMID field.
	 */
	public void setPMID(String pNewPMID) {
		mPMID = pNewPMID;
	}

	/**
	 * Return the Doi field.
	 * 
	 * @return String
	 */
	@Column(name = "Doi", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDoi() {
		return mDoi;
	}

	/**
	 * Set the Doi field.
	 */
	public void setDoi(String pNewDoi) {
		mDoi = pNewDoi;
	}

	/**
	 * Return the Abstract field.
	 * 
	 * @return String
	 */
	@Column(name = "ABSTRACT", length = TBPersistable.CITATION_ABSTRACT_COLUMN_LENGTH)
	public String getAbstract() {
		return mAbstract;
	}

	/**
	 * Set the Abstract field.
	 */
	public void setAbstract(String pNewAbstract) {
		mAbstract = pNewAbstract;
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
	 * Return the Study field.
	 * 
	 * @return Study
	 */
	@OneToOne(mappedBy = "citation")
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
	 * Get an ordered list of authors.
	 * @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "studyCache")
	 * @return
	 */	 
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "CITATION_AUTHOR", joinColumns = {@JoinColumn(name = "CITATION_ID")})
	@IndexColumn(name = "AUTHOR_ORDER")	
	@Fetch(FetchMode.JOIN)
	public List<Person> getAuthors() {
		return mAuthors; // The fetch annotation prevents org.hibernate.LazyInitializationException
	}

	/**
	 * 
	 * @param pAuthors The authors to set.
	 */
	public void setAuthors(List<Person> pAuthors) {
		mAuthors = pAuthors;
	}


	/**
	 * Add an author to the list of authors for this citation
	 * Does not check for duplication
	 * 
	 * @param pAuthor The author to add
	 * @author mjd 20080716 
	 */
	public void addAuthor(Person pAuthor) {
		if (mAuthors == null) {
			mAuthors = new ArrayList<Person>();
		}
		mAuthors.add(pAuthor);
	}
	
	/**
	 * Return the type. It can be changed from outside.
	 * 
	 * @return
	 */
	@Transient
	public String getCitationType() {
		return mCitationType;
	}

	/**
	 * Set the type information.
	 * 
	 * @param pType
	 */
	public void setCitationType(String pType) {
		mCitationType = pType;
	};

	/**
	 * Return true if the citation type is changed.
	 * 
	 */
	@Transient
	public boolean isCitationTypeChanged() {
		return (getCitationType() != null && !getRealCitationType().equalsIgnoreCase(
			getCitationType()));
	}

	/**
	 * Get the build in citation type.
	 * 
	 * @return
	 */
	@Transient
	protected String getRealCitationType() {
		return null;
	}

	/**
	 * Return the CitationStatus field.
	 * 
	 * @return CitationStatus
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinColumn(name = "CITATIONSTATUS_ID")
	public CitationStatus getCitationStatus() {
		return mCitationStatus;
	}

	/**
	 * Set the CitationStatus field.
	 */
	public void setCitationStatus(CitationStatus pNewCitationStatus) {
		mCitationStatus = pNewCitationStatus;
	}

	/**
	 * Return the citation status description.
	 * 
	 * @return
	 */
	@Transient
	public String getCitationStatusDescription() {
		if (getCitationStatus() != null) {
			return getCitationStatus().getDescription();
		}

		return "";
	}

	/**
	 * Save the changed citation status description.
	 * 
	 * @param pDesc
	 */
	public void setCitationStatusDescription(String pDesc) {
		setStatusDescriptionUpdate(pDesc);
	}

	/**
	 * Return the StatusDescriptionUpdate field.
	 * 
	 * @return String mStatusDescriptionUpdate
	 */
	@Transient
	public String getStatusDescriptionUpdate() {
		return mStatusDescriptionUpdate;
	}

	/**
	 * Set the StatusDescriptionUpdate field.
	 */
	private void setStatusDescriptionUpdate(String pNewStatusDescriptionUpdate) {
		mStatusDescriptionUpdate = pNewStatusDescriptionUpdate;
	}

	@Transient
	public String getAuthorsCitationStyle() {

		StringBuilder authorsCitationStyle = new StringBuilder();
		List<Person> authors = getAuthors();
		int size = authors.size();

		if (size > 0) {

			for (int i = 0; i < size; i++) {

				authorsCitationStyle.append("<a href='mailto:");
				authorsCitationStyle.append(authors.get(i).getEmailAddressString());
				authorsCitationStyle.append("?subject=From Treebase-2 Community'>");
				authorsCitationStyle.append(authors.get(i).getFullNameCitationStyle());
				authorsCitationStyle.append("</a>");

				if (size > 1 && i == size - 2) {
					authorsCitationStyle.append(", & ");
				} else if (size > 1 && i < size - 2) {
					authorsCitationStyle.append(", ");
				}
			}

		}

		authorsCitationStyle.append(" ").append(getPublishYear());
		getDetailedPublicationInformation(authorsCitationStyle, true);
		return authorsCitationStyle.toString();
	}

	@Transient
	public String getAuthorsCitationStyleWithoutHtml() {

		StringBuilder authorsCitationStyle = new StringBuilder();
		try {
			List<Person> authors = getAuthors();
			int size = authors.size();
	
			if (size > 0) {
	
				for (int i = 0; i < size; i++) {
	
					authorsCitationStyle.append(authors.get(i).getFullNameCitationStyle());
	
					if (size > 1 && i == size - 2) {
						authorsCitationStyle.append(", & ");
					} else if (size > 1 && i < size - 2) {
						authorsCitationStyle.append(", ");
					}
				}
	
			}
	
			authorsCitationStyle.append(" ").append(getPublishYear());
			getDetailedPublicationInformation(authorsCitationStyle, false);
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return authorsCitationStyle.toString();
	}
	
	@Transient
	private String getAuthorsAsString() {
		StringBuilder authorsCitationStyle = new StringBuilder();
		List<Person> authors = getAuthors();
		int size = authors.size();
		if (size > 0) {
			for ( int i = 0; i < size; i++ ) {
				authorsCitationStyle.append(authors.get(i).getFullNameCitationStyle());
				if ( size > 1 && i == size - 2 ) {
					authorsCitationStyle.append(", & ");
				} 
				else if (size > 1 && i < size - 2) {
					authorsCitationStyle.append(", ");
				}
			}
		}
		return authorsCitationStyle.toString();
	}
	
	@Transient
	public String getAuthorsAsBibtex() {
		return catPersonsBibtex(getAuthors());
	}
	
	@Transient
	public String getRisReference () {
		StringBuilder ris = new StringBuilder(), sub = new StringBuilder();
		try {
			String citationType = this.getRealCitationType(), cls = "\n";
			boolean isArticle = false, isBook = false, isInBook = false;		
			if ( citationType.equals(ArticleCitation.CITATION_TYPE_ARTICLE) ) {
				ris.append("TY  - JOUR\n");
				sub.append("JF  - ").append(appendMe(((ArticleCitation)this).getJournal())).append(cls);
				sub.append("VL  - ").append(appendMe(((ArticleCitation)this).getVolume())).append(cls);
				sub.append("IS  - ").append(appendMe(((ArticleCitation)this).getIssue())).append(cls);
				isArticle = true;
			}
			else if ( citationType.equals(BookCitation.CITATION_TYPE_BOOK) ) {
				ris.append("TY  - BOOK\n");
				isBook = true;
			}	
			else if ( citationType.equals(InBookCitation.CITATION_TYPE_BOOKSECTION) ) {
				ris.append("TY  - CHAP\n");
				sub.append("TI  - ").append(appendMe(((InBookCitation)this).getBookTitle())).append(cls);
				isInBook = true;
			}	
			if ( isBook || isInBook) {
				sub.append("SN  - ISBN ").append(appendMe(((BookCitation)this).getISBN())).append(cls);
				sub.append("PB  - ").append(appendMe(((BookCitation)this).getPublisher())).append(cls);
				sub.append("CY  - ").append(appendMe(((BookCitation)this).getCity())).append(cls);
				catPersonsRis(sub,"ED - ",((BookCitation)this).getEditors());			
			}
			if ( isInBook || isArticle ) {
				String pages = getPages();
				if ( pages != null && pages.matches("^\\d+-\\d+$") ) {
					String[] pageRange = pages.split("-");
					sub.append("SP  - ").append(pageRange[0]).append(cls);
					sub.append("EP  - ").append(pageRange[1]).append(cls);
				}
			}
			ris.append("ID  - ").append(getId()).append(cls);	
			catPersonsRis(ris,"AU  - ",getAuthors());		
			ris.append("T1  - ").append(appendMe(getTitle())).append(cls);		
			ris.append("PY  - ").append(appendMe(getPublishYear())).append(cls);
			String keyWords = getKeywords();
			if ( keyWords != null ) {
				String[] words = keyWords.split(",\\s*");
				for ( int i = 0; i < words.length; i++ ) {
					ris.append("KW  - ").append(appendMe(words[i])).append(cls);
				}
			}
			String url = getURL(), doi = getDoi(), pmid = getPMID();
			String theUrl = null;
			if ( url != null && ! url.equals("http://") ) {
				theUrl = url;
			}
			else if ( doi != null ) {
				theUrl = "http://dx.doi.org/" + doi;
			}
			else if ( pmid != null ) {
				theUrl = "http://pmid.us/" + pmid;
			}
			ris.append("UR  - ").append(appendMe(theUrl)).append(cls);
			ris.append("N2  - ").append(appendMe(getAbstract())).append(cls);		
			ris.append("L3  - ").append(appendMe(getDoi())).append(cls);
			ris.append(sub);
			ris.append("ER  - ").append(cls);
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return ris.toString();		
	}
	
	private void catPersonsRis(StringBuilder sb,String pre,List<Person> persons) {
		String cls = "\n";
		for ( Person p : persons ) {
			sb.append(pre)
				.append(p.getLastName())
				.append(",")
				.append(p.getFirstName())
				.append(" ")
				.append(p.getMiddleName())
				.append(cls);
		}
	}
	
	@Transient
	public String getBibtexReference () {
		StringBuilder bib = new StringBuilder(), sub = new StringBuilder();
		try {
			String citationType = this.getRealCitationType(), cls = "},";
			boolean isArticle = false, isBook = false, isInBook = false;		
			if ( citationType.equals(ArticleCitation.CITATION_TYPE_ARTICLE) ) {
				bib.append("@ARTICLE{");
				sub.append("\n\t journal = {").append(appendMe(((ArticleCitation)this).getJournal())).append(cls);
				sub.append("\n\t volume = {").append(appendMe(((ArticleCitation)this).getVolume())).append(cls);
				sub.append("\n\t number = {").append(appendMe(((ArticleCitation)this).getIssue())).append(cls);
				isArticle = true;
			}
			else if ( citationType.equals(BookCitation.CITATION_TYPE_BOOK) ) {
				bib.append("@BOOK{");
				isBook = true;
			}
			else if ( citationType.equals(InBookCitation.CITATION_TYPE_BOOKSECTION) ) {
				bib.append("@INCOLLECTION{");
				sub.append("\n\t booktitle = {").append(appendMe(((InBookCitation)this).getBookTitle())).append(cls);
				isInBook = true;
			}
			if ( isBook || isInBook) {
				sub.append("\n\t isbn = {").append(appendMe(((BookCitation)this).getISBN())).append(cls);
				sub.append("\n\t publisher = {").append(appendMe(((BookCitation)this).getPublisher())).append(cls);
				sub.append("\n\t address = {").append(appendMe(((BookCitation)this).getCity())).append(cls);
				sub.append("\n\t editor = {").append(appendMe(catPersonsBibtex(((BookCitation)this).getEditors()))).append(cls);			
			}
			if ( isInBook || isArticle ) {
				sub.append("\n\t pages = {").append(appendMe(getPages()).replaceFirst("\\-", "--")).append(cls);
			}		
			bib.append("TreeBASE2Ref").append(getId()).append(",");		
			bib.append("\n\t author = {").append(appendMe(catPersonsBibtex(getAuthors()))).append(cls);		
			bib.append("\n\t title = {").append(appendMe(getTitle())).append(cls);		
			bib.append("\n\t year = {").append(appendMe(getPublishYear())).append(cls);
			bib.append("\n\t keywords = {").append(appendMe(getKeywords())).append(cls);
			bib.append("\n\t doi = {").append(appendMe(getDoi())).append(cls);
			bib.append("\n\t url = {").append(appendMe(getURL())).append(cls);
			bib.append("\n\t pmid = {").append(appendMe(getPMID())).append(cls);
			bib.append(sub);
			bib.append("\n\t abstract = {").append(appendMe(getAbstract())).append("}");		
			bib.append("\n}");
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return bib.toString();
	}
	
	private String appendMe(Object input) {
		return input == null ? "" : input.toString();
	}
	
	private String catPersonsBibtex(List<Person> persons) {
		StringBuilder cat = new StringBuilder();
		for ( int i = 0; i < persons.size(); i++ ) {
			if ( i != 0 ) {
				cat.append(" and ");
			}
			cat.append(persons.get(i).getFullName());
		}	
		return cat.toString();
	}

	@Transient
	protected String getTitleInformation() {
		String title = getTitle().trim();
		if (title.endsWith(".")) {
			return title;
		} else {
			return title + ".";
		}
	}

	@Transient
	protected void getDetailedPublicationInformation(StringBuilder pBuilder, boolean pGenerateHtml) {

	}
	
	@Transient
	public List<Annotation> getAnnotations() {		
		List<Annotation> annotations = super.getAnnotations();
		annotations.add(new Annotation(Constants.DCTermsURI,"dcterms:bibliographicCitation",getAuthorsCitationStyleWithoutHtml()));
		annotations.add(new Annotation(Constants.DCURI,"dc:title",getTitle()));
		annotations.add(new Annotation(Constants.DCURI,"dc:creator",getAuthorsAsString()));
		for ( Person person : getAuthors() ) {
			String personName = person.getFullNameCitationStyle();
			annotations.add(new Annotation(Constants.DCURI,"dc:contributor",personName));
		}
		try {
			if ( null != getPublishYear() ) {
				annotations.add(new Annotation(Constants.PrismURI,"prism:publicationDate",getPublishYear().toString()));
			}
			if ( null != getDoi() ) {
				annotations.add(new Annotation(Constants.PrismURI,"prism:doi",getDoi()));
			}
			if ( null != getPages() ) {
				String[] pages = getPages().split("\\-");
				if ( pages.length == 2 ) {
					annotations.add(new Annotation(Constants.PrismURI,"prism:startingPage",pages[0]));
					annotations.add(new Annotation(Constants.PrismURI,"prism:endingPage",pages[1]));
					annotations.add(new Annotation(Constants.PrismURI,"prism:pageRange",getPages()));			
				}
			}
			if ( null != getKeywords() ) {
				String[] keywords = getKeywords().split(", ");
				for ( int i = 0; i < keywords.length; i++ ) {
					annotations.add(new Annotation(Constants.DCURI,"dc:subject",keywords[i]));
				}		
			}
			if ( this instanceof ArticleCitation ) {
				ArticleCitation ac = (ArticleCitation)this;
				String journal = ac.getJournal();
				if ( null != journal ) {
					annotations.add(new Annotation(Constants.PrismURI,"prism:publicationName",journal));
					annotations.add(new Annotation(Constants.DCURI,"dc:publisher",journal));
				}
				if ( null != ac.getVolume() ) {
					annotations.add(new Annotation(Constants.PrismURI,"prism:volume",ac.getVolume()));
				}
				if ( null != ac.getIssue() ) {
					annotations.add(new Annotation(Constants.PrismURI,"prism:number",ac.getIssue()));
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return annotations;
	}	

}
