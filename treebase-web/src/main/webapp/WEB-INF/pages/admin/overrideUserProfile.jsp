<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function checkPasswords() { 

  var x = document.getElementById("password").value;
  var y = document.getElementById("retypedpassword").value; 
 
  if(trim(x).length == 0 || trim(y).length == 0)
  {
     alert("One or both Passwords fields might be empty.");
     return false;
  }
  else if(x != y)
  {
    alert("Two passwords are not identical.");
    return false;
   } 
   return true;
} 

</script>

<title><fmt:message key="userform.title"/></title>
<content tag="heading"><fmt:message key="userform.title"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<c:choose>
				<c:when test="${user.id == null}">
					<p class="mb-0"><fmt:message key="create.profile"/></p>
				</c:when>
				<c:otherwise>
					<p class="mb-0"><fmt:message key="update.profile"/></p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<spring:bind path="user.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="alert alert-danger mb-4" role="alert">
				<c:forEach var="error" items="${status.errorMessages}">
					<i class="fa fa-exclamation-triangle me-2"></i>
					<c:out value="${error}" escapeXml="false"/><br/>
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>

	<form method="post" name="userForm" id="userForm" onsubmit="if (document.userForm.pressedButton.value != '_cancel') return validateUser(this)">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-user-edit"></i> User Registration</span>
			</div>
			<div class="card-body">
				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.username"/>:</label>
					<div class="col-md-6">
						<c:choose>
							<c:when test="${empty user.id}">
								<spring:bind path="user.username">
									<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
									<c:if test="${not empty status.errorMessage}">
										<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
									</c:if>
								</spring:bind>
							</c:when>
							<c:otherwise>
								<input type="text" class="form-control-plaintext" value="<c:out value="${user.username}"/>" readonly/>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.password"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.password">
							<input type="password" class="form-control" name="<c:out value="${status.expression}"/>" id="password" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold">Re-type Password:</label>
					<div class="col-md-6">
						<spring:bind path="user.password">
							<input type="password" class="form-control" name="retypedpassword" id="retypedpassword" value="<c:out value="${status.value}"/>"/>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.firstname"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.firstName">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.middlename"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.middleName">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.lastname"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.lastName">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.phone.number"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.phoneNumber">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-3">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.emailaddressstring"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.emailAddressString">
							<input type="email" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>

				<div class="row mb-4">
					<label class="col-md-3 col-form-label fw-semibold"><fmt:message key="user.role"/>:</label>
					<div class="col-md-6">
						<spring:bind path="user.roleDescription">
							<c:choose>
								<c:when test="${empty allRoles}">
									<input type="text" class="form-control-plaintext" value="<c:out value="${status.value}"/>" readonly/>
								</c:when>
								<c:otherwise>
									<select name="${status.expression}" class="form-select">
										<c:forEach var="roleDesc" items="${allRoles}">
											<option value="${roleDesc}" <c:if test="${roleDesc == user.roleDescription}">selected</c:if>>
												<c:out value="${roleDesc}"/>
											</option>
										</c:forEach>
									</select>
									<c:if test="${not empty status.errorMessage}">
										<div class="text-danger small mt-1"><c:out value="${status.errorMessage}"/></div>
									</c:if>
								</c:otherwise>
							</c:choose>
						</spring:bind>
					</div>
				</div>

				<div class="d-flex gap-2">
					<c:choose>
						<c:when test="${user.id == null}">
							<button type="submit" name="Submit" class="btn btn-primary" onclick="return checkPasswords();">
								<i class="fa fa-user-plus"></i> <fmt:message key="button.register"/>
							</button>
						</c:when>
						<c:otherwise>
							<button type="submit" name="Update" class="btn btn-primary">
								<i class="fa fa-save"></i> <fmt:message key="button.update"/>
							</button>
						</c:otherwise>
					</c:choose>
					<input type="hidden" name="pressedButton" value="">
					<button type="reset" name="Reset" class="btn btn-outline-secondary">
						<i class="fa fa-undo"></i> <fmt:message key="button.reset"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary" onclick="document.userForm.pressedButton.value = '_cancel';">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function test(){
    }
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>