# User Story 7: Governance

## Overview

**As a** governance stakeholder,  
**I want to** learn about the project, its history, contributors, and role in phylogenetic data
management,  
**So that** I can understand TreeBASE's position in the scientific data ecosystem and make
informed decisions about its future.

## User Types

- Funding agency representatives
- Institutional administrators
- Scientific advisory board members
- Policy makers
- Potential partners and collaborators
- Science journalists and communicators

## Current Pages

The following pages in the current TreeBASE web application relate to this user story:

- [x] About page (`/about.html`) - Background information, history, funding, governance, and related resources
- [ ] History/timeline page - *Not a separate page; history content is included in the About page*
- [x] Contributors/team page (`/people.html`) - Current and past contributors with photos and roles
- [x] Mission statement - *Included in About page and Home page*
- [x] Data usage policies (`/dataMan.html`) - NSF Data Management Plan covering submission, integrity, standards, and persistence
- [ ] Terms of service - *Not currently available as a dedicated page*
- [ ] Privacy policy - *Not currently available as a dedicated page*
- [x] Contact information (`/contact.html`) - Help desk email and bug reporting via GitHub issues
- [ ] Governance structure - *Basic information included in About page; no dedicated governance page*
- [x] Funding acknowledgments - *Included in About page with NSF grant numbers*
- [x] Partnerships page (`/partnership.html`) - Current and historical institutional partners
- [x] References/Citations page (`/reference.html`) - How to cite TreeBASE publications
- [x] Technology page (`/technology.html`) - Implementation details and source code information
- [x] Journals page (`/journal.html`) - Partner journals and publication policies

## Navigation Flow

Users navigate to governance-related pages primarily through the left sidebar menu available on the home page and all main pages. The current navigation structure is:

```
[Home] (/home.html)
    |
    +---> Left Sidebar Navigation
              |
              +---> [Search TreeBASE] (/search/studySearch.html)
              |
              +---> [Submit Data] (/user/processUser.html)
              |
              +---> Documentation
              |         |
              |         +---> [Technology] (/technology.html)
              |         |
              |         +---> [Submit Tutorial] (/submitTutorial.html)
              |         |
              |         +---> [Data Access/API] (/urlAPI.html)
              |
              +---> About
              |         |
              |         +---> [Overview] (/about.html)
              |         |           - Background
              |         |           - History, Funding, and Governance
              |         |           - Related resources
              |         |           - Logo
              |         |
              |         +---> [People] (/people.html)
              |         |           - Current contributors
              |         |           - Past developers
              |         |
              |         +---> [Partnerships] (/partnership.html)
              |         |           - Naturalis Biodiversity Center
              |         |           - NESCent (historical)
              |         |           - CIPRES
              |         |           - Dryad
              |         |
              |         +---> [References] (/reference.html)
              |                     - How to cite TreeBASE
              |                     - Further reading
              |
              +---> [NSF Data Management] (/dataMan.html)
              |
              +---> [Journals] (/journal.html)
              |
              +---> [Contact] (/contact.html)
                        - Help desk email
                        - GitHub issue tracker
```

**External Links in Footer:**
- Mendeley group for TreeBASE publications
- Twitter @TreeBASE
- Data Citation Index badge

## Governance Information

### About TreeBASE
- What is TreeBASE
- Mission statement
- Vision for the future
- Value proposition

### History
- Timeline of development
- Major milestones
- Evolution of the platform
- Historical context in phylogenetics

### Team and Contributors
- Current team members
- Advisory board
- Past contributors
- Institutional affiliations

### Governance Structure
- Decision-making processes
- Organizational structure
- Partner institutions
- Community involvement

### Policies
- Data submission policies
- Data usage and licensing
- Terms of service
- Privacy policy
- Code of conduct

### Impact and Metrics
- Usage statistics
- Citation information
- Data holdings summary
- Community reach

### Funding and Support
- Current funders
- Historical funding
- How to support TreeBASE
- Institutional partnerships

### Contact and Engagement
- Contact information
- Social media presence
- Mailing lists
- Community forums

## Pages to Account For

The following is a complete inventory of pages related to governance:

| Page | URL Pattern | Status | Notes |
|------|-------------|--------|-------|
| Home | `/home.html` | ✅ Exists | Welcome message, mission overview, recent studies |
| About/Overview | `/about.html` | ✅ Exists | Background, history, funding, governance, related resources |
| People/Team | `/people.html` | ✅ Exists | Current and past contributors with photos |
| Partnerships | `/partnership.html` | ✅ Exists | Institutional partners (Naturalis, NESCent, CIPRES, Dryad) |
| References | `/reference.html` | ✅ Exists | How to cite TreeBASE |
| Technology | `/technology.html` | ✅ Exists | Implementation stack, source code links |
| Submit Tutorial | `/submitTutorial.html` | ✅ Exists | Submission process documentation |
| Data Access/API | `/urlAPI.html` | ✅ Exists | Web service and programmatic access |
| NSF Data Management | `/dataMan.html` | ✅ Exists | Data management plan details |
| Journals | `/journal.html` | ✅ Exists | Partner journals requiring TreeBASE submissions |
| Contact | `/contact.html` | ✅ Exists | Help desk email, GitHub issue tracker |
| Terms of Service | N/A | ❌ Missing | No dedicated page |
| Privacy Policy | N/A | ❌ Missing | No dedicated page |
| Governance Structure | N/A | ❌ Missing | Basic info in About page only |
| Usage Statistics | N/A | ❌ Missing | Some stats on home page, no dedicated page |
| Impact Metrics | N/A | ❌ Missing | No dedicated page |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What governance information should be prominently featured?
- How should historical information be presented?
- What metrics should be publicly displayed?
- How should contact/support requests be handled?
