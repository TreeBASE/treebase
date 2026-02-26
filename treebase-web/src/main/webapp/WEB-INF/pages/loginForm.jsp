<%@ include file="/common/taglibs.jsp"%>

<div class="container d-flex align-items-center justify-content-center min-vh-100">
	<div class="card shadow-lg w-100" style="max-width: 400px;">
		<div class="card-body p-4">
			<h2 class="card-title mb-4 text-center">Login</h2>
			<form method="post" id="loginForm" action="<c:url value="/j_security_check"/>">
				<% 
					if(request.getParameter("importKey") != null){
						session.setAttribute("importKey",request.getParameter("importKey"));
					}
				%>
				<c:if test="${param.error != null}">
					<div class="alert alert-danger d-flex align-items-center mb-3" id="loginError" role="alert">
						<img src="<c:url value="/images/iconWarning.gif"/>"
								 alt="<fmt:message key="icon.warning"/>" class="me-2" style="height: 1.5em;" />
						<div><fmt:message key="errors.password.mismatch"/></div>
					</div>
				</c:if>
				<div class="mb-3">
					<label for="j_username" class="form-label fw-semibold">
						<em class="text-danger">* </em><fmt:message key="label.username"/>
					</label>
					<input name="j_username" class="form-control form-control-lg" id="j_username" type="text" autocomplete="username" autofocus required />
				</div>
				<div class="mb-3">
					<label for="j_password" class="form-label fw-semibold">
						<em class="text-danger">* </em><fmt:message key="label.password"/>
					</label>
					<input name="j_password" class="form-control form-control-lg" id="j_password" type="password" autocomplete="current-password" required />
				</div>
				<div class="d-grid mb-3">
					<button name="submit" type="submit" class="btn btn-primary btn-lg">
						<fmt:message key="button.login"/>
					</button>
				</div>
				<div class="text-center">
					<small>
						<fmt:message key="login.pleasefillin"/><br/>
						<fmt:message key="login.requiredfields"/>
					</small>
				</div>
        				<div class="text-center">
					<small>
						<fmt:message key="login.signup">
							<fmt:param><c:url value="/register.html"/></fmt:param>
						</fmt:message>
					</small>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	//<![CDATA[
	TreeBASE.register(function() { document.getElementById('j_username').focus(); });
	//]]>
</script>
</table>
</fieldset>
</form>

