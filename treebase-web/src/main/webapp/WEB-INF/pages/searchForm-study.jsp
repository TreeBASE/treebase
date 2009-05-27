<%@include file="/common/taglibs.jsp" %>

<fieldset>
	<legend>Condition Builder for Study Search</legend>
	
	<table border="0" cellpadding="3">
	
		<tr>
        	<th>&nbsp;</th>
        	<th style="width: 50px; text-align: left">Delete?</th>
        	<th style="width: 150px;text-align: left">Attribute</th>
        	<th style="width: 100px;text-align: left">Equals/Contains</th>
        	<th style="width: 200px;text-align: left">Attribute Value</th>
    	</tr>
    	
    	<c:forEach var="criteria" items="${searchCommand.criterias}" varStatus="var">
    	
    	<tr>
    		<th>&nbsp;</th>
    		
    		<td>
    			<input type="checkbox" name="deleteCondition" value="${var.index}"/>
    		</td>
    		
    		<td>
    			<select name="${attribute}">
          			<c:forEach items="${attributes}" var="attribute">
            			<option value="${attribute.value}" <c:if test="${attribute.value == criteria.attribute}">selected="true"</c:if>>
						<c:out value="${attribute.label}"/>
						</option>
          			</c:forEach>
        		</select>
    		</td>
    	
    		<td>
    			<select name="${match}">
          			<c:forEach items="${matches}" var="match">
            			<option value="${match.value}" <c:if test="${match.value == criteria.match}">selected="true"</c:if>>
						<c:out value="${match.label}"/>
						</option>
          			</c:forEach>
        		</select>
    		</td>
    		
    		<td>
    			<input type="text" size="30" name="value" value="${criteria.value}"/>
    		</td>
    		
    		<td>
    			&nbsp;<b><c:out value="${criteria.operator}"/></b>
    		</td>
    		
    	</tr>
    
		</c:forEach>
			
		<!--  allow user to add a new critera -->
		<tr>
			<td style="width: 150px; text-align: left">Add New Condition &raquo;</td>
			<td>&nbsp;</td>
    		<td style="width: 100px; text-align: left">
    			<select name="newAttribute">
          			<c:forEach items="${attributes}" var="attribute">
            			<option value="${attribute.value}"> ${attribute.label}</option>
          			</c:forEach>
        		</select>
    		</td>
    		<td style="width: 100px; text-align="left">
    			<select name="newMatch">
          			<c:forEach items="${matches}" var="match">
            			<option value="${match.value}"> ${match.label}</option>
          			</c:forEach>
        		</select>
    		</td>
    		<td style="width: 200px; text-align="left">
    			<input type="text" name="newValue" value="" size="30"/>
    		</td>
    	</tr>
	</table>
</fieldset>

<tr>
	<table border="0" align="center">
	<th>&nbsp;</th>
    <td align="center">
 		<b>Add &nbsp;</b><input type="submit" name="And" value="Add Condition" />
 		<b>OR &nbsp; </b><input type="submit" name="OR" value="Add Condition" />
 		<b>Delete &nbsp;</b><input type="submit" name="Delete" value="Delete Condition" />
 		<b>Done &nbsp;</b><input type="submit" name="Build Query" value="Build Query" />
	</td>
	</table>
</tr>
	
	