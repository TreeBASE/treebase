<%--
    Inline Tree Viewer Component using phylotree.js
    
    This component renders an inline tree viewer that can be included in any page.
    It displays trees in Newick format using the phylotree.js library.
    
    Usage: Include this JSP fragment and provide a container div with id="inline-tree-viewer"
    
    Required session/request attributes:
    - TREELIST: List of PhyloTree objects to display
    - NEWICKSTRINGNAME: Optional title for the tree list
    
    Example:
    <%@ include file="/common/treeViewerInline.jsp" %>
--%>
<%@ include file="/common/taglibs.jsp" %>

<!-- D3.js v7 for phylotree.js -->
<script src="https://cdn.jsdelivr.net/npm/d3@7" 
        integrity="sha384-u60Dv4QEDY4Y/TLJqrB+Ls+FBLvWJh8lKJ1iRuLFqoYl0dGAGW4sAVzx86g4cH2N" 
        crossorigin="anonymous"></script>
<!-- phylotree.js from unpkg CDN -->
<script src="https://unpkg.com/phylotree@1.1.1/dist/phylotree.js"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://unpkg.com/phylotree@1.1.1/dist/phylotree.css"
      crossorigin="anonymous">

<style type="text/css">
.inline-tree-viewer {
    margin-top: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background: #fafafa;
}

.inline-tree-viewer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 15px;
    background: #f0f0f0;
    border-bottom: 1px solid #ccc;
}

.inline-tree-viewer-header h3 {
    margin: 0;
    font-size: 14px;
    color: #333;
}

.inline-tree-viewer-controls {
    display: flex;
    gap: 10px;
}

.inline-tree-viewer-controls button {
    padding: 5px 12px;
    font-size: 11px;
    cursor: pointer;
    border: 1px solid #ccc;
    border-radius: 3px;
    background: white;
}

.inline-tree-viewer-controls button:hover {
    background: #e8e8e8;
}

.inline-tree-viewer-content {
    display: flex;
    min-height: 400px;
}

.inline-tree-container {
    flex: 1;
    overflow: auto;
    padding: 10px;
    background: white;
}

.inline-tree-container svg {
    display: block;
}

.inline-tree-sidebar {
    width: 250px;
    border-left: 1px solid #ccc;
    background: #f5f5f5;
}

.inline-tree-sidebar fieldset {
    margin: 10px;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 3px;
    background: white;
}

.inline-tree-sidebar legend {
    padding: 0 5px;
    font-weight: bold;
    font-size: 11px;
    color: #666;
}

.inline-tree-list {
    list-style: decimal;
    margin: 0;
    padding: 0 0 0 20px;
    max-height: 200px;
    overflow-y: auto;
}

.inline-tree-list li {
    padding: 4px 0;
    cursor: pointer;
    border-bottom: 1px solid #eee;
    font-size: 11px;
}

.inline-tree-list li:hover {
    background: #e0e0e0;
}

.inline-tree-list li.selected {
    background: #CEE3F6;
    font-weight: bold;
}

.inline-node-info {
    font-size: 10px;
    line-height: 1.5;
}

.inline-tree-loading {
    padding: 40px;
    text-align: center;
    color: #666;
}

/* phylotree styling */
.inline-tree-viewer .phylotree-container .branch {
    fill: none;
    stroke: #999;
    stroke-width: 2px;
}

.inline-tree-viewer .phylotree-container .node circle {
    fill: #fff;
    stroke: steelblue;
    stroke-width: 1.5px;
}

.inline-tree-viewer .phylotree-container .internal-node circle {
    fill: #ccc;
}

.inline-tree-viewer .phylotree-container .node text {
    font: 10px sans-serif;
}

/* Collapsed state */
.inline-tree-viewer.collapsed .inline-tree-viewer-content {
    display: none;
}
</style>

