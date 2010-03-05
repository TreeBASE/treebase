<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.file.title"/></title>
<content tag="heading"><fmt:message key="upload.file.title"/></content>
<script type="text/javascript" src="../scripts/xp_progress.js"></script>




<body id="submissions"/>

<p>Use this form to upload <b>Row Segment Data</b></p>
<!--form method="post" enctype="multipart/form-data" onsubmit="setTimeout('queryStatus()', 200);"-->
<!-- Line above is needed to show the actal amount of data uploaded using Ajax -->
<!-- Since we are not ready for it that is why line is commented -->
<form method="post" enctype="multipart/form-data" >
<fieldset>
	<legend>Upload tab delimited file
	<a href="#" class="openHelp" onclick="openHelp('uploadRowSegmentData')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>	
	</legend>
	<table border="0" cellpadding="1">
	
		<tr>
			<th>&nbsp;</th>
			<td>
				<input type="file" name="data" size="40"/>
			</td>
		</tr>
		
		
		<c:if test="${publicationState eq 'Ready' || publicationState eq 'Published'}">
    	   <Strong>For this study, file upload feature is not available.</Strong>
    	</c:if>
    	
    	<c:if test="${publicationState eq 'NotReady'}">
		
		
		<tr>
        <td></td>
        	<td class="buttonBar">
            <input type="submit" name="upload" value="Upload"/>
            <input type="submit" name="_cancel"  value = "Cancel"/>
        	</td>
    	</tr>
    	</c:if>
	</table>
		
	</fieldset>
</form>

