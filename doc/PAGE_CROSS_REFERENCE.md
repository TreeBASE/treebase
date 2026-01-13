# Cross-Reference: treebase-servlet.xml Pages vs v3 User Story Documentation

This document cross-references the URL mappings in `treebase-servlet.xml` against the pages documented in the v3 UI/UX user story markdown files (`doc/v3/*.md`).

**Analysis Date:** 2026-01-13

**Source Documents:**
- `treebase-web/src/main/webapp/WEB-INF/treebase-servlet.xml`
- `doc/v3/user-story-01-search.md`
- `doc/v3/user-story-02-account.md`
- `doc/v3/user-story-03-submission.md`
- `doc/v3/user-story-04-review.md`
- `doc/v3/user-story-05-admin.md`
- `doc/v3/user-story-06-technical.md`
- `doc/v3/user-story-07-governance.md`

**Summary:**
- Total URL mappings in treebase-servlet.xml: 138
- Pages accounted for in user stories: 111
- Pages NOT in user stories: 27
  - Reachable from documented pages: 15
  - Unreachable (no links found): 12

---

## Pages NOT in User Story Documentation

The following pages from `treebase-servlet.xml` are NOT mentioned in any "Pages to Account For" section of the v3 user story documents.

**Reachability Analysis:** Pages marked as "(unreachable)" have no links found in JSP files, menu configuration, or controller redirects from any documented pages.

### Public/Utility Pages

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/accessviolation.html` | filenameController | Access violation error page | Reachable (controller redirects) |
| `/login.html` | filenameController | Login page (note: `/login.jsp` is documented instead) | (unreachable) |

### Alternative Search URL Patterns

These are search-related URLs that use different URL patterns than those documented in User Story 01 (which documents `/search/studySearch.html`, etc.):

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/search/` | studySearchController | Search root redirect | Reachable (redirect) |
| `/searchForm.html` | searchFormController | Search form (alternative URL pattern) | (unreachable) |
| `/searchResult.html` | filenameController | Search results (alternative URL pattern) | (unreachable) |
| `/searchStudy.html` | searchStudyController | Study search (alternative to `/search/studySearch.html`) | Reachable (studyList.jsp) |
| `/searchStudyList.html` | listSearchStudyController | Study search list | (unreachable) |
| `/study-query.html` | updateStudyQueryController | Study query form | (unreachable) |

### Search Download/Export Pages

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/search/matrixRowList.html` | listMatrixRowController | Matrix row list view | Reachable (viewAllRowSegmentData.jsp) |
| `/search/summary.html` | summaryController | Search summary page | Reachable (changeStudyStatus.jsp) |
| `/search/taxonList.html` | listTaxaSearchController | Taxon list view | (unreachable) |

### User Submission Pages

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/user/analyses.html` | summaryController | Study analyses (duplicate of analysisDisplay) | Reachable (menu-config.xml) |
| `/user/authorForm.html` | authorFormController | Author edit form | Reachable (peopleList.jsp) |
| `/user/authorList.html` | authorFormController | Author list | (unreachable) |
| `/user/directToPhyloWidget.html` | directToPhyloWidgetController | Direct PhyloWidget link (alternate to directMapToPhyloWidget) | Reachable (analysisSection-Piece.jsp) |
| `/user/displaySubmissionMenu.html` | displaySubmissionMenuController | Submission menu display | (unreachable) |
| `/user/downloadANexusRCTFile.html` | downloadANexusRCTFileController | Download reconstructed NEXUS | Reachable (nexusFiles.jsp) |
| `/user/editorForm.html` | editorFormController | Editor edit form | Reachable (peopleList.jsp, citationForm-booksection.jsp) |
| `/user/nexusFiles.html` | nexusFilesController | View NEXUS files | Reachable (menu-config.xml) |
| `/user/readOnlyListTree.html` | readOnlyListTreeController | Read-only tree list | (unreachable) |
| `/user/submissionMain.html` | filenameController | Submission main page | (unreachable) |
| `/user/treeParser.html` | treeParserController | Tree parser | Reachable (testParser.jsp) |
| `/user/treeParserResult.html` | treeParserResultController | Tree parser results | Reachable (treeList.jsp) |
| `/user/viewXML.html` | filenameController | View XML output | Reachable (testParserResult.jsp) |

