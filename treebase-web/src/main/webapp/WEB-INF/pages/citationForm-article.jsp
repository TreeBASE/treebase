<%@include file="/common/taglibs.jsp" %>

<tr>
	<td colspan="6">
	<fieldset style="width:100%">
		<legend>Article Information
		<a href="#" class="openHelp" onclick="openHelp('citationForm-article')"><img class="iconButton" src="<fmt:message key="icons.help"/>" alt="Help" /></a>		
		</legend>
	<table cellpadding="5">	
	    <tr>
	        <th align="right"><fmt:message key="citation.article.title"/>:</th>
	        <td colspan="3">
	            <spring:bind path="citation.title">
	            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
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
	        <th align="right"><fmt:message key="citation.url"/>:</th>
	        <td colspan="3">
	            <spring:bind path="citation.URL">
	            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
	            </spring:bind>
	        </td>
	    </tr>	            
	    <tr>
	        <th nowrap="nowrap" align="right"><fmt:message key="citation.article.journal"/>:</th>
	        <td colspan="3">
	            <spring:bind path="citation.journal">	            	
	            	<div id="ac">
						<input 
							style="width:100%" 
							type="text" 
							class="textCell"
							id="<c:out value="${status.expression}"/>" 
							name="<c:out value="${status.expression}"/>" 
							value="<c:out value="${status.value}"/>"/>	            	
	            		<div id="journalList" class="auto_complete"></div>
	            		<script type="text/javascript">
					 		new Autocompleter.DWR('<c:out value="${status.expression}"/>', 'journalList', updateJournalNameList,{ valueSelector: nameValueSelector, partialChars: 0 });		
						</script>
					</div>
	            	<span class="fieldError"><c:out value="${status.errorMessage}"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	  	<tr>
	        <th align ="right"><fmt:message key="citation.article.volume"/>:</th>
	        <td>
	            <spring:bind path="citation.volume">
	            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
	            </spring:bind>
	        </td>
	        <th><fmt:message key="citation.article.issue"/>:</th>
	        <td>
	            <spring:bind path="citation.issue">
	            <input style="width:100%" type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
	            </spring:bind>
	        </td>
	    </tr>    
	    <tr>
	     <th align="right" title = "Please provide page numbers in microsoft word format, e.g. 11-28"><fmt:message key="citation.article.pages"/>:</th>
	        <td colspan="3">
	            <spring:bind path="citation.pages">
	            <input style="width:100%" class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title = "Please provide page numbers in microsoft word format, e.g. 11-28"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
	            </spring:bind>
	        </td>
	    </tr>
    </table>
    </fieldset>
    </td>
</tr>