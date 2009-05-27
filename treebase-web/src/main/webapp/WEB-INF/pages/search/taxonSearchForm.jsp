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
		$('searchTerm').disabled = true;
	}
	function toggleTextArea(button) {
		var noneChecked = true;
		var siblings = $(button.parentNode).getElementsByClassName('selectionNeeded');
		for ( var i = 0; i < siblings.length; i++ ) {
			if ( siblings[i].checked ) {
				noneChecked = false;
			}
		}
		$('searchTerm').disabled = noneChecked;
	}
	function toggleSubmit(textarea) {
		if ( textarea.value != "" ) {
			$('submitSearch').disabled = false;
		}		
		else {
			$('submitSearch').disabled = true;
		}
	}
</script>

<div>
  <form id="searchByTaxonLabel" method="post">
  <fieldset>
	  Search: <input type="hidden" name="formName" value="searchByTaxonLabel"/>
	  <input type="hidden" name="searchType" value="${searchType }"/>
	  <fieldset id="Identifiers">
	  	<legend>
	  		<input onclick="toggleFieldSet('TextSearch','Identifiers')" type="radio" name="searchOn" value="Identifiers"/>Identifiers
	  		<a href="#" class="openHelp" onclick="openHelp('taxonSearchIdentifiers')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>
	  	</legend>
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" onchange="toggleTextArea(this)" value="TreeBASE" disabled="disabled"/>TreeBASE taxon ID
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" onchange="toggleTextArea(this)" value="NCBI" disabled="disabled"/> NCBI taxon ID
	  	<input type="radio" name="objectIdentifier" class="selectionNeeded" onchange="toggleTextArea(this)" value="uBio" disabled="disabled"/> uBio nameBankID
	  </fieldset>
	  <fieldset id="TextSearch">
	  	<legend>
	  		<input onclick="toggleFieldSet('Identifiers','TextSearch')" type="radio" name="searchOn" value="TextSearch"/>Text search
	  		<a href="#" class="openHelp" onclick="openHelp('taxonSearchTextSearch')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>	  		
	  	</legend>
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" onchange="toggleTextArea(this)" value="taxonLabel" disabled="disabled"/> Taxon label
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" onchange="toggleTextArea(this)" value="taxonVariant" disabled="disabled"/> Taxon variant
	  	<input type="checkbox" name="stringProperty" class="selectionNeeded" onchange="toggleTextArea(this)" value="taxon" disabled="disabled"/> Taxon
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
	  	<textarea name="searchTerm" id="searchTerm" style="width:100%" onchange="toggleSubmit(this)" disabled="disabled">${searchTerm}</textarea>	  	
	  </fieldset>
	  <input type=submit name="Action" id="submitSearch" value="Search" disabled="disabled"/>
  </fieldset>
  </form>
</div>