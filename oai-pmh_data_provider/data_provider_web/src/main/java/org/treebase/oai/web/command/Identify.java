package org.treebase.oai.web.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class Identify {

	private String baseRUL;
	private String repositoryName;
	private String protocolVersion;
	private String adminEmail;
	private String earliestDatestamp;
	private String deletedRecord;
	private String granularity;
	private String granularityPattern;
	
	public String getBaseRUL() {
		return baseRUL;
	}
	public void setBaseRUL(String baseRUL) {
		this.baseRUL = baseRUL;
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
	
	public String getResponseDate(Date date){				         
	     SimpleDateFormat format = new SimpleDateFormat(granularityPattern);
	     Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
	     format.setCalendar(cal);
	     return format.format(date);
	         
	} 
	
}
