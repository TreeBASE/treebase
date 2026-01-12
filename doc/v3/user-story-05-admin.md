# User Story 5: Administration

## Overview

**As an** administrator,  
**I want to** administer studies and users,  
**So that** I can ensure efficient processing of phylogenetic data submissions.

## User Types

- System administrators
- TreeBASE curators with admin privileges
- Queue managers

## Prerequisites

- User must be logged in with administrator privileges

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Admin landing page
- [ ] Study management for a user
- [ ] Management of ready studies
- [ ] Search submissions
- [ ] Select studies
- [ ] Select users
- [ ] Update user info
- [ ] Delete user
- [ ] Merge users
- [ ] Select persons
- [ ] Merge persons

## Navigation Flow

*TODO: Document how users navigate through these pages in a mermaid plot*

```
[Admin Dashboard] --> [Submission Queue]
        |                    |
        |                    v
        |             [Assign Reviewer]
        |                    |
        v                    v
[Review Queue] <--> [Monitor Progress]
        |                    |
        v                    v
[User Management]    [Reports/Analytics]
        |                    |
        v                    v
[System Settings]    [Audit Logs]
```

## Administration Functions

### Submission Queue Management

*Todo: identify all pages and their respective form fields for the operations*

- View all pending submissions
- Filter by status, submitter
- Search submissions
- Bulk actions

### User Management

*Todo: identify all pages and their respective form fields for the operations*

- Select users
- Update user info 
- Delete user
- Merge users
- Select persons
- Merge persons

## Pages to Account For

*TODO: Complete inventory of pages related to administration*

| Page | URL Pattern | Status |
|------|-------------|--------|
| | | |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What admin roles/permission levels are needed?
- What metrics should be tracked on the dashboard?
- How should escalation workflows work?
- What audit requirements exist?
