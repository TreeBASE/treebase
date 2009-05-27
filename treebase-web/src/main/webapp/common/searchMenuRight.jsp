<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter" >
    <menu:displayMenu name="Searches" />
 </menu:useMenuDisplayer>
</div>
</div>
</div>
</div>
<script type="text/javascript">
    initializeMenus();
    var menu = document.getElementById("menuDiv");
    if (menu != null) {
        openMenu("menuDiv");
    }
</script>
