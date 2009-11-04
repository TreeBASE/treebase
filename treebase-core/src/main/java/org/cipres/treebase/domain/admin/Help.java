package org.cipres.treebase.domain.admin;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * Help.java
 * 
 * Created on Nov 17, 2008
 * 
 * @author mjd 20081117 
 */
@Entity
@Table(name = "HELP")
@AttributeOverride(name = "id", column = @Column(name = "HELP_ID"))
// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class Help extends AbstractPersistedObject {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Help.class);

	private String tag;	// Convenient short identifier for this help text

	private String helpText; // The help text itself

	public Help() {
		super();
		helpText = "";
	}

	public Help(String tag) {
		this();
		setTag(tag);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Lob
	@Column(name = "HELPTEXT", length = 65536)
	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
}
