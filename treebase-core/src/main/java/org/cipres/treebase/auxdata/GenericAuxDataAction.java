
package org.cipres.treebase.auxdata;

import java.util.Set;

public class GenericAuxDataAction extends GenericAction {
	Set<String> interestingLegacyIDs = null;

	public boolean isInterestingAuxData(AuxData aux) {
		return  interestingLegacyIDs == null || interestingLegacyIDs.contains(aux.getStudyID());
	}
	
	public Set<String> getInterestingLegacyIDs() {
		return interestingLegacyIDs;
	}

	public void setInterestingLegacyIDs(Set<String> interestingLegacyIDs) {
		this.interestingLegacyIDs = interestingLegacyIDs;
	}
	
	public void clearInterestingLegacyIDs() {
		this.interestingLegacyIDs = null;
	}
}
