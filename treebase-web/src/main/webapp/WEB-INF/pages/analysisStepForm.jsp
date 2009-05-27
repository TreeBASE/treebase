<%@ include file="/common/taglibs.jsp"%>

<script language="Javascript"><!--

		
        function showDiv(divName, show) {
          if (show) {
            display = '';
          } else {
            display = 'none';
          }
          
          document.getElementById(divName).style.display = display;
        }

        function showAndHideDivs(divToShow) {
          var indexvalue = document.getElementById('algorithmType');
          if(indexvalue.selectedIndex ==  indexvalue.length - 1){
          	showDiv('algorithmSpan', true);
          } else {
          	showDiv('algorithmSpan', false);
          }
     
        }

		function checkValue(form)
		{
			if (form.algorithmType.value == "Other Algorithm")
			{
				var newAlgorithm = form.newAlgorithm.value;
				if (newAlgorithm.length <= 0)
				{
					alert("New Algorithm must not be null when Other Algorithm is selected");
					return false;
				}
			}
		}
        
--></script>

<title><fmt:message key="analysis.step.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.title"/></content>
<body id="submissions"/>

<spring:bind path="step.*">
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

<form method="post"  onsubmit="return checkValue(this)">
<fieldset>
<legend>Analysis Step - Information
<a href="#" class="openHelp" onclick="openHelp('analysisStepInfo')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<p>Please complete the following analysis step information for <strong>analysis ${analysisMap['id']} - ${analysisMap['name']}</strong>.</p>
<input type="hidden" name="id" value="${status.value}"/>

<c:choose>
<c:when test="${empty step.algorithmType }">
	<c:set var="algorithmType" value="maximum likelihood"/>
</c:when>
<c:otherwise>
	<c:set var="algorithmType" value="${step.algorithmType}"/>
</c:otherwise>
</c:choose>
<table>
    <tr>
        <th align="right"><fmt:message key="analysis.step.name"/>:</th>
        <td>
            <spring:bind path="step.name">
            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="analysis.step.notes"/>:</th>
        <td colspan = 4>
            <spring:bind path="step.notes">
            <textarea rows="2" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="analysis.step.commands"/>:</th>
        <td colspan = 4>
            <spring:bind path="step.commands">
            <textarea rows="2" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <!-- Software Information -->
    <tr>
        <th align="right"><fmt:message key="analysis.step.software.name"/>:</th>
        <td>
          <spring:bind path="step.softwareInfo.name">
            <div id="ac">
           		<input class="textCell" style="width:100%" type="text" id="<c:out value="${status.expression}"/>"  name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            	<div id="aSoftwareNameList" class="auto_complete"></div>
            	<script type="text/javascript">
				 		new Autocompleter.DWR( '<c:out value="${status.expression}"/>', 'aSoftwareNameList',  updateSoftwareNameList,{ valueSelector: nameValueSelector, partialChars: 0 });
				</script>
            </div>
     
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
          </spring:bind>
         
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="analysis.step.software.version"/>:</th>
        <td>
            <spring:bind path="step.softwareInfo.softwareVersion">
            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <tr>
        <th align="right"><fmt:message key="analysis.step.software.url"/>:</th>
        <td>
            <spring:bind path="step.softwareInfo.softwareURL">
            <input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <tr >
        <th align="right"><fmt:message key="analysis.step.software.description"/>:</th>
        <td colspan = 3>
            <spring:bind path="step.softwareInfo.description">            
            	<input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>            
            	<span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <tr >
        <th align="right"><fmt:message key="analysis.step.algorithm.type"/>:</th>
        <td colspan="1">
        	<spring:bind path="step.algorithmType">
        	
        	<select name="${status.expression}" id="${status.expression}" style="width:130px" onchange="formSubmit(form)">
        		<option value="">--- Please Select ---
        			<c:forEach var="type" items="${algorithmtypes}">
        				<option value="${type}" <c:if test="${type == step.algorithmType}">selected="true"</c:if> >
        					<c:out value="${type}"/>
        				</option>
        			</c:forEach>
        		</option>
        	</select>
        	</spring:bind>
     	</td>          	
          
        	<c:if test="${step.algorithmType =='other algorithm'}">
 			  <th colspan="1" align="left"><strong> New Algorithm: </strong> </th>
 			     <td colspan="1">
       				<spring:bind path="step.algorithmMap[${algorithmType}].description">
        				<div id="ac">        				
           					<input type="text" class="textCell" style="width:100%" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
           					<div id="aUniqueOtherAlgorithmList" class="auto_complete"></div>
            				<script type="text/javascript">
				 				new Autocompleter.DWR( '<c:out value="${status.expression}"/>', 'aUniqueOtherAlgorithmList',  updateUniqueOtherAlgorithmList,{ valueSelector: nameValueSelector, partialChars: 0 });
							</script>
           				</div>
           			</spring:bind>
    		  	 </td> 	
    	    </c:if>		          
    </tr>   
  	<tr>
    	<th></th>
    	<td >
    	    <c:if test="${publicationState eq 'NotReady'}">
    	 	  <c:choose>
    			<c:when test="${empty step.id}">
	        		<input type="submit" name="Submit" value="<fmt:message key="button.submit"/>"/>
	        	</c:when>
	        	<c:otherwise>
	        		<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
	        		<input type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />
	        	</c:otherwise>
	          </c:choose>
	        	<input type="submit" name="Reset" value="<fmt:message key="button.reset"/>" />
	            <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
	        </c:if>
        </td>
    </tr>
</table>
</fieldset>
</form>
<script type="text/javascript">
function formSubmit(form) {
	form.submit();
}
</script>

<c:if test="${! empty step.id }">
	<c:set var="counter" value="0"/>
	<fieldset>
		<legend>Analyzed data
	<a href="#" class="openHelp" onclick="openHelp('analyzedDataList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>	
		</legend>
	<a href="/treebase-web/user/analyzedDataForm.html?analysis_step_id=${step.id}">
	<img src="<fmt:message key="icons.add"/>" class="iconButton"/></a> Add data
	
	<c:set var="updateAnalyzedDataURL" value="/treebase-web/user/updateAnalyzedDataList.html?id="/>
	<c:set var="counter" value="0"/>
	<display:table name="requestScope.step.analyzedDataCommandList"
				   requestURI=""
				   defaultsort="1"
				   class="list"
				   id="userList"
				   cellspacing="3"
				   cellpadding="3">	
		
		<display:column property="displayName" title="Analysis data name" 
					sortable="true"/>
					
		<display:column property="dataType" title="Data type" 
					sortable="true"/>				
	
		<display:column property="inputOutputType" title="Input/output" 
					sortable="true"/>
	
		<display:column class="iconColumn" headerClass="iconColumn">
			<c:if test="${not empty step.analyzedDataCommandList}">
				<spring:bind path="step.analyzedDataCommandList[${counter}]">				
					<a href="${updateAnalyzedDataURL}<c:out value="${status.value.id}"/>&analysis_step_id=${step.id}">
						<img src="<fmt:message key="icons.delete"/>" class="iconButton" />
					</a>				
				</spring:bind>
			</c:if>
		</display:column>
						
	    <display:setProperty name="export.pdf" value="true" />	
		<display:setProperty name="basic.empty.showtable" value="true"/>
		<c:set var="counter" value="${counter+1}"/>
	</display:table>	
		
	</fieldset>
</c:if>

