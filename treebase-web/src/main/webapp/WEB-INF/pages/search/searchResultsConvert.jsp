<%@ include file="/common/taglibs.jsp"%>



<c:if test="${empty resultsList || resultSet.isTrivial}" var="isTrivial"/>

<c:if test="${! isTrivial}">
  <form id="searchResultsConversionForm" method="post">
  <fieldset>
  <input type="hidden" name="formName" value="searchResultsConversion"/>
 <!--  <legend>Convert results to a different type</legend> -->
    <c:if test="${resultSet.resultType == StudySearchResultsType}">
      <button type="submit" name="conversion" value="toTrees"><fmt:message key="search.button.convert.StudyToTree"/></button>
      <button type="submit" name="conversion" value="toMatrices"><fmt:message key="search.button.convert.StudyToMatrix"/></button>
   </c:if>
    <c:if test="${resultSet.resultType == TreeSearchResultsType}">
       <button type="submit" name="conversion" value="toStudies"><fmt:message key="search.button.convert.TreeToStudy"/></button>
   </c:if>
    <c:if test="${resultSet.resultType == MatrixSearchResultsType}">
       <button type="submit" name="conversion" value="toStudies"><fmt:message key="search.button.convert.MatrixToStudy"/></button>
   </c:if>
    </fieldset>
  </form>
</c:if>

