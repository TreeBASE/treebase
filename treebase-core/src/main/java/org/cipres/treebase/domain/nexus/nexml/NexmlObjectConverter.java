package org.cipres.treebase.domain.nexus.nexml;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.Set;

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
	protected static URI mDCURI;
	protected static URI mPrismURI;
	private static String mDCURIString = "http://purl.org/dc/elements/1.1/";
	private static String mPrismURIString = "http://prismstandard.org/namespaces/1.2/basic/";
	private static String mDCIdentifier = "dc:identifier";	
	public static String TreeBASE2Prefix = "TreeBASE2";
	private Document mDocument;
		
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlObjectConverter(Study study, TaxonLabelHome taxonLabelHome, Document document) {
		try {
			mDCURI = new URI(mDCURIString);
			mPrismURI = new URI(mPrismURIString);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}			
		setTaxonLabelHome(taxonLabelHome);
		setStudy(study);
		setDocument(document);
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
			attachAnnotation(mDCIdentifier,makeNamespacedID(tbPersistable,persistableClass),mDCURI,annotatable);			
			String uriString = "http://localhost:8080/treebase-web/PhyloWS/" + makeNamespacedID(tbPersistable,persistableClass);
			try {
				annotatable.addAnnotationValue("dc:relation",mDCURI, new URI(uriString));
			} catch ( Exception e ) {
				
			}
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
		Annotation annotation = annotatable.addAnnotationValue(key, namespace, value); // FIXME! Attaches meta element as last child
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
		Set<Object> dublinCoreIdentifierObjects = annotatable.getAnnotationValues(mDCIdentifier);
		Iterator<Object> objectIterator = dublinCoreIdentifierObjects.iterator();
		while ( objectIterator.hasNext() ) {
			TreebaseIDString treebaseIDString = null;
			NamespacedGUID namespacedGUID = null;
			try {
				namespacedGUID = new NamespacedGUID(objectIterator.next().toString());
				treebaseIDString = namespacedGUID.getTreebaseIDString();
				return treebaseIDString.getId();
			} catch ( MalformedTreebaseIDString e ) {
				// XXX do nothing, it's OK, it means we're 
				// parsing an id from a different naming
				// authority, e.g. uBio or NCBI
			}
		}
		return null;
	}
	
	protected OTUs getOTUsById(Long taxonLabelSetId) {
		for ( OTUs otus : getDocument().getOTUsList() ) {
			Long annotatedID = readTreeBaseID(otus);
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
}
