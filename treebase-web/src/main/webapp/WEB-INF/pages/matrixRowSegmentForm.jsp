<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="matrix.row.segment.selection"/></title>
<content tag="heading"><fmt:message key="matrix.row.segment.selection"/></content>
<body id="submissions"/>

<p>Fill in the following information to create a new row segment</p>

<spring:bind path="arowsegment.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post" name="RowSegmentForm">
<input type="hidden" name="arowsegment.id" value="${status.value}"/>

<fieldset>
<legend>Matrix Row Segment Information
<a href="#" class="openHelp" onclick="openHelp('matrixRowSegmentForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<table border="0" cellpadding="5" cellspacing="5">
	<tr>
        <th align="right"><fmt:message key="matrix.row.taxon.label"/>:</th>
        <td colspan="3">
        	<c:if test="${empty arowsegment.taxonLabel.id}">
            	<c:out value="${arowsegment.taxonLabel.taxonLabel}"/>
            </c:if>
            <c:if test="${not empty arowsegment.taxonLabel.id}">
            	<a href="<c:url value="/user/editTaxonLabel.html"/>?taxonlabelid=<c:out value="${arowsegment.matrixRow.taxonLabel.id}"/>"><c:out value="${arowsegment.matrixRow.taxonLabel.taxonLabel}"/></a>
        	</c:if>
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="matrix.row.segment.title"/>:</th>
        <td colspan="3">
            <spring:bind path="arowsegment.title">
            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>    
    <tr>
    	<th align = "right">&nbsp;</th>
    	<td colspan="3">
    		<font color="blue" >You can highlight the segment from data below and press the button on right to select</font>
    		<input type="button" value="Select" name="selectbutton" onclick="addPTag(document.getElementById('rowtext'),'Segment')"></input>
    	</td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="matrix.row.data"/>:</th>
        <td colspan="3">
            <spring:bind path="arowsegment.matrixRow.symbolString">
            <textarea id="rowtext" readonly="readonly" rows="3" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>       
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.startindex"/>:</th>
        <td>
            <spring:bind path="arowsegment.startIndex">
            <input type="text" class="textCell" style="width:100%" maxlength="10" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>      
        <th align="right"><fmt:message key="rowsegment.endindex"/>:</th>
        <td>
            <spring:bind path="arowsegment.endIndex">
            <input type="text" class="textCell" style="width:100%" maxlength="10" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="matrix.row.data.selected"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.segmentData">                    
            <input type="text" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.instAcronym"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.instAcronym">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.collectioncode"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.collectionCode">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.catalognumber"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.catalogNumber">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.genbankid"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.genBankAccession">                    
            <input type="text" class="textCell" style="width:100%" maxlength="15" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.otherAccession"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.otherAccession">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.sampledate"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.sampleDateString">                    
            <input type="text" class="textCell" style="width:50%" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
           	<c:out value="Format: YYYY-MM-DD (Example: 2000-02-25)"/>
        </td>
    </tr>
    <tr>
        <th align = "right"><fmt:message key="rowsegment.sampletaxonlabel"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenTaxonLabelAsString">                    
            	<c:out value="${status.value}"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="rowsegment.collector"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.collector">                    
            <input type="text" class="textCell" style="width:100%" maxlength="255" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="rowsegment.latitude"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.latitude">                    
            <input type="text" class="textCell" style="width:10%" maxlength="8" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.longitude"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.longitude">                    
            <input type="text" class="textCell" style="width:10%" maxlength="8" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.elevation"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.elevation">                    
            <input type="text" class="textCell" style="width:10%" maxlength="8" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.country"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.country">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.state"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.state">                    
            <input type="text" class="textCell" style="width:100%" maxlength="50" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.locality"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.locality">                    
            <input type="text" class="textCell" style="width:100%" maxlength="255" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr>    
    <tr>
        <th align="right"><fmt:message key="rowsegment.notes"/>:</th>
        <td colspan="3">
        	<spring:bind path="arowsegment.specimenLabel.notes">          	
        	<textarea rows="3" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>          
        </td>
    </tr> 
    <tr>   
		<th></th>
		<td colspan="3">
		  <table align="center">
		  	<tr>
		    	<th></th>
		    	<td>
		 			<c:choose>
		 				<c:when test="${arowsegment.id == null}">
		 					<input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
		 				</c:when>
		 				<c:otherwise>
		 					<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
		 					<input type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />
		 				</c:otherwise>
		 			</c:choose>
			        <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
			        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" />
		        </td>
		    </tr>
		  </table>
	 	 </td>
  		</tr>
    </table>
  </fieldset> 
</form>

<script type="text/javascript">
function getSelection(ta)
  { var bits = [ta.value,'','','']; 
    if(document.selection)
      { var vs = '#$%^%$#';
        var tr=document.selection.createRange()
        if(tr.parentElement()!=ta) return null;
        bits[2] = tr.text;
        tr.text = vs;
        fb = ta.value.split(vs);
        tr.moveStart('character',-vs.length);
        tr.text = bits[2];
        bits[1] = fb[0];
        bits[3] = fb[1];
      }
    else
      { if(ta.selectionStart == ta.selectionEnd) return null;
        bits=(new RegExp('([\x00-\xff]{'+ta.selectionStart+'})([\x00-\xff]{'+(ta.selectionEnd - ta.selectionStart)+'})([\x00-\xff]*)')).exec(ta.value);
      }
     return bits;
  }

function matchPTags(str)
  { str = ' ' + str + ' ';
    ot = str.split(/\[[B|U|I].*?\]/i);
    ct = str.split(/\[\/[B|U|I].*?\]/i);
    return ot.length==ct.length;
  }

function addPTag(ta,pTag)
  { 
  	var startTag = "[Segment]";
  	var endTag = "[/Segment]";
  	var startCol = 0;
  	var endCol = 0;
  	
  	/* save the original data */
  	var original = ta.value;
  	
  	bits = getSelection(ta);
    if(bits)
      { if(!matchPTags(bits[2]))
          { alert('\t\tInvalid Selection\nSelection contains unmatched opening or closing tags.');
            return;
          }
        /* this insert <Segment> and </Segment> around the selected text */
        ta.value = bits[1] + '[' + pTag + ']' + bits[2] + '[/' + pTag + ']' + bits[3];
        
        /* determine the cols */
        startCol = ta.value.indexOf(startTag);
        endCol = ta.value.indexOf(endTag) - (endTag.length) ;
    	ta.value = original; /* do not show the inserted tags */
    	
    	var theForm = document.forms["RowSegmentForm"];
    	theForm.elements["startIndex"].value = startCol;
    	theForm.elements["endIndex"].value = endCol;
    	theForm.elements["segmentData"].value = original.substring(startCol, endCol +1);
      }
  }
  
</script>
