<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="login.title"/></title>

<content tag="heading"><fmt:message key="login.title"/>

</content>

<table width="501" border="0">
  <tr>
	<td width="495"><p><strong>A Special Note to Submitters to the Previous Version 
	  of TreeBASE:</strong> If you started a submission in the previous 
	  version, but that submission remained &quot;in progress,&quot; 
	  you will need to start the submission all over again 
	in this new version of TreeBASE.</p>
	</td>
  </tr>
</table>

<hr/>
<p>Please fill in following information to login<br/>
<span class="required">* Required Fields</span></p>

<body id="info"/>

<!-- loginForm.jsp -->
<c:import url="/WEB-INF/pages/loginForm.jsp"/>
<!-- password -->
<p>Forgot your password? Have your <a href="<c:url value="passwordForm.html"/>">password e-mailed to you</a></p>
