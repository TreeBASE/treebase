<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.file.title"/></title>
<content tag="heading"><fmt:message key="upload.file.title"/></content>
<script type="text/javascript" src="../scripts/xp_progress.js"></script>

<script type="text/javascript">

function show_or_hide(layer_ref, state) { 
  if (document.all) { //IS IE 4 or 5 (or 6 beta) 
    eval( "document.all." + layer_ref + ".style.display = state"); 
  } 
  else if (document.layers) { //IS NETSCAPE 4 or below 
    document.layers[layer_ref].display = state; 
  } 
  else if (document.getElementById && !document.all) { 
    hza = document.getElementById(layer_ref); 
    hza.style.display = state; 
  } 
} 


</script>


<body id="submissions"/>
<p>Use the following form to upload your Nexus files for <b>submission ${studyMap['id']} - ${studyMap['name']}</b><br>
Please note that only the first ~30 trees will be parsed, otherwise large numbers of trees resulting from the 
same analysis will overwhelm the user experience in TreeBASE's search interface with what are largely redundant trees. 
If you have a large number of trees, please put your preferred trees, or a consensus tree, within the first ~30  
trees in the tree block. For more information, please see the help 
<a href="#" class="openHelp" onclick="openHelp('uploadFile')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>.</p>	
<!--form method="post" enctype="multipart/form-data" onsubmit="setTimeout('queryStatus()', 200);"-->
<!-- Line above is needed to show the actal amount of data uploaded using Ajax -->
<!-- Since we are not ready for it that is why line is commented -->
<form method="post" enctype="multipart/form-data" >
<fieldset>
	<legend>Nexus Files Upload
	<a href="#" class="openHelp" onclick="openHelp('uploadFile')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>	
	</legend>
	<table border="0" cellpadding="1">	
		<tr>
			<th>&nbsp;</th>
			<td>
				<input type="file" name="data" size="40"/>
			</td>
		</tr>
		<tr>
			<th></th>
			<td><table id="attachments" border="0"><tr><td></td></tr></table></td>
		</tr>		
		<c:if test="${publicationState eq 'Ready' || publicationState eq 'Published'}">
    	   <strong>For this study, file upload feature is not available.</strong>
    	</c:if>    	
    	<c:if test="${publicationState eq 'NotReady'}">		
		<tr>
			<td>&nbsp;</td>
			<td><a href="javascript:addFile()">Attach another file</a></td>
		</tr>
		<tr>
        <td></td>
        	<td class="buttonBar">
            <input type="submit" name="upload" class="button" 
                value="<fmt:message key="button.upload"/>" onclick="show_or_hide('kids', 'block');" />
            <input type="submit" name="_cancel" class="button" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />
        	</td>
    	</tr>
    	</c:if>
	</table>
	<div id="progressBar" style="display: none;">
		<div id="progressBarBox">
			<div id="progressBarBoxContent"></div>
			<div id="progressBarText">
				<spring:message code="progressBarTransferLabel"/>
				<span id="percentage"></span>
				<spring:message code="progressBarPercentLabel"/>
				<spring:message code="progressBarTransferSizeLabel"/>
				<!--span id="bytesRead"></span-->
				<!-- spring:message code="progressBarMetricUnitLabel"/-->
				<!-- spring:message code="progressBarFromLabel"/-->
				<!-- span id="totalSize"></span-->
				<!-- spring:message code="progressBarMetricUnitLabel"/-->;
			</div>
			<div id="progressBarSuccessful">
				<spring:message code="progressBarTextSuccessLabel"/>
			</div>
		</div>
	</div>
	
	<!--  tag below if for xp bar provided by Minh -->
	<div id="kids" style="display: none;">
		<h3>Uploading ...</h3>
        <script type="text/javascript">
          var bar1= createBar(500,15,'white',1,'black','#7DCBDA',85,7,3,"");
        </script>
	</div>
	
	</fieldset>
</form>

<%@ include file="/scripts/multiFileUpload.js"%>
