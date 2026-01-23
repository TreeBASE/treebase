<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Tree Viewer</title>

<!--
    JavaScript Library Isolation Layer
    
    This script must run BEFORE D3.js and phylotree.js are loaded.
    It saves references to native JavaScript methods that prototype.js
    may override. If prototype.js is detected later (e.g., injected by
    decorators or other includes), we can restore the native methods.
    
    The main issue is that prototype.js modifies Array.prototype with
    methods that can interfere with D3.js's internal array operations,
    causing NaN values in SVG coordinate calculations.
-->
<script type="text/javascript">
(function() {
    'use strict';
    
    // Save references to native Array methods before any libraries load
    var nativeArrayMethods = {
        forEach: Array.prototype.forEach,
        map: Array.prototype.map,
        filter: Array.prototype.filter,
        reduce: Array.prototype.reduce,
        reduceRight: Array.prototype.reduceRight,
        indexOf: Array.prototype.indexOf,
        lastIndexOf: Array.prototype.lastIndexOf,
        every: Array.prototype.every,
        some: Array.prototype.some,
        find: Array.prototype.find,
        findIndex: Array.prototype.findIndex,
        includes: Array.prototype.includes,
        flat: Array.prototype.flat,
        flatMap: Array.prototype.flatMap,
        entries: Array.prototype.entries,
        keys: Array.prototype.keys,
        values: Array.prototype.values
    };
    
    // Save native Object methods
    var nativeObjectMethods = {
        keys: Object.keys,
        values: Object.values,
        entries: Object.entries,
        assign: Object.assign
    };
    
    // Save native String methods
    var nativeStringMethods = {
        trim: String.prototype.trim,
        trimStart: String.prototype.trimStart,
        trimEnd: String.prototype.trimEnd,
        includes: String.prototype.includes,
        startsWith: String.prototype.startsWith,
        endsWith: String.prototype.endsWith,
        repeat: String.prototype.repeat,
        padStart: String.prototype.padStart,
        padEnd: String.prototype.padEnd
    };
    
    // Function to check if Prototype.js has been loaded
    window.isPrototypeJsLoaded = function() {
        return typeof Prototype !== 'undefined' || 
               typeof $$ === 'function' ||
               (Array.prototype._each !== undefined);
    };
    
    // Function to restore native methods if needed
    window.restoreNativeMethods = function() {
        // Restore Array methods
        for (var method in nativeArrayMethods) {
            if (nativeArrayMethods.hasOwnProperty(method) && nativeArrayMethods[method]) {
                Array.prototype[method] = nativeArrayMethods[method];
            }
        }
        
        // Restore Object methods
        for (var method in nativeObjectMethods) {
            if (nativeObjectMethods.hasOwnProperty(method) && nativeObjectMethods[method]) {
                Object[method] = nativeObjectMethods[method];
            }
        }
        
        // Restore String methods
        for (var method in nativeStringMethods) {
            if (nativeStringMethods.hasOwnProperty(method) && nativeStringMethods[method]) {
                String.prototype[method] = nativeStringMethods[method];
            }
        }
        
        console.log('TreeViewer: Native JavaScript methods restored');
    };
    
    // Run restoration after DOM is ready (in case prototype.js loads later)
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            if (window.isPrototypeJsLoaded()) {
                console.warn('TreeViewer: Prototype.js detected! Restoring native methods for D3.js compatibility.');
                window.restoreNativeMethods();
            }
        });
    }
    
    console.log('TreeViewer: JavaScript isolation layer initialized');
})();
</script>

<!-- D3.js v7 for phylotree.js with SRI hash for integrity verification -->
<script src="https://cdn.jsdelivr.net/npm/d3@7" 
        integrity="sha384-u60Dv4QEDY4Y/TLJqrB+Ls+FBLvWJh8lKJ1iRuLFqoYl0dGAGW4sAVzx86g4cH2N" 
        crossorigin="anonymous"></script>
