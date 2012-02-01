package org.cipres.treebase.domain.taxon;

import java.util.Iterator;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import org.cipres.treebase.domain.study.Study;

public class TaxonLabelValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class pClass) {
		return TaxonLabel.class.equals(pClass);
	}

	public void validate(Object pValidatable, Errors pError) {
        //ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        TaxonLabel myTaxonLabel = (TaxonLabel) pValidatable;
        
//      Checking Taxon Label is not Empty 
// IMPORTANT: taxonLabel in the following line and everywhere below refers to getTaxonLabel() method in myTaxonLabel class
        ValidationUtils.rejectIfEmptyOrWhitespace(pError, "taxonLabel", "Taxon Label cannot be empty.", null, "Taxon Label cannot be empty.");
        
		String valueLabel = myTaxonLabel.getTaxonLabel().trim();
/*
		if(valueLabel.length() == 0){
			pError.rejectValue("taxonLabel", null, "Taxon Label cannot be empty.");
		}
*/
		long valueId = myTaxonLabel.getId();
		String valueLabelUC = valueLabel.toUpperCase();
//		 Check for integer values, float are acceptable			
		try{
//			BigInteger bigint = new  BigInteger(valueLabel);
//			long tmp = Long.parseLong(txnLabel.getTaxonLabel());
			pError.rejectValue("taxonLabel", null, "Taxon Label cannot consist of integers alone.");
						
		}catch ( NumberFormatException nfe){
//It is good if this Exception is thrown, becasue the Taxon Label cannot be an integer or a long.		
		}

//		Checking for the duplication of the Taxon Labels (String) in the Set
		
		Study study = myTaxonLabel.getStudy(); //ControllerUtil.findStudy(request, mStudyService);
		List<TaxonLabel> taxonLabelSet = study.getSubmission().getSubmittedTaxonLabelsReadOnly();
					
		Iterator<TaxonLabel> itTxnLbl = taxonLabelSet.iterator();
		
		String testLbl = null;
		long testId = 0L;
		boolean testCheck = false;
		
		while(itTxnLbl.hasNext()){
			
			TaxonLabel testTxnLbl = itTxnLbl.next();
			testLbl = testTxnLbl.getTaxonLabel().trim().toUpperCase();
			testId = testTxnLbl.getId();
			
			if(testLbl.equals(valueLabelUC)){
				
				if(testId == valueId){
//DO NOTHING, it means user is trying to update the existing label with identical value.						
				}
				else{
					testCheck = true;// It means Taxon Label Already Exists
					break;
				}
			}
					
		}
		
		if(testCheck){
//			getTaxonLabelService().update(taxonLabel);
			pError.rejectValue("taxonLabel", null, "This Label already exists.");
			
		}

	}

}
