package org.cipres.treebase.domain.nexus.nexml;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.nexus.AbstractNexusConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.Annotatable;
import org.nexml.model.Annotation;
import org.nexml.model.Document;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;

public class NexmlObjectConverter extends AbstractNexusConverter {
	private Logger logger = Logger.getLogger(NexmlObjectConverter.class);
	protected static URI mDCURI;
	protected static URI mPrismURI;
	protected static URI mTBTermsURI;
	protected static URI mDwCURI;
	protected URI mBaseURI;	
	private static String mDwCString = "http://rs.tdwg.org/dwc/terms/";
	private static String mTBTermsString = "http://treebase.org/terms#";
	private static String mDCURIString = "http://purl.org/dc/terms/";
	private static String mPrismURIString = "http://prismstandard.org/namespaces/1.2/basic/";
	private static String mBaseURIString = "http://purl.org/PHYLO/TREEBASE/PHYLOWS/";
	private static String mDCIdentifier = "dcterms:identifier";	
	public static String TreeBASE2Prefix = "TreeBASE2";
	private Document mDocument;
		
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlObjectConverter(Study study, TaxonLabelHome taxonLabelHome, Document document, String baseURI) {
		try {
			mDCURI = new URI(mDCURIString);
			mPrismURI = new URI(mPrismURIString);
			mTBTermsURI = new URI(mTBTermsString);
			mDwCURI = new URI(mDwCString);
			if ( null != baseURI ) {
				mBaseURI = new URI(baseURI);
			}
			else {
				mBaseURI = new URI(mBaseURIString);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if ( null != baseURI ) {
			document.setBaseURI(mBaseURI);
		}
		setTaxonLabelHome(taxonLabelHome);
		setStudy(study);
		setDocument(document);
	}
	
	public NexmlObjectConverter(Study study, TaxonLabelHome taxonLabelHome, Document document) {
		this(study,taxonLabelHome,document,null);
	}

	
	/**
	 * 
	 * @param tbPersistable
	 * @return
	 */
	private String makeNamespacedID (TBPersistable tbPersistable,Class<?> persistableClass) {
		TreebaseIDString tbIDString = new TreebaseIDString(persistableClass,tbPersistable.getId());
		return tbIDString.getNamespacedGUID().toString();
	}
	
	/**
	 * 
	 * @param annotatable
	 * @param tbPersistable
	 */
	protected void attachTreeBaseID(Annotatable annotatable,TBPersistable tbPersistable,Class<?> persistableClass) {
		if ( null != tbPersistable.getId() ) {
			//attachAnnotation(mDCIdentifier,makeNamespacedID(tbPersistable,persistableClass),mDCURI,annotatable);			
			String uriString = getDocument().getBaseURI().toString() + tbPersistable.getPhyloWSPath().toString();
			annotatable.addAnnotationValue("tb:resource",mTBTermsURI, URI.create(uriString));
			annotatable.addAnnotationValue("dcterms:relation",mDCURI, URI.create(uriString));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param namespace
	 * @param annotatable
	 */
	protected void attachAnnotation(String key,String value,URI namespace,Annotatable annotatable) {
		Annotation annotation = annotatable.addAnnotationValue(key, namespace, value);
	}
	
	/**
	 * 
	 * @param annotatable
	 * @param persistableClass
	 * @return
	 */
	protected Long readTreeBaseID(Annotatable annotatable) {
		
		// this will return the value object associated with a
		// dc:identifier predicate in a nexml meta annotation,
		// e.g. <meta property="dc:identifier" content="TB2:Tr231"/>
		// this will return something that stringifies to TB2:Tr231
		Set<Object> dublinCoreRelationObjects = annotatable.getRelValues("dcterms:relation");
		Iterator<Object> objectIterator = dublinCoreRelationObjects.iterator();
		while ( objectIterator.hasNext() ) {
			URI relationURI = (URI)objectIterator.next();
			String urlFragment = getDocument().getBaseURI().toString() + "taxon/TB2:";
			if ( relationURI.toString().startsWith(urlFragment) ) {
				String rawTreebaseIDString = relationURI.toString().substring(urlFragment.length());
				try {
					TreebaseIDString treebaseIDString = new TreebaseIDString(rawTreebaseIDString);
					return treebaseIDString.getId();
				} catch ( MalformedTreebaseIDString e ) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	protected OTUs getOTUsById(Long taxonLabelSetId) {
		logger.debug("Going to look for taxa block "+taxonLabelSetId);
		for ( OTUs otus : getDocument().getOTUsList() ) {			
			Long annotatedID = readTreeBaseID(otus);
			logger.debug("Seen taxa block "+annotatedID);
			if ( taxonLabelSetId.equals(annotatedID) ) {
				return otus;
			}
		}	
		return null;
	}
	
	protected OTU getOTUById(OTUs otus,Long taxonLabelId) {
		for ( OTU otu : otus.getAllOTUs() ) {
			Long annotatedID = readTreeBaseID(otu);
			if ( taxonLabelId.equals(annotatedID) ) {
				return otu;
			}
		}
		return null;
	}

	public Document getDocument() {
		return mDocument;
	}

	public void setDocument(Document document) {
		mDocument = document;
	}
	
	public static String forXML(String aText){
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '&') {
				result.append("&amp;");
			} else {
				//the char is not a special one
				//add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public URI getBaseURI() {
		return mBaseURI;
	}

	public void setBaseURI(URI baseURI) {
		mBaseURI = baseURI;
	}	
}
