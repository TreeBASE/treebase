<form onsubmit="return TreeBASE.redirect(phyloWSURI + escape($('expanded').value));">
    <div class="row g-3 align-items-center mb-3">
        <div class="col-auto">
            <label for="query" class="col-form-label fw-semibold">Search:</label>
        </div>
        <div class="col-md-4">
            <input type="text" id="query" onchange="return TreeBASE.expandQuery();" class="form-control" placeholder="Enter search term..."/>
        </div>
        <div class="col-auto">
            <button type="submit" onclick="return TreeBASE.redirect(phyloWSURI + escape($('expanded').value));" class="btn btn-primary">
                <i class="fa fa-search me-1"></i>Search
            </button>
        </div>
    </div>
    <div class="mb-3">
        <div class="form-check form-check-inline">
            <input type="radio" name="join" onclick="TreeBASE.expandQuery(); return true;" id="all" value="and" class="form-check-input"/>
            <label class="form-check-label" for="all">All</label>
        </div>
        <div class="form-check form-check-inline">
            <input type="radio" name="join" onclick="TreeBASE.expandQuery(); return true;" value="or" checked="checked" id="any" class="form-check-input"/>
            <label class="form-check-label" for="any">Any</label>
        </div>
    </div>
    <div class="mt-3">
        <a href="#expanded" class="text-decoration-none collapse-toggle" data-bs-toggle="collapse" aria-expanded="false" aria-controls="expanded" id="expander">
            <i class="fa fa-search-plus me-1"></i>
            <span>Advanced search...</span>
        </a>
    </div>
    <div id="expanded" class="collapse card mt-3">
        <div class="card-body">
            <textarea class="form-control" rows="8" placeholder="Enter advanced query..."></textarea>
        </div>
    </div>
</form>