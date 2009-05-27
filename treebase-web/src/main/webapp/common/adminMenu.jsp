<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="StudyManagementMenu"/>
    <menu:displayMenu name="UserManagementMenu"/>
</menu:useMenuDisplayer>
</div>
</div>
</div>
</div>
<script type="text/javascript">
    initializeMenus();
</script>
