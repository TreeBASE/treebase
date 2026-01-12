# Cross-Reference: treebase-servlet.xml Pages Not in Documentation

This table lists all URL mappings from `treebase-servlet.xml` that are NOT mentioned in any markdown documentation file in the repository.

**Analysis Date:** 2026-01-12

**Summary:**
- Total URL mappings in treebase-servlet.xml: 138
- Documented URLs: 8
- Undocumented URLs: 130

---

## Public Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/contact.html` | filenameController | Contact information page |
| `/dataMan.html` | filenameController | Data management page |
| `/help.html` | helpController | Help page |
| `/journal.html` | filenameController | Journal information page |
| `/login.html` | filenameController | User login page |
| `/partnership.html` | filenameController | Partnership information page |
| `/people.html` | filenameController | People/contributors page |
| `/register.html` | registerUserController | User registration page |
| `/resetPassword.html` | resetPasswordController | Password reset page |
| `/passwordForm.html` | passwordFormController | Password form page |
| `/submitTutorial.html` | filenameController | Submission tutorial page |
| `/technology.html` | filenameController | Technology stack page |
| `/urlAPI.html` | filenameController | URL API documentation page |
| `/accessviolation.html` | filenameController | Access violation error page |

## Search Section

| URL | Controller | Description |
|-----|------------|-------------|
| `/searchForm.html` | searchFormController | Search form page |
| `/searchResult.html` | filenameController | Search results page |
| `/searchStudy.html` | searchStudyController | Search study page |
| `/searchStudyList.html` | listSearchStudyController | Search study list |
| `/study-query.html` | updateStudyQueryController | Study query form |
| `/search/classificationSearch.html` | classificationSearchController | Classification search |
| `/search/matrixSearch.html` | matrixSearchController | Matrix search |
| `/search/studySearch.html` | studySearchController | Study search |
| `/search/taxonSearch.html` | taxonSearchController | Taxon search |
| `/search/treeSearch.html` | treeSearchController | Tree search |
| `/search/treeTopSearch.html` | treeTopSearchController | Tree topology search |
| `/search/summary.html` | summaryController | Search summary |
| `/search/matrixRowList.html` | listMatrixRowController | Matrix row list |
| `/search/taxonList.html` | listTaxaSearchController | Taxon list |
| `/search/downloadAMatrix.html` | downloadAMatrixController | Download matrix |
| `/search/downloadANexusFile.html` | downloadANexusFileController | Download NEXUS file |
| `/search/downloadANexusRCTFile.html` | downloadANexusRCTFileController | Download NEXUS RCT file |
| `/search/downloadAStudy.html` | downloadAStudyController | Download study |
| `/search/downloadATree.html` | downloadATreeController | Download tree |
| `/search/downloadATreeBlock.html` | downloadATreeBlockController | Download tree block |
| `/search/downloadAnAnalysisStep.html` | downloadAnAnalysisStepController | Download analysis step |
| `/search/searchResultsAsRDF.rdf` | searchResultsAsRDFController | Search results as RDF |

## Search Study Detail Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/search/study/analyses.html` | searchSummaryController | Study analyses view |
| `/search/study/analysis.html` | searchSummaryController | Analysis detail view |
| `/search/study/anyObjectAsRDF.rdf` | anyObjectAsRDFController | RDF export for any object |
| `/search/study/matrices.html` | searchSummaryController | Study matrices view |
| `/search/study/matrix.html` | searchSummaryController | Matrix detail view |
| `/search/study/rowSegments.html` | searchSummaryController | Row segments view |
| `/search/study/rowSegmentsTSV.html` | downloadRowSegmentDataController | Row segments TSV download |
| `/search/study/summary.html` | searchSummaryController | Study summary view |
| `/search/study/taxa.html` | searchSummaryController | Study taxa view |
| `/search/study/tree.html` | searchMapToPhyloWidgetController | Tree viewer |
| `/search/study/treeBlock.html` | searchMapToPhyloWidgetController | Tree block viewer |
| `/search/study/treeBlocks.html` | searchSummaryController | Tree blocks list |
| `/search/study/trees.html` | searchSummaryController | Trees list |

## PhyloWS API Endpoints

