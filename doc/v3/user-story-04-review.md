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

- User must be logged in with reviewer privileges
- Study must be assigned to the reviewer

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Review queue/dashboard
- [ ] Study review page
- [ ] Tree review interface
- [ ] Matrix review interface
- [ ] Taxa review interface
- [ ] Analysis review interface
- [ ] Comment/feedback entry
- [ ] Approval/rejection interface

## Navigation Flow

*TODO: Document how users navigate through these pages*

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

### Step 1: Access Review Queue
- View assigned reviews
- Filter by status, date, study type
- Priority indicators

### Step 2: Study Overview Review
- Verify publication details
- Check metadata completeness
- Validate citations

### Step 3: Tree Data Review
- Visualize tree structure
- Check topology
- Verify branch support values
- Validate tree labels

### Step 4: Matrix Data Review
- Review character matrix
- Check for data completeness
- Validate character definitions
- Review coding consistency

### Step 5: Taxonomic Review
- Verify taxon names
- Check taxonomic placement
- Review OTU mappings
- Flag nomenclatural issues

### Step 6: Analysis Review
- Check analysis parameters
- Verify software citations
- Review methodology description

### Step 7: Final Decision
- Approve for publication
- Request revisions with comments
- Reject with explanation

## Review Features

### Comments and Feedback
- Line-level comments on trees/matrices
- General study comments
- Suggestion for corrections
- Communication with submitter

### Quality Checks
- Automated validation results
- Manual checklist items
- Cross-reference verification

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
