<%@ include file="/common/taglibs.jsp" %>

<p class="footerLogo"><!-- Revision <%=  org.cipres.treebase.Version.VCSID %>  -->
<!-- there used to be a logo here - but we don't want a NESCent logo here as 
     NESCent is only the host, and the TreeBASE logo is already in the header
-->
</p>	
<p style="text-align:center">
	<a href="http://validator.w3.org/check?uri=referer">
		<img 
			src="<fmt:message key="icons.xhtml" />" 
			alt="XHTML" 
			title="Validate page markup" 
			class="iconButton" 
			style="vertical-align:middle"/>
	</a> 
	<a href="http://jigsaw.w3.org/css-validator/check/referer">
		<img 
			src="<fmt:message key="icons.css"/>" 
			alt="CSS" 
			title="Validate page styles" 
			class="iconButton"  
			style="vertical-align:middle"/>
	</a>
</p>
