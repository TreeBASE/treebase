# User Story 3: Data Submission

## Overview

**As a** registered user,  
**I want to** submit study, tree, matrix, taxon or analysis data,  
**So that** I can share my phylogenetic research with the scientific community.

## User Types

- Researchers submitting their own studies
- Students submitting work on behalf of advisors
- Data curators submitting historical data

## Prerequisites

- User must be logged in (see [User Story 2: Account Management](user-story-02-account.md))

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Submission dashboard
- [ ] New submission form
- [ ] Study metadata entry
- [ ] File upload interface
- [ ] Tree upload/editor
- [ ] Matrix upload/editor
- [ ] Taxa management
- [ ] Analysis metadata entry
- [ ] Submission preview
- [ ] Submission confirmation

## Navigation Flow

*TODO: Document how users navigate through these pages as a mermaid plot*

```
[User Dashboard] --> [New Submission] --> [Study Metadata]
                                               |
                                               v
                                         [File Upload]
                                               |
                                               v
                                    [Tree/Matrix/Taxa Entry]
                                               |
                                               v
                                      [Analysis Metadata]
                                               |
                                               v
                                    [Preview & Validate]
                                               |
                                               v
                                          [Submit]
```

## Submission Workflow

### Step 1: Study Information

*TODO: verify the exact fields that are currently required and the pages that record this information*

- Publication details (journal, DOI, authors)
- Study title and abstract
- Keywords and categories

### Step 2: Data Upload

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- NEXUS file upload
- Newick file upload
- Other supported formats
- File validation

### Step 3: Tree Data

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- Tree visualization
- Tree metadata
- Multiple trees per study

### Step 4: Matrix Data

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- Character matrix display
- Matrix metadata
- Character definitions

### Step 5: Taxonomic Data

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- Taxon name reconciliation
- Taxonomic hierarchy
- OTU mapping

### Step 6: Analysis Information

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- Analysis type/method
- Software used
- Parameters and settings

### Step 7: Review and Submit

*TODO: verify the exact fields and interactions that are currently required and the pages that record this information*

- Validation summary
- Preview all data
- Submit for review

## Data Validation

*TODO: Document validation rules and error handling*

- File format validation
- Required field validation
- Taxonomic name validation
- Consistency checks

## Pages to Account For

*TODO: Complete inventory of pages related to data submission*

| Page | URL Pattern | Status |
|------|-------------|--------|
| | | |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What file formats should be supported?
- What is the maximum file size?
- Can submissions be saved as drafts?
- What validation happens at each step vs. at submission?