<div id="inline-tree-viewer" class="inline-tree-viewer">
    <div class="inline-tree-viewer-header">
        <h3><c:out value="${NEWICKSTRINGNAME}" default="Tree Viewer"/></h3>
        <div class="inline-tree-viewer-controls">
            <button onclick="inlineTreeViewer.resetView()">Reset View</button>
            <button onclick="inlineTreeViewer.toggleViewer()">Toggle</button>
        </div>
    </div>
    
    <div class="inline-tree-viewer-content">
        <div id="inline-tree-container" class="inline-tree-container">
            <div class="inline-tree-loading">Select a tree from the list to view it.</div>
        </div>
        
        <div class="inline-tree-sidebar">
            <fieldset>
                <legend>Trees</legend>
                <ol id="inline-tree-list" class="inline-tree-list">
                    <c:forEach var="tree" items="${TREELIST}" varStatus="status">
                        <li data-newick="${tree.newickString}" 
                            data-id="${tree.id}"
                            data-label="${tree.label}"
                            data-title="${tree.title}"
                            data-ntax="${tree.nTax}"
                            onclick="inlineTreeViewer.displayTree(this)">
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
                <legend>Info</legend>
                <div id="inline-node-info" class="inline-node-info">
                    Hover over nodes for details.
                </div>
            </fieldset>
        </div>
    </div>
</div>

<script type="text/javascript">
var inlineTreeViewer = {
    currentTree: null,
    currentElement: null,
    
    displayTree: function(element) {
        // Mark selected
        document.querySelectorAll('#inline-tree-list li').forEach(function(li) {
            li.classList.remove('selected');
        });
        element.classList.add('selected');
        this.currentElement = element;
        
        var newick = element.getAttribute('data-newick');
        var treeId = element.getAttribute('data-id');
        var label = element.getAttribute('data-label');
        var title = element.getAttribute('data-title');
        var ntax = parseInt(element.getAttribute('data-ntax')) || 50;
        
        if (!newick || newick.trim() === '') {
            document.getElementById('inline-tree-container').innerHTML = 
                '<div class="inline-tree-loading">No Newick string available for this tree.</div>';
            return;
        }
        
        // Clear container
        document.getElementById('inline-tree-container').innerHTML = '';
        
        try {
            // Calculate dimensions based on number of taxa
            var height = Math.max(350, ntax * 12);
            var width = 600;
            
            // Create phylotree instance
            this.currentTree = new phylotree.phylotree(newick);
            
            // Render the tree
            var self = this;
            this.currentTree.render({
                container: "#inline-tree-container",
                width: width,
                height: height,
                "left-offset": 10,
                "show-scale": true,
                "align-tips": false,
                "node-styler": function(element, node) {
                    element.on("mouseover", function() {
                        self.showNodeInfo(node);
                    });
                },
                "edge-styler": function(element, edge) {
                    element.style("stroke-width", "2px");
                }
            });
            
            // Update node info with tree metadata
            this.updateTreeInfo(treeId, label, title, ntax);
            
        } catch (e) {
            console.error("Error rendering tree:", e);
            document.getElementById('inline-tree-container').innerHTML = 
                '<div class="inline-tree-loading">Error rendering tree: ' + e.message + '</div>';
        }
    },
    
    showNodeInfo: function(node) {
        var info = '<strong>Node:</strong> ' + (node.data.name || 'Internal node') + '<br/>';
        if (node.data.attribute !== undefined) {
            info += '<strong>Branch length:</strong> ' + node.data.attribute + '<br/>';
        }
        if (node.children && node.children.length > 0) {
            info += '<strong>Type:</strong> Internal node<br/>';
            info += '<strong>Children:</strong> ' + node.children.length;
        } else {
            info += '<strong>Type:</strong> Leaf (tip)';
        }
        document.getElementById('inline-node-info').innerHTML = info;
    },
    
    updateTreeInfo: function(treeId, label, title, ntax) {
        var info = '<strong>Tree ID:</strong> ' + treeId + '<br/>';
        if (label) info += '<strong>Label:</strong> ' + label + '<br/>';
        if (title) info += '<strong>Title:</strong> ' + title + '<br/>';
        if (ntax) info += '<strong>Taxa:</strong> ' + ntax;
        document.getElementById('inline-node-info').innerHTML = info;
    },
    
    resetView: function() {
        if (this.currentElement) {
            this.displayTree(this.currentElement);
        }
    },
    
    toggleViewer: function() {
        var viewer = document.getElementById('inline-tree-viewer');
        viewer.classList.toggle('collapsed');
    },
    
    init: function() {
        var firstTree = document.querySelector('#inline-tree-list li');
        if (firstTree) {
            this.displayTree(firstTree);
        }
    }
};

// Auto-initialize on page load
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        inlineTreeViewer.init();
    });
} else {
    inlineTreeViewer.init();
}
</script>
