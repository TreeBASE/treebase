<!-- Bootstrap 5 CSS -->
<link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/messages.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ajaxProgress.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/submissionSummary.css'/>" />
<!-- jQuery - replaces Prototype.js -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<!-- Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
<!-- End Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>

<!-- Following script lines have been added for DWR and they are used for now only on author page -->

<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/autocomplete.css'/>" />

<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"> </script>
<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>

<script type="text/javascript" src="<c:url value='/dwr/interface/RemotePersonService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteSoftwareNameService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteJournalNameService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteAjaxProgressListener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteUniqueOtherAlgorithmService.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/autocomplete.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/ajaxProgress.js'/>"></script>

<script language="Javascript" type="text/javascript">
    // Configure DWR to suppress default error alerts for autocomplete
    if (typeof dwr !== 'undefined' && dwr.engine) {
        dwr.engine.setErrorHandler(function(message, ex) {
            // Silently handle DWR errors for autocomplete (e.g., empty database)
            if (typeof console !== 'undefined' && console.log) {
                console.log('DWR error (suppressed): ' + message);
            }
        });
    }
</script>