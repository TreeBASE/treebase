/*
 * CONSTANT DEFINITIONS
 */
// The ID of the tree input element.
var treeInputID = "treeText";
// The ID of the clipboard input element.
var clipInputID = "clipText";

/*
 * This function is called by Java to update the HTML's tree text.
 */
function updateTree(newtext)
{
  getObject(treeInputID).value = newtext;
  return;
}

function updateClip(newtext)
{
  getObject(clipInputID).value = newtext;
  return;
}

/*
 * This function calls Java's updateTree method to update PhyloWidget's
 * representation of the tree.
 */
function updateJavaTree()
{
  document.PhyloWidget.updateTree(getObject(treeInputID).value);
}

/*
 *  This function calls Java's updateClip method to update PhyloWidget's
 *  tree clipboard.
 */
function updateJavaClip()
{
  document.PhyloWidget.updateClip(getObject(clipInputID).value);
}

// Wrapper function to get an object from the DOM via its ID attribute.
function getObject(id) {
	var el = document.getElementById(id);
	return (el);
}


/*
 *  This function causes the newick input 
 */
var selected=false;
function selectOnce(el)
{
  if (selected)return;
  el.select();
  selected = true;
  document.getElementById("");
}

var simple="((a,b),c);";
var noname="(,(,,),);";
var greek="(Alpha,Beta,Gamma,Delta,,Epsilon,,,);";

/*
 *  This function loads a tree from a list into the treeText field
 */
function pickTree(){
	document.TreeForm.treeText.value = document.TreeForm.treeList.value;
	updateJavaTree();
}

