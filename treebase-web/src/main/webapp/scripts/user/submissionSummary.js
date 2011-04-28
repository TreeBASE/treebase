/*
	simple debugger, usage:
	<div id="debug"></div>
	debug('foo');	
*/
var debugging = true;
var debugContainer = $('debug');
function debug(msg) {
    if ( debugging ) {    		
    	if ( debugContainer ) {
    		debugContainer.innerHTML += '<pre>' + msg + '</pre>';
    	}
    }
}

/* Initialize TreeBASE namespace */
if(TreeBASE==null) var TreeBASE = {};
if(TreeBASE.study==null) TreeBASE.study = {};

/* Utility class methods */
TreeBASE.isDataAnalyzed = function(id,dataType) {
    var analyses = TreeBASE.study.analyses;
    if ( analyses && analyses.length > 0 ) {
        for ( var i = 0; i < analyses.length; i++ ) {
        	if ( analyses[i] ) {
	            var analysisSteps = analyses[i].analysisSteps;
	            for ( var j = 0; j < analysisSteps.length; j++ ) {
	            	if ( analysisSteps[j] ) {
		                var analyzedData = analysisSteps[j].analyzedData;
		                for ( var k = 0; k < analyzedData.length; k++ ) {
		                    if ( analyzedData[k] && analyzedData[k].dataType == dataType ) {
		                        var matrixId = analyzedData[k][dataType].id;
		                        if ( matrixId == id ) {
		                            return true;
		                        }
		                    }
		                }
	                }
	            }
            }
        }
    }
    else {
        return false;
    }
};
TreeBASE.isAnalysisValidated = function (id) {
	var analyses = TreeBASE.study.analyses;
	if ( analyses && analyses.length > 0 ) {
		for ( var i = 0; i < analyses.length; i++ ) {
			if ( analyses[i].id == id ) {
				return analyses[i].validated;
			}
		}
	}
	else {
		alert(id);
	}
};
TreeBASE.isAnalysisStepValidated = function (id) {
	var analyses = TreeBASE.study.analyses;
	if ( analyses && analyses.length > 0 ) {
		for ( var i = 0; i < analyses.length; i++ ) {
	        var analysisSteps = analyses[i].analysisSteps;		
			for ( var j = 0; j < analysisSteps.length; j++ ) {
				if ( analysisSteps[i].id == id ) {
					return analysisSteps[i].validated;
				}
			}
		}
	}
	else {
		alert(id);
	}	
};
TreeBASE.isTaxonLinkingAttempted = function(id) {
	var taxonLabels = TreeBASE.submission.submittedTaxonLabels;
	if ( taxonLabels && taxonLabels.length > 0 ) {
		for ( var i = 0; i < taxonLabels.length; i++ ) {
			if ( taxonLabels[i].id == id ) {
				return taxonLabels[i].attemptedLinking;
			}
		}
	}
};

