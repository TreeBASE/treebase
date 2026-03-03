<%@ include file="/common/taglibs.jsp"%>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <span>Search for trees with this topology</span>
        <a href="#" class="openHelp" onclick="openHelp('treeTopology4sSearchForm')">
            <i class="fa fa-question-circle fa-icon"></i> Help
        </a>
    </div>
    <div class="card-body">
        <form id="topology4" method="post">
            <input type="hidden" name="formName" value="topology4s"/>
            <table class="mb-3" style="border-collapse: collapse;">
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/DR.gif" alt=""/></td>
                    <td class="line"><input type="text" class="form-control" style="width:150px" name="taxon_a" placeholder="Taxon A"/></td>
                </tr>
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/DR.gif" alt=""/></td>
                    <td class="line"><img src="../images/lines/ULD.gif" alt=""/></td>
                </tr>
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/UD.gif" alt=""/></td>
                    <td class="line"><img src="../images/lines/UR.gif" alt=""/></td>
                    <td class="line"><input type="text" class="form-control" style="width:150px" name="taxon_b" placeholder="Taxon B"/></td>
                </tr>
                <tr>
                    <td class="line"><img src="../images/lines/LR.gif" alt=""/></td>
                    <td class="line"><img src="../images/lines/ULD.gif" alt=""/></td>
                    <td class="line">&nbsp;</td>
                </tr>
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/UD.gif" alt=""/></td>
                    <td class="line"><img src="../images/lines/DR.gif" alt=""/></td>
                    <td class="line"><input type="text" class="form-control" style="width:150px" name="taxon_c" placeholder="Taxon C"/></td>
                </tr>
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/UR.gif" alt=""/></td>
                    <td class="line"><img src="../images/lines/ULD.gif" alt=""/></td>
                </tr>
                <tr>
                    <td class="line">&nbsp;</td>
                    <td class="line">&nbsp;</td>
                    <td class="line"><img src="../images/lines/UR.gif" alt=""/></td>
                    <td class="line"><input type="text" class="form-control" style="width:150px" name="taxon_d" placeholder="Taxon D"/></td>
                </tr>
            </table>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">Search</button>
            </div>
        </form>
    </div>
</div>