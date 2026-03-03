<%@ include file="/common/taglibs.jsp"%>

<div class="mt-3">
    <a href="#" class="text-decoration-none" onclick="TreeBASE.collapseExpand('CQLBox','block',this); return false;" title="Toggle advanced search">
        <img class="iconButton me-1" src="<fmt:message key="icons.expand"/>" style="vertical-align:middle" alt="expand"/>
        <span>Advanced search...</span>
    </a>
</div>

<div id="CQLBox" class="card mt-3" style="display:none">
    <div class="card-header">
        <strong>CQL Query Testing</strong>
    </div>
    <div class="card-body">
        <p class="card-text">
            The TreeBASE website can be searched using a subset of constructs from the 
            <a href="http://www.loc.gov/standards/sru/specs/cql.html" target="_blank">CQL</a> specification. For
            more information on how this is used, visit the 
            <a href="/treebase-web/urlAPI.html">TreeBASE help page about searching</a>.
        </p>
        <div class="mb-3">
            <textarea name="query" class="form-control" rows="4" placeholder="Enter CQL query..."></textarea>
        </div>
        <div class="d-grid">
            <button type="submit" class="btn btn-primary">Evaluate Query</button>
        </div>
    </div>
</div>
