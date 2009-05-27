<%@ include file="/common/taglibs.jsp"%>

<p class="bvnotice">
[ Bill and Val: The taxon search itself is working properly, but there was an error in the importation of the TB1
data, so that the taxa are not properly linked to the studies in the database.
Because the data is corrupt, the searches fail to find any studies.  I have corrected
the bug in the importer, but I have not yet repaired the imported data.&nbsp;]
</p>


<c:choose>
<c:when test="${searchType == 'study'}"><c:set var="studies" value="studies"/></c:when>
<c:when test="${searchType == 'matrix'}"><c:set var="studies" value="matrices"/></c:when>
<c:when test="${searchType == 'tree'}"><c:set var="studies" value="trees"/></c:when>
</c:choose>

<div style="width: 50%; float: left;">
  <form id="searchByTaxonLabel" method="post">
  <fieldset>
  <input type="hidden" name="formName" value="searchByTaxonLabel"/>
  <input type="hidden" name="searchType" value="${searchType }"/>
  <legend>Search by taxa</legend>
  Enter one taxon name per line:<br/>
  <table><tr>
    <td><textarea rows=5 cols=30 name="searchTerm" id="taxonLabel">${taxonLabels}</textarea></td>
    <td>Find ${studies} with:<br/>
    <input type=radio name="boolean" value="or"/>ANY<br/>
       <input type=radio name="boolean" value="and" checked/>ALL<br/>
 	of these taxa</td>
   <c:if test="${unrecognizedTaxa != null}">
	  <td>
	  <c:choose>
	  <c:when test="${not empty unrecognizedTaxa }">
	  <p><span style="font-size: 200%; color: red; font-weight: bold;">Unrecognized taxa:</span></p>
	  <ul>
	    <c:forEach var="label" items="${ unrecognizedTaxa }" >
	      <li><c:out value="${ label }"/></li>
	    </c:forEach>
	  </ul>
	  </c:when>
	  <c:otherwise><!--  empty list -->
	    <p><span style="font-size: 200%; color: red; font-weight: bold;">All taxa recognized</span></p>
	  </c:otherwise>
	  </c:choose>
	  </td>
    </c:if>
    	</tr></table>
    <input type=submit name="Action" value="Search">
 <!--    <input type=submit name="Action" value="Validate Taxon Labels"> -->
    </fieldset>
  </form>
</div>