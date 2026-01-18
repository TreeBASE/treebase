<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Tree Viewer</title>

<!-- D3.js v7 - local copy -->
<script src="/treebase-web/scripts/d3.min.js"></script>
<!-- lodash - required by phylotree.js -->
<script src="/treebase-web/scripts/lodash.js"></script>
<!-- underscore - required by phylotree.js -->
<script src="/treebase-web/scripts/underscore.js"></script>
<!-- SHIM: Create wrapper objects for phylotree.js UMD bundle -->
<!-- Prototype.js pollutes Function.prototype, which breaks phylotree's _interopNamespaceDefault -->
<script>
(function() {
    // Create a plain object copy of a library, using only OWN properties
    // This avoids inheriting Function.prototype methods added by Prototype.js
    function wrapLibrary(lib) {
        var wrapper = {};
        var keys = Object.getOwnPropertyNames(lib);
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            try {
                wrapper[key] = lib[key];
            } catch(e) {
                // Skip properties that throw on access
            }
        }
        return wrapper;
    }
    
    // Store original for any code that needs the function form
    var originalUnderscore = window._;
    
    // Replace globals with wrapped versions
    window._ = wrapLibrary(originalUnderscore);
    window._$1 = wrapLibrary(originalUnderscore);
    
    // Preserve noConflict if needed
    if (originalUnderscore.noConflict) {
        window._.noConflict = originalUnderscore.noConflict;
    }
})();
</script>
<!-- phylotree.js v2.4.0 - local copy -->
<script src="/treebase-web/scripts/phylotree.js"></script>
<!-- phylotree.css -->
<link rel="stylesheet" href="/treebase-web/styles/phylotree.css" type="text/css">

<style type="text/css">
body {
  margin: 0;
  padding: 10px;
  font-family: verdana, geneva, arial, helvetica, sans-serif; 
  font-size: 11px; 
  background-color: white; 
}
 
a          { color: #3399CC; text-decoration: none; }
a:link     { color: #3399CC; text-decoration: none; }
a:visited  { color: #3399CC; text-decoration: none; }
a:active   { color: #3399CC; text-decoration: underline; }
a:hover    { color: #3399CC; text-decoration: underline; }

#content {
  display: flex;
  gap: 20px;
}

#tree-container {
  flex: 1;
  min-width: 600px;
  border: 1px solid #ccc;
  background: #fafafa;
  overflow: auto;
}

#tree-container svg {
  display: block;
}

#sidebar {
  width: 300px;
}

fieldset {
  display: block;
  margin: 0 0 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  background: #f5f5f5;
}

legend {
  background: white;
  border: 1px solid #ccc;
  padding: 5px 10px;
  font-weight: bold;
}

#tree-list {
  list-style: decimal;
  margin: 0;
  padding: 0 0 0 20px;
  max-height: 300px;
  overflow-y: auto;
}

#tree-list li {
  padding: 5px 0;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

#tree-list li:hover {
  background: #e0e0e0;
}

#tree-list li.selected {
  background: #CEE3F6;
  font-weight: bold;
}

.quick-links p {
  margin: 5px 0;
}

.iconButton {
  vertical-align: middle;
  margin-right: 5px;
}

#loading {
  padding: 20px;
  text-align: center;
  color: #666;
}

#node-info {
  background: white;
  padding: 10px;
  min-height: 60px;
  font-size: 10px;
}

.controls {
  margin-bottom: 10px;
}

.controls button {
  margin-right: 5px;
  padding: 5px 10px;
  cursor: pointer;
}

/* phylotree overrides */
.node text {
  font-size: 10px;
}
</style>
</head>
<body>

<div id="content">
    <div id="tree-container">
        <div id="loading">Select a tree from the list to view it.</div>
    </div>
    
    <div id="sidebar">
        <fieldset>
            <legend><c:out value="${NEWICKSTRINGNAME}" default="Trees"/></legend>
            <ol id="tree-list">
                <c:forEach var="tree" items="${TREELIST}" varStatus="status">
                    <li data-newick="${fn:escapeXml(tree.newickString)}" 
                        data-id="${fn:escapeXml(tree.id)}"
                        data-label="${fn:escapeXml(tree.label)}"
                        data-title="${fn:escapeXml(tree.title)}"
                        data-ntax="${fn:escapeXml(tree.nTax)}"
                        onclick="displayTree(this)">
                        <c:choose>
                            <c:when test="${not empty tree.label}">
                                <c:out value="${tree.label}"/>
                            </c:when>
                            <c:when test="${not empty tree.title}">
                                <c:out value="${tree.title}"/>
                            </c:when>
                            <c:otherwise>
                                Tree <c:out value="${tree.id}"/>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ol>
        </fieldset>
        
        <fieldset>
            <legend>Node Info</legend>
            <div id="node-info">
                Hover over or click a node to view its information here.
            </div>
        </fieldset>
        
        <c:if test="${treeBlockID != null}">
            <fieldset class="quick-links">
                <legend>Quick Links</legend>
                <p>
                    <a href="/treebase-web/search/study/trees.html?id=${studyID}">
                        <img class="iconButton" src="<fmt:message key="icons.trees"/>" alt="Trees"/>
                        Containing tree set
                    </a>
                </p>
                <p>
                    <a href="/treebase-web/search/study/summary.html?id=${studyID}">
                        <img class="iconButton" src="<fmt:message key="icons.citation"/>" alt="Study"/>
                        Containing study
                    </a>
                </p>
            </fieldset>
        </c:if>
        
        <fieldset>
            <legend>View Controls</legend>
            <div class="controls">
                <button onclick="resetView()">Reset View</button>
                <button onclick="toggleRadial()">Toggle Radial</button>
            </div>
        </fieldset>
    </div>
