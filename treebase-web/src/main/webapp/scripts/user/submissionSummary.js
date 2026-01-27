/*
	simple debugger, usage:
	<div id="debug"></div>
	debug('foo');	
*/
var debugging = true;
var $debugContainer = $('#debug');
function debug(msg) {
    if ( debugging ) {    		
    	if ( $debugContainer.length > 0 ) {
    		$debugContainer.append('<pre>' + msg + '</pre>');
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
        $('#menuList').find('a').each(function() {
        	var title = this.title;
        	$(this).addClass(title);
        	$(this.parentNode).addClass(title);
        });    
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
    	var $filesLi = $('#menuList').find('li.Files').first();
    }
    function decorateAnalyses() {
    	var $analysesLi = $('#menuList').find('li.Analyses').first();    	
    	if ( ! $analysesLi.hasClass('emptyList') ) {
    		var $analysisLis = $analysesLi.find('li.analysis');
    		var analyzed = 'analyzed';
    		$analysisLis.each(function() {
    			var id = this.id;
    			var analysisId = id.replace(/^analysis/,'');
    			if ( TreeBASE.isAnalysisValidated(analysisId) ) {
    				$(this).addClass('analyzed');
    			}
    			else {
    				$(this).addClass('notAnalyzed');
    				analyzed = 'notAnalyzed';
    				$(this).find('a.analysis').first().attr('title', 'This analysis does not validate');
    			}
    		});
    		$analysesLi.addClass(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			$analysesLi.find('a.Analyses').first().attr('title', 'Some analyses have not been validated');
    		}
    	}    
    }
    function decorateMatrices() {
    	var $matricesLi = $('#menuList').find('li.Matrices').first();
    	if ( ! $matricesLi.hasClass('emptyList') ) {
    		var $matrixLis = $matricesLi.find('li.matrix');
    		var analyzed = 'analyzed';
    		$matrixLis.each(function() {
    			var id = this.id;
    			var matrixId = id.replace(/^matrix/,'');
    			if ( TreeBASE.isDataAnalyzed( matrixId, 'matrix' ) ) {
    				$(this).addClass('analyzed');
    			}
    			else {
    				$(this).addClass('notAnalyzed');
    				analyzed = 'notAnalyzed';
    				$(this).find('a.matrix').first().attr('title', 'This matrix is not part of any analysis');
    			}
    		});
    		$matricesLi.addClass(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			$matricesLi.find('a.Matrices').first().attr('title', 'Some matrices are not part of any analysis');
    		}
    	}
    }
    function decorateTrees() {
    	var $treeBlocksLi = $('#menuList').find('li.Trees').first();
    	if ( ! $treeBlocksLi.hasClass('emptyList') ) {
    		var $treeBlockLis = $treeBlocksLi.find('li.treeBlock');
    		var treesAnalyzed = 'analyzed';
    		$treeBlockLis.each(function() {
    			var $treeLis = $(this).find('li.tree');
    			var analyzed = 'analyzed';
    			var $treeBlockLi = $(this);
    			$treeLis.each(function() {
    				var id = this.id;
    				var treeId = id.replace(/^tree/,'');
    				if ( TreeBASE.isDataAnalyzed( treeId, 'tree') ) {
    					$(this).addClass('analyzed');
    				}
    				else {
    					$(this).addClass('notAnalyzed');
    					analyzed = 'notAnalyzed';
    					treesAnalyzed = 'notAnalyzed';
    					$(this).find('a.tree').first().attr('title', 'This tree is not part of any analysis');
    				}
    			});
    			$treeBlockLi.addClass(analyzed);
    			if ( analyzed == 'notAnalyzed' ) {
    				$treeBlockLi.find('a.treeBlock').first().attr('title', 'Some trees in this block are not part of any analysis');
    			}
    		});  
    		$treeBlocksLi.addClass(treesAnalyzed); 	
    		if ( treesAnalyzed != 'analyzed' ) {
    			$treeBlocksLi.find('a.Trees').first().attr('title', 'Some trees are not part of any analysis');
    		}	
    	}    
    }
    function decorateNotes() {
    	if ( TreeBASE.study.notes.match(/^\s*$/) || TreeBASE.study.name.match(/^\s*$/) ) {
    		var $notesLi = $('#menuList').find('li.Notes').first();
    		$notesLi.addClass('notAnalyzed');
    		$notesLi.find('a.Notes').first().attr('title', 'No notes have been entered yet');
    	}
    }
    function decorateCitation() {
    	if ( TreeBASE.study.citation == null ) {
    		var $citationLi = $('#menuList').find('li.Citation').first();
    		$citationLi.addClass('notAnalyzed');
    		$citationLi.find('a.Citation').first().attr('title', 'No citation information has been entered yet');
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
    			var $citationLi = $('#menuList').find('li.Citation').first();
        		$citationLi.addClass('notAnalyzed');
        		$citationLi.find('a.Citation').first().attr('title', citationErrorMessage);
    		}
    	}
    	
    }
    function decorateAuthors() {
    	if ( TreeBASE.study.authors.length == 0 ) {
    		var $authorsLi = $('#menuList').find('li.Authors').first();
    		$authorsLi.addClass('notAnalyzed');   
    		$authorsLi.find('a.Authors').first().attr('title', 'No authors have been entered yet'); 	
    	}
    }
    function decorateUpload() {
   		if ( TreeBASE.study.nexusFileNames.length == 0 ) {
   			var $uploadLi = $('#menuList').find('li.Upload').first();
   			$uploadLi.addClass('notAnalyzed');
   			$uploadLi.find('a.Upload').first().attr('title', 'No files have been uploaded yet');    			
   		} 
    }
    function writeNexusFileNames() {
    	var $filesLi = $('#menuList').find('li.Files').first();
    	var nexusFileNames = TreeBASE.study.nexusFileNames;
    	var $ul = null;
    	if ( nexusFileNames.length > 0 ) {
    		$ul = createUnorderedList(nexusFileNames,'id','nexusFile');
    		$ul.css('display', 'none');
    		$filesLi.append($ul);
    	}  
    	else {
    		$filesLi.addClass('emptyList');
    		$filesLi.find('a.Files').first().attr('title', 'No files have been uploaded yet');
    	}
    	var $a = createButton($ul);
    	$filesLi.prepend($a);    	
    }
    function writeAnalyses() {
    	var $analysesLi = $('#menuList').find('li.Analyses').first();
    	var analyses = TreeBASE.study.analyses;
    	var $ul = null;
    	if ( analyses.length > 0 ) { 
    		var allValid = true;
	    	ANALYSES: for ( var i = 0; i < analyses.length; i++ ) {
	    		var analysisSteps = analyses[i].analysisSteps;
	    		for ( var j = 0; j < analysisSteps.length; j++ ) {
	    			if ( ! analysisSteps[j].validated ) {
	    				$analysesLi.find('a.Analyses').first().attr('title', 'Some analysis steps are invalid');
	    				$analysesLi.addClass('notAnalyzed');
	    				allValid = false;
	    				break ANALYSES;
	    			}
	    		}
	    	}
	    	if ( allValid ) {
	    		$analysesLi.find('a.Analyses').first().attr('title', 'All analysis steps validate');	    		
	    	}
    	}
    	else {
    		$analysesLi.addClass('emptyList');
    		$analysesLi.find('a.Analyses').first().attr('title', 'No analyses have been created yet');
    	}	
    	var $a = createButton($ul);
    	$analysesLi.prepend($a);   
    	$('span.isAnalysisStepValid').each(function() {
    		var stepId = this.title;
    		if ( ! TreeBASE.isAnalysisStepValidated(stepId) ) {
    			$(this).css('display', 'inline');
    			var $heading = $(this.parentNode);
    			$heading.css('backgroundColor', '#ffffcc');
    			$heading.css('border', '1px solid red');
    		}
    	}); 	 	
    }    
    function writeTreeBlocks() {
    	var $treesLi = $('#menuList').find('li.Trees').first();
    	var treeBlocks = TreeBASE.submission.submittedTreeBlocks;
    	var $ul = null;
    	if ( treeBlocks.length > 0 ) {
	    	$ul = createUnorderedList(treeBlocks,'title','treeBlock');
	    	$ul.css('display', 'none');
	    	$treesLi.append($ul);   
	    	var $Lis = $ul.find('li.treeBlock');   
	    	for ( var i = 0; i < treeBlocks.length; i++ ) {
	    		if ( treeBlocks[i] ) {
		    		var $innerUl = createUnorderedList(treeBlocks[i].treeList,'label','tree');
		    		$innerUl.css('display', 'none');
		    		if ( $Lis[i] ) {
		    			$($Lis[i]).append($innerUl);
		    			var $innerButton = createButton($innerUl);
		    			$($Lis[i]).prepend($innerButton);
		    		}	    		
	    		}
	    	} 	 	
    	}
    	else {
    		$treesLi.addClass('emptyList');
    		$treesLi.find('a.Trees').first().attr('title', 'No trees have been uploaded yet');
    	}
	    var $a = createButton($ul);
	    $treesLi.prepend($a);    	
    }
    function writeTaxonLabels() {
    	var $taxaLi = $('#menuList').find('li.Taxa').first();
    	var taxonLabels = TreeBASE.submission.submittedTaxonLabels;
    	var $ul = null;
    	if ( taxonLabels.length > 0 ) {
    		$ul = createUnorderedList(taxonLabels,'taxonLabel','taxonLabel');
    		$ul.css('display', 'none');
    		$taxaLi.append($ul);  
    		var $taxonLabelLis = $ul.find('li'); 
    		var analyzed = 'analyzed';	
    		$taxonLabelLis.each(function() {
    			var id = this.id;
    			var attempted = TreeBASE.isTaxonLinkingAttempted(id.replace(/[a-zA-Z]+/,""));
    			if ( attempted ) {
    				$(this).addClass('analyzed');
    			}
    			else {
    				$(this).addClass('notAnalyzed');
    				$(this).find('a.taxonLabel').first().attr('title', 'This taxon has not been linked to external taxonomy');
    				analyzed = 'notAnalyzed';
    			}
    		});	
    		$taxaLi.addClass(analyzed);
    		if ( analyzed == 'notAnalyzed' ) {
    			$taxaLi.find('a.Taxa').first().attr('title', 'Some taxa have not been linked to external taxonomy yet');
    		}
    	}
    	else {
    		$taxaLi.addClass('emptyList');
    		$taxaLi.find('a.Taxa').first().attr('title', 'No taxa have been uploaded yet');
    	}
    	var $a = createButton($ul);    	
    	$taxaLi.prepend($a);
    }
    function writeMatrices() {
    	var $matricesLi = $('#menuList').find('li.Matrices').first();
    	var submittedMatrices = TreeBASE.submission.submittedMatrices;
    	var $ul = null;
    	if ( submittedMatrices.length > 0 ) {
    		$ul = createUnorderedList(submittedMatrices,'title','matrix');
    		$ul.css('display', 'none');
    		$matricesLi.append($ul);
    	}
    	else {
    		$matricesLi.addClass('emptyList');
    		$matricesLi.find('a.Matrices').first().attr('title', 'No matrices have been uploaded yet');
    	}
    	var $a = createButton($ul);
    	$matricesLi.prepend($a);    	
    }
    function createUnorderedList(model,property,modelClass) {
    	var $ul = $('<ul>');
    	$ul.addClass('menuList');
    	$ul.addClass(modelClass);
    	for ( var i = 0; i < model.length; i++ ) { 
            if ( model[i] ) { 	
	    		var $li = $('<li>', {id: modelClass+model[i].id});
	            $li.addClass('menubar');
	            $li.addClass(modelClass);
	            var theUrl = url[modelClass](model[i].id,TreeBASE.submission.id);
	            var defaultName = '';
	            if ( modelClass == 'analysis' && model[i][property] == '' ) {
	            	defaultName = 'analysis ' + ( i + 1 );
	            }
	            else if ( modelClass == 'analysisStep' && model[i][property] == '' ) {
	            	defaultName = 'step ' + ( i + 1 );
	            }
	            var $a = $('<a>', {href: theUrl}).html(defaultName + model[i][property]);
	            $a.addClass('standalone');
	            $a.addClass(modelClass);
	            $li.append($a);
	            $ul.append($li);
            }
    	}
    	return $ul;
    } 
    function createButton ($toggleMe) {
    	var imagePath = '/treebase-web/images/';
    	var $a = $('<a>');
    	$a.addClass('expander');
    	var $img = $('<img>');
    	$img.addClass('iconButton');
    	$img.addClass('collapsed');
    	$a.append($img);
    	if ( $toggleMe != null ) {
    		$img.attr('src', imagePath + 'plus.gif');
	    	$a.on('click', function () {
	    		$img.addClass('iconButton');
	    		if ( $toggleMe.css('display') == 'none' ) {
	    			$toggleMe.css('display', 'block');
	    			$img.attr('src', imagePath + 'minus.gif');
	    			$img.addClass('expanded');
	    		}
	    		else {
	    			$toggleMe.css('display', 'none');
	    			$img.attr('src', imagePath + 'plus.gif');
	    			$img.addClass('collapsed');
	    		}
	    	});
    	}
    	else {
    		$img.attr('src', imagePath + 'square.gif');
    	}
    	return $a;
    }
    function checkSubmitButton() {
    	var $submitButton = $('#submitReadyState');
    	if ($submitButton.length > 0) {
    		var $notAnalyzed = $('#menuList').find('li.notAnalyzed');
    		if ($notAnalyzed.length > 0) {
    			$submitButton.prop('disabled', true);
    			$('p.readyStateError').css('display', 'block');
    		}
    	};
    }
    //TreeBASE.writeSummary = writeSummary;
    TreeBASE.register(decorateMenu);
	TreeBASE.register(function(){
		$.ajax({
			url: '/treebase-web/json/submissionIsland.html',
			method: 'GET',
			dataType: 'text',
			success: function(response){
				var tmp; 
				eval('tmp='+response);
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