/* start of closure */
(function(){
    var baseUrl = '/treebase-web/user/';
    var url = {
        Taxa : function(id,submission) { 
            return baseUrl + 'taxaList.html';
        },
        taxonLabel : function(id,submission) {
            return baseUrl + 'editTaxonLabel.html?taxonlabelid=' + id;
        },
        Matrices : function(id,submission) {
            return baseUrl + 'matrixList.html';
        },
        matrix : function(id,submission) { 
            return baseUrl + 'matrixRowList.html?id=' + id;
        },
        Trees : function(id,submission) {
            return baseUrl + 'treeBlockList.html';
        },
        treeBlock : function(id,submission) {
            return baseUrl + 'treeList.html?id=' + submission + '&treeblockid=' + id;
        },
        tree : function(id,submission) { 
            return baseUrl + 'directMapToPhyloWidget.html?treeid=' + id;
        },
        analysis : function(id,submission) {  
            return baseUrl + 'analysisForm.html?id=' + id;
        },
        analyzedData : function(id,submission) {   
            return baseUrl + 'analyzedDataForm.html?analysis_step_id=' + id;
        },
        Analyses : function(id,submission) {
            return baseUrl + 'analyses.html';
        },
        analysisStep : function(id,submission) {
            return baseUrl + 'analysisStepForm.html?id=' + id;
        },
        nexusFile : function(id,submission) {
        	return baseUrl + 'downloadANexusRCTFile.html?nexusfile=' + id;
        },
    };
    function decorateMenu () {
        var links = $('menuList').select('a');
        for ( var i = 0; i < links.length; i++ ) {
        	var title = links[i].title;
        	$(links[i]).addClassName(title);
        	$(links[i].parentNode).addClassName(title);
        }    
    }
    function writeSummary () {
		if ( TreeBASE.submission ) {
	        writeTaxonLabels();
	        writeMatrices();
	        writeTreeBlocks();
	    }
	    if ( TreeBASE.study && TreeBASE.study.analyses ) {
	        writeAnalyses(); 
	        writeNexusFileNames();       
	        //decorateNotes();   
	        decorateCitation();
	        decorateAuthors();  
	        decorateUpload();
	        decorateMatrices();
	        decorateTrees();
	        //decorateAnalyses();   
	    }
    }
    function decorateFiles() {
    	var filesLi = $('menuList').select('li.Files')[0];
    }
    function decorateAnalyses() {
    	var analysesLi = $('menuList').select('li.Analyses')[0];    	
    	if ( ! analysesLi.hasClassName('emptyList') ) {
    		var analysisLis = analysesLi.select('li.analysis');
    		var analyzed = 'analyzed';
    		for ( var i = 0; i < analysisLis.length; i++ ) {
    			var id = analysisLis[i].id;
    			var analysisId = id.replace(/^analysis/,'');
    			if ( TreeBASE.isAnalysisValidated(analysisId) ) {
    				analysisLis[i].addClassName('analyzed');
    			}
    			else {
    				analysisLis[i].addClassName('notAnalyzed');
    				analyzed = 'notAnalyzed';
    				analysisLis[i].select('a.analysis')[0].title = 'This analysis does not validate';
    			}
    		}
    		analysesLi.addClassName(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			analysesLi.select('a.Analyses')[0].title = 'Some analyses have not been validated';
    		}
    	}    
    }
    function decorateMatrices() {
    	var matricesLi = $('menuList').select('li.Matrices')[0];
    	if ( ! matricesLi.hasClassName('emptyList') ) {
    		var matrixLis = matricesLi.select('li.matrix');
    		var analyzed = 'analyzed';
    		for ( var i = 0; i < matrixLis.length; i++ ) {
    			if ( matrixLis[i] ) {
	    			var id = matrixLis[i].id;
	    			var matrixId = id.replace(/^matrix/,'');
	    			if ( TreeBASE.isDataAnalyzed( matrixId, 'matrix' ) ) {
	    				matrixLis[i].addClassName('analyzed');
	    			}
	    			else {
	    				matrixLis[i].addClassName('notAnalyzed');
	    				analyzed = 'notAnalyzed';
	    				matrixLis[i].select('a.matrix')[0].title = 'This matrix is not part of any analysis';
	    			}
    			}
    		}
    		matricesLi.addClassName(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			matricesLi.select('a.Matrices')[0].title = 'Some matrices are not part of any analysis';
    		}
    	}
    }
    function decorateTrees() {
    	var treeBlocksLi = $('menuList').select('li.Trees')[0];
    	if ( ! treeBlocksLi.hasClassName('emptyList') ) {
    		var treeBlockLis = treeBlocksLi.select('li.treeBlock');
    		var treesAnalyzed = 'analyzed';
    		for ( var i = 0; i < treeBlockLis.length; i++ ) {
    			var treeLis = treeBlockLis[i].select('li.tree');
    			var analyzed = 'analyzed';
    			for ( var j = 0; j < treeLis.length; j++ ) {
    				var id = treeLis[j].id;
    				var treeId = id.replace(/^tree/,'');
    				if ( TreeBASE.isDataAnalyzed( treeId, 'tree') ) {
    					treeLis[j].addClassName('analyzed');
    				}
    				else {
    					treeLis[j].addClassName('notAnalyzed');
    					analyzed = 'notAnalyzed';
    					treesAnalyzed = 'notAnalyzed';
    					treeLis[j].select('a.tree')[0].title = 'This tree is not part of any analysis';
    				}
    			}
    			treeBlockLis[i].addClassName(analyzed);
    			if ( analyzed == 'notAnalyzed' ) {
    				treeBlockLis[i].select('a.treeBlock')[0].title = 'Some trees in this block are not part of any analysis';
    			}
    		}  
    		treeBlocksLi.addClassName(treesAnalyzed); 	
    		if ( treesAnalyzed != 'analyzed' ) {
    			treeBlocksLi.select('a.Trees')[0].title = 'Some trees are not part of any analysis';
    		}	
    	}    
    }
    function decorateNotes() {
    	if ( TreeBASE.study.notes.match(/^\s*$/) || TreeBASE.study.name.match(/^\s*$/) ) {
    		var notesLi = $('menuList').select('li.Notes')[0];
    		notesLi.addClassName('notAnalyzed');
    		notesLi.select('a.Notes')[0].title = 'No notes have been entered yet';
    	}
    }
    function decorateCitation() {
    	if ( TreeBASE.study.citation == null ) {
    		var citationLi = $('menuList').select('li.Citation')[0];
    		citationLi.addClassName('notAnalyzed');
    		citationLi.select('a.Citation')[0].title = 'No citation information has been entered yet';
    	}
    	else {
    		var isCitationError = false;
    		var citationErrorMessage = '';
    		switch( TreeBASE.study.citation.citationType ){
	    		case 'Article':
	    		if ( TreeBASE.study.citation.journal.length == 0 ) {
	    			isCitationError = true;
	    			citationErrorMessage = 'No journal name has been entered';
	    		}
	    		break
	    		case 'Book':
	    			if ( TreeBASE.study.citation.booktitle.length == 0 ) {
		    			isCitationError = true;
		    			citationErrorMessage = 'No book title has been entered';
		    		}
	    		break
	    		case 'Book Section':
	    		if (TreeBASE.study.citation.booktitle.length == 0) {
	    			isCitationError = true;
	    			citationErrorMessage = 'No book title has been entered';
	    		}
	    		if (TreeBASE.study.citation.sectiontitle.length == 0) {
	    			if ( isCitationError ) {
	    				citationErrorMessage += '; ';
	    			}
	    			isCitationError = true;
	    			citationErrorMessage += 'No book section has been entered';
	    		} 
	    		break
    		}
    		
    		if ( isCitationError ){
    			var citationLi = $('menuList').select('li.Citation')[0];
        		citationLi.addClassName('notAnalyzed');
        		citationLi.select('a.Citation')[0].title = citationErrorMessage;
    		}
    	}
    	
    }
    function decorateAuthors() {
    	if ( TreeBASE.study.authors.length == 0 ) {
    		var authorsLi = $('menuList').select('li.Authors')[0];
    		authorsLi.addClassName('notAnalyzed');   
    		authorsLi.select('a.Authors')[0].title = 'No authors have been entered yet'; 	
    	}
    }
    function decorateUpload() {
   		if ( TreeBASE.study.nexusFileNames.size() == 0 ) {
   			var uploadLi = $('menuList').select('li.Upload')[0];
   			uploadLi.addClassName('notAnalyzed');
   			uploadLi.select('a.Upload')[0].title = 'No files have been uploaded yet';    			
   		} 
    }
    function writeNexusFileNames() {
    	var filesLi = $('menuList').select('li.Files')[0];
    	var nexusFileNames = TreeBASE.study.nexusFileNames;
    	var ul = null;
    	if ( nexusFileNames.size() > 0 ) {
    		ul = createUnorderedList(nexusFileNames,'id','nexusFile');
    		ul.style.display = 'none';
    		filesLi.appendChild(ul);
    	}  
    	else {
    		filesLi.addClassName('emptyList');
    		filesLi.select('a.Files')[0].title = 'No files have been uploaded yet';
    	}
    	var a = createButton(ul);
    	filesLi.insertBefore(a,filesLi.firstChild);    	
    }
    function writeAnalyses() {
    	var analysesLi = $('menuList').select('li.Analyses')[0];
    	var analyses = TreeBASE.study.analyses;
    	var ul = null;
    	if ( analyses.size() > 0 ) { 
    		var allValid = true;
	    	ANALYSES: for ( var i = 0; i < analyses.length; i++ ) {
	    		var analysisSteps = analyses[i].analysisSteps;
	    		for ( var j = 0; j < analysisSteps.length; j++ ) {
	    			if ( ! analysisSteps[j].validated ) {
	    				analysesLi.select('a.Analyses')[0].title = 'Some analysis steps are invalid';
	    				analysesLi.addClassName('notAnalyzed');
	    				allValid = false;
	    				break ANALYSES;
	    			}
	    		}
	    	}
	    	if ( allValid ) {
	    		analysesLi.select('a.Analyses')[0].title = 'All analysis steps validate';	    		
	    	}
    	}
    	else {
    		analysesLi.addClassName('emptyList');
    		analysesLi.select('a.Analyses')[0].title = 'No analyses have been created yet';
    	}	
    	var a = createButton(ul);
    	analysesLi.insertBefore(a,analysesLi.firstChild);   
    	var spans = $$('span.isAnalysisStepValid');
    	for ( var i = 0; i < spans.length; i++ ) {
    		var stepId = spans[i].title;
    		if ( ! TreeBASE.isAnalysisStepValidated(stepId) ) {
    			spans[i].style.display = 'inline';
    			var heading = spans[i].parentNode;
    			heading.style.backgroundColor = '#ffffcc';
    			heading.style.border = '1px solid red';
    		}
    	} 	 	
    }    
    function writeTreeBlocks() {
    	var treesLi = $('menuList').select('li.Trees')[0];
    	var treeBlocks = TreeBASE.submission.submittedTreeBlocks;
    	var ul = null;
    	if ( treeBlocks.size() > 0 ) {
	    	ul = createUnorderedList(treeBlocks,'title','treeBlock');
	    	ul.style.display = 'none';
	    	treesLi.appendChild(ul);   
	    	var Lis = ul.select('li.treeBlock');   
	    	for ( var i = 0; i < treeBlocks.length; i++ ) {
	    		if ( treeBlocks[i] ) {
		    		var innerUl = createUnorderedList(treeBlocks[i].treeList,'label','tree');
		    		innerUl.style.display = 'none';
		    		if ( Lis[i] ) {
		    		Lis[i].appendChild(innerUl);
		    			var innerButton = createButton(innerUl);
		    			Lis[i].insertBefore(innerButton,Lis[i].firstChild);
		    		}	    		
	    		}
	    	} 	 	
    	}
    	else {
    		treesLi.addClassName('emptyList');
    		treesLi.select('a.Trees')[0].title = 'No trees have been uploaded yet';
    	}
	    var a = createButton(ul);
	    treesLi.insertBefore(a,treesLi.firstChild);    	
    }
    function writeTaxonLabels() {
    	var taxaLi = $('menuList').select('li.Taxa')[0];
    	var taxonLabels = TreeBASE.submission.submittedTaxonLabels;
    	var ul = null;
    	if ( taxonLabels.length > 0 ) {
    		ul = createUnorderedList(taxonLabels,'taxonLabel','taxonLabel');
    		ul.style.display = 'none';
    		taxaLi.appendChild(ul);  
    		var taxonLabelLis = ul.select('li'); 
    		var analyzed = 'analyzed';	
    		for ( var i = 0; i < taxonLabelLis.length; i++ ) {
    			var id = taxonLabelLis[i].id;
    			var attempted = TreeBASE.isTaxonLinkingAttempted(id.replace(/[a-zA-Z]+/,""));
    			if ( attempted ) {
    				taxonLabelLis[i].addClassName('analyzed');
    			}
    			else {
    				taxonLabelLis[i].addClassName('notAnalyzed');
    				taxonLabelLis[i].select('a.taxonLabel')[0].title = 'This taxon has not been linked to external taxonomy';
    				analyzed = 'notAnalyzed';
    			}
    		}	
    		taxaLi.addClassName(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			taxaLi.select('a.Taxa')[0].title = 'Some taxa have not been linked to external taxonomy yet';
    		}
    	}
    	else {
    		taxaLi.addClassName('emptyList');
    		taxaLi.select('a.Taxa')[0].title = 'No taxa have been uploaded yet';
    	}
    	var a = createButton(ul);    	
    	taxaLi.insertBefore(a,taxaLi.firstChild);
    }
    function writeMatrices() {
    	var matricesLi = $('menuList').select('li.Matrices')[0];
    	var submittedMatrices = TreeBASE.submission.submittedMatrices;
    	var ul = null;
    	if ( submittedMatrices.size() > 0 ) {
    		ul = createUnorderedList(submittedMatrices,'title','matrix');
    		ul.style.display = 'none';
    		matricesLi.appendChild(ul);
    	}
    	else {
    		matricesLi.addClassName('emptyList');
    		matricesLi.select('a.Matrices')[0].title = 'No matrices have been uploaded yet';
    	}
    	var a = createButton(ul);
    	matricesLi.insertBefore(a,matricesLi.firstChild);    	
    }
    function createUnorderedList(model,property,modelClass) {
    	var ul = new Element('ul');
    	ul.addClassName('menuList');
    	ul.addClassName(modelClass);
    	for ( var i = 0; i < model.length; i++ ) { 
            if ( model[i] ) { 	
	    		var li = new Element('li',{'id':modelClass+model[i].id});
	            li.addClassName('menubar');
	            li.addClassName(modelClass);
	            var theUrl = url[modelClass](model[i].id,TreeBASE.submission.id);
	            var defaultName = '';
	            if ( modelClass == 'analysis' && model[i][property] == '' ) {
	            	defaultName = 'analysis ' + ( i + 1 );
	            }
	            else if ( modelClass == 'analysisStep' && model[i][property] == '' ) {
	            	defaultName = 'step ' + ( i + 1 );
	            }
	            var a = new Element('a',{'href':theUrl}).update(defaultName + model[i][property]);
	            a.addClassName('standalone');
	            a.addClassName(modelClass);
	            li.appendChild(a);
	            ul.appendChild(li);
            }
    	}
    	return ul;
    } 
    function createButton (toggleMe) {
    	var imagePath = '/treebase-web/images/';
    	var a = new Element('a');
    	a.addClassName('expander');
    	var img = new Element('img');
    	img.addClassName('iconButton');
    	img.addClassName('collapsed');
    	a.appendChild(img);
    	if ( toggleMe != null ) {
    		img.src = imagePath + 'plus.gif';
	    	a.onclick = function () {
	    		img.addClassName('iconButton');
	    		if ( toggleMe.style.display == 'none' ) {
	    			toggleMe.style.display = 'block';
	    			img.src = imagePath + 'minus.gif';
	    			img.addClassName('expanded');
	    		}
	    		else {
	    			toggleMe.style.display = 'none';
	    			img.src = imagePath + 'plus.gif';
	    			img.addClassName('collapsed');
	    		}
	    	}
    	}
    	else {
    		img.src = imagePath + 'square.gif';
    	}
    	return a;
    }
    function checkSubmitButton() {
    	var submitButton = $('submitReadyState');
    	if (submitButton != null && submitButton != undefined) {
    		var notAnalyzed = $('menuList').select('li.notAnalyzed');
    		if (notAnalyzed != '') {
    			submitButton.disable();
    			$$('p.readyStateError').invoke('setStyle', { display: 'block' });
    		}
    	};
    }
    //TreeBASE.writeSummary = writeSummary;
    TreeBASE.register(decorateMenu);
	TreeBASE.register(function(){
		var req = new Ajax.Request('/treebase-web/json/submissionIsland.html', {
			'method':'get',
			'onSuccess':function(response){
				var tmp; 
				eval('tmp='+response.responseText);
				TreeBASE.study = tmp.study;
				TreeBASE.submission = tmp.submission;
				//TreeBASE.writeSummary();
				writeSummary();
				checkSubmitButton();
			}
		});
	});
	TreeBASE.register(initializeMenus); 
})()

/* end of closure */