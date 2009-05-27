<%@ include file="/common/taglibs.jsp"%>

<c:if test ="${PEOPLE == 'Author'}">
	<title><fmt:message key="author.form.title"/></title>
	<content tag="heading"><fmt:message key="author.list.title"/></content>
</c:if>
<c:if test ="${PEOPLE == 'Editor'}">
	<title><fmt:message key="editor.form.title"/></title>
	<content tag="heading"><fmt:message key="editor.list.title"/></content>
</c:if>

<body id="submissions" onLoad = "test();"/>

<spring:bind path="person.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>


<form  name="peopleForm" onsubmit="return validateAuthor(this)">
	<fieldset>
		<legend>New ${PEOPLE} Information
		<a href="#" class="openHelp" onclick="openHelp('peopleForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
		</legend>
		<p>${PEOPLE} information for citation</p>
		<table border="0" cellpadding="3">
			<tr>
		        <th style="text-align:right"><fmt:message key="user.firstname"/>:</th>
		        <td>
		            <spring:bind path="person.firstName">
		            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>    
		        <th style="text-align:right"><fmt:message key="user.middlename"/>:</th>
		        <td>
		            <spring:bind path="person.middleName">
		            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		        <th style="text-align:right"><fmt:message key="user.lastname"/>:</th>
		        <td>
		            <spring:bind path="person.lastName">            
		            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>
		    <tr>
		        <th style="text-align:right"><fmt:message key="user.emailaddressstring"/>:</th>
		        <td colspan="5">          
		            <spring:bind path="person.emailAddressString">
		              <div id="ac">
		            	<input style="width:100%" class="textCell" type="text" id="<c:out value="${status.expression}"/>"  name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
		            	<div id="aemailList" class="auto_complete"></div>
		            	<script type="text/javascript">
						 		new Autocompleter.DWR('emailAddressString', 'aemailList', updateList,{ valueSelector: nameValueSelector, partialChars: 0 });		
						</script>
		              </div>
		              <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>          
		        </td>
		    </tr>    
		    <tr>
		       <td colspan="6"><input type="hidden" name="authorIds"  id="authorIdsList"/> </td>
		    </tr>         
		    <tr>
		      <c:if test="${publicationState eq 'NotReady'}">
		    	<td colspan="6" align="center">
				    <input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
		    		<input type="submit" name="Submit and Continue" value="<fmt:message key="button.submit.and.continue"/>" />
		 	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" onclick ="bCancel=true"/>    			
		        </td>
		       </c:if>       
		    </tr>
		</table>
	</fieldset>
	<jsp:include page="peopleList.jsp"/>
</form>

<script type="text/javascript">
	function test(){
    }
</script>

<script type="text/javascript">

var mainTable = document.getElementById("userList");
function swapRowUp(chosenRow){
   
  if (chosenRow.rowIndex > 1 ) {
    moveRow(chosenRow, chosenRow.rowIndex-1);
    extractIds();
  }
}
function swapRowDown(chosenRow) {                                                
 if (chosenRow.rowIndex != mainTable.rows.length-1) {                           
   moveRow(chosenRow, chosenRow.rowIndex+1);
   extractIds();
 }                                                                              
}                                                                                

//moves the target row object to the input row index
function moveRow(targetRow, newIndex) {

 //since we are not actually swapping
 //but simulating a swap, have to "skip over"
 //the current index
 if (newIndex > targetRow.rowIndex) {
   newIndex++;
 }

 //establish proper reference to the table
 var mainTable = document.getElementById('userList');
 var theCopiedRow = mainTable.insertRow(newIndex);

 //copy all the cells from the row to move
 //into the new row
 for ( var i = 0; i < targetRow.cells.length; i++ ) {
   var oldCell = targetRow.cells[i];
   var newCell = document.createElement("td");
   newCell.innerHTML = oldCell.innerHTML;
   theCopiedRow.appendChild(newCell);
 }

 //delete the old row
 mainTable.deleteRow(targetRow.rowIndex);
}

</script>

<script type="text/javascript">
	
	function extractIds(){	
		var mainTable = document.getElementById("userList");
		var tmp = "";
		for(var i = 1; i < mainTable.rows.length; i++){
			tmp = tmp + mainTable.rows[i].cells[0].innerHTML;
			if(i != mainTable.rows.length-1){
				tmp = tmp + ",";
			}
		}
		document.getElementById("authorIdsList").value = tmp;
		document.peopleForm.submit();
	}

</script>

<v:javascript formName="person" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
