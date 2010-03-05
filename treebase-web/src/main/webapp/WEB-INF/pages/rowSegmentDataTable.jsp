<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Row Segment Data</title>
<content tag="heading">Row Segment Data</content>
<br/>
<body id="submissions"/>

<form method="post" >


<fieldset>
<legend>Row segment data
	<a href="#" class="openHelp" onclick="openHelp('rowSegmentDataTable')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>	
	<b style="font-size:125%"> Check if header is included in the file: </b> <input type="checkbox" name="cbox" value="Butter" checked = true><br> 
  	<div style="overflow:auto; width:100%;">
  	<table border="1" class="list">
  		<c:set var="row"   value="0"/>
    	<c:forEach var="tdr" items="${ROWSEGMENTDATAARRAY}" > <!-- tdr is two dimentional array row -->
     			
        		<tr> 
        		  <c:if test="${row == 0}">
        		   <c:set var="headercol"   value="0"/>
        			<c:forEach var="tdcheader" items="${tdr}" >
      					<th>
      					  <select name="coldata${headercol}" id="coldata${headercol}" style="width:130px">
      					  
        					<c:forEach var="type" items="${ROWSEGMENTHEADERS}">
        						<option value="${type}" <c:if test="${type == SELECTEDHEADERLIST[headercol]}">selected="true"</c:if>>
        							<c:out value="${type}"/>
        						</option>
        					</c:forEach>
        				   
        				  </select>
      					</th>
      					<c:set var="headercol"   value="${headercol+1}"/>
      				 
      				</c:forEach>
      			 </c:if>
      			</tr>
      			
      			<c:set var="col"   value="0"/>
      			
      			<tr>
        			<c:forEach var="tdc" items="${tdr}" > <!-- tdc is two dimentional array column -->
      			     			
          				<td align = "center"><c:out value="${ROWSEGMENTDATAARRAY[row][col]}" /></td>
          				<c:set var="col"   value="${col+1}"/>
          		 
        			</c:forEach>
      	  		</tr>
      		<c:set var="row" value="${row+1}"/>	   
    	</c:forEach>
  
    </table>
    </div>	
  		<c:if test="${publicationState eq 'NotReady'}">
			<p align="center">
    			
	        		<input type="submit" class="button" name="Submit" value="<fmt:message key="button.submit"/>" />
	        		<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
    		
      		</p>
 		</c:if>
 
 </fieldset>
</form>



