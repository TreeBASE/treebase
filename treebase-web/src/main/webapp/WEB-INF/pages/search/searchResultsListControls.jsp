<%@ include file="/common/taglibs.jsp"%>
<%-- script type="text/javascript" src="/treebase-web/scripts/prototype/prototype-1.6.0.3.js"></script --%>
<c:if test="${! isEmpty }">

<script type="text/javascript">
	function confirmDiscard () {
		if ( confirm("This will discard all your current search results. Continue?") ) {
  			doAction('discardResults')
  		}		
	}
</script>

<!-- 
  <input type="submit" id="selectAllButton" value="Select All Above" onclick="checkBoxes('searchResultsList', true)"/> 
  <a href="#" class="openHelp" onclick="openHelp('s+res+select-all+btn')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>

  <input type="submit" id="deselectAllButton" value="Deselect All Above" onclick="checkBoxes('searchResultsList', false)"/>
  <a href="#" class="openHelp" onclick="openHelp('s+res+deselect-all+btn')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
 -->

  <input type="submit" id="refineSearch" value="Discard Unchecked Items" onclick="doAction('refineSearch')"/>
  <a href="#" class="openHelp" onclick="openHelp('s+res+discard-unchecked-items+btn')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
  
  <input style="align: right;" type="submit" id="discardResults" value="Discard All Results" onclick="confirmDiscard()"/>
  <a href="#" class="openHelp" onclick="openHelp('s+res+discard-these-results+btn')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>

  <c:if test="${resultSet.resultType == 'TREE'}">
  	<input style="align: right;" type="submit" id="downloadAllTrees" value="Download All Trees" onclick="doAction('downloadAllTrees')"/>
  	<a href="#" class="openHelp" onclick="openHelp('s+res+download-all-treess+btn')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
  </c:if>
</c:if>
