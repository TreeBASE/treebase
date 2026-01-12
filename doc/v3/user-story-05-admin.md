# User Story 5: Administration

## Overview

**As an** administrator,  
**I want to** administer the submission and review queue,  
**So that** I can ensure efficient processing of phylogenetic data submissions.

## User Types

- System administrators
- TreeBASE curators with admin privileges
- Queue managers

## Prerequisites

- User must be logged in with administrator privileges

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Admin dashboard
- [ ] Submission queue management
- [ ] Review queue management
- [ ] User management
- [ ] Reviewer assignment interface
- [ ] System settings
- [ ] Reports and analytics
- [ ] Audit logs

## Navigation Flow

*TODO: Document how users navigate through these pages*

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
- View all pending submissions
- Filter by status, date, submitter
- Prioritize submissions
- Assign to review track
- Bulk actions

### Review Queue Management
- View all reviews in progress
- Monitor review timelines
- Reassign reviewers
- Escalate overdue reviews
- Track completion rates

### Reviewer Assignment
- View available reviewers
- Match expertise to submissions
- Balance reviewer workload
- Set review deadlines

### User Management
- View all users
- Edit user profiles
- Manage user roles (submitter, reviewer, admin)
- Activate/deactivate accounts
- Reset passwords

### System Settings
- Configure submission requirements
- Set review workflows
- Manage notification templates
- Configure validation rules

### Reports and Analytics
- Submission statistics
- Review turnaround times
- User activity reports
- Data growth metrics

### Audit Logs
- User login history
- Data modification history
- Administrative actions
- Security events

## Admin Features

### Queue Operations
- Sort and filter options
- Bulk status updates
- Quick actions menu
- Search within queues

### Communication
- Send notifications to users
- Broadcast announcements
- Email templates

### Monitoring
- Dashboard widgets
- Alert configurations
- Status indicators

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
