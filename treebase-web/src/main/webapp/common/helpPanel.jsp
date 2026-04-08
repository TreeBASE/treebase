<%@ include file="/common/taglibs.jsp"%>
<%-- Bootstrap 5 Offcanvas Help Panel --%>
<div class="offcanvas offcanvas-end" tabindex="-1" id="helpPanel" aria-labelledby="helpPanelLabel">
    <div class="offcanvas-header bg-primary text-white">
        <h5 class="offcanvas-title" id="helpPanelLabel">
            <i class="fa fa-question-circle me-2"></i>Help
        </h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body" id="helpPanelContent">
        <div class="d-flex justify-content-center align-items-center h-100">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
</div>
