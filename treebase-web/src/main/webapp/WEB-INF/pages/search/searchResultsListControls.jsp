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


<div class="d-flex flex-wrap gap-2 mt-3">
  <div class="btn-group">
    <button type="button" class="btn btn-outline-primary" id="refineSearch" onclick="doAction('refineSearch')">
      <i class="fa fa-filter me-1"></i>Discard Unchecked Items
    </button>
<tb:helpButton topic="s+res+discard-unchecked-items+btn"/>

  </div>
  
  <div class="btn-group">
    <button type="button" class="btn btn-outline-danger" id="discardResults" onclick="confirmDiscard()">
      <i class="fa fa-trash me-1"></i>Discard All Results
    </button>

<tb:helpButton topic="s+res+discard-these-results+btn"/>
  </div>

  <c:if test="${resultSet.resultType == 'TREE'}">
    <div class="btn-group">
      <button type="button" class="btn btn-outline-success" id="downloadAllTrees" onclick="doAction('downloadAllTrees')">
        <i class="fa fa-download me-1"></i>Download All Trees
      </button>

<tb:helpButton topic="s+res+download-all-treess+btn"/>

    </div>
  </c:if>
</div>
</c:if>
