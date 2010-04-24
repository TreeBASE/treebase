<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.by.submission.id"/></title>
<content tag="heading"><fmt:message key="search.by.submission.id"/></content>
<body id="admin"/>

<form method="post"  id="dataForm">

<fieldset>
<legend>Search for submissions by identifiers
<a href="#" class="openHelp" onclick="openHelp('searchBySubmissionID')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<table border="0" cellpadding="3" cellspacing="3">

	<tr>
        <th><fmt:message key="submission.accession"/>:</th>
        <td>  	
        	<input type="radio" name="identifierType" value="TB2" checked="checked"/>TreeBASE2 Submission ID   
        	<input type="radio" name="identifierType" value="TB1"/>TreeBASE1 Legacy Study ID 
        	<input type="radio" name="identifierType" value="TB0"/>TreeBase2 Study ID        	       	
        	<input type="text" name="submissionaccession"  maxlength = "25"/>       
        </td>
    </tr>
       
  	<tr>
    	<th></th>
    	<td>
	        <input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
	        <!--input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" /-->
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>
  	
</table>
</fieldset>
</form>