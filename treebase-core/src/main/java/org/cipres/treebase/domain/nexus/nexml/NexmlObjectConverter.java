package org.cipres.treebase.domain.nexus.nexml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.nexus.AbstractNexusConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.Annotatable;
import org.nexml.model.Document;

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
	public String makeNamespacedID (TBPersistable tbPersistable) {
		StringBuilder sb = new StringBuilder();
		sb
			.append(TreeBASE2Prefix)
			.append(':')
			.append(TreebaseIDString.getPrefixForClass(tbPersistable.getClass()))
			.append(tbPersistable.getId());
		return sb.toString();
	}
	
	/**
	 * 
	 * @param annotatable
	 * @param tbPersistable
	 */
	protected void attachTreeBaseID(Annotatable annotatable,TBPersistable tbPersistable) {
		attachAnnotation(mDCIdentifier,makeNamespacedID(tbPersistable),mDCURI,annotatable);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param namespace
	 * @param annotatable
	 */
	protected void attachAnnotation(String key,String value,URI namespace,Annotatable annotatable) {
		annotatable.addAnnotationValue(key, namespace, value);
	}
	
	/**
	 * 
	 * @param annotatable
	 * @param persistableClass
	 * @return
	 */
	protected Long readTreeBaseID(Annotatable annotatable, Class<? extends TBPersistable> persistableClass) {
		Set<Object> values = annotatable.getAnnotationValues(mDCIdentifier);
		Iterator<Object> objectIterator = values.iterator();
		while ( objectIterator.hasNext() ) {
			String namespacedId = objectIterator.next().toString();
			StringBuffer prefix = new StringBuffer();
			prefix
				.append(TreeBASE2Prefix)
				.append(':')
				.append(TreebaseIDString.getPrefixForClass(persistableClass));
			if ( namespacedId.startsWith(prefix.toString()) ) {
				return Long.parseLong(namespacedId.substring(prefix.length()));
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
}
