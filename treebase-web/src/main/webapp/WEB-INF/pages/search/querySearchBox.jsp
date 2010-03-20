<%@ include file="/common/taglibs.jsp"%>

<a onclick="TreeBASE.collapseExpand('CQLBox','block',this)"
	style="border:none"
	title="collapse">
		<img 
			class="iconButton" 
			src="<fmt:message key="icons.expand"/>"
			style="vertical-align:middle" 
			alt="collapse" /> Advanced search...
</a>
<fieldset id="CQLBox" style="display:none">
	<legend>
		CQL query testing
	</legend>
	<p>
		The TreeBASE website can be searched using a subset of constructs from the 
		<a href="http://www.loc.gov/standards/sru/specs/cql.html">CQL</a> specification. For
		more information on how this is used, visit the 
		<a href="/treebase-web/urlAPI.html">TreeBASE help page about searching</a>.
	</p>
	<div>
		<textarea name="query" style="width:100%"></textarea><br/>
		<input type="submit" style="width:100%" value="Evaluate query"/>
	</div>
</fieldset>
