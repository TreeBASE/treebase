var newwindow

function popup_new_window(url) 
{
    newwindow=window.open(url,'help','width=800,height=400,scrollbars=yes,menubar=no,resizable=yes,toolbar=no,status=no');
}
function popup(url) {
    if (newwindow)
	newwindow.close();
	
    newwindow=window.open(url,'help','width=600,height=400,scrollbars=yes,menubar=no,resizable=yes,toolbar=no,status=no');
    newwindow.moveTo(220, 100)
}
function popupWithSizes(url, width, height) {
    if (newwindow) newwindow.close();
    newwindow=window.open(url,'help','width=' + width + ',height=' + height + ',scrollbars=yes,menubar=no,resizable=yes,toolbar=no,status=no');
}
function openHelp(tag) {
	var url = '/treebase-web/help.html?helpTag=' + tag;	
	var req = new Ajax.Request(url, {
		'method':'get',
		'onSuccess':function(response){				
			top.consoleRef=window.open('','help',
		  		'width=400,height=350'
		   		+',menubar=no'
		   		+',toolbar=no'
		   		+',location=no'
		   		+',status=no'
		   		+',top=100'
		   		+',left=100'
		   		+',scrollbars=yes'
		   		+',resizable=yes');	
		   	if ( top.consoleRef == null || top.consoleRef.closed ) {
		   		alert("Couldn't open window! The help system requires that popups are allowed for the TreeBASE site.");
		   	}
		 	top.consoleRef.document.writeln(response.responseText);
		 	top.consoleRef.document.close();				
		}
	});	
}

/*
	The TreeBASE object has three functions:
	1. it provides for a way to scope our javascript code within the TreeBASE
	namespace so it doesn't clash with all the other libraries we're using
	2. it allows you to register callbacks with it
	3. it implements a TreeBASE.initialize() method that executes those 
	registered callbacks. Typically we would call this from within the
	body tag, i.e. <body onload="TreeBASE.initialize()"/>, as is done in the
	main template. This allows us to write more unobtrusive javascript.
*/
var TreeBASE = {
	'init' : [],
	'initialize' : function () {
		for ( var i = 0; i < TreeBASE.init.length; i++ ) {
			TreeBASE.init[i]();
		}
	},
	'register' : function(func) {
		TreeBASE.init.push(func);
	}
};

/*
	Here we register a function that fetches all table headers for columns that
	contain icon buttons (i.e. of class "iconColumn"). It sets the background of
	those header cells to pink, and places a link to the icon legend in the first
	of these header cells. 
*/
TreeBASE.register( 
	function () {
		var headers = document.getElementsByTagName('th');
		var iconColumnHeaders = new Array();
		for ( var i = 0; i < headers.length; i++ ) {
			if ( headers[i].className == 'iconColumn' ) {
				iconColumnHeaders.push(headers[i]);
			}
		}
		if ( iconColumnHeaders.length > 0 ) {
			iconColumnHeaders[0].innerHTML = '<a href="/treebase-web/help/iconLegend.jsp"><img src="/treebase-web/images/icons/help.png" class="iconButton" alt="Icon Legend" title="Icon Legend"/></a>';
			for ( var i = 0; i < iconColumnHeaders.length; i++ ) {
				iconColumnHeaders[i].style.backgroundColor = 'pink';
			}
		}
	}
);

/* 
	Here we register a function that fetches all input elements for text of class
	"textCell", to which we add the following behaviours:
	1. onfocus (i.e. we click in the input) it selects the text it contains
	2. onfocus we change the border to soft blue
	3. onblur (i.e. we leave the input) we go back to the previous border
*/

TreeBASE.register(
	function() {
		var inputs = $$('.textCell');
		for ( var i = 0; i < inputs.length; i++ ) {
			if ( inputs[i] ) {
				var currentColor = inputs[i].style.borderColor;
				inputs[i].onfocus = function () {
					this.style.borderColor = '#3863A4';
					this.select();
				}
				inputs[i].onblur = function () {
					this.style.borderColor = currentColor;
				}
			}
		}
	}
);

