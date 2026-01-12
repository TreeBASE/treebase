# Cross-Reference: treebase-servlet.xml Pages vs v3 User Story Documentation

This document cross-references the URL mappings in `treebase-servlet.xml` against the pages documented in the v3 UI/UX user story markdown files (`doc/v3/*.md`).

**Analysis Date:** 2026-01-12

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
- Pages accounted for in user stories: 99
- Pages NOT in user stories: 40

---

## Pages NOT in User Story Documentation

The following pages from `treebase-servlet.xml` are NOT mentioned in any "Pages to Account For" section of the v3 user story documents:

### Public/Utility Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/accessviolation.html` | filenameController | Access violation error page |
| `/login.html` | filenameController | Login page (note: `/login.jsp` is documented instead) |

### RSS and Feed Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/feed.xml` | rssFeedController | RSS feed (alternate URL) |
| `/rss.xml` | rssFeedController | RSS feed |

### Alternative Search URL Patterns

These are search-related URLs that use different URL patterns than those documented in User Story 01 (which documents `/search/studySearch.html`, etc.):

| URL | Controller | Description |
|-----|------------|-------------|
| `/search/` | studySearchController | Search root redirect |
| `/searchForm.html` | searchFormController | Search form (alternative URL pattern) |
| `/searchResult.html` | filenameController | Search results (alternative URL pattern) |
| `/searchStudy.html` | searchStudyController | Study search (alternative to `/search/studySearch.html`) |
| `/searchStudyList.html` | listSearchStudyController | Study search list |
| `/study-query.html` | updateStudyQueryController | Study query form |

### Search Download/Export Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/search/downloadANexusRCTFile.html` | downloadANexusRCTFileController | Download reconstructed NEXUS file |
| `/search/downloadAStudy.html` | downloadAStudyController | Download entire study |
| `/search/downloadATreeBlock.html` | downloadATreeBlockController | Download tree block |
| `/search/downloadAnAnalysisStep.html` | downloadAnAnalysisStepController | Download analysis step data |
| `/search/matrixRowList.html` | listMatrixRowController | Matrix row list view |
| `/search/searchResultsAsRDF.rdf` | searchResultsAsRDFController | Search results as RDF |
| `/search/summary.html` | summaryController | Search summary page |
| `/search/taxonList.html` | listTaxaSearchController | Taxon list view |

### User Submission Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/user/analyses.html` | summaryController | Study analyses (duplicate of analysisDisplay) |
| `/user/authorForm.html` | authorFormController | Author edit form |
| `/user/authorList.html` | authorFormController | Author list |
| `/user/deleteARowSegment.html` | deleteARowSegmentController | Delete row segment |
| `/user/directToPhyloWidget.html` | directToPhyloWidgetController | Direct PhyloWidget link (alternate) |
| `/user/displaySubmissionMenu.html` | displaySubmissionMenuController | Submission menu display |
| `/user/downloadANexusRCTFile.html` | downloadANexusRCTFileController | Download reconstructed NEXUS |
| `/user/editorForm.html` | editorFormController | Editor edit form |
| `/user/exportRowSegmentData.html` | exportRowSegmentDataController | Export row segment data |
| `/user/exportRowSegmentTemplate.html` | exportRowSegmentTemplateController | Export row segment template |
| `/user/matrixRowSegmentForm.html` | matrixRowSegmentFormController | Matrix row segment form |
| `/user/nexusFiles.html` | nexusFilesController | View NEXUS files |
| `/user/readOnlyListTree.html` | readOnlyListTreeController | Read-only tree list |
| `/user/rowSegmentDataTable.html` | rowSegmentDataTableController | Row segment data table |
| `/user/submissionMain.html` | filenameController | Submission main page |
| `/user/treeParser.html` | treeParserController | Tree parser |
| `/user/treeParserResult.html` | treeParserResultController | Tree parser results |
| `/user/uploadRowSegmentData.html` | uploadRowSegmentDataController | Upload row segment data |
| `/user/viewXML.html` | filenameController | View XML output |

### Development/Test Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/test/testParser.html` | treeParserController | Test parser (development) |

### JSON/API Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/json/submissionIsland.html` | submissionToJsonController | Submission JSON data |
| `/appletInteraction.html` | appletInteractionController | Applet interaction handler |

---

## Pages Accounted For in User Stories

The following pages from `treebase-servlet.xml` ARE documented in the v3 user story "Pages to Account For" sections:

### User Story 01: Search (25 pages)
- `/search/studySearch.html`, `/search/matrixSearch.html`, `/search/treeSearch.html`
- `/search/taxonSearch.html`, `/search/treeTopSearch.html`, `/search/classificationSearch.html`
- `/search/study/summary.html`, `/search/study/matrices.html`, `/search/study/trees.html`
- `/search/study/taxa.html`, `/search/study/analyses.html`, `/search/study/analysis.html`
- `/search/study/matrix.html`, `/search/study/tree.html`, `/search/study/treeBlock.html`
- `/search/study/treeBlocks.html`, `/search/study/rowSegments.html`, `/search/study/rowSegmentsTSV.html`
- `/search/study/anyObjectAsRDF.rdf`, `/search/downloadATree.html`, `/search/downloadAMatrix.html`
- `/search/downloadANexusFile.html`, `/phylows/study/**`, `/phylows/matrix/**`
- `/phylows/tree/**`, `/phylows/taxon/**`

