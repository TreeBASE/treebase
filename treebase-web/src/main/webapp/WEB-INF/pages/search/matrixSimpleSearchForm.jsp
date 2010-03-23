<%@ include file="/common/taglibs.jsp"%>
  <form id="searchSimple" method="post">
  <fieldset>
  Search: <input type="hidden" name="formName" value="matrixSimple"/>
    <input type=text class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
   <button type="submit" name="searchButton" value="matrixID">Matrix ID</button>
     <button type="submit" name="searchButton" value="matrixTitle">Title</button>
   <button type="submit" name="searchButton" value="matrixType">Type</button>
    <button type="submit" name="searchButton" value="matrixNTAX">NTAX</button>
    <button type="submit" name="searchButton" value="matrixNCHAR">NCHAR</button>
	  		<a href="#" class="openHelp" onclick="openHelp('matrixSimpleSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>    
    </fieldset>
    <jsp:include page="querySearchBox.jsp"/>   
  </form>
