<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<a onclick="switchMenu();" style="cursor: pointer;"><img id="tb" src="../images/minus.gif;"/>&nbsp;&nbsp;Tool Box</a>
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

    function switchMenu() {
        var el = document.getElementById('menuDiv');
        var tb = document.getElementById('tb');
        if ( el.style.display != "none" ) {
            el.style.display = 'none';
            tb.src="../images/plus.gif";
                    }
        else {
            el.style.display = 'block';
            tb.src="../images/minus.gif";
                   }
    }
</script>
