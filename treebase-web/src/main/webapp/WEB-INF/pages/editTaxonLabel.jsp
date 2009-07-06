<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="taxonlabel.change.title"/></title>
<content tag="heading"><fmt:message key="taxonlabel.change.title"/></content>
<body id="submissions"/>

<spring:bind path="txnlabel.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<c:set var="ncbiTaxonomyURL" value="http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" />
<c:set var="uBioTaxonomyURL" value="http://www.ubio.org/browser/details.php?namebankID=" />
<form method="post" name="edtTxnLbl">
	<fieldset>
		<legend>Taxon Label
		<a href="#" class="openHelp" onclick="openHelp('editTaxonLabel')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
		</legend>
		<input type="hidden" name="taxonlabelid" value="${status.value}"/>			
		<p>
			Please edit your taxon labels so that they are in compliance with TreeBASE guidelines: each name
			should be readily recognizable by other biologists, which means writing the scientific binomials 
			in full (i.e. no abbreviations); any numbers or codes should be added as suffixes to the scientific
			names and separated from the names with a space.
		</p>
		<spring:bind path="txnlabel.taxonLabel">
			<input class="textCell" style="width:200px" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</fieldset>
	<fieldset>
		<legend>External Taxonomy
		<a href="#" class="openHelp" onclick="openHelp('editTaxonomyRef')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
		</legend>
		<p>
			Please select the correct link to the NCBI taxonomy from the suggestions below (if any). If no 
			correct link is suggested, search 
			<spring:bind path="txnlabel.taxonLabel">
				<a href="http://www.ubio.org/browser/search.php?search_all=<c:out value="${status.value}"/>" title="uBio taxonomy" target="_new">
					<img src="<fmt:message key="icons.weblink"/>" class="iconButton"/> uBio
				</a>
			</spring:bind>
			for a match and enter its <acronym 
				title="The NamebankID is listed at the top of every uBio 'Namebank Record Detail' page. It is also the numerical part of LSIDs issued by uBio." style="border-bottom: 1px dotted black">NamebankID</acronym> in the textarea. If your taxon label does not exist
			in uBio (this should be rare!) select <em>no association</em>.			
		</p>
		<c:set var="counter" value="0"></c:set>
		<c:forEach var="variant" items="${variants}">
			<c:set var="ncbiTaxoURL" value="${ncbiTaxonomyURL}${variant.taxon.ncbiTaxId}"/>
			<c:set var="uBioTaxoURL" value="${uBioTaxonomyURL}${variant.namebankID}"/>	
			<div>
				<input type="radio" name="taxonvariantid" value="${variant.id}" 
					<c:if test="${variant.id==currentVariantId}">checked="checked"</c:if> />
				<c:if test="${variant.name}">
					<c:out value="${variant.name}" />, 
				</c:if>
				<c:if test="${!variant.name}">
					<c:out value="${variant.taxon.name}" />,
				</c:if>
				(Internal ID: <c:out value="${variant.id}" />,
				uBio: <a href="${uBioTaxoURL}" target="_blank"><c:out value="${variant.namebankID}" /></a>,
				NCBI: <a href="${ncbiTaxoURL}" target="_blank"><c:out value="${variant.taxon.ncbiTaxId}" /></a>)
			</div>
			<c:set var="counter" value="${counter+1}"/>
		</c:forEach>
		<div>
			<input type="radio" name="taxonvariantid" id="textBoxRadio" value="-1"/>
			I did a uBio lookup for 
			<spring:bind path="txnlabel.taxonLabel">
				<a href="http://www.ubio.org/browser/search.php?search_all=<c:out value="${status.value}"/>" title="uBio taxonomy" target="_new">
					<img src="<fmt:message key="icons.weblink"/>" class="iconButton"/><c:out value="${status.value}"/></a>
			</spring:bind>		
			and here is the namebank id:
			<script type="text/javascript">
				function selectRadio(textBoxRadio) {
					$(textBoxRadio).checked = true;
				}
			</script>
			<input style="width:100px" class="textCell" type="text" name="manualid" onchange="selectRadio('textBoxRadio')" value=""/> 
		</div>
		<div>
			<input type="radio" name="taxonvariantid" value="-1" 
				<c:if test="${counter==0}">checked="checked"</c:if> /> <em>no association</em>
		</div>		
	</fieldset>    	
	<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
	<input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" />	    
</form>


