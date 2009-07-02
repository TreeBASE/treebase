package org.cipres.treebase.web.model;

import java.net.URI;

import javax.xml.namespace.QName;

/*
 * XXX this is work in progress, not functional right now
 */
public class TreebaseTriple {
	private URI mSubject;
	private QName mPredicate;
	private URI mObject;
	
	public TreebaseTriple (URI subject,QName predicate,URI object) {
		mSubject = subject;
		mPredicate = predicate;
		mObject = object;		
	}
	
	public URI getSubject() {
		return mSubject;
	}
	
	public QName getPredicate() {
		return mPredicate;
	}
	
	public URI getObject() {
		return mObject;
	}
}
