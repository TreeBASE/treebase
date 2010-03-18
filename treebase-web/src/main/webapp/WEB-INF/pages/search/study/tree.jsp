<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp" %>

<!--  copied from 
      treebase-web/src/main/webapp/WEB-INF/pages/newPhylowidget.jsp
      20080724
 -->
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<title>PhyloWidget: TreeBASE's tree viewer</title>

<script type="text/javascript" src="<c:url value='/scripts/phylowidget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype/prototype-1.6.0.3.js'/>"></script>

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
  background:#CEE3F6;
  margin:auto;
  text-align:center;
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
                        <legend>PhyloWidget
<a href="#" class="openHelp" onclick="openHelp('newPhyloWidget')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>                        
                        </legend>
                        <applet 
						
                            code="org.phylowidget.PhyloWidget"
                            id="PhyloWidget"
                            name="PhyloWidget"
                            codebase="./phylowidget" 
                            archive="PhyloWidget.jar,core.jar,itext.jar,jgrapht-jdk1.5.jar,pdf.jar"
                            mayscript="true"
                            width="600" height="600">
                            
                            <param name="image" value="loading.gif" />
                            <param name="boxmessage" value="Loading Processing software..." />
                            <param name="boxbgcolor" value="#FFFFFF" />
                            <param name="progressbar" value="true" />
                            <param name="subapplet.classname" value="org.phylowidget.PhyloWidget" />
                            <param name="subapplet.displayname" value="PhyloWidget" />
                            
                            <!-- This is the message that shows up when people don't have
                                 Java installed in their browser. -->
                            To view this content, you need to install Java from 
                            <a href="http://java.com">java.com</a>                        
                        </applet>
                    </fieldset>
                </td>
                <td style="vertical-align:top">			
					<fieldset style="margin-left:5px;margin-right:5px">
						<legend>Node Info</legend>
						<div id="nodeText" style="background:white;margin:5px;padding:5px;">
							Mouse over a node to view its detailed information here. 
							Node details will be displayed if they were described in 
							the uploaded tree.
						</div>
					</fieldset>
					<fieldset  style="margin-top:10px;margin-left:5px;margin-right:5px">
						<legend><c:out value="${NEWICKSTRINGNAME}"/></legend>
						<select 
						    name="treeList" 
						    size="10" 
						    onclick="javascript:pickTree();" 
						    id="treeList">
							<c:forEach var="x" items="${NEWICKSTRINGSMAP}" > 
					  			<option value="${x.value}">${x.key}</option>
					 		</c:forEach>
					 	</select>
					</fieldset>
                    <input 
                        type="hidden" 
                        name="treeText" 
                        id="treeText" 
                        onfocus="selectOnce(this);" 
                        value="" />
                    <input 
                        type="hidden" 
                        name="clipText" 
                        id="clipText" 
                        onchange="updateJavaClip();" 
                        value="" />                    
                    <div id="comments" style="margin: 5px">
                        Make a selection by clicking on the tree list.
                    </div>
		        </td>
		    </tr>
        </table>	
	</form>
	
	<script type="text/javascript">
		
	    TreeBASE.register(			
	        function () {		
	            var treeList = document.getElementById('treeList');
	            var option = treeList.getElementsByTagName('option');
	            document.TreeForm.treeText.value = option[0].value;				
	            updateJavaTree();			
	        }		
	    );
	
	</script>
</body>
</html>