TreeBASE.register(
	function () {
	    if ( document.getElementsByClassName ) {
            var checkBoxCells = $$('.checkBoxColumn');
            var buttonContainer = $('buttonContainer');
            if ( checkBoxCells.length > 0 && buttonContainer ) {
                var checkButton = new Element('input',{'type':'button','value':'Check all'});
                checkButton.observe('click',function () {
                    for ( var i = 0; i < checkBoxCells.length; i++ ) {					
                        if ( checkBoxCells[i] ) {
                            var checkBoxes = $(checkBoxCells[i]).select('input');
                            for ( var j = 0; j < checkBoxes.length; j++ ) {
                                if ( checkBoxes[j] && checkBoxes[j].type == 'checkbox' || checkBoxes[j].type == 'radio' ) {
                                    if ( ! checkBoxes[j].disabled ) {
                                        checkBoxes[j].checked = 'checked';
                                    }					
                                }
                            }		
                        }
                    }
                });
                var uncheckButton = new Element('input',{'type':'button','value':'Uncheck all'});	
                uncheckButton.observe('click',function () {
                    for ( var i = 0; i < checkBoxCells.length; i++ ) {
                        if ( checkBoxCells[i] ) {
                            var checkBoxes = $(checkBoxCells[i]).select('input');
                            for ( var j = 0; j < checkBoxes.length; j++ ) {
                                if ( checkBoxes[j] && checkBoxes[j].type == 'checkbox' || checkBoxes[j].type == 'radio' ) {
                                    if ( ! checkBoxes[j].disabled ) {
                                        checkBoxes[j].checked = null;	
                                    }				
                                }
                            }							
                        }
                    }
                });			
                var invertButton = new Element('input',{'type':'button','value':'Invert'});
                invertButton.observe('click',function () {
                    for ( var i = 0; i < checkBoxCells.length; i++ ) {
                        if ( checkBoxCells[i] ) {
                            var checkBoxes = $(checkBoxCells[i]).select('input');
                            for ( var j = 0; j < checkBoxes.length; j++ ) {
                                if ( checkBoxes[j] && checkBoxes[j].type == 'checkbox' || checkBoxes[j].type == 'radio' ) {
                                    if ( ! checkBoxes[j].disabled ) {
                                        if ( checkBoxes[j].checked ) {
                                            checkBoxes[j].checked = null;
                                        }					
                                        else {
                                            checkBoxes[j].checked = 'checked';
                                        }
                                    }
                                }
                            }							
                        }
                    }
                });	
                buttonContainer.insertBefore(invertButton,buttonContainer.firstChild);
                buttonContainer.insertBefore(uncheckButton,buttonContainer.firstChild);	
                buttonContainer.insertBefore(checkButton,buttonContainer.firstChild);	
            }
		}
	}
);

/* add a tooltip for the help buttons */
TreeBASE.register(
	function() {
		var links = document.getElementsByTagName('a');
		for ( var i = 0; i < links.length; i++ ) {
			var link = $(links[i]);
			if ( link.hasClassName('openHelp') ) {
				if ( link.title == null || link.title == '' ) {
					link.title = 'Open help popup';
				}
			}
		}
	}	
);

/*
	Collapse/expands elements (identified by id), where
	the expanded version is shown as 'displayAs' (i.e.
	'block' or 'inline'). This behaviour is added to
	'link', such that:
	<a onclick="TreeBASE.collapseExpand('foo','block',this)">
		<img src="/treebase-web/images/plus.gif"/>
	</a>
	<div id="foo">
		<!-- this will be collapsed/expanded -->
	</div>
*/
TreeBASE.collapseExpand = function(id,displayAs,link) {
	var objToExpand = $(id);
	var img = $(link).firstDescendant();
	if ( img == null ) {
		img = document.createElement(img);
		link.appendChild(img);
	}	
	if ( objToExpand.style.display == 'none' ) {
		objToExpand.style.display = displayAs;
		img.src='/treebase-web/images/minus.gif';
		img.alt='collapse'
		link.title='collapse';
	}
	else {
		objToExpand.style.display = 'none';
		img.src='/treebase-web/images/plus.gif';
		img.alt='expand';
		link.title='expand';
	}
}
//expands what a user types in the text box into a PhyloWS query
TreeBASE.expandQuery = function () {
  var query = $('query').value;
  var split = TreeBASE.splitWords(query);
  var terms = new Array();
  for ( var i = 0; i < split.length; i++ ) {
    var type = TreeBASE.inferType(split[i]);
    var index = predicates[type];
    for ( var j = 0; j < index.length; j++ ) {
      terms.push( index[j] + '=' + split[i] );
    }
  }
  var joiner = ' or ';
  if ( $('all').checked ) {
    joiner = ' and ';
  }
  $('expanded').value = terms.join(joiner);
  return false;
};

// splits a string on commas, strip leading and trailing white space, quote words with spaces in them
TreeBASE.splitWords = function(query){
	var words = query.split(",");
	var split = new Array();
	for ( var i = 0; i < words.length; i++ ) {
		var word = words[i].replace(/^ */,'');
		word = word.replace(/ *$/,'');
		if ( word.match(/ /) && ! word.match(/^".*"$/) ) {
			split.push('"' + word + '"');
		}
		else {
			split.push(word);
		}
	}
	return split;
};

// infers whether a search word is an identifier or a string
TreeBASE.inferType = function(word) {
  if ( word.match(/^([A-Z][a-z]*)\d+$/) ) {
    return 'id';
  }
  else if ( word.match(/^\d+$/) ) {
    return 'integer';
  }
  else if ( word.match(/^doi:.+$/) ) {
    return 'doi';
  }
  else if ( word.match(/^pmid:.+$/) ) {
    return 'pubmed';
  }
  else {
    return 'word';
  }
};

// redirects to the phylows api
TreeBASE.redirect = function(newLocation){
  window.location = newLocation;
  return false;
};
