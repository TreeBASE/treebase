<?xml version="1.0" encoding="UTF-8"?>
<%@ include file="/common/taglibs.jsp"%>
<% response.setContentType("application/rss+xml; charset=UTF-8"); %>
<rdf:RDF
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns="http://purl.org/rss/1.0/">
  <channel rdf:about="${baseURL}${phyloWSPath}">
    <title><c:out value="${theObject.label}"/></title>
    <link>${baseURL}</link>
    <description>Serializations for ${phyloWSPath}</description>
    <c:if test="${theObject.context != null}">
    	<rdfs:isDefinedBy rdf:resource="${baseURL}<c:out value="${theObject.context.phyloWSPath}"/>"/>
    </c:if>
	<c:forEach var="anno" items="${theObject.annotations}" varStatus="annoStatus">
		<<c:out value="${anno.property}"/> 
			xmlns:<c:out value="${anno.prefix}"/>="<c:out value="${anno.URI}"/>"><c:out value="${anno.value}" escapeXml="true"/></<c:out value="${anno.property}"/>>
	</c:forEach>    
    <%--image rdf:resource="<c:url value="/images/logo16px.png"/>"/--%>
    <items>
      <rdf:Seq>
		<c:if test="${hasWebPage}"><rdf:li rdf:resource="${baseURL}${phyloWSPath}?format=html"/></c:if>
		<c:if test="${hasNeXML}"><rdf:li rdf:resource="${baseURL}${phyloWSPath}?format=nexml"/></c:if>
		<c:if test="${hasNexus}"><rdf:li rdf:resource="${baseURL}${phyloWSPath}?format=nexus"/></c:if>
		<c:if test="${hasRdf}"><rdf:li rdf:resource="${baseURL}${phyloWSPath}?format=rdf"/></c:if>
      </rdf:Seq>
    </items>
  </channel>
  <%--image rdf:about="<c:url value="/images/logo16px.png"/>">
    <title>${phyloWSPath}</title>
    <link>${baseURL}</link>
    <url><c:url value="/images/logo16px.png"/></url>
  </image--%>
  <c:if test="${hasWebPage}">
  <item rdf:about="${baseURL}${phyloWSPath}?format=html">
    <title>Web page</title>
    <link>${baseURL}${phyloWSPath}?format=html</link>
    <description>A human-readable version of the resource</description>
    <dc:format>text/html</dc:format>
    <dc:language>EN-US</dc:language>
  </item>
  </c:if>
  <c:if test="${hasNeXML}">
  <item rdf:about="${baseURL}${phyloWSPath}?format=nexml">
    <title>NeXML file</title>
    <link>${baseURL}${phyloWSPath}?format=nexml</link>
    <description>A NeXML serialization of the resource</description>
    <dc:format>application/xml</dc:format>
  </item>
  </c:if>  
  <c:if test="${hasNexus}">
  <item rdf:about="${baseURL}${phyloWSPath}?format=nexus">
    <title>Nexus file</title>
    <link>${baseURL}${phyloWSPath}?format=nexus</link>
    <description>A Nexus serialization of the resource</description>
    <dc:format>text/plain</dc:format>
  </item>
  </c:if>   
<%-- <c:if test="${hasRdf}">
  <item rdf:about="${baseURL}${phyloWSPath}?format=rdf">
    <title>RDF file</title>
    <link>${baseURL}${phyloWSPath}?format=rdf</link>
    <description>An RDF/XML serialization of the resource</description>
    <dc:format>application/rdf+xml</dc:format>
  </item>
  </c:if>  --%> 
</rdf:RDF>