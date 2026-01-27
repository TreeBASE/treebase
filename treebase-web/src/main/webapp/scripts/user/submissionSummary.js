/*
	simple debugger, usage:
	<div id="debug"></div>
	debug('foo');	
*/
var debugging = true;
var debugContainer = document.getElementById('debug');
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
        var menuList = document.getElementById('menuList');
        var links = menuList ? menuList.querySelectorAll('a') : [];
        for ( var i = 0; i < links.length; i++ ) {
        	var title = links[i].title;
        	links[i].classList.add(title);
        	links[i].parentNode.classList.add(title);
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
    	var menuList = document.getElementById('menuList');
    	var filesLi = menuList ? menuList.querySelector('li.Files') : null;
    }
    function decorateAnalyses() {
    	var menuList = document.getElementById('menuList');
    	var analysesLi = menuList ? menuList.querySelector('li.Analyses') : null;    	
    	if ( analysesLi && ! analysesLi.classList.contains('emptyList') ) {
    		var analysisLis = analysesLi.querySelectorAll('li.analysis');
    		var analyzed = 'analyzed';
    		for ( var i = 0; i < analysisLis.length; i++ ) {
    			var id = analysisLis[i].id;
    			var analysisId = id.replace(/^analysis/,'');
    			if ( TreeBASE.isAnalysisValidated(analysisId) ) {
    				analysisLis[i].classList.add('analyzed');
    			}
    			else {
    				analysisLis[i].classList.add('notAnalyzed');
    				analyzed = 'notAnalyzed';
    				analysisLis[i].querySelector('a.analysis').title = 'This analysis does not validate';
    			}
    		}
    		analysesLi.classList.add(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			analysesLi.querySelector('a.Analyses').title = 'Some analyses have not been validated';
    		}
    	}    
    }
    function decorateMatrices() {
    	var menuList = document.getElementById('menuList');
    	var matricesLi = menuList ? menuList.querySelector('li.Matrices') : null;
    	if ( matricesLi && ! matricesLi.classList.contains('emptyList') ) {
    		var matrixLis = matricesLi.querySelectorAll('li.matrix');
    		var analyzed = 'analyzed';
    		for ( var i = 0; i < matrixLis.length; i++ ) {
    			if ( matrixLis[i] ) {
	    			var id = matrixLis[i].id;
	    			var matrixId = id.replace(/^matrix/,'');
	    			if ( TreeBASE.isDataAnalyzed( matrixId, 'matrix' ) ) {
	    				matrixLis[i].classList.add('analyzed');
	    			}
	    			else {
	    				matrixLis[i].classList.add('notAnalyzed');
	    				analyzed = 'notAnalyzed';
	    				matrixLis[i].querySelector('a.matrix').title = 'This matrix is not part of any analysis';
	    			}
    			}
    		}
    		matricesLi.classList.add(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			matricesLi.querySelector('a.Matrices').title = 'Some matrices are not part of any analysis';
    		}
    	}
    }
    function decorateTrees() {
    	var menuList = document.getElementById('menuList');
    	var treeBlocksLi = menuList ? menuList.querySelector('li.Trees') : null;
    	if ( treeBlocksLi && ! treeBlocksLi.classList.contains('emptyList') ) {
    		var treeBlockLis = treeBlocksLi.querySelectorAll('li.treeBlock');
    		var treesAnalyzed = 'analyzed';
    		for ( var i = 0; i < treeBlockLis.length; i++ ) {
    			var treeLis = treeBlockLis[i].querySelectorAll('li.tree');
    			var analyzed = 'analyzed';
    			for ( var j = 0; j < treeLis.length; j++ ) {
    				var id = treeLis[j].id;
    				var treeId = id.replace(/^tree/,'');
    				if ( TreeBASE.isDataAnalyzed( treeId, 'tree') ) {
    					treeLis[j].classList.add('analyzed');
    				}
    				else {
    					treeLis[j].classList.add('notAnalyzed');
    					analyzed = 'notAnalyzed';
    					treesAnalyzed = 'notAnalyzed';
    					treeLis[j].querySelector('a.tree').title = 'This tree is not part of any analysis';
    				}
    			}
    			treeBlockLis[i].classList.add(analyzed);
    			if ( analyzed == 'notAnalyzed' ) {
    				treeBlockLis[i].querySelector('a.treeBlock').title = 'Some trees in this block are not part of any analysis';
    			}
    		}  
    		treeBlocksLi.classList.add(treesAnalyzed); 	
    		if ( treesAnalyzed != 'analyzed' ) {
    			treeBlocksLi.querySelector('a.Trees').title = 'Some trees are not part of any analysis';
    		}	
    	}    
    }
    function decorateNotes() {
    	if ( TreeBASE.study.notes.match(/^\s*$/) || TreeBASE.study.name.match(/^\s*$/) ) {
    		var menuList = document.getElementById('menuList');
    		var notesLi = menuList ? menuList.querySelector('li.Notes') : null;
    		if (notesLi) {
    			notesLi.classList.add('notAnalyzed');
    			notesLi.querySelector('a.Notes').title = 'No notes have been entered yet';
    		}
    	}
    }
    function decorateCitation() {
    	var menuList = document.getElementById('menuList');
    	if ( TreeBASE.study.citation == null ) {
    		var citationLi = menuList ? menuList.querySelector('li.Citation') : null;
    		if (citationLi) {
    			citationLi.classList.add('notAnalyzed');
    			citationLi.querySelector('a.Citation').title = 'No citation information has been entered yet';
    		}
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
    			var citationLi = menuList ? menuList.querySelector('li.Citation') : null;
    			if (citationLi) {
        			citationLi.classList.add('notAnalyzed');
        			citationLi.querySelector('a.Citation').title = citationErrorMessage;
    			}
    		}
    	}
    	
    }
    function decorateAuthors() {
    	if ( TreeBASE.study.authors.length == 0 ) {
    		var menuList = document.getElementById('menuList');
    		var authorsLi = menuList ? menuList.querySelector('li.Authors') : null;
    		if (authorsLi) {
    			authorsLi.classList.add('notAnalyzed');   
    			authorsLi.querySelector('a.Authors').title = 'No authors have been entered yet';
    		}
    	}
    }
    function decorateUpload() {
   		if ( TreeBASE.study.nexusFileNames.length == 0 ) {
   			var menuList = document.getElementById('menuList');
   			var uploadLi = menuList ? menuList.querySelector('li.Upload') : null;
   			if (uploadLi) {
   				uploadLi.classList.add('notAnalyzed');
   				uploadLi.querySelector('a.Upload').title = 'No files have been uploaded yet';
   			}
   		} 
    }
    function writeNexusFileNames() {
    	var menuList = document.getElementById('menuList');
    	var filesLi = menuList ? menuList.querySelector('li.Files') : null;
    	var nexusFileNames = TreeBASE.study.nexusFileNames;
    	var ul = null;
    	if ( filesLi && nexusFileNames.length > 0 ) {
    		ul = createUnorderedList(nexusFileNames,'id','nexusFile');
    		ul.style.display = 'none';
    		filesLi.appendChild(ul);
    	}  
    	else if (filesLi) {
    		filesLi.classList.add('emptyList');
    		filesLi.querySelector('a.Files').title = 'No files have been uploaded yet';
    	}
    	if (filesLi) {
    		var a = createButton(ul);
    		filesLi.insertBefore(a,filesLi.firstChild);
    	}
    }
    function writeAnalyses() {
    	var menuList = document.getElementById('menuList');
    	var analysesLi = menuList ? menuList.querySelector('li.Analyses') : null;
    	var analyses = TreeBASE.study.analyses;
    	var ul = null;
    	if ( analysesLi && analyses.length > 0 ) { 
    		var allValid = true;
	    	ANALYSES: for ( var i = 0; i < analyses.length; i++ ) {
	    		var analysisSteps = analyses[i].analysisSteps;
	    		for ( var j = 0; j < analysisSteps.length; j++ ) {
	    			if ( ! analysisSteps[j].validated ) {
	    				analysesLi.querySelector('a.Analyses').title = 'Some analysis steps are invalid';
	    				analysesLi.classList.add('notAnalyzed');
	    				allValid = false;
	    				break ANALYSES;
	    			}
	    		}
	    	}
	    	if ( allValid ) {
	    		analysesLi.querySelector('a.Analyses').title = 'All analysis steps validate';	    		
	    	}
    	}
    	else if (analysesLi) {
    		analysesLi.classList.add('emptyList');
    		analysesLi.querySelector('a.Analyses').title = 'No analyses have been created yet';
    	}
    	if (analysesLi) {
    		var a = createButton(ul);
    		analysesLi.insertBefore(a,analysesLi.firstChild);
    	}
    	var spans = document.querySelectorAll('span.isAnalysisStepValid');
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
    	var menuList = document.getElementById('menuList');
    	var treesLi = menuList ? menuList.querySelector('li.Trees') : null;
    	var treeBlocks = TreeBASE.submission.submittedTreeBlocks;
    	var ul = null;
    	if ( treesLi && treeBlocks.length > 0 ) {
	    	ul = createUnorderedList(treeBlocks,'title','treeBlock');
	    	ul.style.display = 'none';
	    	treesLi.appendChild(ul);   
	    	var Lis = ul.querySelectorAll('li.treeBlock');   
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
    	else if (treesLi) {
    		treesLi.classList.add('emptyList');
    		treesLi.querySelector('a.Trees').title = 'No trees have been uploaded yet';
    	}
    	if (treesLi) {
	    	var a = createButton(ul);
	    	treesLi.insertBefore(a,treesLi.firstChild);
    	}
    }
    function writeTaxonLabels() {
    	var menuList = document.getElementById('menuList');
    	var taxaLi = menuList ? menuList.querySelector('li.Taxa') : null;
    	var taxonLabels = TreeBASE.submission.submittedTaxonLabels;
    	var ul = null;
    	if ( taxaLi && taxonLabels.length > 0 ) {
    		ul = createUnorderedList(taxonLabels,'taxonLabel','taxonLabel');
    		ul.style.display = 'none';
    		taxaLi.appendChild(ul);  
    		var taxonLabelLis = ul.querySelectorAll('li'); 
    		var analyzed = 'analyzed';	
    		for ( var i = 0; i < taxonLabelLis.length; i++ ) {
    			var id = taxonLabelLis[i].id;
    			var attempted = TreeBASE.isTaxonLinkingAttempted(id.replace(/[a-zA-Z]+/,""));
    			if ( attempted ) {
    				taxonLabelLis[i].classList.add('analyzed');
    			}
    			else {
    				taxonLabelLis[i].classList.add('notAnalyzed');
    				taxonLabelLis[i].querySelector('a.taxonLabel').title = 'This taxon has not been linked to external taxonomy';
    				analyzed = 'notAnalyzed';
    			}
    		}	
    		taxaLi.classList.add(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			taxaLi.querySelector('a.Taxa').title = 'Some taxa have not been linked to external taxonomy yet';
    		}
    	}
    	else if (taxaLi) {
    		taxaLi.classList.add('emptyList');
    		taxaLi.querySelector('a.Taxa').title = 'No taxa have been uploaded yet';
    	}
    	if (taxaLi) {
    		var a = createButton(ul);    	
    		taxaLi.insertBefore(a,taxaLi.firstChild);
    	}
    }
    function writeMatrices() {
    	var menuList = document.getElementById('menuList');
    	var matricesLi = menuList ? menuList.querySelector('li.Matrices') : null;
    	var submittedMatrices = TreeBASE.submission.submittedMatrices;
    	var ul = null;
    	if ( matricesLi && submittedMatrices.length > 0 ) {
    		ul = createUnorderedList(submittedMatrices,'title','matrix');
    		ul.style.display = 'none';
    		matricesLi.appendChild(ul);
    	}
    	else if (matricesLi) {
    		matricesLi.classList.add('emptyList');
    		matricesLi.querySelector('a.Matrices').title = 'No matrices have been uploaded yet';
    	}
    	if (matricesLi) {
    		var a = createButton(ul);
    		matricesLi.insertBefore(a,matricesLi.firstChild);
    	}
    }
    function createUnorderedList(model,property,modelClass) {
    	var ul = document.createElement('ul');
    	ul.classList.add('menuList');
    	ul.classList.add(modelClass);
    	for ( var i = 0; i < model.length; i++ ) { 
            if ( model[i] ) { 	
	    		var li = document.createElement('li');
	    		li.id = modelClass+model[i].id;
	            li.classList.add('menubar');
	            li.classList.add(modelClass);
	            var theUrl = url[modelClass](model[i].id,TreeBASE.submission.id);
	            var defaultName = '';
	            if ( modelClass == 'analysis' && model[i][property] == '' ) {
	            	defaultName = 'analysis ' + ( i + 1 );
	            }
	            else if ( modelClass == 'analysisStep' && model[i][property] == '' ) {
	            	defaultName = 'step ' + ( i + 1 );
	            }
	            var a = document.createElement('a');
	            a.href = theUrl;
	            a.innerHTML = defaultName + model[i][property];
	            a.classList.add('standalone');
	            a.classList.add(modelClass);
	            li.appendChild(a);
	            ul.appendChild(li);
            }
    	}
    	return ul;
    } 
    function createButton (toggleMe) {
    	var imagePath = '/treebase-web/images/';
    	var a = document.createElement('a');
    	a.classList.add('expander');
    	var img = document.createElement('img');
    	img.classList.add('iconButton');
    	img.classList.add('collapsed');
    	a.appendChild(img);
    	if ( toggleMe != null ) {
    		img.src = imagePath + 'plus.gif';
	    	a.onclick = function () {
	    		img.classList.add('iconButton');
	    		if ( toggleMe.style.display == 'none' ) {
	    			toggleMe.style.display = 'block';
	    			img.src = imagePath + 'minus.gif';
	    			img.classList.add('expanded');
	    		}
	    		else {
	    			toggleMe.style.display = 'none';
	    			img.src = imagePath + 'plus.gif';
	    			img.classList.add('collapsed');
	    		}
	    	}
    	}
    	else {
    		img.src = imagePath + 'square.gif';
    	}
    	return a;
    }
    function checkSubmitButton() {
    	var submitButton = document.getElementById('submitReadyState');
    	if (submitButton != null && submitButton != undefined) {
    		var menuList = document.getElementById('menuList');
    		var notAnalyzed = menuList ? menuList.querySelectorAll('li.notAnalyzed') : [];
    		if (notAnalyzed.length > 0) {
    			submitButton.disabled = true;
    			var readyStateErrors = document.querySelectorAll('p.readyStateError');
    			for (var i = 0; i < readyStateErrors.length; i++) {
    				readyStateErrors[i].style.display = 'block';
    			}
    		}
    	};
    }
    //TreeBASE.writeSummary = writeSummary;
    TreeBASE.register(decorateMenu);
	TreeBASE.register(function(){
		tbFetch('/treebase-web/json/submissionIsland.html', {
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