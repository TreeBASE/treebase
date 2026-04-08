<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->

<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head><%@ include file="/common/meta.jsp" %>
		<meta name="template" content="defaultSearchTemplate"/>
		<title>TreeBASE Search-<decorator:title/></title>
		<%@ include file="/decorators/decorator-head.jsp" %>

		<script type="text/javascript">
			//<![CDATA[
			TreeBASE.register(
			function() {
				if ( document.cookie.length > 0 )	{
					var c_name = 'citeuser';
					var c_start = document.cookie.indexOf(c_name + '=');
					if ( c_start != -1 ) {
						c_start = c_start + c_name.length + 1;
						var c_end = document.cookie.indexOf(';', c_start);
						if ( c_end == -1 ) {
							c_end=document.cookie.length;
						}
						var citeuser = document.cookie.substring(c_start,c_end);
						$('citeuser').value = citeuser;
					}
				}
			}
			);
			
			function toggle_visibility(id) {
				var e = document.getElementById(id);
				if(e.style.display == 'block')
				e.style.display = 'none';
				else
				e.style.display = 'block';
			}
			
			//]]>
		</script>

		<!-- DWR STUFF END -->
		<decorator:head/>
	</head>

	<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> onload="TreeBASE.initialize()">
		<%-- Sticky Bootstrap header --%>
		<jsp:include page="/common/header.jsp"/>
		<jsp:include page="/common/nav.jsp"/>
		<jsp:include page="/common/nav-search.jsp"/>

		<%@ include file="/decorators/decorator-body.jsp" %>
		<%@ include file="/decorators/decorator-scripts.jsp" %>
	</body>
</html>