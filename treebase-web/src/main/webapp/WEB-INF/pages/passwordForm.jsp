<%@ include file="/common/taglibs.jsp"%>


<div class="container d-flex justify-content-center align-items-center py-5">
    <div class="card shadow-lg" style="width: 100%; max-width: 400px;">
        <div class="card-body p-4">
            <h2 class="card-title mb-4 text-center"><fmt:message key="user.password.title"/></h2>
            <p class="mb-2 text-center">Please fill out either the user name or the email address on file. If an account exists, we will send a password reset link to the registered email address.</p>
            <p class="mb-4 text-center"><strong>Note:</strong> For security reasons, we never send passwords via email. You will receive a link to create a new password.</p>

            <c:url var="homepageURL" value="/login.jsp" />

            <spring:bind path="user.*">
                <c:if test="${not empty status.errorMessages}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" id="passwordError" role="alert">
                        <img src="<c:url value="/images/iconWarning.gif"/>"
                                 alt="<fmt:message key="icon.warning"/>" class="me-2" style="height: 1.5em;" />
                        <div>
                            <c:forEach var="error" items="${status.errorMessages}">
                                <c:out value="${error}" escapeXml="false"/><br />
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </spring:bind>

            <form method="post" id="passwordForm">
                <input type="hidden" name="id" value="${status.value}"/>
                <div class="mb-3">
                    <label for="username" class="form-label fw-semibold">
                        <fmt:message key="user.username"/>
                    </label>
                    <spring:bind path="user.username">
                        <input type="text" class="form-control form-control-lg" id="username" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" autocomplete="username" />
                        <span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
                    </spring:bind>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label fw-semibold">
                        <fmt:message key="user.emailaddressstring"/>
                    </label>
                    <spring:bind path="user.tmpEmailAddress">
                        <input type="email" class="form-control form-control-lg" id="email" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" autocomplete="email" />
                        <span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
                    </spring:bind>
                </div>


                 <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="submit" name="Submit" class="btn btn-primary">Request Password Reset</button>
                <button type="reset" name="Reset" class="btn btn-secondary"><fmt:message key="button.reset"/></button>
            </div>

            </form>
        </div>
    </div>
</div>