These PhyloWS endpoints ARE documented in [API.md](API.md):
- `/phylows/study/**` - Study web service
- `/phylows/taxon/**` - Taxon web service
- `/phylows/tree/**` - Tree web service

These PhyloWS endpoints are NOT documented:

| URL | Controller | Description |
|-----|------------|-------------|
| `/phylows/classification/**` | phyloWSClassificationController | Classification web service |
| `/phylows/matrix/**` | phyloWSMatrixController | Matrix web service |

## Admin Section

| URL | Controller | Description |
|-----|------------|-------------|
| `/admin/adminDeletingUserStepOne.html` | adminDeletingUserStepOneController | Delete user - step 1 |
| `/admin/adminDeletingUserStepTwo.html` | adminDeletingUserStepTwoController | Delete user - step 2 |
| `/admin/adminMergingPersons.html` | adminMergingPersonsController | Merge person records |
| `/admin/adminMergingUsers.html` | adminMergingUsersController | Merge user accounts |
| `/admin/adminSelectPersons.html` | adminSelectPersonsController | Select persons for admin action |
| `/admin/adminSelectUsers.html` | adminSelectUsersController | Select users for admin action |
| `/admin/adminUpdatingUserInfo.html` | adminUpdatingUserInfoController | Update user information |
| `/admin/administrationPage.html` | filenameController | Main administration page |
| `/admin/changeStudyStatus.html` | changeStudyStatusController | Change study status |
| `/admin/messageToAdminAfterAction.html` | filenameController | Admin action confirmation |
| `/admin/overrideUserProfile.html` | adminOverridingUserFormController | Override user profile |
| `/admin/personList.html` | filenameController | List persons |
| `/admin/readyStateStudies.html` | changeStudyStatusController | Ready state studies list |
| `/admin/searchBySubmissionID.html` | searchBySubmissionIDController | Search by submission ID |
| `/admin/selectStudies.html` | selectStudiesController | Select studies for admin action |
| `/admin/userList.html` | filenameController | List users |
| `/admin/userManagement.html` | userManagementController | User management |

## User Submission Section

