<%@include file="/common/taglibs.jsp" %>
<c:url var="editorURL" value="/user/editorForm.html" />

<tr>
	<td colspan="6">
	<fieldset style="width:100%">
		<legend>Book Section / Conference Proceedings Information
		<a href="#" class="openHelp" onclick="openHelp('citationForm-booksection')"><img class="iconButton" src="<fmt:message key="icons.help"/>" alt="Help" /></a>						
		</legend>
		<table cellpadding="5">	
		    <tr>
		        <th align="right"><fmt:message key="citation.booksection.title"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.sectionTitle">
		            <input type="text" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>
		    <tr>
		        <th align="right"><fmt:message key="citation.keywords"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.keywords">
		            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>	
		     <tr>
		        <th align="right"><fmt:message key="citation.abstract"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.abstract">
		            <textarea rows="5" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>		    
		    <tr>
		        <th title="PubMed ID" align="right"><fmt:message key="citation.pmid"/>:</th>
		        <td>
		            <spring:bind path="citation.PMID">
		            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title="PubMed ID" maxlength="10"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		        <th align="right" title="Digital Object Identifier"><fmt:message key="citation.doi"/>:</th>
		        <td title = "Digital Object Identifier">
		            <spring:bind path="citation.doi">
		            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>		    	    
		    <tr>
		        <th align ="right" title = "Please provide page numbers in microsoft word format, e.g. 11-28"><fmt:message key="citation.booksection.page"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.sectionPages">
		            <input type="text" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title = "Please provide page numbers in microsoft word format, e.g. 11-28"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>
		    <tr>
		        <th align ="right"><fmt:message key="citation.book.title"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.bookTitle">
		            <input type="text" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>        
		    <tr align ="right">
		    	<th><fmt:message key="citation.book.publisher"/>:</th>
		        <td>
		            <spring:bind path="citation.publisher">
		            <input type="text" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    	<th><fmt:message key="citation.book.city"/>:</th>
		        <td>
		            <spring:bind path="citation.city">
		            <input type="text" style="width:100%" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>
		    <tr>
		        <th align="right"><fmt:message key="citation.book.isbn"/>:</th>
		        <td colspan="3">
		            <spring:bind path="citation.ISBN">
		            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		            </spring:bind>
		        </td>
		    </tr>		    
		    <c:if test="${not empty citation.id}">
		    	<tr>
		    		<td colspan="4" align="center"><a href="<c:out value="${editorURL}"/>"> Editor(s) <img class="iconButton" src="<fmt:message key="icons.edit"/>" /></a></td>
		    	</tr>
		    </c:if>    
	    </table>
    </fieldset>
</tr>