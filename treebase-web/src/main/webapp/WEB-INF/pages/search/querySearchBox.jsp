<%@ include file="/common/taglibs.jsp"%>

<div class="mt-3">
    <a href="#CQLBox" class="text-decoration-none collapse-toggle" data-bs-toggle="collapse" aria-expanded="false" aria-controls="CQLBox">
        <i class="fa fa-search-plus me-1" id="advancedSearchIcon"></i>
        <span>Advanced search...</span>
    </a>
</div>

<div id="CQLBox" class="collapse mt-3">
    <div class="">
        <strong>CQL Query Testing</strong>
    </div>
    <div class="">
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