| URL | Controller | Description |
|-----|------------|-------------|
| `/user/addAnalyzedData.html` | addAnalyzedDataController | Add analyzed data to study |
| `/user/addAuthor.html` | addAuthorController | Add author to citation |
| `/user/analyses.html` | summaryController | View study analyses |
| `/user/analysisDisplay.html` | displayAnalysisController | Display analysis details |
| `/user/analysisForm.html` | analysisFormController | Edit analysis form |
| `/user/analysisList.html` | listAnalysisController | List analyses |
| `/user/analysisStepForm.html` | analysisStepFormController | Edit analysis step |
| `/user/analysisStepList.html` | listAnalysisStepController | List analysis steps |
| `/user/analyzedDataForm.html` | analyzedDataFormController | Edit analyzed data |
| `/user/analyzedDataList.html` | listAnalyzedDataController | List analyzed data |
| `/user/authorForm.html` | authorFormController | Edit author form |
| `/user/authorList.html` | authorFormController | List authors |
| `/user/authorSearchForm.html` | authorSearchFormController | Search for authors |
| `/user/citationForm.html` | citationFormController | Edit citation form |
| `/user/deleteAMatrix.html` | deleteAMatrixController | Delete a matrix |
| `/user/deleteARowSegment.html` | deleteARowSegmentController | Delete a row segment |
| `/user/deleteATree.html` | deleteATreeController | Delete a tree |
| `/user/deleteATreeBlock.html` | deleteATreeBlockController | Delete a tree block |
| `/user/deleteStudy.html` | deleteStudyController | Delete a study |
| `/user/directMapToPhyloWidget.html` | directMapToPhyloWidgetController | Direct map to tree viewer |
| `/user/directToPhyloWidget.html` | directToPhyloWidgetController | Direct to tree viewer |
| `/user/displaySubmissionMenu.html` | displaySubmissionMenuController | Submission menu |
| `/user/downloadAMatrix.html` | downloadAMatrixController | Download matrix |
| `/user/downloadANexusFile.html` | downloadANexusFileController | Download NEXUS file |
| `/user/downloadANexusRCTFile.html` | downloadANexusRCTFileController | Download NEXUS RCT file |
| `/user/downloadATree.html` | downloadATreeController | Download tree |
| `/user/downloadATreeBlock.html` | downloadATreeBlockController | Download tree block |
| `/user/editSetTaxonLabel.html` | editSetTaxonLabelController | Edit taxon label set |
| `/user/editTaxonLabel.html` | editTaxonLabelController | Edit taxon label |
| `/user/editorForm.html` | editorFormController | Edit editor form |
| `/user/exportRowSegmentData.html` | exportRowSegmentDataController | Export row segment data |
| `/user/exportRowSegmentTemplate.html` | exportRowSegmentTemplateController | Export row segment template |
| `/user/matrixList.html` | listMatrixController | List matrices |
| `/user/matrixRowList.html` | listMatrixRowController | List matrix rows |
| `/user/matrixRowSegmentForm.html` | matrixRowSegmentFormController | Edit matrix row segment |
| `/user/matrixRowSegmentList.html` | listMatrixRowSegmentController | List matrix row segments |
| `/user/nexusFiles.html` | nexusFilesController | View NEXUS files |
| `/user/processUser.html` | processUserController | Process user action |
| `/user/readOnlyListTree.html` | readOnlyListTreeController | Read-only tree list |
| `/user/readyState.html` | readyStateController | Change ready state |
| `/user/rowSegmentDataTable.html` | rowSegmentDataTableController | Row segment data table |
| `/user/studyForm.html` | studyFormController | Edit study form |
| `/user/submissionList.html` | listSubmissionController | List submissions |
| `/user/submissionMain.html` | filenameController | Submission main page |
| `/user/summary.html` | summaryController | Submission summary |
| `/user/taxaList.html` | listTaxaController | List taxa |
| `/user/treeBlockList.html` | listTreeBlockController | List tree blocks |
| `/user/treeList.html` | listTreeController | List trees |
| `/user/treeParser.html` | treeParserController | Tree parser |
| `/user/treeParserResult.html` | treeParserResultController | Tree parser results |
| `/user/updateProfile.html` | userFormController | Update user profile |
| `/user/uploadFile.html` | uploadFileController | Upload file |
| `/user/uploadFileSummary.html` | uploadFileSummaryController | Upload file summary |
| `/user/uploadRowSegmentData.html` | uploadRowSegmentDataController | Upload row segment data |
| `/user/viewAllRowSegmentData.html` | viewAllRowSegmentDataController | View all row segment data |
| `/user/viewXML.html` | filenameController | View XML |

## Other Pages

| URL | Controller | Description |
|-----|------------|-------------|
| `/appletInteraction.html` | appletInteractionController | Applet interaction handler |
| `/feed.xml` | rssFeedController | RSS feed (alternate) |
| `/json/submissionIsland.html` | submissionToJsonController | JSON submission data |
| `/rss.xml` | rssFeedController | RSS feed |
| `/sitemap.xml` | siteMapController | XML sitemap for SEO |
| `/test/testParser.html` | treeParserController | Test parser (development) |

---

## Documented URLs (8 total)

The following URLs from `treebase-servlet.xml` ARE mentioned in the markdown documentation:

| URL | Documented In |
|-----|---------------|
| `/about.html` | README.md |
| `/home.html` | Various docs |
| `/phylows/study/**` | API.md |
| `/phylows/taxon/**` | API.md |
| `/phylows/tree/**` | API.md |
| `/reference.html` | Various docs |
| `/search/` | Various docs |
| `/top/**` | OAI-PMH.md |

---

## Notes

1. **filenameController** - These pages use the `ShortPathUrlFilenameViewController` which maps URLs directly to JSP files without custom controller logic.

2. **PhyloWS API** - The API.md documentation covers the study, taxon, and tree endpoints but not the classification and matrix endpoints.

3. **Admin Section** - All admin pages are undocumented. These are internal administrative tools.

4. **User Section** - All user submission pages are undocumented. These are internal user workflow pages.

5. **Search Section** - All search pages are undocumented except for the general `/search/` path mention.

---

## Recommendations

1. **Document PhyloWS endpoints** - Add `/phylows/classification/**` and `/phylows/matrix/**` to API.md
2. **Create User Guide** - Document the user submission workflow pages
3. **Create Admin Guide** - Document administrative functionality
4. **Add Search Documentation** - Document the various search interfaces and their capabilities
