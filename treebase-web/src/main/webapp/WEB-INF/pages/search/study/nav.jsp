<%@ include file="/common/taglibs.jsp"%>
<c:set var="cit" value="${study.citation}"/>
<div style="display:none" id="bibtex">
<c:out value="${study.citation.bibtexReference}"/>
</div>
<div style="padding-bottom:60px">
<ul id="s-nav">
	<li>
		<a href="/treebase-web/search/studySearch.html">
			<img class="iconButton" src="<fmt:message key="icons.back"/>" alt="Back"/>	
			Go to search
		</a>
	</li>
	<li id="st-study">
		<a href="summary.html?id=<c:out value="${study.id}"/>">
			<img class="iconButton" src="<fmt:message key="icons.citation"/>" alt="Citation"/>	
			Citation
		</a>
	</li>
	<li id="st-taxon">	
		<a href="taxa.html?id=<c:out value="${study.id}"/>">
			<img class="iconButton" src="<fmt:message key="icons.taxa"/>" alt="Taxa"/>	
			Taxa
		</a>
	</li>
	<li id="st-matrix">
		<a href="matrices.html?id=<c:out value="${study.id}"/>">
			<img class="iconButton" src="<fmt:message key="icons.matrices"/>" alt="Matrices"/>		
			Matrices
		</a>
	</li>
	<li id="st-tree">
		<a href="trees.html?id=<c:out value="${study.id}"/>">
			<img class="iconButton" src="<fmt:message key="icons.trees"/>" alt="Trees"/>	
			Trees
		</a>
	</li>
	<li id="st-analysis">
		<a href="analyses.html?id=<c:out value="${study.id}"/>">
			<img class="iconButton" src="<fmt:message key="icons.analyses"/>" alt="Analyses"/>	
			Analyses
		</a>
	</li>
	<li style="background-color:transparent">
  		<a href="#" class="openHelp" style="background-color:transparent; border:none" onclick="openHelp('summaryTabs')">
  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
  		</a>		
	</li>	
</ul>
</div>

