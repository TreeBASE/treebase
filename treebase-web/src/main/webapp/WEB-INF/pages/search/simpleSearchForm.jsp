
	<form onsubmit="return TreeBASE.redirect(phyloWSURI + escape($('expanded').value));">
		<input type="text" id="query" onchange="return TreeBASE.expandQuery();" class="textCell" style="width:150px" />
		<input type="submit" onclick="return TreeBASE.redirect(phyloWSURI + escape($('expanded').value));" value="Go"/><br/>
		<input type="radio" name="join" onclick="TreeBASE.expandQuery(); return true;" id="all" value="and"/> All
		<input type="radio" name="join" onclick="TreeBASE.expandQuery(); return true;" value="or" checked="checked"/> Any<br/>
		<a onclick="TreeBASE.collapseExpand('expanded','block',this)" id="expander">
			<img src="http://www.treebase.org/treebase-web/images/plus.gif"/>
		  	<small style="color:silver">Advanced search</small>
		</a>
		<textarea id="expanded" style="display:none;width:500px;height:300px"></textarea><br/>
	</form>