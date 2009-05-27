<%@ include file="/common/taglibs.jsp"%>

<div style="float: right; width: 50%;">
  <form id="topology4" method="post">
  <fieldset>
  <input type="hidden" name="formName" value="topology4s"/>
  <legend>Search for trees with this topology
	  		<a href="#" class="openHelp" onclick="openHelp('treeTopology4sSearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>   
  </legend>
  <table cellspacing=0 cellpadding=0>
  
  <tr><td>&nbsp;</td><td>&nbsp;</td><td><img src="../images/lines/DR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_a"/></td>
  <tr><td>&nbsp;</td><td><img src="../images/lines/DR.gif"/></td><td><img src="../images/lines/ULD.gif"/></td>
  <tr><td>&nbsp;</td><td><img src="../images/lines/UD.gif"/></td><td><img src="../images/lines/UR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_b"/></td>
  <tr><td><img src="../images/lines/LR.gif"/></td><td><img src="../images/lines/ULD.gif"/></td><td>&nbsp;</td>
  <tr><td>&nbsp;</td><td><img src="../images/lines/UD.gif"/></td><td><img src="../images/lines/DR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_c"/></td>
  <tr><td>&nbsp;</td><td><img src="../images/lines/UR.gif"/></td><td><img src="../images/lines/ULD.gif"/></td>
  <tr><td>&nbsp;</td><td>&nbsp;</td><td><img src="../images/lines/UR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_d"/></td>
   </table>
  <input type=submit value="Search"/>
  </fieldset>
   </form>

  </div>