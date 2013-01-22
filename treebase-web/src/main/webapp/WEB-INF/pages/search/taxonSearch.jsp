<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.taxon"/></title>
<body id="s-taxon"/>

<div id="wrap">
		<jsp:include page="/common/search-nav.jsp"/>

<div id="s-clear"></div>

<script type="text/javascript">
  //<![CDATA[
    // These are the mappings from inferred search term types to search predicates for this scope
    var predicates = {
      'integer' : [ 'tb.identifier.ncbi', 'tb.identifier.ubio', 'tb.identifier.taxon', 'tb.identifier.taxon.tb1' ],
      'id' : [ 'tb.identifier.taxon', 'tb.identifier.taxon.tb1' ],
      'word' : [ 'tb.title.taxon', 'tb.title.taxonLabel', 'tb.title.taxonVariant' ],
      'doi' : [ 'prism.doi' ] // this doesn't work yet, we have no search on doi
    };
    
 	// purlBase is assigned in /common/search-nav.jsp
    var phyloWSURI = purlBase + 'taxon/find?query=';
  //]]>
</script>

<c:set var="searchType" value="taxon" scope="request"/>

<%--jsp:include page="taxonSearchForm.jsp"/ --%>

<fieldset>
	<legend>
	  	Taxon search
		<a href="#" class="openHelp" onclick="openHelp('taxonSearchTextSearch')">
			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
		</a>	  		
	</legend>
<jsp:include page="simpleSearchForm.jsp"/>
</fieldset>

<div id="s-clear"></div>

<jsp:include page="searchMessages.jsp"/>

<c:if test="${not empty taxonSearchResults && ! taxonSearchResults.isTrivial}">
  <c:set var="items" value="items"/>
  <c:if test="${taxonSearchResults.count == 1 }"><c:set var="items" value="item"/></c:if>
  <h3>Your search has yielded ${taxonSearchResults.count } ${items }:</h3>
</c:if>

<jsp:include page="searchResultsList.jsp"/> 

</div>