### Development/Test Pages

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/test/testParser.html` | treeParserController | Test parser (development) | (unreachable) |

### JSON/API Pages

| URL | Controller | Description | Reachability |
|-----|------------|-------------|--------------|
| `/appletInteraction.html` | appletInteractionController | Applet interaction handler | (unreachable) |
| `/top/**` | (OAI-PMH) | OAI-PMH interface (note: `/top/oai` documented but wildcard not) | Reachable (OAI-PMH endpoint) |

---

## Summary of Unreachable Pages

The following 12 pages have no links from documented pages and may be candidates for deprecation or need explicit documentation:

1. `/login.html` - Alternative to documented `/login.jsp`
2. `/searchForm.html` - Alternative search form
3. `/searchResult.html` - Alternative search results
4. `/searchStudyList.html` - Study search list
5. `/study-query.html` - Study query form
6. `/search/taxonList.html` - Taxon list view
7. `/user/authorList.html` - Author list
8. `/user/displaySubmissionMenu.html` - Submission menu display
9. `/user/readOnlyListTree.html` - Read-only tree list
10. `/user/submissionMain.html` - Submission main page
11. `/test/testParser.html` - Development test page
12. `/appletInteraction.html` - Applet interaction handler

---

## Pages Accounted For in User Stories

The following pages from `treebase-servlet.xml` ARE documented in the v3 user story "Pages to Account For" sections:

### User Story 01: Search (29 pages)
- `/search/studySearch.html`, `/search/matrixSearch.html`, `/search/treeSearch.html`
- `/search/taxonSearch.html`, `/search/treeTopSearch.html`, `/search/classificationSearch.html`
- `/search/study/summary.html`, `/search/study/matrices.html`, `/search/study/trees.html`
- `/search/study/taxa.html`, `/search/study/analyses.html`, `/search/study/analysis.html`
- `/search/study/matrix.html`, `/search/study/tree.html`, `/search/study/treeBlock.html`
- `/search/study/treeBlocks.html`, `/search/study/rowSegments.html`, `/search/study/rowSegmentsTSV.html`
- `/search/study/anyObjectAsRDF.rdf`, `/search/downloadATree.html`, `/search/downloadAMatrix.html`
- `/search/downloadANexusFile.html`, `/search/downloadAStudy.html`, `/search/downloadATreeBlock.html`
- `/search/downloadAnAnalysisStep.html`, `/search/downloadANexusRCTFile.html`
- `/phylows/study/**`, `/phylows/matrix/**`, `/phylows/tree/**`, `/phylows/taxon/**`

### User Story 02: Account (17 pages)
- `/register.html`, `/passwordForm.html`, `/resetPassword.html`
- `/user/updateProfile.html`, `/user/processUser.html`, `/user/submissionList.html`
- `/admin/adminSelectUsers.html`, `/admin/userList.html`, `/admin/overrideUserProfile.html`
- `/admin/adminUpdatingUserInfo.html`, `/admin/adminDeletingUserStepOne.html`
- `/admin/adminDeletingUserStepTwo.html`, `/admin/adminMergingUsers.html`, `/admin/userManagement.html`

### User Story 03: Submission (44 pages)
- `/user/submissionList.html`, `/user/studyForm.html`, `/user/summary.html`
- `/user/citationForm.html`, `/user/authorSearchForm.html`, `/user/addAuthor.html`
- `/user/uploadFile.html`, `/user/uploadFileSummary.html`, `/user/treeBlockList.html`
- `/user/treeList.html`, `/user/directMapToPhyloWidget.html`, `/user/deleteATree.html`
- `/user/deleteATreeBlock.html`, `/user/matrixList.html`, `/user/matrixRowList.html`
- `/user/matrixRowSegmentList.html`, `/user/matrixRowSegmentForm.html`
- `/user/uploadRowSegmentData.html`, `/user/rowSegmentDataTable.html`
- `/user/viewAllRowSegmentData.html`, `/user/exportRowSegmentData.html`
- `/user/exportRowSegmentTemplate.html`, `/user/deleteARowSegment.html`
- `/user/deleteAMatrix.html`, `/user/taxaList.html`, `/user/editTaxonLabel.html`
- `/user/editSetTaxonLabel.html`, `/user/analysisList.html`, `/user/analysisDisplay.html`
- `/user/analysisForm.html`, `/user/analysisStepList.html`, `/user/analysisStepForm.html`
- `/user/analyzedDataList.html`, `/user/analyzedDataForm.html`, `/user/addAnalyzedData.html`
- `/user/readyState.html`, `/user/deleteStudy.html`, `/submitTutorial.html`
- `/user/downloadATree.html`, `/user/downloadATreeBlock.html`
- `/user/downloadAMatrix.html`, `/user/downloadANexusFile.html`
- `/json/submissionIsland.html`

### User Story 04: Review (8 pages)
- `/search/study/summary.html`, `/search/study/trees.html`, `/search/study/tree.html`
- `/search/study/matrices.html`, `/search/study/matrix.html`, `/search/study/taxa.html`
- `/search/study/analyses.html`, `/search/study/analysis.html`

### User Story 05: Administration (17 pages)
- `/admin/administrationPage.html`, `/admin/userManagement.html`, `/admin/readyStateStudies.html`
- `/admin/searchBySubmissionID.html`, `/admin/selectStudies.html`, `/admin/changeStudyStatus.html`
- `/admin/adminSelectUsers.html`, `/admin/userList.html`, `/admin/adminUpdatingUserInfo.html`
- `/admin/overrideUserProfile.html`, `/admin/adminDeletingUserStepOne.html`
- `/admin/adminDeletingUserStepTwo.html`, `/admin/adminMergingUsers.html`
- `/admin/adminSelectPersons.html`, `/admin/personList.html`, `/admin/adminMergingPersons.html`
- `/admin/messageToAdminAfterAction.html`

### User Story 06: Technical (16 pages)
- `/urlAPI.html`, `/technology.html`, `/about.html`, `/contact.html`
- `/submitTutorial.html`, `/help.html`, `/sitemap.xml`
- `/phylows/study/**`, `/phylows/tree/**`, `/phylows/matrix/**`
- `/phylows/taxon/**`, `/phylows/classification/**`
- `/rss.xml`, `/feed.xml`
- `/search/searchResultsAsRDF.rdf`, `/top/oai`

### User Story 07: Governance (11 pages)
- `/home.html`, `/about.html`, `/people.html`, `/partnership.html`
- `/reference.html`, `/technology.html`, `/submitTutorial.html`
- `/urlAPI.html`, `/dataMan.html`, `/journal.html`, `/contact.html`

---

## Recommendations

### Pages to Consider Adding to User Stories

1. **Alternative Search URL Patterns** - These URLs (`/searchForm.html`, `/searchStudy.html`, etc.) may need to be documented in User Story 01 if they are actively used, or redirected to the documented URLs (`/search/studySearch.html`, etc.) for consistency

2. **Tree Parser** (`/user/treeParser.html`, `/user/treeParserResult.html`) - Consider adding to User Story 03

3. **Download Pages** - Some download endpoints are missing:
   - `/user/downloadANexusRCTFile.html`
   - This should be added to User Story 03 (Submission)

4. **Unreachable Pages** - The 12 unreachable pages may be candidates for deprecation or may need their links restored if they provide needed functionality

---

## Notes

1. **Duplicate URLs**: Some URLs appear in multiple user stories (e.g., `/user/submissionList.html` appears in both User Story 02 and 03). This is expected as pages serve multiple workflows.

2. **Static JSP vs Controller**: Pages using `filenameController` map directly to JSP files without custom controller logic.

3. **PhyloWS Wildcards**: Endpoints like `/phylows/study/**` cover multiple sub-paths for the REST API.

4. **login.jsp vs login.html**: The user stories reference `/login.jsp` but the servlet maps `/login.html` - this may be a routing inconsistency to investigate.
