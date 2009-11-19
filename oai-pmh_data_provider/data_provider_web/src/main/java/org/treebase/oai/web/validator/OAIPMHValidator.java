package org.treebase.oai.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.treebase.oai.web.command.OAIPMHCommand;

public class OAIPMHValidator implements Validator {
	
	public boolean supports(Class pClass) {
		return OAIPMHCommand.class.equals(pClass);
	}

	public void validate(Object pValidatable, Errors pError) {
		// ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
		OAIPMHCommand command = (OAIPMHCommand) pValidatable;				
		
		String verb = command.getVerb();
		String fPrefix = command.getMetadataPrefix();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(pError, "verb", "empyt verb");
		if(verb=="GetRecord")
			ValidationUtils.rejectIfEmptyOrWhitespace(pError, "identifier", "empyt id");
		
		// check format prefix
		
		if(verb=="GetRecord"||verb=="ListIdentifiers"||verb=="ListRecords"){
			ValidationUtils.rejectIfEmptyOrWhitespace(pError, "metadataPrefix", "cannotDisseminateFormat");
			if(fPrefix!="oai_dc"&fPrefix!="dryad")
				pError.rejectValue("metadataPrefix","cannotDisseminateFormat");									
		}
	}		
	   
}
