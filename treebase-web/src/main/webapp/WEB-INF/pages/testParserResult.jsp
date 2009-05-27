<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Nexus Parser Test Result</title>
<content tag="heading">Tree View</content>

<script type="text/javascript" src="../scripts/xp_progress.js"></script>

<script type="text/javascript">

function show_or_hide(layer_ref, state) { 
  if (document.all) { //IS IE 4 or 5 (or 6 beta) 
    eval( "document.all." + layer_ref + ".style.display = state"); 
  } 
  else if (document.layers) { //IS NETSCAPE 4 or below 
    document.layers[layer_ref].display = state; 
  } 
  else if (document.getElementById && !document.all) { 
    hza = document.getElementById(layer_ref); 
    hza.style.display = state; 
  } 
} 


</script>
<style type="text/css">

 h2 span   {margin-left: 2em;  background: #FFE4B5;}
 h3 span   {margin-left: 6em;  background: #B0E0E6;}
 h4 span1  {margin-left: 10em; background: #ffcccc;}
 h4 span2  {margin-left: 13em; background: #F0E68C;}
 h4 span3  {margin-left: 16em; background: #ccffff;}
 h4 span4  {margin-left: 19em; background: #ccff99;}
 h4 span5  {margin-left: 22em; background: #FFEBCD;}
 h4 span6  {margin-left: 25em; background: #E0FFFF;}
 h4 span7  {margin-left: 28em; background: #ffff99;}
 h4 span8  {margin-left: 31em; background: #E6E6FA;}
 h4 span9  {margin-left: 34em; background: #FFEFD5;}
 h4 span10 {margin-left: 37em; background: #ccffff;}
 h4 span11 {margin-left: 40em; background: #FAFAD2;}
 h4 span12 {margin-left: 43em; background: #F0FFF0;}
 h4 span13 {margin-left: 46em; background: #FFFFF0;}
 h4 span14 {margin-left: 49em; }
 h4 span15 {margin-left: 52em; }
 h4 span16 {margin-left: 55em; }
 h4 span17 {margin-left: 58em; }
 h4 span18 {margin-left: 61em; }
  
 h2.main { background: #F0F8FF; width: 96%; text-align: center;
    font-family: "Times Roman";  padding: 4Px; }
 p.caption {color: black; background: #EEE; width: 200Px;
   text-align: center; font-size: 12Px; padding: 3Px;}
   
 a:link, a:visited, a:after {
	background: transparent;
	font-size: 11pt;
	text-decoration: underline;
 }
 
 </style>
</head>

<body id="submissions">


<form method="post" >

<p>The result of the Nexus Parsing: </p>

<fieldset>
<legend>Study Details</legend>

<table border="0" cellpadding="5" cellspacing="5">

	<tr>
		<th>Study ID:</th>
		<td><c:out value="${study.id}"/></td>
       	<td>
       		<strong>Study Name: </strong>
        
            <spring:bind path="study.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
	</tr>	
	<tr>
    </tr>
    </table>
    
    <c:url var="xmlURL" value="/test/viewXML.html" />
    <c:url var="matrixURL" value="/test/matrixList.html" />
	
	<a href="<c:out value="${xmlURL}"/>" target = "_blank">View Phylotree(s) in XML format</a> <br/>
    <a href="<c:out value="${matrixURL}"/>" target = "_blank">View Matrices</a> <br/><br/>
   			 			
   			<h2 class = "main"> There 
   				<c:if test="${treesSize > 1}" > are </c:if>
   				<c:if test="${treesSize == 1}" > is </c:if>
   				<c:out value="${treesSize}"/> tree<c:if test="${treesSize > 1}">s</c:if>
   				 displayed below.  </h2> <br/>
   				 
        		<c:forEach var="tree" items="${trees}">

        			<h2 class = "main">Title--<c:out value="${tree.title}"/>  </h2> <br/>	
        			<h2 class = "main">Root-<c:out value="${tree.rootNode.id}"/> </h2>	<br/>
        				
        				<c:forEach var="childnode_1" items="${tree.rootNode.childNodes}">
        				  
        				  <h2><span>L1-----------------------------------------------------------------------<c:out value="${childnode_1.id}"/><c:if test="${childnode_1.branchLength ne 0.0}">:<c:out value="${childnode_1.branchLength}"/></c:if><c:if test="${childnode_1.taxonLabelAsString ne null}">-<c:out value="${childnode_1.taxonLabelAsString}"/> </c:if> </span> </h2> 
        				    
        				    <c:forEach var="childnode_2" items="${childnode_1.childNodes}">
        				    	
        				    	<h3><span>L2-------------------------------------------------------------------------------------<c:out value="${childnode_2.id}"/><c:if test="${childnode_2.branchLength ne 0.0}">:<c:out value="${childnode_2.branchLength}"/></c:if><c:if test="${childnode_2.taxonLabelAsString ne null}">-<c:out value="${childnode_2.taxonLabelAsString}"/> </c:if> </span></h3>
        				    	<c:forEach var="childnode_3" items="${childnode_2.childNodes}">
        				    	
        				    		<h4><span1>L3----------------------------------------------------------------------------------------------<c:out value="${childnode_3.id}"/><c:if test="${childnode_3.branchLength ne 0.0}">:<c:out value="${childnode_3.branchLength}"/></c:if><c:if test="${childnode_3.taxonLabelAsString ne null}">-<c:out value="${childnode_3.taxonLabelAsString}"/>	</c:if></span2> </h4>		    		
        							<c:forEach var="childnode_4" items="${childnode_3.childNodes}">
        				    	
        				    			<h4><span2>L4-----------------------------------------------------------------------------------------<c:out value="${childnode_4.id}"/><c:if test="${childnode_4.branchLength ne 0.0}">:<c:out value="${childnode_4.branchLength}"/></c:if><c:if test="${childnode_4.taxonLabelAsString ne null}">-<c:out value="${childnode_4.taxonLabelAsString}"/></c:if></span2> </h4>
        				    			<c:forEach var="childnode_5" items="${childnode_4.childNodes}">
        				    	
        				    				<h4><span3>L5-----------------------------------------------------------------------------------<c:out value="${childnode_5.id}"/><c:if test="${childnode_5.branchLength ne 0.0}">:<c:out value="${childnode_5.branchLength}"/></c:if><c:if test="${childnode_5.taxonLabelAsString ne null}">-<c:out value="${childnode_5.taxonLabelAsString}"/></c:if></span3> </h4>
        				    				<c:forEach var="childnode_6" items="${childnode_5.childNodes}">
        				    	
        				    					<h4><span4>L6------------------------------------------------------------------------------<c:out value="${childnode_6.id}"/><c:if test="${childnode_6.branchLength ne 0.0}">:<c:out value="${childnode_6.branchLength}"/></c:if><c:if test="${childnode_6.taxonLabelAsString ne null}">-<c:out value="${childnode_6.taxonLabelAsString}"/></c:if></span4> </h4>
        				    					<c:forEach var="childnode_7" items="${childnode_6.childNodes}">
        				    	
        				    						<h4><span5>L7------------------------------------------------------------------------<c:out value="${childnode_7.id}"/><c:if test="${childnode_7.branchLength ne 0.0}">:<c:out value="${childnode_7.branchLength}"/></c:if><c:if test="${childnode_7.taxonLabelAsString ne null}">-<c:out value="${childnode_7.taxonLabelAsString}"/></c:if></span5> </h4>
        				    						<c:forEach var="childnode_8" items="${childnode_7.childNodes}">
        				    	
        				    							<h4><span6>L8------------------------------------------------------------------<c:out value="${childnode_8.id}"/><c:if test="${childnode_8.branchLength ne 0.0}">:<c:out value="${childnode_8.branchLength}"/></c:if><c:if test="${childnode_8.taxonLabelAsString ne null}">-<c:out value="${childnode_8.taxonLabelAsString}"/> </c:if> </span6> </h4>
        				    							<c:forEach var="childnode_9" items="${childnode_8.childNodes}">
        				    	
        				    								<h4><span7>L9-------------------------------------------------------------<c:out value="${childnode_9.id}"/><c:if test="${childnode_9.branchLength ne 0.0}">:<c:out value="${childnode_9.branchLength}"/></c:if><c:if test="${childnode_9.taxonLabelAsString ne null}">-<c:out value="${childnode_9.taxonLabelAsString}"/> </c:if> </span7> </h4>
        				    								<c:forEach var="childnode_10" items="${childnode_9.childNodes}">
        				    	
        				    									<h4><span8>L10------------------------------------------------------<c:out value="${childnode_10.id}"/><c:if test="${childnode_10.branchLength != 0.0}">:<c:out value="${childnode_10.branchLength}"/></c:if><c:if test="${childnode_10.taxonLabelAsString ne null}">-<c:out value="${childnode_10.taxonLabelAsString}"/> </c:if> </span8> </h4>
        				    									<c:forEach var="childnode_11" items="${childnode_10.childNodes}">
        				    	
        				    										<h4><span9>L11-------------------------------------------------<c:out value="${childnode_11.id}"/><c:if test="${childnode_11.branchLength != 0.0}">:<c:out value="${childnode_11.branchLength}"/></c:if><c:if test="${childnode_11.taxonLabelAsString ne null}">-<c:out value="${childnode_11.taxonLabelAsString}"/> </c:if> </span9> </h4>
        				    										<c:forEach var="childnode_12" items="${childnode_11.childNodes}">
        				    	
        				    											<h4><span10>L12-------------------------------------------<c:out value="${childnode_12.id}"/><c:if test="${childnode_12.branchLength != 0.0}">:<c:out value="${childnode_12.branchLength}"/></c:if><c:if test="${childnode_12.taxonLabelAsString ne null}">-<c:out value="${childnode_12.taxonLabelAsString}"/></c:if></span10> </h4>
        				    											<c:forEach var="childnode_13" items="${childnode_12.childNodes}">
        				    	
        				    												<h4><span11>L13---------------------------------------<c:out value="${childnode_13.id}"/><c:if test="${childnode_13.branchLength != 0.0}">:<c:out value="${childnode_13.branchLength}"/></c:if><c:if test="${childnode_13.taxonLabelAsString ne null}">-<c:out value="${childnode_13.taxonLabelAsString}"/> </c:if> </span11> </h4>
        				    												<c:forEach var="childnode_14" items="${childnode_13.childNodes}">
        				    	
        				    													<h4><span12>L14----------------------------------<c:out value="${childnode_14.id}"/><c:if test="${childnode_14.branchLength != 0.0}">:<c:out value="${childnode_14.branchLength}"/></c:if><c:if test="${childnode_14.taxonLabelAsString ne null}"> -<c:out value="${childnode_14.taxonLabelAsString}"/> </c:if> </span12> </h4>
        				    													<c:forEach var="childnode_15" items="${childnode_14.childNodes}">
        				    	
        				    														<h4><span13>L15----------------------------<c:out value="${childnode_15.id}"/><c:if test="${childnode_15.branchLength != 0.0}">:<c:out value="${childnode_15.branchLength}"/></c:if><c:if test="${childnode_15.taxonLabelAsString ne null}">-<c:out value="${childnode_15.taxonLabelAsString}"/> </c:if> </span13> </h4>
        				    														<c:forEach var="childnode_16" items="${childnode_15.childNodes}">
        				    	
        				    															<h4><span14>L16-----------------------<c:out value="${childnode_16.id}"/><c:if test="${childnode_16.branchLength != 0.0}">:<c:out value="${childnode_16.branchLength}"/></c:if><c:if test="${childnode_16.taxonLabelAsString ne null}">-<c:out value="${childnode_16.taxonLabelAsString}"/> </c:if> </span14> </h4>
        				    															<c:forEach var="childnode_17" items="${childnode_16.childNodes}">
        				    	
        				    																<h4><span15>L17------------------<c:out value="${childnode_17.id}"/><c:if test="${childnode_17.branchLength != 0.0}">:<c:out value="${childnode_17.branchLength}"/></c:if><c:if test="${childnode_17.taxonLabelAsString ne null}">-<c:out value="${childnode_17.taxonLabelAsString}"/> </c:if> </span15> </h4>
        				    																<c:forEach var="childnode_18" items="${childnode_17.childNodes}">
        				    	
        				    																	<h4><span16>L18------------<c:out value="${childnode_18.id}"/><c:if test="${childnode_18.branchLength != 0.0}">:<c:out value="${childnode_18.branchLength}"/></c:if><c:if test="${childnode_18.taxonLabelAsString ne null}">-<c:out value="${childnode_18.taxonLabelAsString}"/> </c:if> </span16> </h4>
        				    																	<c:forEach var="childnode_19" items="${childnode_18.childNodes}">
        				    	
        				    																		<h4><span17>L19------<c:out value="${childnode_19.id}"/><c:if test="${childnode_19.branchLength != 0.0}">:<c:out value="${childnode_19.branchLength}"/></c:if><c:if test="${childnode_19.taxonLabelAsString ne null}">-<c:out value="${childnode_19.taxonLabelAsString}"/> </c:if> </span17> </h4>
        				    																		<c:forEach var="childnode_20" items="${childnode_19.childNodes}">
        				    	
        				    																			<h4><span18>L20---<c:out value="${childnode_20.id}"/><c:if test="${childnode_20.branchLength != 0.0}">:<c:out value="${childnode_20.branchLength}"/></c:if><c:if test="${childnode_20.taxonLabelAsString ne null}">-<c:out value="${childnode_20.taxonLabelAsString}"/> </c:if> </span18> </h4>
        																							</c:forEach>
        																						</c:forEach>
        																					</c:forEach>
        																				</c:forEach>
        																			</c:forEach>
        																		</c:forEach>
        																	</c:forEach>
        																</c:forEach>
        															</c:forEach>
        														</c:forEach>
        													</c:forEach>
        												</c:forEach>
        											</c:forEach>
        										</c:forEach>
        									</c:forEach>
        								</c:forEach>
        							</c:forEach>
        						
        						</c:forEach>
        					</c:forEach>
        				</c:forEach>
        				
        		</c:forEach>
        	
 
    <table>
    <!--  tr>
    	<th>&nbsp;</th>
    	<td>

    		<input type="submit" name="Delete" value="<fmt:message key="button.clean"/>" onclick="show_or_hide('kids', 'block');" />
    	</td>
    </tr -->
    <!-- tr>
    		<th>&nbsp;</th>
    		<td>
    		Pressing this button <br/>
    		will erase the tree(s)<br/>
    		related to this <br/>
    		particular study.
    		</td>
    	
    </tr -->	
</table>

<!--  <div id="kids" style="display: none;">
		<h3>Cleaning ...</h3>
        <script type="text/javascript">
          var bar1= createBar(500,15,'white',1,'black','#7DCBDA',85,7,3,"");
        </script>
</div> -->

</fieldset>
</form>
</body>

