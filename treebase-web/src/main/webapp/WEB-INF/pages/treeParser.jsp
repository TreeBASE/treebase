<%@ include file="/common/taglibs.jsp"%>

<title>Nexus Parser Test</title>
<content tag="heading">Mesquite Nexus Parser Test Page</content>

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

<p>Use the following form to upload your Nexus files for testing Mesquite Parser</p>

<form method="post" enctype="multipart/form-data">
<fieldset>
	<legend>Nexus Files Upload</legend>
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
		<tr>
			<td>&nbsp;</td>
			<td><a href="javascript:addFile()">Attach another file</a></td>
		</tr>
		<tr>
        <td></td>
        <td class="buttonBar">
            <input type="submit" name="upload" class="button" onclick="show_or_hide('kids', 'block');"
                value="<fmt:message key="button.upload"/>" />
            <input type="submit" name="_cancel" class="button" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
	</table>
	<div id="kids" style="display: none;">
		<h3>Uploading ...</h3>
        <script type="text/javascript">
          var bar1= createBar(500,15,'white',1,'black','#7DCBDA',85,7,3,"");
        </script>
	</div>
	
	</fieldset>
</form>

<%@ include file="/scripts/multiFileUpload.js"%>
