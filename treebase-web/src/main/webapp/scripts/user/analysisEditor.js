if ( TreeBASE == null ) {
	TreeBASE = {};
}
if ( TreeBASE.analysisEditor == null ) {
	TreeBASE.analysisEditor = {};
}
(function(){
	TreeBASE.analysisEditor.editAnalysis = function (link,counter) {
		var table = link.parentNode.parentNode.parentNode;
		var $inputs = $(table).find('input');
		var firstTextBox;
		$inputs.each(function() {
			$(this).removeClass('disabled');
			this.readOnly = false;
			if ( $(this).css('display') == 'none' ) {
				$(this).css('display', 'inline');
			}
			if ( this.type == 'text' ) {
				$(this).addClass('enabled');
				if ( firstTextBox == null ) {
					firstTextBox = this;
				}
			}
		});
		firstTextBox.focus();
		firstTextBox.select();
		return false;
	};
	TreeBASE.analysisEditor.addStep = function (link) {
		link.parentNode.submit();
		return false;
	};
	TreeBASE.analysisEditor.submitIfNotReady = function (publicationState) {
		if ( publicationState == 'NotReady' ) {
			return true;
		}
		else {
			return false;
		}
	};
	TreeBASE.analysisEditor.addData = function (link) {
		var theDiv = link.parentNode;
		var selects = theDiv.getElementsByTagName('select');
		selects[0].style.display = 'block';
		link.style.display = 'none';
		return false;
	};
	TreeBASE.analysisEditor.addSelected = function (button,inputOutput,analysisStepId) {
		var theDiv = button.parentNode;
		var selects = theDiv.getElementsByTagName('select');
		var dataType = selects[1].name;
		var options = selects[1].getElementsByTagName('option');
		var theForm = '<form method="post" action="/treebase-web/user/addAnalyzedData.html" style="display:none">';
		theForm += '<textarea name="ids">';	
		for ( var i = 0; i < options.length; i++ ) {		
			if ( options[i].selected ) {
				theForm += ' ' + options[i].value;
			}
		}
		theForm += '</textarea>';
		theForm += '<input type="hidden" name="action" value="add"/>';
		theForm += '<input type="hidden" name="dataType" value="' + dataType + '"/>';
		theForm += '<input type="hidden" name="inputOutput" value="' + inputOutput + '"/>';
		theForm += '<input type="hidden" name="analysisStepId" value="' + analysisStepId + '"/>';
		theForm += '</form>';
		var childDivs = theDiv.getElementsByTagName('div');
		childDivs[0].innerHTML = theForm;
		var theChildForm = childDivs[0].getElementsByTagName('form');
		theChildForm[0].submit();
	};
	TreeBASE.analysisEditor.selectData = function (selector) {
		var theDiv = selector.parentNode;
		var selects = theDiv.getElementsByTagName('select');
		selects[0].style.display = 'none';
		selects[1].style.display = 'block';
		var innerOptions = '';
		if ( selector.value == 'Matrices' ) {
			var m = TreeBASE.submission.submittedMatrices;
			for ( var i = 0; i < m.length; i++ ) {
				if(typeof(m[i])!= "undefined"){
						innerOptions += '<option value=\"' + m[i].id + '\">' + m[i].title + '</option>'; 
				}
			}
		}
		else if ( selector.value == 'TreeBlocks' ) {
			var tb = TreeBASE.submission.submittedTreeBlocks;
			for ( var i = 0; i < tb.length; i++ ) {
				if(typeof(tb[i])!= "undefined"){
					innerOptions += '<option value=\"' + tb[i].id + '\">' + tb[i].title + '</option>'; 
				}
			}
		}
		else if ( selector.value == 'Trees' ) {
			var tb = TreeBASE.submission.submittedTreeBlocks;
			for ( var i = 0; i < tb.length; i++ ) {
				if(typeof(tb[i])!= "undefined"){
					var t = tb[i].treeList;
					for ( var j = 0; j < t.length; j++ ) {
						if(typeof(t[j])!= "undefined"){
							innerOptions += '<option value=\"' + t[j].id + '\">' + t[j].label + '</option>';
						}
					}
			    }
			}	
		}
		
		$(selects[1]).html(innerOptions);
		selects[1].name = selector.value;
		var inputs = theDiv.getElementsByTagName('input');
		inputs[0].style.display = 'block';
	};
	TreeBASE.analysisEditor.checkValue = function(form,counter,publicationState) {
		if ( publicationState != 'NotReady' ) {
			return false;
		}	
		if ( form.algorithmType.value == "other algorithm" ) {
			var newAlgorithm = form['algorithmType'+counter].value;
			if ( newAlgorithm.length <= 0 ) {
				alert("Algorithm description must not be null when 'other algorithm' is selected");
				return false;
			}
		}
		form.submit();
	};
	TreeBASE.analysisEditor.checkOther = function(selectWidget,counter) {
		var options = selectWidget.getElementsByTagName('option');
		for ( var i = 0; i < options.length; i++ ) {
			if ( options[i].selected ) {
				var $theDiv = $('#ac' + counter);
				if ( options[i].value == 'other algorithm' ) {
					alert("Please describe the other algorithm you used");
					$theDiv.css('display', 'inline');
					$('#algorithmType'+counter).focus();
					$('#algorithmType'+counter).select();
				}
				else {
					$theDiv.css('display', 'none');
				}
			}
		}
	};
	TreeBASE.analysisEditor.enableEdit = function(counter) {
		var $softwareForm = $( '#form' + counter );
		var $inputs = $softwareForm.find('input, textarea');
		var firstTextBox;
		$inputs.each(function() {
			this.readOnly = false;
			$(this).removeClass('disabled');
			if ( this.type == 'text' ) {
				$(this).addClass('enabled');
				if ( firstTextBox == null ) {
					firstTextBox = this;
				}
			}
			if ( this.type == 'submit' ) {
				$(this).css('display', 'inline');
			}		
		});
		$( '#algorithmTypeInput' + counter ).css('display', 'none');
		$( '#algorithmSelectWidget' + counter ).css('display', 'inline');
		$( '[id="softwareInfo.softwareURL' + counter + '"]' ).css('display', 'inline');
		var $softwareLink = $( '[id="softwareInfo.softwareLink' + counter + '"]' );
		if ( $softwareLink.length > 0 ) {
			$softwareLink.css('display', 'none');
		}
		firstTextBox.focus();
		firstTextBox.select();
		return false;
	};		
})();