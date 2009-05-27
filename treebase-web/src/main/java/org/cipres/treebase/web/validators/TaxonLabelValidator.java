package org.cipres.treebase.web.validators;

import java.util.Iterator;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;

public class TaxonLabelValidator implements Validator {

	public boolean supports(Class pClass) {
		return TaxonLabel.class.equals(pClass);
	}

	public void validate(Object pValidatable, Errors pError) {
		// ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
		TaxonLabel myTaxonLabel = (TaxonLabel) pValidatable;

		// Checking Taxon Label is not Empty
		// IMPORTANT: taxonLabel in the following line and everywhere below refers to
		// getTaxonLabel() method in myTaxonLabel class
		ValidationUtils.rejectIfEmptyOrWhitespace(
			pError,
			"taxonLabel",
			"Taxon Label cannot be empty.",
			null,
			"Taxon Label cannot be empty.");

		// Check string length
		String valueLabel = myTaxonLabel.getTaxonLabel().trim();
		if (valueLabel.length() > TBPersistable.COLUMN_LENGTH_STRING) {
			pError.rejectValue("taxonLabel", null, "Taxon Label cannot exceed "
				+ TBPersistable.COLUMN_LENGTH_STRING + " characters.");
		}
		
		// Check for integer values, float are acceptable
		if ( valueLabel.matches("^[0-9]+$") ) {
			pError.rejectValue("taxonLabel", null, "Taxon Label cannot consist of integers alone.");
		}

		// Checking for the duplication of the Taxon Labels (String) in the Set
		Study study = myTaxonLabel.getStudy();
		List<TaxonLabel> taxonLabelSet = study.getSubmission().getSubmittedTaxonLabelsReadOnly();
		String testLbl = null;
		long valueId = myTaxonLabel.getId();
		String valueLabelUC = valueLabel.toUpperCase();		
		Iterator<TaxonLabel> itTxnLbl = taxonLabelSet.iterator();
		while (itTxnLbl.hasNext()) {
			TaxonLabel testTxnLbl = itTxnLbl.next();
			testLbl = testTxnLbl.getTaxonLabel().trim().toUpperCase();
			if (testLbl.equals(valueLabelUC) && testTxnLbl.getId() != valueId ) {
				pError.rejectValue("taxonLabel", null, "This Label already exists.");
				break;
			}
		}
	}

}
