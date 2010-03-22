<%@ include file="/common/taglibs.jsp"%>
  <form id="searchSimple" method="post">
  <fieldset>
  Search: <input type="hidden" name="formName" value="searchKeyword"/>
   	<script type="text/javascript">
   		//<![CDATA[
   			TreeBASE.register( function() { $('keyword').focus(); } );
   		//]]>   
   	</script>  
    <input type="text" class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
   <button type="submit" name="searchButton" value="studyID">Study ID</button>
      <button type="submit" name="searchButton" value="legacyStudyID">Legacy Study ID</button>
     <button type="submit" name="searchButton" value="authorKeyword">Author</button>
   <button type="submit" name="searchButton" value="titleKeyword">Title</button>
    <button type="submit" name="searchButton" value="abstractKeyword">Abstract</button>
    <button type="submit" name="searchButton" value="citationKeyword">Entire citation</button>
    <button type="submit" name="searchButton" value="textKeyword">All text</button>
	  		<a href="#" class="openHelp" onclick="openHelp('studyKeywordSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>     
    </fieldset>
    <jsp:include page="querySearchBox.jsp"/>  
  </form>
