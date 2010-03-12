<%@ include file="/common/taglibs.jsp" %>

<p class="footerLogo"><!-- Revision <%=  org.cipres.treebase.Version.VCSID %>  -->
	<a href="http://www.nescent.org/">
		<img 
			src="<c:url value="/images/nescent_logo.png"/>" 
			alt="NESCent" 
			width="83" 
			height="83" 
			border="0" />
	</a>
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