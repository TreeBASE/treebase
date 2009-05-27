<%@ include file="/common/taglibs.jsp"%>

  <form id="topology3" method="post">
  <fieldset>
  <input type="hidden" name="formName" value="topology3"/>
  <legend>Search for trees with this topology
	  		<a href="#" class="openHelp" onclick="openHelp('treeTopology3SearchForm')">
	  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
	  		</a>   
  </legend>
  <table cellspacing=0 cellpadding=0>
  <tr><td>&nbsp;</td><td><img src="../images/lines/DR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_a" value="" /></td></tr>
  <tr><td><img src="../images/lines/DR.gif"/></td><td><img src="../images/lines/ULD.gif"/></td><td>&nbsp;</td></tr>
  <tr><td><img src="../images/lines/UD.gif"/></td><td><img src="../images/lines/UR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_b" value="" /></td><td>&nbsp;</td></tr>
  <tr><td><img src="../images/lines/ULD.gif"/></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
  <tr><td><img src="../images/lines/UR.gif"/></td><td><img src="../images/lines/LR.gif"/></td><td><input type="text" class="textCell" style="width:150px"  name="taxon_c" value="" /></td><td>&nbsp;</td></tr>
 
  </table>
  <input type=submit value="Search"/>
  </fieldset>
   </form>

   