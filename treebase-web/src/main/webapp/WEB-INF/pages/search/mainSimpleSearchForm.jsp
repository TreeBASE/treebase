<%@ include file="/common/taglibs.jsp"%>
<form id="searchSimple" method="post">
	<fieldset>
		<input type="hidden" name="formName" value="mainSimple"/> 
    	<input type="text" class="textCell" style="width:150px" name="query" id="keyword" value="${query}"/>
   		<input type="submit" value="Search">
  		<a href="#" class="openHelp" onclick="openHelp('mainSimpleSearchForm')">
  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
  		</a>   
    </fieldset>
</form>