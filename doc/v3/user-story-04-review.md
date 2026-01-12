# User Story 4: Review

## Overview

**As a** reviewer,  
**I want to** review a study with tree, matrix, taxon and analysis data,  
**So that** I can verify the quality and accuracy of submitted phylogenetic data.

## User Types

- Journal editors reviewing submissions
- Peer reviewers assigned to studies
- TreeBASE curators performing quality control

## Prerequisites

- Data submitter has provided the reviewer with a URL with a special access
  token in the query string
- Reviewer visits the website via this URL to access the unpublished study

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Reviewer popup 
- [ ] Study review page 
- [ ] Tree review interface
- [ ] Matrix review interface
- [ ] Taxa review interface
- [ ] Analysis review interface

## Navigation Flow

*TODO: Document how users navigate through these pages in a mermaid plot*

```
[Reviewer Dashboard] --> [Review Queue] --> [Study Review]
                                                 |
                    +----------------------------+
                    |            |               |
                    v            v               v
              [Trees]      [Matrices]        [Taxa]
                    |            |               |
                    v            v               v
              [Review]     [Review]         [Review]
                    |            |               |
                    +----------------------------+
                                 |
                                 v
                    [Analysis Review] --> [Final Decision]
                                               |
                          +--------------------+--------------------+
                          |                    |                    |
                          v                    v                    v
                    [Approve]           [Request Changes]      [Reject]
```

## Review Workflow

*TODO: Document how the reviewer navigates the unlocked pages to review the study*

### Step 1: Study Overview Review
- Verify publication details
- Check metadata completeness
- Validate citations

### Step 2: Tree Data Review
- Visualize tree structure
- Check topology
- Verify branch support values
- Validate tree labels

### Step 3: Matrix Data Review
- Review character matrix
- Check for data completeness
- Validate character definitions
- Review coding consistency

### Step 4: Taxonomic Review
- Verify taxon names
- Check taxonomic placement
- Review OTU mappings
- Flag nomenclatural issues

### Step 5: Analysis Review
- Check analysis parameters
- Verify software citations
- Review methodology description

## Pages to Account For

*TODO: Complete inventory of pages related to review functionality*

| Page | URL Pattern | Status |
|------|-------------|--------|
| | | |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What review statuses are needed?
- How should reviewer assignments work?
- What communication happens between reviewer and submitter?
- Can reviews be collaborative (multiple reviewers)?
