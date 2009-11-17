package org.treebase.oai.web.command;
/**
 * 
 * @author youjun
 * class transfer page/query input data to controller
 */
public class OAIPMHCommand {
	
	private String from;
	private String until;
	private String resumptionToken;
	private String identifier;
	private String metadataPrefix;
	private String set;	
	private String verb;
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getModifiedFrom() {
		if(from!=null&&from.trim()!="")return from;
	    return "1995-11-04T00:00:00Z";
	}
	
	public String getFrom() {
		 return from;   
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	public String getModifiedUntil() {
		if(until != null && until.trim()!="") return until;
	    return "";
	}
	
	public String getUntil() {
		 return until;   
	}
	
	public void setUntil(String until) {
		this.until = until;
	}
	public String getResumptionToken() {
		return resumptionToken;
	}
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}
	public String getSet() {
		return set;
	}
	public void setSet(String set) {
		this.set = set;
	}
	
	

}
