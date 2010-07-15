<%@ include file="/common/taglibs.jsp"%>

<form method="post" id="loginForm" action="<c:url value="/j_security_check"/>" >


<% 
	if(request.getParameter("importKey") != null){

       session.setAttribute("importKey",request.getParameter("importKey"));
	}
%>

 
<fieldset>
<legend>Login</legend>

<table border="0" cellpadding="3">
    <tr>
        <td colspan="2">
            <c:if test="${param.error != null}">
                <div class="error" id="loginError"
                style="margin-right: 0; margin-bottom: 3px; margin-top: 3px">
                    <img src="<c:url value="/images/iconWarning.gif"/>"
                        alt="<fmt:message key="icon.warning"/>" class="icon" />
                    <fmt:message key="errors.password.mismatch"/>
                </div>
            </c:if>
        </td>
    </tr>
    
    <tr>
    	<script type="text/javascript">
    		//<![CDATA[
    			TreeBASE.register( function() { $('j_username').focus(); } );
    		//]]> 
    	</script>
    	<td><label for="j_username"><em class="required">* </em><fmt:message key="label.username"/></label></td>
    	<td><input name="j_username" class="textCell" id="j_username" size="25" type="text" /></td>
  	</tr>

  	<tr>
    	<td><label for="j_password"><em class="required">* </em><fmt:message key="label.password"/></label></td>
    	<td><input name="j_password" class="textCell" id="j_password" size="25" type="password" /></td>
  	</tr>

  	<tr>
    	<td>&nbsp;</td>
    	<td>
    		<input name="submit" type="submit" value="<fmt:message key="button.login"/>" />
    		<!--input name="reset" type="submit" name="reset" value="<fmt:message key="button.reset"/>" /-->
    	</td>
  	</tr>
  	 <tr>
  	 	<td>&nbsp;</td>
        <td align="right">
          	<fmt:message key="login.signup">
                <fmt:param><c:url value="/register.html"/></fmt:param>
            </fmt:message>
        </td>
	</tr>
</table>
</fieldset>
</form>

