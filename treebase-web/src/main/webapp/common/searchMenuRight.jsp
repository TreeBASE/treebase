<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<a onclick="switchMenu('menuDiv');" style="cursor: pointer;">[X]</a>
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

    function switchMenu(obj) {
        var el = document.getElementById(obj);
        if ( el.style.display != "none" ) {
            el.style.display = 'none';
        }
        else {
            el.style.display = '';
        }
    }
</script>
