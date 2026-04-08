insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (8, 'add-help-messages', 
       'Add help messages for all popup help buttons')
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Clean up any existing empty help records (created by auto-creation when clicking help)
DELETE FROM help WHERE helptext IS NULL OR helptext = '';

-- Add unique constraint on tag to prevent duplicates (if not exists)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'help_tag_unique'
    ) THEN
        ALTER TABLE help ADD CONSTRAINT help_tag_unique UNIQUE (tag);
    END IF;
END $$;

-- User Registration Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'userForm',
    '<h2>User Registration</h2>' ||
    '<p>To register for a TreeBASE account, please provide the following required information:</p>' ||
    '<ul>' ||
    '<li><strong>Username</strong>: Choose a unique username for your account.</li>' ||
    '<li><strong>Password</strong>: Enter a secure password.</li>' ||
    '<li><strong>Re-type Password</strong>: Re-enter your password to confirm it and avoid mistyping.</li>' ||
    '<li><strong>Email Address</strong>: Provide a valid email address for account communication.</li>' ||
    '</ul>' ||
    '<p>The name and phone number fields are optional.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Submission List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'submissionList',
    '<h2>Submissions List</h2>' ||
    '<p>This page displays all your TreeBASE submissions. Each row shows:</p>' ||
    '<ul>' ||
    '<li><strong>Submission ID</strong>: The unique identifier for each submission.</li>' ||
    '<li><strong>Study Name</strong>: The title of your study.</li>' ||
    '<li><strong>Study Notes</strong>: Your notes about the submission.</li>' ||
    '<li><strong>Status</strong>: Current status (In Progress, Ready, or Published).</li>' ||
    '</ul>' ||
    '<p>You can change the status of In Progress submissions to Ready when your data is complete.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Study Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'studyForm',
    '<h2>Submission Information</h2>' ||
    '<p>Enter the basic information for your study submission:</p>' ||
    '<ul>' ||
    '<li><strong>Study Name</strong>: Usually the same title as your publication.</li>' ||
    '<li><strong>Notes</strong>: Internal notes for yourself and TreeBASE staff. These are not visible to the public.</li>' ||
    '</ul>' ||
    '<p>If your submission is part of a sponsored research data management plan, please indicate this in the Notes.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Upload File
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'uploadFile',
    '<h2>Nexus File Upload</h2>' ||
    '<p>Upload your NEXUS files containing phylogenetic data:</p>' ||
    '<ul>' ||
    '<li>Only the first ~30 trees will be parsed to maintain usability.</li>' ||
    '<li>Place your preferred or consensus trees within the first 30 trees in the tree block.</li>' ||
    '<li>You can attach multiple files using the "Attach another file" link.</li>' ||
    '</ul>' ||
    '<p>Supported format: NEXUS (.nex) files.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Citation Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'citationForm',
    '<h2>Citation Information</h2>' ||
    '<p>Enter the publication details for your study:</p>' ||
    '<ul>' ||
    '<li><strong>Citation Type</strong>: Select the type of publication (Article, Book, Book Section, etc.).</li>' ||
    '<li><strong>Publish Year</strong>: The year of publication.</li>' ||
    '<li><strong>Publication Status</strong>: Current status of your publication.</li>' ||
    '</ul>' ||
    '<p>Additional fields will appear based on the citation type selected.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Citation Form - Article
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'citationForm-article',
    '<h2>Article Information</h2>' ||
    '<p>Enter the journal article details:</p>' ||
    '<ul>' ||
    '<li><strong>Title</strong>: The article title.</li>' ||
    '<li><strong>Keywords</strong>: Relevant keywords for the article.</li>' ||
    '<li><strong>Abstract</strong>: The article abstract.</li>' ||
    '<li><strong>PMID</strong>: PubMed ID (if available).</li>' ||
    '<li><strong>DOI</strong>: Digital Object Identifier.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Citation Form - Book
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'citationForm-book',
    '<h2>Book / Thesis Information</h2>' ||
    '<p>Enter the book or thesis details:</p>' ||
    '<ul>' ||
    '<li><strong>Book Title</strong>: The title of the book or thesis.</li>' ||
    '<li><strong>Keywords</strong>: Relevant keywords.</li>' ||
    '<li><strong>Abstract</strong>: A brief description.</li>' ||
    '<li><strong>PMID</strong>: PubMed ID (if available).</li>' ||
    '<li><strong>DOI</strong>: Digital Object Identifier.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Citation Form - Book Section
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'citationForm-booksection',
    '<h2>Book Section / Conference Proceedings</h2>' ||
    '<p>Enter the book section or conference proceedings details:</p>' ||
    '<ul>' ||
    '<li><strong>Section Title</strong>: The title of the chapter or section.</li>' ||
    '<li><strong>Keywords</strong>: Relevant keywords.</li>' ||
    '<li><strong>Abstract</strong>: A brief description.</li>' ||
    '<li><strong>PMID</strong>: PubMed ID (if available).</li>' ||
    '<li><strong>DOI</strong>: Digital Object Identifier.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Matrix List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'matrixList',
    '<h2>Matrices</h2>' ||
    '<p>This page shows all character matrices uploaded for your study:</p>' ||
    '<ul>' ||
    '<li><strong>Title</strong>: The matrix title (editable).</li>' ||
    '<li><strong>Description</strong>: A brief description (editable).</li>' ||
    '<li><strong>Matrix Kind</strong>: The type of data (DNA, RNA, Protein, etc.).</li>' ||
    '</ul>' ||
    '<p>Use the icons to view matrix rows, delete matrices, or perform other actions.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeList',
    '<h2>Trees</h2>' ||
    '<p>This page shows all phylogenetic trees in the selected tree block:</p>' ||
    '<ul>' ||
    '<li><strong>Label</strong>: The tree label (editable).</li>' ||
    '<li><strong>Title</strong>: The tree title (editable).</li>' ||
    '<li><strong>Tree Type</strong>: Single or Consensus tree.</li>' ||
    '<li><strong>Tree Kind</strong>: The method used (Species Tree, Gene Tree, etc.).</li>' ||
    '</ul>' ||
    '<p>Use the icons to visualize trees or perform other actions.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree Block List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeBlockList',
    '<h2>Tree Blocks</h2>' ||
    '<p>Tree blocks group related phylogenetic trees together:</p>' ||
    '<ul>' ||
    '<li><strong>Title</strong>: The tree block title (editable).</li>' ||
    '<li><strong>Tree Count</strong>: Number of trees in the block.</li>' ||
    '</ul>' ||
    '<p>Click on a tree block to view its individual trees.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Taxon Labels
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'taxonLabels',
    '<h2>Taxon Labels</h2>' ||
    '<p>Taxon labels must comply with TreeBASE guidelines:</p>' ||
    '<ul>' ||
    '<li>Write scientific binomials in full (no abbreviations).</li>' ||
    '<li>Add numbers or codes as suffixes, separated by a space.</li>' ||
    '<li>Validate names against uBIO and NCBI taxonomies.</li>' ||
    '</ul>' ||
    '<p>Select taxon labels and click validate to link them to external taxonomies.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Nexus Files
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'nexusFiles',
    '<h2>NEXUS Files</h2>' ||
    '<p>This page lists all NEXUS files uploaded to your submission:</p>' ||
    '<ul>' ||
    '<li>Files cannot be deleted unless the entire submission is deleted.</li>' ||
    '<li>Files are only visible to the public when their trees/matrices are published.</li>' ||
    '<li>Download original or reconstructed versions of your files.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Section
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisSection',
    '<h2>List of Analyses</h2>' ||
    '<p>TreeBASE only publishes matrices and trees listed with analysis entries:</p>' ||
    '<ul>' ||
    '<li>Each submission must have at least one analysis entry.</li>' ||
    '<li>Each analysis must contain at least one analysis step.</li>' ||
    '<li>Click the add icon to create a new analysis.</li>' ||
    '<li>Click the edit icon to add steps and define input/output data.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Form - Analysis Info
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisInfo',
    '<h2>Analysis Information</h2>' ||
    '<p>Enter the details for your phylogenetic analysis:</p>' ||
    '<ul>' ||
    '<li><strong>Name</strong>: A descriptive name for this analysis.</li>' ||
    '<li><strong>Notes</strong>: Additional notes about the analysis methodology.</li>' ||
    '</ul>' ||
    '<p>After creating the analysis, add analysis steps to define the workflow.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Steps
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisSteps',
    '<h2>Analysis Steps</h2>' ||
    '<p>Analysis steps define the computational workflow:</p>' ||
    '<ul>' ||
    '<li>Each step represents a distinct phase of the analysis.</li>' ||
    '<li>Click add to create a new step.</li>' ||
    '<li>Click edit to modify step details and define input/output data.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Step Info
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisStepInfo',
    '<h2>Analysis Step Information</h2>' ||
    '<p>Define the details for this analysis step:</p>' ||
    '<ul>' ||
    '<li><strong>Name</strong>: A descriptive name for this step.</li>' ||
    '<li><strong>Notes</strong>: Additional notes about this step.</li>' ||
    '<li><strong>Commands</strong>: The commands or parameters used.</li>' ||
    '<li><strong>Algorithm</strong>: Select the analysis algorithm used.</li>' ||
    '<li><strong>Software</strong>: Select the software package used.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analyzed Data List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analyzedDataList',
    '<h2>Analyzed Data</h2>' ||
    '<p>This list shows the input and output data for the analysis step:</p>' ||
    '<ul>' ||
    '<li>Click add to associate matrices or trees as input or output.</li>' ||
    '<li>Input data: matrices or trees used as starting data.</li>' ||
    '<li>Output data: trees or other results produced by the analysis.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analyzed Data Type Selection
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analyzedDataTypeSelection',
    '<h2>Data Type Selection</h2>' ||
    '<p>Select the type of data to associate with this analysis step:</p>' ||
    '<ul>' ||
    '<li><strong>Input/Output Type</strong>: Whether this is input or output data.</li>' ||
    '<li><strong>Data Type</strong>: Matrix, Tree, or Tree Block.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analyzed Data Matrix Selection
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analyzedDataMatrixSelection',
    '<h2>Matrix Selection</h2>' ||
    '<p>Select the matrices to use for this analysis step:</p>' ||
    '<ul>' ||
    '<li>Check the boxes next to the matrices you want to include.</li>' ||
    '<li>Already-used matrices will be shown as checked and disabled.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analyzed Tree Selection
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analyzedTreeSelection',
    '<h2>Tree Selection</h2>' ||
    '<p>Select the trees to use for this analysis step:</p>' ||
    '<ul>' ||
    '<li>Check the boxes next to the trees you want to include.</li>' ||
    '<li>Already-used trees will be shown as checked and disabled.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analyzed Tree Block Selection
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analyzedTreeBlockSelection',
    '<h2>Tree Block Selection</h2>' ||
    '<p>Select the tree blocks to use for this analysis step:</p>' ||
    '<ul>' ||
    '<li>Check the boxes next to the tree blocks you want to include.</li>' ||
    '<li>Already-used tree blocks will be shown as checked and disabled.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Submission Summary View
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'submissionSummaryView',
    '<h2>Submission Summary</h2>' ||
    '<p>This page provides an overview of your submission:</p>' ||
    '<ul>' ||
    '<li><strong>Study Accession URL</strong>: The permanent URL for your study (cite this in your manuscript).</li>' ||
    '<li><strong>Reviewer Access URL</strong>: Share this URL with journal editors for reviewer access.</li>' ||
    '<li>Citation information, matrices, trees, and analyses are summarized here.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- People Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'peopleForm',
    '<h2>Author/Editor Information</h2>' ||
    '<p>Enter the person''s details for the citation:</p>' ||
    '<ul>' ||
    '<li><strong>First Name</strong>: The person''s first name.</li>' ||
    '<li><strong>Middle Name</strong>: The person''s middle name (optional).</li>' ||
    '<li><strong>Last Name</strong>: The person''s last name (required).</li>' ||
    '<li><strong>Email</strong>: Contact email address.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Add Person Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'addPersonForm',
    '<h2>Add Author/Editor</h2>' ||
    '<p>Add a new author or editor to the citation:</p>' ||
    '<ul>' ||
    '<li><strong>First Name</strong>: The person''s first name.</li>' ||
    '<li><strong>Middle Name</strong>: The person''s middle name (optional).</li>' ||
    '<li><strong>Last Name</strong>: The person''s last name (required).</li>' ||
    '<li><strong>Email</strong>: Contact email address.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- People List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'peopleList',
    '<h2>Authors/Editors List</h2>' ||
    '<p>This table shows the current list of authors or editors:</p>' ||
    '<ul>' ||
    '<li>Use the UP/DOWN buttons to reorder entries.</li>' ||
    '<li>Click delete to remove an entry.</li>' ||
    '<li>Use the form above to add new entries.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- People Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'peopleSearchForm',
    '<h2>Search for Author/Editor</h2>' ||
    '<p>Search for existing authors or editors by last name:</p>' ||
    '<ul>' ||
    '<li>Enter the last name and click Search.</li>' ||
    '<li>Select from the results to add to your citation.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- People Match List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'peopleMatchList',
    '<h2>Matched Authors/Editors</h2>' ||
    '<p>This table shows authors or editors matching your search:</p>' ||
    '<ul>' ||
    '<li>Review the results and select the correct person.</li>' ||
    '<li>If no match is found, use the form above to add a new entry.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Matrix Row List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'matrixRowList',
    '<h2>Matrix Rows</h2>' ||
    '<p>This page shows the rows (taxa) in the selected matrix:</p>' ||
    '<ul>' ||
    '<li>View and edit row segments for each taxon.</li>' ||
    '<li>Export row segment templates for bulk data entry.</li>' ||
    '<li>Upload row segment data from files.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Matrix Row Segment List
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'matrixRowSegmentList',
    '<h2>Matrix Row Segments</h2>' ||
    '<p>Row segments divide matrix rows into parts with metadata:</p>' ||
    '<ul>' ||
    '<li><strong>Title</strong>: A descriptive name for the segment.</li>' ||
    '<li><strong>Start Index</strong>: Beginning position in the sequence.</li>' ||
    '<li><strong>End Index</strong>: Ending position in the sequence.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Matrix Row Segment Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'matrixRowSegmentForm',
    '<h2>Matrix Row Segment Information</h2>' ||
    '<p>Define a segment of the matrix row:</p>' ||
    '<ul>' ||
    '<li><strong>Title</strong>: A descriptive name for this segment.</li>' ||
    '<li>Highlight the segment in the data below and click Select.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Row Segment Data Table
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'rowSegmentDataTable',
    '<h2>Row Segment Data</h2>' ||
    '<p>Map the columns from your uploaded data file:</p>' ||
    '<ul>' ||
    '<li>Use the dropdown menus to assign column headers.</li>' ||
    '<li>Check if your file includes a header row.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- View All Row Segment Data
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'viewAllRowSegmentData',
    '<h2>Update Row Segment Data</h2>' ||
    '<p>View and manage all row segment data:</p>' ||
    '<ul>' ||
    '<li>Edit segment titles and metadata.</li>' ||
    '<li>Delete unwanted segments by checking the boxes.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Upload Row Segment Data
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'uploadRowSegmentData',
    '<h2>Upload Row Segment Data</h2>' ||
    '<p>Upload a tab-delimited file with row segment information:</p>' ||
    '<ul>' ||
    '<li>File should be tab-delimited.</li>' ||
    '<li>Include columns for taxon label, segment title, and indices.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Edit Taxon Label
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'editTaxonLabel',
    '<h2>Taxon Label</h2>' ||
    '<p>Edit your taxon label to comply with TreeBASE guidelines:</p>' ||
    '<ul>' ||
    '<li>Write scientific binomials in full (no abbreviations).</li>' ||
    '<li>Add numbers or codes as suffixes separated by a space.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Edit Taxonomy Reference
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'editTaxonomyRef',
    '<h2>External Taxonomy</h2>' ||
    '<p>Link your taxon label to external taxonomies:</p>' ||
    '<ul>' ||
    '<li>Select from suggested NCBI taxonomy matches.</li>' ||
    '<li>Search uBio for a match and enter the NamebankID.</li>' ||
    '<li>Select "no association" if no match exists.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Edit Set Taxon Label
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'editSetTaxonLabel',
    '<h2>Taxon Label List Editor</h2>' ||
    '<p>Edit multiple taxon labels at once:</p>' ||
    '<ul>' ||
    '<li>Modify labels directly in the text fields.</li>' ||
    '<li>Click Update to save all changes.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Search Tabs
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'searchTabs',
    '<h2>Search TreeBASE</h2>' ||
    '<p>Use these tabs to search different types of data:</p>' ||
    '<ul>' ||
    '<li><strong>Study</strong>: Search for studies by citation, author, etc.</li>' ||
    '<li><strong>Taxa</strong>: Search by taxonomic names.</li>' ||
    '<li><strong>Tree Topology</strong>: Search for trees with specific topologies.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Summary Tabs (Study Summary Navigation)
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'summaryTabs',
    '<h2>Study Navigation</h2>' ||
    '<p>Use these tabs to navigate different aspects of the study:</p>' ||
    '<ul>' ||
    '<li><strong>Summary</strong>: Overview of the study.</li>' ||
    '<li><strong>Matrices</strong>: View character matrices.</li>' ||
    '<li><strong>Trees</strong>: View phylogenetic trees.</li>' ||
    '<li><strong>Analyses</strong>: View analysis details.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Study Keyword Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'studyKeywordSearchForm',
    '<h2>Study Search</h2>' ||
    '<p>Search for studies using various criteria:</p>' ||
    '<ul>' ||
    '<li><strong>Study ID</strong>: TreeBASE2 submission ID.</li>' ||
    '<li><strong>Legacy Study ID</strong>: For studies with IDs less than 3000.</li>' ||
    '<li><strong>Author</strong>: Search by author name.</li>' ||
    '<li><strong>Title/Abstract/Citation</strong>: Search publication text.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree Simple Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeSimpleSearchForm',
    '<h2>Tree Search</h2>' ||
    '<p>Search for phylogenetic trees:</p>' ||
    '<ul>' ||
    '<li><strong>Tree ID</strong>: Search by tree identifier.</li>' ||
    '<li><strong>Title</strong>: Search by tree title.</li>' ||
    '<li><strong>Type</strong>: Single or Consensus tree.</li>' ||
    '<li><strong>NTAX</strong>: Number of taxa in the tree.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Matrix Simple Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'matrixSimpleSearchForm',
    '<h2>Matrix Search</h2>' ||
    '<p>Search for character matrices:</p>' ||
    '<ul>' ||
    '<li><strong>Matrix ID</strong>: Search by matrix identifier.</li>' ||
    '<li><strong>Title</strong>: Search by matrix title.</li>' ||
    '<li><strong>Type</strong>: DNA, RNA, Protein, etc.</li>' ||
    '<li><strong>NTAX</strong>: Number of taxa.</li>' ||
    '<li><strong>NCHAR</strong>: Number of characters.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Taxon Search - Identifiers
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'taxonSearchIdentifiers',
    '<h2>Search by Identifiers</h2>' ||
    '<p>Search for taxa using external database identifiers:</p>' ||
    '<ul>' ||
    '<li><strong>TreeBASE taxon ID</strong>: Internal TreeBASE identifier.</li>' ||
    '<li><strong>NCBI taxon ID</strong>: NCBI Taxonomy database ID.</li>' ||
    '<li><strong>uBio nameBankID</strong>: uBio nomenclature database ID.</li>' ||
    '</ul>' ||
    '<p>Enter one identifier per line in the search box.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Taxon Search - Text Search
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'taxonSearchTextSearch',
    '<h2>Text Search</h2>' ||
    '<p>Search for taxa by name:</p>' ||
    '<ul>' ||
    '<li><strong>Taxon label</strong>: The label as it appears in matrices/trees.</li>' ||
    '<li><strong>Taxon variant</strong>: Alternative name spellings.</li>' ||
    '<li><strong>Taxon</strong>: The canonical taxon name.</li>' ||
    '</ul>' ||
    '<p>Use Case sensitive and Exact match options to refine your search.</p>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Taxon Search - Terms
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'taxonSearchTerms',
    '<h2>Search Terms</h2>' ||
    '<p>Enter your search terms in this area:</p>' ||
    '<ul>' ||
    '<li>Enter one term per line.</li>' ||
    '<li>For identifiers, enter numeric IDs.</li>' ||
    '<li>For text search, enter taxonomic names.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree Topology 3 Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeTopology3SearchForm',
    '<h2>3-Taxon Topology Search</h2>' ||
    '<p>Search for trees containing a specific 3-taxon topology:</p>' ||
    '<ul>' ||
    '<li>Enter taxon names in the three input fields.</li>' ||
    '<li>The diagram shows the relationship being searched.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree Topology 4a Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeTopology4aSearchForm',
    '<h2>4-Taxon Topology Search (Asymmetric)</h2>' ||
    '<p>Search for trees containing a specific 4-taxon asymmetric topology:</p>' ||
    '<ul>' ||
    '<li>Enter taxon names in the four input fields.</li>' ||
    '<li>The diagram shows the pectinate (ladder-like) relationship.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Tree Topology 4s Search Form
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'treeTopology4sSearchForm',
    '<h2>4-Taxon Topology Search (Symmetric)</h2>' ||
    '<p>Search for trees containing a specific 4-taxon symmetric topology:</p>' ||
    '<ul>' ||
    '<li>Enter taxon names in the four input fields.</li>' ||
    '<li>The diagram shows the symmetric (balanced) relationship.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Search Results - Discard Unchecked Items
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    's+res+discard-unchecked-items+btn',
    '<h2>Discard Unchecked Items</h2>' ||
    '<p>Remove all unchecked items from your search results:</p>' ||
    '<ul>' ||
    '<li>Check the items you want to keep.</li>' ||
    '<li>Click this button to discard unchecked items.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Search Results - Discard All Results
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    's+res+discard-these-results+btn',
    '<h2>Discard All Results</h2>' ||
    '<p>Remove all current search results:</p>' ||
    '<ul>' ||
    '<li>This will clear your entire result set.</li>' ||
    '<li>You will need to perform a new search.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Search Results - Download All Trees
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    's+res+download-all-treess+btn',
    '<h2>Download All Trees</h2>' ||
    '<p>Download all trees from your search results:</p>' ||
    '<ul>' ||
    '<li>Trees will be downloaded in NEXUS format.</li>' ||
    '<li>Use this to export multiple trees at once.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Search By Submission ID
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'searchBySubmissionID',
    '<h2>Search by Identifiers</h2>' ||
    '<p>Search for submissions using their identifiers:</p>' ||
    '<ul>' ||
    '<li><strong>TreeBASE2 Submission ID</strong>: The current TreeBASE identifier.</li>' ||
    '<li><strong>TreeBASE1 Legacy Study ID</strong>: Identifier from the original TreeBASE.</li>' ||
    '<li><strong>TreeBASE2 Study ID</strong>: Internal study identifier.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Details View/Edit
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisDetailsViewEdit',
    '<h2>Analysis Details</h2>' ||
    '<p>View or edit the analysis information:</p>' ||
    '<ul>' ||
    '<li><strong>Name</strong>: The analysis name.</li>' ||
    '<li><strong>Notes</strong>: Additional notes about the analysis.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Step Details View/Edit
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisStepDetailsViewEdit',
    '<h2>Analysis Step Details</h2>' ||
    '<p>View or edit the analysis step information:</p>' ||
    '<ul>' ||
    '<li>Algorithm and software used.</li>' ||
    '<li>Commands or parameters.</li>' ||
    '<li>Input and output data associations.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Step Input Data View/Edit
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisStepInputDataDetailsViewEdit',
    '<h2>Input Data</h2>' ||
    '<p>View or edit input data for this analysis step:</p>' ||
    '<ul>' ||
    '<li>Matrices used as input.</li>' ||
    '<li>Trees used as starting points.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Analysis Step Output Data View/Edit
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'analysisStepOutputDataDetailsViewEdit',
    '<h2>Output Data</h2>' ||
    '<p>View or edit output data from this analysis step:</p>' ||
    '<ul>' ||
    '<li>Trees produced by the analysis.</li>' ||
    '<li>Other results generated.</li>' ||
    '</ul>'
)
ON CONFLICT (tag) DO UPDATE SET helptext = EXCLUDED.helptext;

-- Dryad Import
INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'dryadImport',
    '<h2>Dryad Import</h2>' ||
    '<p>Import data from the Dryad data repository:</p>' ||
    '<ul>' ||
    '<li>Enter the Dryad DOI to import associated data.</li>' ||
    '<li>Data will be linked to your TreeBASE submission.</li>' ||
    '</ul>'
);

