package org.treebase.oai.web.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
/**
 * 
 * @author youjun
 * class hold identify data about the service
 */

public class Identify {


	private String baseURL;
	private String repositoryName;
	private String protocolVersion;
	private String adminEmail;
	private String earliestDatestamp;
	private String deletedRecord;
	private String granularity;
	private String granularityPattern;
	private String repositoryIdentifier;
	private String sampleIdentifier;
	private String dryadPerfix;
	private String identifierDelimiter;
	private String dublinPerfix;
	
	
	public String getGranularityPattern() {
		return granularityPattern;
	}
	public void setGranularityPattern(String granularityPattern) {
		this.granularityPattern = granularityPattern;
	}

	
	public String getIdentifierDelimiter() {
		return identifierDelimiter;
	}
	public void setIdentifierDelimiter(String identifierDelimiter) {
		this.identifierDelimiter = identifierDelimiter;
	}
	public String getDublinPerfix() {
		return dublinPerfix;
	}
	public void setDublinPerfix(String dublinPerfix) {
		this.dublinPerfix = dublinPerfix;
	}
	public String getDryadPerfix() {
		return dryadPerfix;
	}
	public void setDryadPerfix(String dryadPerfix) {
		this.dryadPerfix = dryadPerfix;
	}	
	
	public String getRepositoryIdentifier() {
		return repositoryIdentifier;
	}
	public void setRepositoryIdentifier(String repositoryIdentifier) {
		this.repositoryIdentifier = repositoryIdentifier;
	}
	public String getSampleIdentifier() {
		return sampleIdentifier;
	}
	public void setSampleIdentifier(String sampleIdentifier) {
		this.sampleIdentifier = sampleIdentifier;
	}

	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseRUL) {
		this.baseURL = baseRUL;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getEarliestDatestamp() {
		return earliestDatestamp;
	}
	public void setEarliestDatestamp(String earliestDatestamp) {
		this.earliestDatestamp = earliestDatestamp;
	}
	public String getDeletedRecord() {
		return deletedRecord;
	}
	public void setDeletedRecord(String deletedRecord) {
		this.deletedRecord = deletedRecord;
	}
	public String getGranularity() {
		return granularity;
	}
	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}
	
	// return a current date object in UTC 
	public String getResponseDate(){				         
	     SimpleDateFormat format = new SimpleDateFormat(granularityPattern);
	     Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
	     format.setCalendar(cal);
	     return format.format(new Date());
	         
	} 
	
}
