<%@ include file="/common/taglibs.jsp"%>
  <form id="searchSimple" method="post">
  <fieldset>
  Search: <input type="hidden" name="formName" value="treeSimple"/>
   	<script type="text/javascript">
   		//<![CDATA[
   			TreeBASE.register( function() { $('keyword').focus(); } );
   		//]]>   
   	</script>   
    <input type="text" class="textCell" style="width:150px" name="searchTerm" id="keyword" value="${searchTerm}"/>
   <button type="submit" name="searchButton" value="treeID">Tree ID</button>
     <button type="submit" name="searchButton" value="treeTitle">Title</button>
   <button type="submit" name="searchButton" value="treeType">Type</button>
    <button type="submit" name="searchButton" value="treeKind">Kind</button>
    <button type="submit" name="searchButton" value="treeQuality">Quality</button>
    <button type="submit" name="searchButton" value="treeNTAX">NTAX</button>
	  		<a href="#" class="openHelp" onclick="openHelp('treeSimpleSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>   
    </fieldset>
  </form>
