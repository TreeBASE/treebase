package org.cipres.treebase.domain;

import java.net.URI;

public class Annotation {
	private URI mURI;
	private String mProperty;
	private Object mValue;
	
	/**
	 * 
	 * @param uri
	 * @param property
	 * @param value
	 */
	public Annotation (URI uri, String property, Object value) {
		mURI = uri;
		mProperty = property;
		mValue = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public URI getURI() { // XXX change me to getUri
		return mURI;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProperty () {
		return mProperty;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getValue() {
		return mValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPrefix() {
        String[] curie = mProperty.split(":");
        return curie[0];
	}

}
