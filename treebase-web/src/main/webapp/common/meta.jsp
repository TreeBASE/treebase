<!-- HTTP 1.1 -->
<meta http-equiv="Cache-Control" content="no-store"/>
<!-- HTTP 1.0 -->
<meta http-equiv="Pragma" content="no-cache"/>
<!-- Prevents caching at the Proxy Server -->
<meta http-equiv="Expires" content="0"/>        
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/> 
<meta name="author" content="Rutger A. Vos (rutgeraldo@gmail.com)"/>
<meta name="google-site-verification" content="9Lr7BwyD6VMDIk5dENnSt_2YsUdpq_aEhnee0mMTxrw" />
<meta name="y_key" content="8c736195266b5ebb" />
<meta name="msvalidate.01" content="D931446190993D0D2D719F2978AC44EB" />
<link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>
<%
	String browsername = "";
	String browserversion = "";
	String ua = request.getHeader( "User-Agent" );
	boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
	boolean isOldMSIE = false;
	if ( isMSIE ) {
		//isOldMSIE = ua.indexOf("MSIE 8.0") == -1;
        String subsString = ua.substring( ua.indexOf("MSIE"));
        String Info[] = (subsString.split(";")[0]).split(" ");
        browsername = Info[0];
        browserversion = Info[1];
        float version = Float.parseFloat(browserversion);
        if (version < 8) {
        	isOldMSIE = true;
        
        }
		
	}
%>
<!-- <%= ua %> -->