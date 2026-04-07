<%@ include file="/common/taglibs.jsp"%>

<c:if test ="${PEOPLE == 'Author'}">
	<title><fmt:message key="author.form.title"/></title>
</c:if>
<c:if test ="${PEOPLE == 'Editor'}">
	<title><fmt:message key="editor.form.title"/></title>
</c:if>

<div class="container py-5">
	<spring:bind path="person.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
				<i class="fa fa-exclamation-triangle me-2"></i>
				<div>
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false"/><br />
					</c:forEach>
				</div>
			</div>
		</c:if>
	</spring:bind>

	<form name="peopleForm" onsubmit="return validateAuthor(this)">
		<c:if test="${publicationState eq 'NotReady'}">
			<div class="card shadow-lg mb-4">
				<div class="card-header d-flex justify-content-between align-items-center">
					<span class="fw-semibold">Search ${PEOPLE} By Last Name</span>
					<a href="#" class="openHelp" onclick="openHelp('peopleSearchForm')">
						<i class="fa fa-question-circle"></i> Help
					</a>
				</div>
				<div class="card-body">
					<p class="text-muted mb-4">${PEOPLE} information for citation</p>
					
					<div class="row g-3 align-items-end">
						<div class="col-md-6">
							<label class="form-label fw-semibold"><fmt:message key="user.lastname"/></label>
							<spring:bind path="person.lastName">
								<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
								<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
							</spring:bind>
						</div>
						<div class="col-md-6">
							<input type="hidden" name="authorIds" id="authorIdsList"/>
							<div class="d-flex gap-2">
								<button type="submit" name="Submit" class="btn btn-primary">
									<i class="fa fa-search"></i> Search
								</button>
								<button type="reset" name="Reset" class="btn btn-secondary">
									<fmt:message key="button.reset"/>
								</button>
								<button type="submit" name="_cancel" class="btn btn-outline-secondary" onclick="bCancel=true">
									<fmt:message key="button.cancel"/>
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<jsp:include page="peopleList.jsp"/>
	</form>
</div>

<script type="text/javascript">
	function test(){}
</script>

<script type="text/javascript">
var mainTable = document.getElementById("userList");

function swapRowUp(chosenRow) {
	if (chosenRow.rowIndex > 1) {
		moveRow(chosenRow, chosenRow.rowIndex - 1);
		extractIds();
	}
}

function swapRowDown(chosenRow) {
	if (chosenRow.rowIndex != mainTable.rows.length - 1) {
		moveRow(chosenRow, chosenRow.rowIndex + 1);
		extractIds();
	}
}

function moveRow(targetRow, newIndex) {
	if (newIndex > targetRow.rowIndex) {
		newIndex++;
	}
	var mainTable = document.getElementById('userList');
	var theCopiedRow = mainTable.insertRow(newIndex);
	
	for (var i = 0; i < targetRow.cells.length; i++) {
		var oldCell = targetRow.cells[i];
		var newCell = document.createElement("td");
		newCell.innerHTML = oldCell.innerHTML;
		theCopiedRow.appendChild(newCell);
	}
	mainTable.deleteRow(targetRow.rowIndex);
}

function extractIds() {
	var mainTable = document.getElementById("userList");
	var tmp = "";
	for (var i = 1; i < mainTable.rows.length; i++) {
		tmp = tmp + mainTable.rows[i].cells[0].innerHTML;
		if (i != mainTable.rows.length - 1) {
			tmp = tmp + ",";
		}
	}
	document.getElementById("authorIdsList").value = tmp;
	document.peopleForm.submit();
}
</script>

