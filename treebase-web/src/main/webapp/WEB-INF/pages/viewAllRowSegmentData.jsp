<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="rowsegment.update"/></title>
<content tag="heading"><fmt:message key="rowsegment.update"/></content>

<style type="text/css">
	label.inverted { 
		display: inline !important;
		text-align: left !important;
		font-weight: bold
	}	
	th.inverted {
		text-align: right !important;
		border: none !important;
		background-color: white !important;
	}	
</style>

<body id="submissions"/>

<c:url var="exportRowSegmentDataURL" value="/user/exportRowSegmentData.html"/>
<c:url var="uploadAnotherRowSegmentFileURL" value="/user/uploadRowSegmentData.html"/>
<c:url var="matrixRowsURL" value="/user/matrixRowList.html"/>

<p align="center">
	<a href="${exportRowSegmentDataURL}"><b style="font-size:125%">Export Row Segment Data</b></a>
</p>

<p align="center">
	<a href="${uploadAnotherRowSegmentFileURL}"><b style="font-size:125%">Upload Another Row Segment Data File</b></a>
</p>
<p align="center">
	<a href="${matrixRowsURL}?id=${MATRIX_ID}"><b style="font-size:125%">Go Back To Matrix Rows</b></a>
</p>

<spring:bind path="arowsegmentlist.*">
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

<form method="post" name="segmentform">
<fieldset>
	<legend>Update Row Segment Data
<a href="#" class="openHelp" onclick="openHelp('viewAllRowSegmentData')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>	
	</legend>
	  
	<c:set var="counter"   value="0"/>
	
	<div style="overflow:auto; width:100%;">
	
	<display:table name="requestScope.arowsegmentlist.myList"
			   requestURI=""		   
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">

		<c:if test="${publicationState eq 'NotReady'}">
				
			<display:column titleKey="link.delete" 
			sortable="false" class="checkBoxColumn">
				<spring:bind path="arowsegmentlist.myList[${counter}].checked">
					<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
					<!--  do not allow user select the matrix if it's been used either as input or output -->
					<!--  or you can select data.dataType == selected if only specific the type input or output type -->
					<input type="checkbox" name="${status.expression}" value="true"/>
	            </spring:bind>
			</display:column>
						
		</c:if>
				
		<display:column  titleKey="rowsegment.rowtaxonlabel" 
				sortable="true" style="text-align:left">
			<c:out value="${userList.matrixRow.taxonLabel.taxonLabel}"/>				
		</display:column>
		
		<display:column  titleKey="rowsegment.sampletaxonlabel" property ="specimenTaxonLabelAsString"
				sortable="true" style="text-align:left">			
		</display:column>		
		
		<display:column title="Details" sortable="false" style="width:600px">
			<table style="width:600px">
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.title"/></th>
					<td>
						<spring:bind path="arowsegmentlist.myList[${counter}].title">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="15" maxlength="50" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>						
					</td>
				</tr>
				<tr>
					<th class="inverted">Segment coordinates</th>
					<td>
						<spring:bind path="arowsegmentlist.myList[${counter}].startIndex">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="startIndex${counter}"><fmt:message key="rowsegment.startindex"/></label>
							<input 
								class="textCell"
								style="width:30%" 
								id="startIndex${counter}" 
								type="text" 
								size="6" 
								maxlength="6" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>	
						<spring:bind path="arowsegmentlist.myList[${counter}].endIndex">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="endIndex${counter}"><fmt:message key="rowsegment.endindex"/></label>
							<input 
								class="textCell"
								style="width:30%"
								id="endIndex${counter}"
								type="text"
								size="6" 
								maxlength="6" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>											
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.instAcronym"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.instAcronym">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="6" maxlength="50" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>											
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.collectioncode"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.collectionCode">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="6" maxlength="50" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>										
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.catalognumber"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.catalogNumber">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="6" maxlength="50" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>										
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.genbankid"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.genBankAccession">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="6" maxlength="30" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>										
					</td>
				</tr>		
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.otherAccession"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.otherAccession">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="6" maxlength="30" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>									
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.sampledate"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.sampleDateString">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="10" maxlength="15" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>								
					</td>
				</tr>	
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.collector"/></th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.collector">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="20" maxlength="255" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>								
					</td>
				</tr>	
				<tr>
					<th class="inverted">Location</th>
					<td>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.latitude">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="latitude${counter}"><fmt:message key="rowsegment.latitude"/></label>
							<input 
								class="textCell" 
								style="width:15%"
								id="latitude${counter}" 
								type="text" 
								size="10" 
								maxlength="10" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.longitude">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="longitude${counter}"><fmt:message key="rowsegment.longitude"/></label>
							<input 
								class="textCell" 
								style="width:15%"
								id="longitude${counter}"
								type="text" 
								size="10" 
								maxlength="10" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.elevation">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="longitude${counter}"><fmt:message key="rowsegment.elevation"/></label>
							<input 
								class="textCell" 
								style="width:15%"
								id="elevation${counter}"
								type="text" 
								size="10" 
								maxlength="10"  
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>																	
					</td>
				</tr>
				<tr>
					<th class="inverted">Administrative unit</th>
					<td>
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.country">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="country${counter}"><fmt:message key="rowsegment.country"/></label>
							<input 
								class="textCell" 
								style="width:15%"							
								id="country${counter}"
								type="text" 
								size="10" 
								maxlength="50" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>	
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.state">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="state${counter}"><fmt:message key="rowsegment.state"/></label>
							<input 
								class="textCell" 
								style="width:15%"							
								id="state${counter}"
								type="text" 
								size="10" 
								maxlength="50" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.locality">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<label class="inverted" for="locality${counter}"><fmt:message key="rowsegment.locality"/></label>
							<input 	
								class="textCell" 
								style="width:15%"														
								id="locality${counter}"
								type="text" 
								size="10" 
								maxlength="255" 
								name="${status.expression}" 
								value="<c:out value="${status.value}"/>" />
						</spring:bind>
					</td>
				</tr>
				<tr>
					<th class="inverted"><fmt:message key="rowsegment.notes"/></th>
					<td>
						<spring:bind path="arowsegmentlist.myList[${counter}].specimenLabel.notes">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input class="textCell" style="width:100%" type="text" size="25" maxlength="2000" name="${status.expression}" value="<c:out value="${status.value}"/>" />
						</spring:bind>					
					</td>
				</tr>
			</table>
		</display:column>
		
		<c:if test="${publicationState eq 'NotReady'}">	
			<display:column 
				sortable="false"
				url="/user/deleteARowSegment.html" paramId="rowsegmentid" paramProperty="id"
				class="iconColumn" 
				headerClass="iconColumn"> 
				<img class="iconButton" src="<fmt:message key="icons.delete"/>" />
			</display:column>		
		</c:if>
					
		<c:set var="counter" value="${counter+1}"/>
						
	</display:table>
	</div>	
	
	<p>
	  <c:out value="Note: the Sample Date Format: YYYY-MM-DD (Example: 2000-02-25)"/>
	</p>
	
	<c:if test="${publicationState eq 'NotReady'}">		
		<p align="center" id="buttonContainer">
			<input type="submit" class="button" name="Update" value="<fmt:message key="button.update"/>" />
			<input type="submit" class="button" name="Delete" value="<fmt:message key="button.delete"/>" />
			<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
		</p>
	</c:if>	
	
	</fieldset>
</form>