</div>

<script type="text/javascript">
var currentTree = null;
var currentElement = null;
var isRadial = false;

// HTML escape function to prevent XSS
function escapeHtml(text) {
    if (text === null || text === undefined) return '';
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function displayTree(element) {
    // Mark selected
    document.querySelectorAll('#tree-list li').forEach(function(li) {
        li.classList.remove('selected');
    });
    element.classList.add('selected');
    currentElement = element;
    
    var newick = element.getAttribute('data-newick');
    var treeId = element.getAttribute('data-id');
    var label = element.getAttribute('data-label');
    var title = element.getAttribute('data-title');
    var ntax = parseInt(element.getAttribute('data-ntax')) || 50;
    
    if (!newick || newick.trim() === '') {
        document.getElementById('tree-container').innerHTML = 
            '<div id="loading">No Newick string available for this tree.</div>';
        return;
    }
    
    // Clear container
    document.getElementById('tree-container').innerHTML = '';
    
    try {
        // Calculate dimensions based on number of taxa
        var height = Math.max(400, ntax * 18);
        var width = 700;
        
        // Create phylotree instance using the correct API
        // phylotree v2.x API: new phylotree.phylotree(newick)
        currentTree = new phylotree.phylotree(newick);
        
        // Render the tree
        currentTree.render({
            container: "#tree-container",
            width: width,
            height: height,
            "left-offset": 20,
            "show-scale": true,
            "align-tips": false,
            "layout": isRadial ? "radial" : "left-to-right",
            zoom: true,
            brush: false,
            collapsible: true,
            selectable: true,
            "node-styler": function(el, node) {
                // Add click handler for node info
                el.on("click", function() {
                    showNodeInfo(node);
                });
                el.on("mouseover", function() {
                    showNodeInfo(node);
                });
            }
        });
        
        // Update node info with tree metadata
        updateTreeInfo(treeId, label, title, ntax);
        
    } catch (e) {
        console.error("Error rendering tree:", e);
        var errorMsg = escapeHtml(e.message);
        document.getElementById('tree-container').innerHTML = 
            '<div id="loading">Error rendering tree: ' + errorMsg + '</div>';
    }
}

function showNodeInfo(node) {
    var name = escapeHtml(node.data.name || 'Internal node');
    var info = '<strong>Node:</strong> ' + name + '<br/>';
    if (node.data.attribute !== undefined) {
        info += '<strong>Branch length:</strong> ' + escapeHtml(String(node.data.attribute)) + '<br/>';
    }
    if (node.children && node.children.length > 0) {
        info += '<strong>Type:</strong> Internal node<br/>';
        info += '<strong>Children:</strong> ' + node.children.length;
    } else {
        info += '<strong>Type:</strong> Leaf node (tip)';
    }
    document.getElementById('node-info').innerHTML = info;
}

function updateTreeInfo(treeId, label, title, ntax) {
    var info = '<strong>Tree ID:</strong> ' + escapeHtml(treeId) + '<br/>';
    if (label) info += '<strong>Label:</strong> ' + escapeHtml(label) + '<br/>';
    if (title) info += '<strong>Title:</strong> ' + escapeHtml(title) + '<br/>';
    if (ntax) info += '<strong>Taxa:</strong> ' + escapeHtml(String(ntax));
    document.getElementById('node-info').innerHTML = info;
}

function resetView() {
    // Re-render the currently selected tree
    if (currentElement) {
        displayTree(currentElement);
    }
}

function toggleRadial() {
    isRadial = !isRadial;
    if (currentElement) {
        displayTree(currentElement);
    }
}

// Auto-select first tree on page load
document.addEventListener('DOMContentLoaded', function() {
    var firstTree = document.querySelector('#tree-list li');
    if (firstTree) {
        displayTree(firstTree);
    }
});
</script>

</body>
</html>
