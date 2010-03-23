<!-- HTTP 1.1 -->
<meta http-equiv="Cache-Control" content="no-store"/>
<!-- HTTP 1.0 -->
<meta http-equiv="Pragma" content="no-cache"/>
<!-- Prevents caching at the Proxy Server -->
<meta http-equiv="Expires" content="0"/>        
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/> 
<meta name="author" content="Rutger A. Vos (rutgeraldo@gmail.com)"/>
<link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>
<%
	String ua = request.getHeader( "User-Agent" );
	boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
	boolean isOldMSIE = false;
	if ( isMSIE ) {
		isOldMSIE = ua.indexOf("MSIE 8.0") == -1;
		
	}
%>
<!-- <%= ua %> -->
<% if( isOldMSIE ){ %>
	<script type="text/javascript">
		alert("Please upgrade your browser!\n\n"+
			"The TreeBASE team simply do not have the resources to support\n"+
			"old versions of Internet Explorer. There are many good, free\n"+
			"browsers available (IE8, FireFox, Safari, Chrome, Opera, etc.),\n"+
			"please use one of those instead.");			 
	</script>
<% } %>