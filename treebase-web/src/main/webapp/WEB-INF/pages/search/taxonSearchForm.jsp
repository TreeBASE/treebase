<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function toggleFieldSet(disableFieldSet,enableFieldSet) {
		var fieldSet = $(disableFieldSet);
		var children = fieldSet.childElements();
		for ( var i = 0; i < children.length; i++ ) {
			if ( children[i].tagName == 'INPUT' ) {
				children[i].checked = false;
				children[i].disabled = true;
			}
		}	
		var fieldSet = $(enableFieldSet);
		var children = fieldSet.childElements();
		for ( var i = 0; i < children.length; i++ ) {
			if ( children[i].tagName == 'INPUT' ) {
				children[i].disabled = false;
				children[i].checked = false;
			}
		}	
		if ( disableFieldSet == 'TextSearch' ) {
			$('searchTermLegend').textContent = 'Enter identifiers (one per line)';
		}	
		else {
			$('searchTermLegend').textContent = 'Enter names (one per line)';
		}
	}
	function validateForm() {
		if ( $('searchTerm').value != "" ) {
			if ( $('IdentifiersRadio').checked ) {
				if ( $('TreeBASERadio').checked || $('NCBIRadio').checked || $('uBioRadio').checked ) {
					return $('searchByTaxonLabel').submit();
				}	
				alert("Please select either TreeBASE, NCBI or uBio");		
			}
			else if ( $('TextSearchRadio').checked ) {
				if ( $('taxonLabelRadio').checked || $('taxonVariantRadio').checked || $('taxonRadio').checked ) {
					return $('searchByTaxonLabel').submit();
				}		
				alert("Please select at least one of Taxon label, Taxon variant or Taxon");		
			}
			else {
				alert("Please select what to search on (identifiers or text)");
			}
		}
		else {
			alert("Please enter search terms in the text area");
		}
		return false;
	}
</script>

<div>
  <form id="searchByTaxonLabel" method="post">
  <fieldset>
	  Search: <input type="hidden" name="formName" value="searchByTaxonLabel"/>
	  <input type="hidden" name="searchType" value="${searchType }"/>
	  <fieldset id="Identifiers">
	  	<legend>
	  		<input onclick="toggleFieldSet('TextSearch','Identifiers')" type="radio" id="IdentifiersRadio" name="searchOn" value="Identifiers"/>Identifiers
	  		<a href="#" class="openHelp" onclick="openHelp('taxonSearchIdentifiers')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>
	  	</legend>
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" id="TreeBASERadio" value="TreeBASE" disabled="disabled"/>TreeBASE taxon ID
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" id="NCBIRadio" value="NCBI" disabled="disabled"/> NCBI taxon ID
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" id="uBioRadio" value="uBio" disabled="disabled"/> uBio nameBankID
	  </fieldset>
	  <fieldset id="TextSearch">
	  	<legend>
	  		<input onclick="toggleFieldSet('Identifiers','TextSearch')" type="radio" name="searchOn" id="TextSearchRadio" value="TextSearch"/>Text search
	  		<a href="#" class="openHelp" onclick="openHelp('taxonSearchTextSearch')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>	  		
	  	</legend>
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" id="taxonLabelRadio" value="taxonLabel" disabled="disabled"/> Taxon label
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" id="taxonVariantRadio" value="taxonVariant" disabled="disabled"/> Taxon variant
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" id="taxonRadio" value="taxon" disabled="disabled"/> Taxon
	  	<hr style="border: 1px solid #cccccc" />
	  	<input type="checkbox" name="stringModifier" value="caseSensitive" disabled="disabled"/> Case sensitive
	  	<input type="checkbox" name="stringModifier" value="exactMatch" disabled="disabled"/> Exact match
	  </fieldset>
	  <fieldset>
	  	<legend>
	  		<span id="searchTermLegend">Search terms</span>
	  		<a href="#" class="openHelp" onclick="openHelp('taxonSearchTerms')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>	  	
	  	</legend>
	  	<textarea name="searchTerm" id="searchTerm" style="width:100%">${searchTerm}</textarea>	  	
	  </fieldset>
	  <input type="hidden" name="Action" value="Search"/>
	  <input type="button" name="Action" id="submitSearch" onclick="validateForm()" value="Search"/>
	  
	  <jsp:include page="querySearchBox.jsp"/>
  </fieldset>
  </form>
</div>