<!-- phylotree.js from unpkg CDN with SRI hash -->
<script src="https://unpkg.com/phylotree@1.1.1/dist/phylotree.js"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://unpkg.com/phylotree@1.1.1/dist/phylotree.css"
      crossorigin="anonymous">

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

/* phylotree styling */
.phylotree-container .branch {
  fill: none;
  stroke: #999;
  stroke-width: 2px;
}

.phylotree-container .node circle {
  fill: #fff;
  stroke: steelblue;
  stroke-width: 1.5px;
}

.phylotree-container .internal-node circle {
  fill: #ccc;
}

.phylotree-container .node text {
  font: 10px sans-serif;
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
                    <li data-newick="${tree.newickString}" 
                        data-id="${tree.id}"
                        data-label="${tree.label}"
                        data-title="${tree.title}"
                        data-ntax="${tree.nTax}"
                        onclick="displayTree(this)">
                        <c:choose>
                            <c:when test="${not empty tree.label}">
                                ${tree.label}
                            </c:when>
                            <c:when test="${not empty tree.title}">
                                ${tree.title}
                            </c:when>
                            <c:otherwise>
                                Tree ${tree.id}
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
                    <a href="/treebase-web/search/study/trees.html?id=${studyID}" target="_blank">
                        <img class="iconButton" src="<fmt:message key="icons.trees"/>" alt="Trees"/>
                        Containing tree set
                    </a>
                </p>
                <p>
                    <a href="/treebase-web/search/study/summary.html?id=${studyID}" target="_blank">
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
            </div>
        </fieldset>
    </div>
</div>

<script type="text/javascript">
var currentTree = null;
var currentElement = null;

/**
 * Check for Prototype.js conflicts and restore native methods if needed
 * This is called before rendering any tree to ensure D3.js works correctly
 */
function ensureNativeMethods() {
    if (window.isPrototypeJsLoaded && window.isPrototypeJsLoaded()) {
        console.warn('TreeViewer: Prototype.js detected before tree render - restoring native methods');
        if (window.restoreNativeMethods) {
            window.restoreNativeMethods();
        }
    }
}

/**
 * Check rendered SVG for NaN values which indicate coordinate calculation errors
 * @param {Element} container - The container element with the SVG
 * @returns {Object} Object with hasNaN boolean and count of affected elements
 */
function checkForNaNInSVG(container) {
    var svg = container.querySelector('svg');
    if (!svg) return { hasNaN: false, count: 0 };
    
    var nanElements = [];
    
    // Check path elements for NaN in d attribute
    var paths = svg.querySelectorAll('path');
    paths.forEach(function(path) {
        var d = path.getAttribute('d');
        if (d && d.indexOf('NaN') !== -1) {
            nanElements.push({ type: 'path', element: path, attr: 'd' });
        }
    });
    
    // Check transforms for NaN
    var transformed = svg.querySelectorAll('[transform]');
    transformed.forEach(function(el) {
        var transform = el.getAttribute('transform');
        if (transform && transform.indexOf('NaN') !== -1) {
            nanElements.push({ type: el.tagName, element: el, attr: 'transform' });
        }
    });
    
    // Check circles and other elements for NaN in position attributes
    var positioned = svg.querySelectorAll('[cx], [cy], [x], [y], [x1], [y1], [x2], [y2]');
    positioned.forEach(function(el) {
        var attrs = ['cx', 'cy', 'x', 'y', 'x1', 'y1', 'x2', 'y2'];
        attrs.forEach(function(attr) {
            var val = el.getAttribute(attr);
            if (val && (val === 'NaN' || isNaN(parseFloat(val)))) {
                nanElements.push({ type: el.tagName, element: el, attr: attr });
            }
        });
    });
    
    return {
        hasNaN: nanElements.length > 0,
        count: nanElements.length,
        elements: nanElements
    };
}

function displayTree(element) {
    // Ensure native methods before rendering
    ensureNativeMethods();
    
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
    var container = document.getElementById('tree-container');
    container.innerHTML = '';
    
    try {
        // Calculate dimensions based on number of taxa
        var height = Math.max(400, ntax * 15);
        var width = 800;
        
        // Create phylotree instance
        currentTree = new phylotree.phylotree(newick);
        
        // Render the tree
        currentTree.render({
            container: "#tree-container",
            width: width,
            height: height,
            "left-offset": 10,
            "show-scale": true,
            "align-tips": false,
            "node-styler": function(element, node) {
                element.on("mouseover", function() {
                    showNodeInfo(node);
                });
            },
            "edge-styler": function(element, edge) {
                element.style("stroke-width", "2px");
            }
        });
        
        // Check for NaN values in the rendered SVG
        var nanCheck = checkForNaNInSVG(container);
        if (nanCheck.hasNaN) {
            console.error('TreeViewer: NaN values detected in SVG coordinates (' + nanCheck.count + ' elements affected)');
            console.error('TreeViewer: This is likely caused by JavaScript library conflicts.');
            console.error('TreeViewer: Affected elements:', nanCheck.elements);
            
            // Check if Prototype.js is present
            if (window.isPrototypeJsLoaded && window.isPrototypeJsLoaded()) {
                console.error('TreeViewer: Prototype.js is loaded - this is the likely cause of the issue.');
                
                // Attempt to recover by restoring methods and re-rendering
                console.log('TreeViewer: Attempting recovery by restoring native methods and re-rendering...');
                if (window.restoreNativeMethods) {
                    window.restoreNativeMethods();
                }
                
                // Clear and retry once
                container.innerHTML = '';
                currentTree = new phylotree.phylotree(newick);
                currentTree.render({
                    container: "#tree-container",
                    width: width,
                    height: height,
                    "left-offset": 10,
                    "show-scale": true,
                    "align-tips": false
                });
                
                // Check again
                var retryCheck = checkForNaNInSVG(container);
                if (retryCheck.hasNaN) {
                    container.innerHTML = '<div id="loading" style="color: red;">' +
                        '<strong>Error:</strong> Unable to render tree due to JavaScript library conflicts.<br/>' +
                        'NaN values detected in ' + retryCheck.count + ' SVG elements.<br/>' +
                        'Please try refreshing the page or contact support.</div>';
                    return;
                } else {
                    console.log('TreeViewer: Recovery successful - tree rendered correctly after restoring native methods');
                }
            }
        }
        
        // Update node info with tree metadata
        updateTreeInfo(treeId, label, title, ntax);
        
    } catch (e) {
        console.error("Error rendering tree:", e);
        document.getElementById('tree-container').innerHTML = 
            '<div id="loading" style="color: red;">Error rendering tree: ' + e.message + '</div>';
    }
}

function showNodeInfo(node) {
    var info = '<strong>Node:</strong> ' + (node.data.name || 'Internal node') + '<br/>';
    if (node.data.attribute !== undefined) {
        info += '<strong>Branch length:</strong> ' + node.data.attribute + '<br/>';
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
    var info = '<strong>Tree ID:</strong> ' + treeId + '<br/>';
    if (label) info += '<strong>Label:</strong> ' + label + '<br/>';
    if (title) info += '<strong>Title:</strong> ' + title + '<br/>';
    if (ntax) info += '<strong>Taxa:</strong> ' + ntax;
    document.getElementById('node-info').innerHTML = info;
}

function resetView() {
    // Re-render the currently selected tree
    if (currentElement) {
        displayTree(currentElement);
    }
}

// Auto-select first tree on page load
document.addEventListener('DOMContentLoaded', function() {
    // Final check for prototype.js and restore if needed
    ensureNativeMethods();
    
    var firstTree = document.querySelector('#tree-list li');
    if (firstTree) {
        displayTree(firstTree);
    }
});
</script>

</body>
</html>