<div style="float:right;background-color:white;padding-bottom:10px;padding-left:10px;margin-left:10px;margin-bottom:10px">
	<fieldset style="margin-top:0px;padding-top:10px; margin-bottom:0px;padding-bottom:10px">
		
		<!--  save to citeulike -->
		<form id="citeulike" enctype="multipart/form-data" method="post" action="" 
			title="Enter your CiteULike user name and click the logo to post this study to your CiteULike account">
			<a href="#" onclick="citeulike()">
				<img src="/treebase-web/images/citeulike.gif" alt="CiteULike" class="iconButton"/>
				CiteULike
			</a>			
			<input 
				type="text" 
				id="citeuser" 
				value="enter your citeulike user name" 
				class="textCell" 
				style="width:50px;border:1px solid red;display:none"/>
			<input type="hidden" name="pasted" id="pasted"/>
			<input type="hidden" name="to_read" value="2"/>
			<input type="hidden" name="tag" value="TreeBASE2Import"/>
			<input type="hidden" name="tag_parsing" value="delimited"/>
			<input type="hidden" name="private" value="t"/>
			<input type="hidden" name="update_allowed" value="t"/>
			<input type="hidden" name="btn_bibtex" value="Import BibTeX file..." />
		</form>
		
		<!-- save to delicious -->		
		<a href="http://delicious.com/save" onclick="window.open('http://delicious.com/save?v=5&amp;noui&amp;jump=close&amp;url='+encodeURIComponent(location.href)+'&amp;title='+encodeURIComponent(document.title), 'delicious','toolbar=no,width=550,height=550'); return false;">
			<img src="/treebase-web/images/delicious.jpg" alt="Delicious" class="iconButton"/>
			Delicious
		</a>
		
		<!-- save to connotea -->
		<form method="post" id="connotea" action="http://www.connotea.org/add" enctype="application/x-www-form-urlencoded" name="edit">
		
		    <input 
		        name="uri" 
		        value="<%= request.getScheme() %>://<%= request.getLocalName() %>:<%= request.getLocalPort() %>/treebase-web/search/study/summary.html?id=<c:out value="${study.id}"/>" 
		        type="hidden"/>
		
		    <input 
		        name="uri" 
		        value="<%= request.getScheme() %>://<%= request.getLocalName() %>:<%= request.getLocalPort() %>/treebase-web/search/study/summary.html?id=<c:out value="${study.id}"/>" 
		        type="hidden"/>
		
		    <input name="ctitle" value="<c:out value="${cit.title}"/>"  type="hidden"/>		        
		    <input name="ctitle2" value="<c:out value="${cit.title}"/>" type="hidden"/>

			<c:choose>		
				<c:when test="${cit.citationType == 'Article'}">
				    <input name="cjournal" value="<c:out value="${cit.journal}"/>" type="hidden"/>       
				    <input name="cjournal2" value="<c:out value="${cit.journal}"/>" type="hidden"/>
				
				    <input name="cvolume" value="<c:out value="${cit.volume}"/>" type="hidden"/> 
				    <input name="cvolume2" value="<c:out value="${cit.volume}"/>" type="hidden"/>
				
				    <input name="cissue" value="<c:out value="${cit.issue}"/>" type="hidden"/>      
				    <input name="cissue2" value="<c:out value="${cit.issue}"/>" type="hidden"/>
		    	</c:when>
		    	<c:otherwise>
				    <input name="cjournal" value="" type="hidden"/>       
				    <input name="cjournal2" value="" type="hidden"/>
				
				    <input name="cvolume" value="" type="hidden"/> 
				    <input name="cvolume2" value="" type="hidden"/>
				
				    <input name="cissue" value="" type="hidden"/>      
				    <input name="cissue2" value="" type="hidden"/>		    	
		    	</c:otherwise>
		    </c:choose>
		
		    <input name="cpages" value="<c:out value="${cit.pages}"/>" type="hidden"/>
		    <input name="cpages2" value="<c:out value="${cit.pages}"/>" type="hidden"/>
		
		    <input name="cdate" value="<c:out value="${cit.publishYear}"/>" type="hidden"/>
		    <input name="cdate2" value="<c:out value="${cit.publishYear}"/>" type="hidden"/>
		    
		    <input name="cauthors" value="<c:out value="${cit.authorsAsBibtex}"/>" type="hidden"/>
		    <input name="cauthors2" value="<c:out value="${cit.authorsAsBibtex}"/>" type="hidden"/>
			
			<!-- JOUR, BOOK or CHAP --><!-- XXX check if article!! -->
			<input name="cristype" value="JOUR" type="hidden"/>
		    <input name="cristype2" value="JOUR" type="hidden"/>
		
	        <input name="cdoi" value="<c:out value="${cit.doi}"/>" type="hidden"/>
	        <input name="cdoi2" value="<c:out value="${cit.doi}"/>" type="hidden"/>
	
	        <input name="cpubmed" value="<c:out value="${cit.PMID}"/>" type="hidden"/>
	        <input name="cpubmed2" value="<c:out value="${cit.PMID}"/>" type="hidden"/>
	
	        <!-- isbn -->
	        <input name="casin" value="" type="hidden"/> <!-- XXX check if book/in book -->
	        <input name="casin2" value="" type="hidden"/>
	
	        <input name="usertitle" value="<c:out value="${cit.title}"/>" type="hidden"/>
	
	        <input name="tags" value="uploaded, <c:out value="${cit.keywords}"/>" type="hidden"/>
	        <input name="button" value="Save" type="hidden"/>
	
	        <input name="description" value="<c:out value="${study.notes}"/>" type="hidden"/>
	        <input name="mywork" value="0" type="hidden"/>
	        <input name="private" value="0" type="hidden"/>
	        <input name="embargo" value="" type="hidden"/>
	
			<c:choose>
				<c:when test="${not empty study.TB1StudyID}">	
						<input name="comment" value="This study was previously identified under the legacy study ID ${study.TB1StudyID} (Status: <c:out value="${study.studyStatus.description}"/>)." type="hidden"/>
				</c:when>
				<c:otherwise>	
						<input name="comment" value="This study is part of submission <c:out value="${study.submission.id}"/> (Status: <c:out value="${study.studyStatus.description}"/>)." type="hidden"/>
				</c:otherwise>
			</c:choose>
	
			<input name="button" value="Save" type="hidden"/>		
			<input name="continue" value="0" type="hidden"/>
			<input name="title" value="<c:out value="${cit.title}"/>" type="hidden"/>
			<input name=".cgifields" value="private" type="hidden"/>
			<input name=".cgifields" value="mywork" type="hidden"/>
			<input name="prefilled" value="1" type="hidden"/>		
		</form>	
			<a href="#" onclick="connotea()">
				<img src="/treebase-web/images/connotea.gif" alt="Connotea" class="iconButton"/>
				Connotea
			</a>
		<div id="notice"></div>
	</fieldset>
</div>

<h2><c:out value="${headerPrefix}"/> Study ${study.id}</h2>

<div class="message" id="searchMessages">

<c:if test="${reviewerAccessGranted}">
  	<div>
  		<img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="icon" />
		<span style="color: red; ">You are in reviewer mode ${initialReviewerRedirect}.</span>
	</div>
</c:if>

<c:if test="${! empty study.citation.title}">
	<div>
		<img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="icon" />
		Citation title: <em>"${study.citation.title}"</em>.
	</div>
</c:if>

<c:if test="${! empty study.name}">
	<div>
		<img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="icon" />
		Study name: <em>"${study.citation.title}"</em>.
	</div>
</c:if>

<c:choose>
	<c:when test="${not empty study.TB1StudyID}">
		<div>
			<img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="icon" />	
			This study was previously identified under the legacy study ID ${study.TB1StudyID}  
			(Status: <c:out value="${study.studyStatus.description}"/>).
		</div>
	</c:when>
	<c:otherwise>
		<div>
			<img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="icon" />	
			This study is part of submission <c:out value="${study.submission.id}"/> 
			(Status: <c:out value="${study.studyStatus.description}"/>).
		</div>
	</c:otherwise>
</c:choose>
</div>
<h3>${currentSection}</h3>