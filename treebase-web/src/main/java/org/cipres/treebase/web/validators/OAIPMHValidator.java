package org.cipres.treebase.web.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.cipres.treebase.web.model.OAIPMHCommand;

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
		
		
		if(verb!=null && verb.equals("GetRecord"))
			ValidationUtils.rejectIfEmptyOrWhitespace(pError, "identifier", "empyt id");
		
		// check format prefix
		if(verb!=null){
		    if(verb.equals("GetRecord")||verb.equals("ListIdentifiers")||verb.equals("ListRecords")){
			   
		    	ValidationUtils.rejectIfEmptyOrWhitespace(pError, "metadataPrefix", "cannotDisseminateFormat");
			   
		    	if(fPrefix!=null)
				   if(!(fPrefix.equals("oai_dc")||fPrefix.equals("dryad")))
				    pError.rejectValue("metadataPrefix","cannotDisseminateFormat");									
		   }
	    }	
	}	
	   
}
