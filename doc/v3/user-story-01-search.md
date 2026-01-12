# User Story 1: Search

## Overview

**As a** user or automated agent,  
**I want to** search for study, tree, matrix, taxon or analysis data,  
**So that** I can find and access phylogenetic data relevant to my research or application.

## User Types

- Anonymous visitors (researchers, students, general public)
- Automated agents (harvesters, API clients, search engines)
- Registered users looking for data

## Current Pages

*TODO: Identify and list all current pages that apply to this user story*

- [ ] Home page search interface
- [ ] Advanced search page
- [ ] Search results page
- [ ] Study detail page
- [ ] Tree detail page
- [ ] Matrix detail page
- [ ] Taxon detail page
- [ ] Analysis detail page

## Navigation Flow

*TODO: Document how users navigate through these pages*

```
[Entry Point] --> [Search Interface] --> [Search Results] --> [Detail Page]
                        |                       |
                        v                       v
              [Advanced Search]          [Related Data]
```

## Search Capabilities

### Search Types
- Full-text search
- Search by study ID
- Search by author
- Search by taxon
- Search by publication
- Search by tree characteristics
- Search by matrix characteristics

### Data Types Searchable
1. **Studies** - Published research studies with phylogenetic data
2. **Trees** - Phylogenetic trees associated with studies
3. **Matrices** - Character matrices used in analyses
4. **Taxa** - Taxonomic names and their occurrences
5. **Analyses** - Analysis metadata and parameters

## API/Agent Access

*TODO: Document programmatic access points for this user story*

- PhyloWS API endpoints
- OAI-PMH harvesting interface
- Direct URL patterns

## Pages to Account For

*TODO: Complete inventory of pages related to search functionality*

| Page | URL Pattern | Status |
|------|-------------|--------|
| | | |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- What search refinement options should be available?
- How should results be sorted and paginated?
- What metadata should be shown in search results?
