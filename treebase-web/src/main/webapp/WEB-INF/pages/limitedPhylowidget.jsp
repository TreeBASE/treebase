<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<title>-Tree viewer</title>

<script type="text/javascript" src="<c:url value='/scripts/prototype/prototype-1.6.0.3.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/raphael-min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jsphylosvg-min.js'/>"></script>

<style type="text/css">
body {
  margin: 0px 0px 0px 0px;
  padding: 0px 0px 0px 0px;
  text-align: center;
  font-family: verdana, geneva, arial, helvetica, sans-serif; 
  font-size: 11px; 
  background-color: white; 
  text-decoration: none; 
  font-weight: normal; 
  line-height: normal;
}
 
a          { color: #3399CC; text-decoration: none; }
a:link     { color: #3399CC; text-decoration: none; }
a:visited  { color: #3399CC; text-decoration: none; }
a:active   { color: #3399CC; text-decoration: underline; }
a:hover    { color: #3399CC; text-decoration: underline; }

div {
  border: 0px;

}

#content {
  margin: auto;
}

#content fieldset {
  margin-top: 5px;
}

#comments {
}

input {
  font-size:12px;
}

fieldset {
  display:block;
  margin:auto;
}

legend {
  background:white;
  border:1px solid black;
  padding:5px;
}

#treeText,#clipText {
  width:100%;
}

table {
  text-align:left;
}

table .key {
  text-align:right;
  padding-right:10px;
}

table .val {
 text-align:left;
}

</style>
</head>
<body>
	<form name="TreeForm">
		<table id="content" style="margin-top: 5px">
			<tr>
                <td style="vertical-align:top">
                    <fieldset>
                    	<legend>Tree window</legend>
						<script type="text/javascript">
						function drawjsPhyloSVGTree(namespacedGUID,ntax){
							var uri = "/treebase-web/phylows/tree/" + namespacedGUID + "?format=nexml";
							new Ajax.Request(uri,{
								method     : 'get',
								onComplete : function(transport) {
									var dataObject = {
											nexml      : transport.responseXML,
											fileSource : true
									};		
									phylocanvas = new Smits.PhyloCanvas(
										dataObject,
										'svgCanvas', 
										600, ntax * 20				
									);
								},
								onFailure: function(){ 
									alert('Something went wrong while attempting to fetch '+uri) 
								}
							});						
						};
						</script>                    	
                    	<div id="svgCanvas" style="width:600px"></div>
                    </fieldset>
                </td>
                <td style="vertical-align:top">
					<fieldset>
						<legend><c:out value="${NEWICKSTRINGNAME}"/></legend>
					 	<ol>
					 		<c:forEach var="tree" items="${TREELIST}">
					 			<li onclick="drawjsPhyloSVGTree('${tree.treebaseIDString.namespacedGUID}',${tree.nTax})">
					 				${tree.label} ${tree.title}
					 			</li>
					 		</c:forEach>
					 	</ol>
					</fieldset>
					<c:if test="${treeBlockID != null}">
						<fieldset>		
							<legend>Quick links</legend>								
							<p>							
								<a href="/treebase-web/search/study/trees.html?id=${studyID}" target="_new">
									<img class="iconButton" style="vertical-align:middle" src="<fmt:message key="icons.trees"/>" />
									Containing tree set
								</a>
							</p>
							<p>							
								<a href="/treebase-web/search/study/summary.html?id=${studyID}" target="_new">
									<img class="iconButton" style="vertical-align:middle" src="<fmt:message key="icons.citation"/>" />
									Containing study
								</a>
							</p>
						</fieldset>
					</c:if>                     
		        </td>
		    </tr>
        </table>	
	</form>
</body>
</html>

