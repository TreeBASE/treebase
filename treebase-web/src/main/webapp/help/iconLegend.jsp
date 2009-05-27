<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="icons.legend"/></title>
<content tag="heading"><fmt:message key="icons.legend"/></content>
<body id="submissions"/>

<p>The TreeBASE website uses a number of shortcut buttons for common operations. This page lists
these buttons and describes what they do.</p>
<p>
	<img src="<fmt:message key="icons.edit"/>" alt="Edit icon"/> leads to a page
	where more detailed information about the selected record can be entered.
</p>
<p>
	<img src="<fmt:message key="icons.delete"/>" alt="Delete icon"/> deletes the selected record.
</p>
<p>
	<img src="<fmt:message key="icons.list"/>" alt="List icon"/> leads to a page that lists more
	detailed information about the current record.
</p>
<p>
	<img src="<fmt:message key="icons.notanalyzed"/>" alt="Warning icon"/> indicates that metadata about
	the current record is incomplete. If the record is a tree or a character matrix this happens when the
	record doesn't take part in any analysis (as input or output); if the record is a taxon label this happens
	when the label hasn't been validated (and linked to uBio and the NCBI taxonomy).
</p>
<p>
	<img src="<fmt:message key="icons.analyzed"/>" alt="Complete icon"/> indicates that metadata about
	the current record is complete, no further steps are necessary for the record to become part of
	a complete and ready submission.
</p>
<p>
	<img src="<fmt:message key="icons.download.original"/>" alt="Download original file icon"/> downloads
	the originally submitted file that contained the selected record.
</p>
<p>
	<img src="<fmt:message key="icons.download.reconstructed"/>" alt="Download reconstructed file icon"/> downloads
	a newly created nexus file that represents the selected record.
</p>
<p>
	<img src="<fmt:message key="icons.tree.edit"/>" alt="Tree edit icon"/> opens a tree editor.
</p>
<p>
	<img src="<fmt:message key="icons.xhtml"/>" alt="XHTML validation icon"/> validates the current page as XHTML.
</p>
<p>
	<img src="<fmt:message key="icons.css"/>" alt="CSS validation icon"/> validates the current CSS styling syntax.
</p>