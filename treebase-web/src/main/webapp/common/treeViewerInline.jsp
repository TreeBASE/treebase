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

<!-- D3.js v7 - local copy -->
<script src="/treebase-web/scripts/d3.min.js"></script>
<!-- lodash - required by phylotree.js -->
<script src="/treebase-web/scripts/lodash.js"></script>
<!-- underscore - required by phylotree.js -->
<script src="/treebase-web/scripts/underscore.js"></script>
<!-- phylotree.js v2.4.0 - local copy -->
<script src="/treebase-web/scripts/phylotree.js"></script>
<!-- phylotree.css -->
<link rel="stylesheet" href="/treebase-web/styles/phylotree.css" type="text/css">

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

/* Collapsed state */
.inline-tree-viewer.collapsed .inline-tree-viewer-content {
    display: none;
}

/* phylotree overrides */
.inline-tree-viewer .node text {
    font-size: 10px;
}
</style>

<div id="inline-tree-viewer" class="inline-tree-viewer">
    <div class="inline-tree-viewer-header">
        <h3><c:out value="${NEWICKSTRINGNAME}" default="Tree Viewer"/></h3>
        <div class="inline-tree-viewer-controls">
            <button onclick="inlineTreeViewer.resetView()">Reset View</button>
            <button onclick="inlineTreeViewer.toggleRadial()">Toggle Radial</button>
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
                        <li data-newick="${fn:escapeXml(tree.newickString)}" 
                            data-id="${fn:escapeXml(tree.id)}"
                            data-label="${fn:escapeXml(tree.label)}"
                            data-title="${fn:escapeXml(tree.title)}"
                            data-ntax="${fn:escapeXml(tree.nTax)}"
                            onclick="inlineTreeViewer.displayTree(this)">
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
    isRadial: false,
    isCladogram: false,
    
    // HTML escape function to prevent XSS
    escapeHtml: function(text) {
        if (text === null || text === undefined) return '';
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },
    
    /**
     * Check if a parsed phylotree has no branch lengths (i.e., it's a cladogram).
     * Traverses the tree and checks if all branch lengths are undefined or zero.
     */
    checkIfCladogram: function(tree) {
        var hasBranchLengths = false;
        tree.traverse_and_compute(function(node) {
            if (node.data && node.data.attribute !== undefined && 
                node.data.attribute !== null && node.data.attribute !== '' &&
                !isNaN(parseFloat(node.data.attribute)) && parseFloat(node.data.attribute) > 0) {
                hasBranchLengths = true;
            }
        }, "pre-order");
        return !hasBranchLengths;
    },
    
    /**
     * Convert a cladogram to an ultrametric tree by:
     * 1. Assigning unit branch lengths (1.0) to all branches
     * 2. Computing the depth of each node from the root
     * 3. Extending terminal branches so all tips are at the same distance from root
     */
    makeUltrametric: function(tree) {
        // First pass: assign unit branch lengths to all nodes
        // NOTE: phylotree.js expects attribute to be a STRING, not a number
        // The defBranchLengthAccessor checks attribute.length which only works for strings
        tree.traverse_and_compute(function(node) {
            if (node.parent) { // Non-root nodes get branch length of 1
                node.data.attribute = "1";
            } else {
                // Root node needs attribute "0" for phylotree.js compatibility
                // (defBranchLengthAccessor checks for attribute.length)
                node.data.attribute = "0";
            }
        }, "pre-order");
        
        // Second pass: compute depth from root for each node
        tree.traverse_and_compute(function(node) {
            if (!node.parent) {
                node.data._depth = 0;
            } else {
                var parentDepth = node.parent.data._depth || 0;
                var branchLength = parseFloat(node.data.attribute) || 1;
                node.data._depth = parentDepth + branchLength;
            }
        }, "pre-order");
        
        // Find the maximum depth (deepest tip)
        var maxDepth = 0;
        tree.traverse_and_compute(function(node) {
            if (!node.children || node.children.length === 0) {
                // This is a tip/leaf node
                if (node.data._depth > maxDepth) {
                    maxDepth = node.data._depth;
                }
            }
        }, "pre-order");
        
        // Third pass: extend terminal branches to make all tips reach maxDepth
        tree.traverse_and_compute(function(node) {
            if (!node.children || node.children.length === 0) {
                // This is a tip/leaf node - extend its branch
                var currentDepth = node.data._depth;
                var extension = maxDepth - currentDepth;
                if (extension > 0) {
                    // Ensure attribute is a string for phylotree.js compatibility
                    node.data.attribute = String((parseFloat(node.data.attribute) || 1) + extension);
                }
            }
            // Clean up temporary depth property
            delete node.data._depth;
        }, "pre-order");
        
        return tree;
    },
    
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
            var height = Math.max(350, ntax * 15);
            var width = 550;
            
            // Create phylotree instance - parse the Newick string
            this.currentTree = new phylotree.phylotree(newick);
            
            // Check if this is a cladogram (no branch lengths)
            this.isCladogram = this.checkIfCladogram(this.currentTree);
            
            if (this.isCladogram) {
                console.log("Detected cladogram (no branch lengths), converting to ultrametric representation");
                // Convert to ultrametric by adding branch lengths and extending terminal branches
                this.makeUltrametric(this.currentTree);
                
                // Add cladogram notice
                var notice = document.createElement('div');
                notice.id = 'inline-cladogram-notice';
                notice.style.cssText = 'background: #fff3cd; border: 1px solid #ffc107; padding: 8px 12px; margin-bottom: 10px; border-radius: 4px; font-size: 11px; color: #856404;';
                notice.innerHTML = '<strong>Note:</strong> This tree is a cladogram (no branch lengths). It is displayed as an ultrametric tree with equal internal branch lengths for visualization purposes.';
                document.getElementById('inline-tree-container').appendChild(notice);
            }
            
            // Render the tree - this creates a TreeRender object
            var self = this;
            var renderer = this.currentTree.render({
                container: "#inline-tree-container",
                width: width,
                height: height,
                "left-offset": 20,
                "show-scale": !this.isCladogram, // Don't show scale for cladograms
                "align-tips": this.isCladogram,  // Align tips for cladograms
                "layout": this.isRadial ? "radial" : "left-to-right",
                zoom: true,
                brush: false,
                collapsible: true,
                selectable: true,
                "node-styler": function(el, node) {
                    el.on("click", function() {
                        self.showNodeInfo(node);
                    });
                    el.on("mouseover", function() {
                        self.showNodeInfo(node);
                    });
                }
            });
            
            // Manually append the SVG to the container
            // The render() method creates the SVG but doesn't append it automatically
            var container = document.querySelector("#inline-tree-container");
            container.appendChild(renderer.show());
            
            // Update node info with tree metadata
            this.updateTreeInfo(treeId, label, title, ntax);
            
        } catch (e) {
            console.error("Error rendering tree:", e);
            var errorMsg = this.escapeHtml(e.message);
            document.getElementById('inline-tree-container').innerHTML = 
                '<div class="inline-tree-loading">Error rendering tree: ' + errorMsg + '</div>';
        }
    },
    
    showNodeInfo: function(node) {
        var name = this.escapeHtml(node.data.name || 'Internal node');
        var info = '<strong>Node:</strong> ' + name + '<br/>';
        // Don't show branch length for cladograms since it's artificial
        if (!this.isCladogram && node.data.attribute !== undefined) {
            info += '<strong>Branch length:</strong> ' + this.escapeHtml(String(node.data.attribute)) + '<br/>';
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
        var info = '<strong>Tree ID:</strong> ' + this.escapeHtml(treeId) + '<br/>';
        if (label) info += '<strong>Label:</strong> ' + this.escapeHtml(label) + '<br/>';
        if (title) info += '<strong>Title:</strong> ' + this.escapeHtml(title) + '<br/>';
        if (ntax) info += '<strong>Taxa:</strong> ' + this.escapeHtml(String(ntax));
        document.getElementById('inline-node-info').innerHTML = info;
    },
    
    resetView: function() {
        if (this.currentElement) {
            this.displayTree(this.currentElement);
        }
    },
    
    toggleRadial: function() {
        this.isRadial = !this.isRadial;
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
