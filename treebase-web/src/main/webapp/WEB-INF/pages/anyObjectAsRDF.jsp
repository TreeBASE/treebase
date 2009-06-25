<%@ include file="/common/taglibs.jsp"%>
<% response.setContentType("application/rdf+xml"); %>
<rdf:RDF
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:dc="http://purl.org/dc/elements/1.1/">
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}">
		<dc:identifier>${namespacedGUID}</dc:identifier>
		<c:if test="${hasWebPage}"><dc:relation rdf:resource="${baseURL}/${namespacedGUID}.html"/></c:if>
		<c:if test="${hasNeXML}"><dc:relation rdf:resource="${baseURL}/${namespacedGUID}.xml"/></c:if>
		<c:if test="${hasNexus}"><dc:relation rdf:resource="${baseURL}/${namespacedGUID}.nex"/></c:if>
		<c:if test="${hasRdf}"><dc:relation rdf:resource="${baseURL}/${namespacedGUID}.rdf"/></c:if>
	</rdf:Description><c:if test="${hasWebPage}">	
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}.html">
		<dcterms:format>text/html</dcterms:format>
		<dc:language>EN-US</dc:language>
		<dc:description>A Web page</dc:description>
	</rdf:Description></c:if><c:if test="${hasNeXML}">
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}.xml">
		<dcterms:format>application/xml</dcterms:format>
		<dc:description>A NeXML serialization</dc:description>
	</rdf:Description></c:if><c:if test="${hasNexus}">
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}.nex">
		<dcterms:format>text/plain</dcterms:format>
		<dc:description>A Nexus serialization</dc:description>
	</rdf:Description></c:if><c:if test="${hasRdf}">
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}.rdf">
		<dcterms:format>application/rdf+xml</dcterms:format>
		<dc:description>RDF/XML metadata about the resource</dc:description> 
	</rdf:Description></c:if><%--c:if test="${hasJSON}">
	<rdf:Description rdf:about="${baseURL}/${namespacedGUID}.js">
		<dcterms:format>text/javascript</dcterms:format>
		<dc:description>A JSON mapping of a NeXML serialization</dc:description> 
	</rdf:Description></c:if--%>	
</rdf:RDF>