### User Story 02: Account (17 pages)
- `/register.html`, `/passwordForm.html`, `/resetPassword.html`
- `/user/updateProfile.html`, `/user/processUser.html`, `/user/submissionList.html`
- `/admin/adminSelectUsers.html`, `/admin/userList.html`, `/admin/overrideUserProfile.html`
- `/admin/adminUpdatingUserInfo.html`, `/admin/adminDeletingUserStepOne.html`
- `/admin/adminDeletingUserStepTwo.html`, `/admin/adminMergingUsers.html`, `/admin/userManagement.html`

### User Story 03: Submission (35 pages)
- `/user/submissionList.html`, `/user/studyForm.html`, `/user/summary.html`
- `/user/citationForm.html`, `/user/authorSearchForm.html`, `/user/addAuthor.html`
- `/user/uploadFile.html`, `/user/uploadFileSummary.html`, `/user/treeBlockList.html`
- `/user/treeList.html`, `/user/directMapToPhyloWidget.html`, `/user/deleteATree.html`
- `/user/deleteATreeBlock.html`, `/user/matrixList.html`, `/user/matrixRowList.html`
- `/user/matrixRowSegmentList.html`, `/user/viewAllRowSegmentData.html`, `/user/deleteAMatrix.html`
- `/user/taxaList.html`, `/user/editTaxonLabel.html`, `/user/editSetTaxonLabel.html`
- `/user/analysisList.html`, `/user/analysisDisplay.html`, `/user/analysisForm.html`
- `/user/analysisStepList.html`, `/user/analysisStepForm.html`, `/user/analyzedDataList.html`
- `/user/analyzedDataForm.html`, `/user/addAnalyzedData.html`, `/user/readyState.html`
- `/user/deleteStudy.html`, `/submitTutorial.html`, `/user/downloadATree.html`
- `/user/downloadATreeBlock.html`, `/user/downloadAMatrix.html`, `/user/downloadANexusFile.html`

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

### User Story 06: Technical (12 pages)
- `/urlAPI.html`, `/technology.html`, `/about.html`, `/contact.html`
- `/submitTutorial.html`, `/help.html`, `/sitemap.xml`
- `/phylows/study/**`, `/phylows/tree/**`, `/phylows/matrix/**`
- `/phylows/taxon/**`, `/phylows/classification/**`, `/top/**`

### User Story 07: Governance (11 pages)
- `/home.html`, `/about.html`, `/people.html`, `/partnership.html`
- `/reference.html`, `/technology.html`, `/submitTutorial.html`
- `/urlAPI.html`, `/dataMan.html`, `/journal.html`, `/contact.html`

---

## Recommendations

### Pages to Consider Adding to User Stories

1. **RSS Feeds** (`/feed.xml`, `/rss.xml`) - Consider adding to User Story 06 (Technical) as these are programmatic access points

2. **Alternative Search URL Patterns** - These URLs (`/searchForm.html`, `/searchStudy.html`, etc.) may need to be documented in User Story 01 if they are actively used, or redirected to the documented URLs (`/search/studySearch.html`, etc.) for consistency

3. **Row Segment Operations** - Several row segment pages are not documented:
   - `/user/rowSegmentDataTable.html`
   - `/user/uploadRowSegmentData.html`
   - `/user/exportRowSegmentData.html`
   - `/user/deleteARowSegment.html`
   - These should be added to User Story 03 (Submission)

4. **Tree Parser** (`/user/treeParser.html`, `/user/treeParserResult.html`) - Consider adding to User Story 03

5. **Download Pages** - Some download endpoints are missing:
   - `/search/downloadAStudy.html`
   - `/search/downloadATreeBlock.html`
   - `/search/downloadAnAnalysisStep.html`
   - `/search/downloadANexusRCTFile.html`
   - These should be added to User Story 01 (Search)

6. **JSON/API Endpoints** (`/json/submissionIsland.html`) - Consider adding to User Story 06 (Technical)

---

## Notes

1. **Duplicate URLs**: Some URLs appear in multiple user stories (e.g., `/user/submissionList.html` appears in both User Story 02 and 03). This is expected as pages serve multiple workflows.

2. **Static JSP vs Controller**: Pages using `filenameController` map directly to JSP files without custom controller logic.

3. **PhyloWS Wildcards**: Endpoints like `/phylows/study/**` cover multiple sub-paths for the REST API.

4. **login.jsp vs login.html**: The user stories reference `/login.jsp` but the servlet maps `/login.html` - this may be a routing inconsistency to investigate.
