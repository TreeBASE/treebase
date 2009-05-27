<%@ include file="/common/taglibs.jsp"%>

  <form id="searchByCitation" method="post">
  <fieldset>
  <input type="hidden" name="formName" value="searchByCitation"/>
  <legend>Search by keyword (in study citation)</legend>
  <div style="font-size: smaller;"><p>(Searches for keywords in study title, keywords, URL, journal name, and abstract.)</p>
</div>
    <input type=text name="searchTerm" id="citationWord">
    <input type=submit value="Search">
    </fieldset>
  </form>
