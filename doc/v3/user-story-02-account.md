# User Story 2: Account Management

## Overview

**As a** user,  
**I want to** create or update my user account and log in,  
**So that** I can access authenticated features like data submission and study management.

## User Types

- New users creating an account
- Existing users logging in
- Users updating their profile information
- Users managing their password/credentials

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Registration page
- [ ] Login page
- [ ] Logout functionality
- [ ] User profile page
- [ ] Password reset page
- [ ] Account settings page

## Navigation Flow

*TODO: Document how users navigate through these pages*

```
[Any Page] --> [Login Link] --> [Login Page] --> [Authenticated Home]
                   |                 |
                   v                 v
          [Registration]     [Forgot Password]
                   |                 |
                   v                 v
            [Verify Email]   [Reset Password]
```

## Account Features

### Registration
- Email address
- Username
- Password requirements
- Institutional affiliation
- ORCID integration (if applicable)

### Authentication
- Username/password login
- Session management
- Remember me functionality
- Logout from all devices

### Profile Management
- Update personal information
- Change email address
- Update password
- Manage notification preferences
- View submission history

## Pages to Account For

*TODO: Complete inventory of pages related to account management*

| Page | URL Pattern | Status |
|------|-------------|--------|
| | | |

## Security Considerations

- Password strength requirements
- Account lockout policies
- Session timeout
- HTTPS requirements
- Email verification

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What OAuth/SSO options should be supported?
- What information is required vs optional during registration?
- How long should sessions last?
