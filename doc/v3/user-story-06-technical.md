# User Story 6: Technical Documentation

## Overview

**As a** technical stakeholder,  
**I want to** learn about the technology of the web app, database, data standards, APIs, or other
tools,  
**So that** I can integrate with TreeBASE, contribute to development, or understand its technical
architecture.

## User Types

- Software developers integrating with TreeBASE
- API consumers building applications
- Contributors to TreeBASE development
- Researchers using programmatic access
- System administrators deploying TreeBASE

## Current Pages

The following JSP pages and documentation files serve technical stakeholders:

### Web Application Pages (JSP)
- [x] **urlAPI.jsp** (`/urlAPI.html`) - Data Access page with PhyloWS API overview, URI patterns, and RSS feeds
- [x] **technology.jsp** (`/technology.html`) - Implementation technologies and architecture overview
- [x] **about.jsp** (`/about.html`) - Background, history, funding, and related resources
- [x] **contact.jsp** (`/contact.html`) - Helpdesk contact and GitHub issue tracker links
- [x] **submitTutorial.jsp** (`/submitTutorial.html`) - NEXUS file preparation and submission process
- [x] **help.jsp** (`/help.html`) - Dynamic help text system

### Documentation Files (Markdown)
- [x] **doc/API.md** - Comprehensive PhyloWS API documentation with CQL search syntax
- [x] **doc/OAI-PMH.md** - OAI-PMH metadata harvesting service documentation
- [x] **doc/development/BUILDING.md** - Build prerequisites and instructions (Java 17, Maven)
- [x] **doc/development/DEPLOYING.md** - Tomcat deployment guide with troubleshooting
- [x] **doc/technical-notes/DWR.md** - DWR (Direct Web Remoting) integration for AJAX
- [x] **doc/technical-notes/UPGRADES.md** - Dependency upgrade history (Spring, Jersey, etc.)

### External Resources
- [ ] GitHub Wiki - API documentation (linked from urlAPI.jsp) - *External, not maintained in repository*
- [ ] GitHub Wiki - OAI-PMH documentation (linked from urlAPI.jsp) - *External, not maintained in repository*

## Navigation Flow

Technical users navigate through documentation pages based on their integration needs:

```mermaid
flowchart TD
    A[Entry Points] --> B{User Goal}
    
    B -->|API Integration| C[urlAPI.html<br/>Data Access]
    B -->|Understand System| D[technology.html<br/>Implementation]
    B -->|Contribute Code| E[GitHub Repository]
    B -->|Deploy Instance| F[doc/development/]
    
    C --> G[doc/API.md<br/>PhyloWS Reference]
    C --> H[doc/OAI-PMH.md<br/>Harvesting Interface]
    
    G --> I[/phylows/study/**]
    G --> J[/phylows/tree/**]
    G --> K[/phylows/matrix/**]
    G --> L[/phylows/taxon/**]
    
    D --> M[about.html<br/>Background]
    D --> N[Architecture Diagram]
    
    E --> O[doc/development/BUILDING.md]
    E --> P[doc/technical-notes/]
    
    F --> O
    F --> Q[doc/development/DEPLOYING.md]
    
    H --> R[OAI-PMH Verbs]
    
    style C fill:#e1f5fe
    style D fill:#e8f5e9
    style E fill:#fff3e0
    style F fill:#fce4ec
```

## Technical Documentation Areas

### API Documentation

Available documentation for API consumers:

| Resource | Location | Description |
|----------|----------|-------------|
| **urlAPI.jsp** | `/urlAPI.html` | Overview of programmatic access, URI patterns, RSS feeds |
| **doc/API.md** | Repository | PhyloWS API reference with CQL search syntax and examples |
| **doc/OAI-PMH.md** | Repository | OAI-PMH harvesting verbs and usage |
| **GitHub Wiki** | External | Extended API documentation (linked from urlAPI.jsp) |

**PhyloWS Endpoints:**
- `/phylows/study/**` - Study records (PhyloWSStudyController)
- `/phylows/tree/**` - Phylogenetic trees (PhyloWSTreeController)
- `/phylows/matrix/**` - Character matrices (PhyloWSMatrixController)
- `/phylows/taxon/**` - Taxonomic data (PhyloWSTaxonController)
- `/phylows/classification/**` - Classification data (PhyloWSClassificationController)

**Output Formats:** HTML, NeXML, NEXUS, RDF, RSS 1.0, JSON

### Data Standards

Documentation for phylogenetic data formats is distributed across in-app pages and external links:

| Format | Coverage | Location |
|--------|----------|----------|
| **NEXUS** | Submission format | submitTutorial.jsp, Mesquite documentation |
| **NeXML** | Primary output format | doc/API.md, nexml.org (external) |
| **Newick** | Tree representation | Implicit in tree endpoints |
| **RDF/CDAO** | Semantic web output | doc/API.md, evolutionaryontology.org |
| **RSS 1.0** | Search result feeds | doc/API.md |

**Data Preparation:**
- **submitTutorial.jsp** (`/submitTutorial.html`) - NEXUS file preparation using Mesquite
- YouTube instructional videos embedded in submitTutorial.jsp

**Metadata Standards:**
- Dublin Core (dcterms) predicates for bibliographic metadata
- PRISM for publication dates
- TreeBASE-specific predicates (tb:) documented in API.md

### Developer Resources

Resources for contributors and developers:

| Resource | Location | Description |
|----------|----------|-------------|
| **technology.jsp** | `/technology.html` | Architecture overview, technology stack diagram |
| **BUILDING.md** | `doc/development/` | Build prerequisites (Java 17, Maven), compilation steps |
| **DWR.md** | `doc/technical-notes/` | AJAX integration with Spring 5 compatibility |
| **UPGRADES.md** | `doc/technical-notes/` | Dependency upgrade history and migration notes |
| **GitHub Repository** | github.com/TreeBASE/treebase | Source code under BSD license |
| **GitHub Issues** | Linked from contact.jsp | Bug reports and feature requests |

**Technology Stack (from technology.jsp):**
- Database: PostgreSQL
- ORM: Hibernate
- Framework: Spring
- File Parsing: Mesquite
- Tree Visualization: phylotree.js

**Project Structure:**
- `treebase-core` - ORM API for PostgreSQL database access
- `treebase-web` - MVC web application with JSP/HTML GUI
- `oai-pmh_data_provider` - OAI-PMH interface functionality

### Integration Guides

Programmatic integration documentation:

| Integration Type | Documentation | Key Information |
|-----------------|---------------|-----------------|
| **REST API Queries** | doc/API.md | PhyloWS URL patterns, CQL search syntax |
| **Metadata Harvesting** | doc/OAI-PMH.md | OAI-PMH verbs: Identify, ListRecords, GetRecord, etc. |
| **Data Download** | urlAPI.jsp | NEXUS, NeXML, JSON format parameters |
| **RSS Feeds** | doc/API.md, urlAPI.jsp | RSS 1.0 for search results and alerts |

**URI Patterns (from urlAPI.jsp):**
- Study: `{purlBase}study/TB2:S{id}`
- Matrix: `{purlBase}matrix/TB2:M{id}`
- Tree: `{purlBase}tree/TB2:Tr{id}`

**Search Examples (from API.md):**
- `/study/find?query=dcterms.contributor=Huelsenbeck`
- `/taxon/find?query=dcterms.title=="Homo sapiens"&format=rss1&recordSchema=tree`

**Note:** No official client libraries or SDKs are currently provided.

### Database Documentation

Database and data model documentation:

| Resource | Location | Description |
|----------|----------|-------------|
| **technology.jsp** | `/technology.html` | Data content overview, architecture diagram |
| **BUILDING.md** | `doc/development/` | Database connection configuration |
| **Hibernate Mappings** | `treebase-core` source | Entity relationships via ORM |

**Data Model Overview (from technology.jsp):**
1. Studies contain bibliographic references to published phylogenetic research
2. Each study has one or more analyses; each analysis has steps associating matrices, trees, algorithms, and software
3. Character matrices contain taxon labels mapped to tree leaf nodes
4. Row segments link to specimen, tissue, or gene sequence metadata
5. Taxon names map to uBio NameBank and NCBI taxonomy

**Key Entity Types:**
- Study, Citation, Analysis, AnalysisStep
- Matrix, Tree, TreeBlock
- Taxon, TaxonLabel, TaxonVariant

**Note:** Formal data dictionary and entity-relationship diagrams are not currently published.

### Deployment

Deployment and operations documentation:

| Resource | Location | Description |
|----------|----------|-------------|
| **BUILDING.md** | `doc/development/` | Prerequisites, compilation, WAR packaging |
| **DEPLOYING.md** | `doc/development/` | Tomcat configuration, JVM arguments, verification |
| **UPGRADES.md** | `doc/technical-notes/` | Dependency versions, security fixes, migration paths |
| **DOCKER.md** | Repository root | Docker deployment option |
| **docker-compose.yml** | Repository root | Container orchestration |

**System Requirements (from BUILDING.md/DEPLOYING.md):**
- Java 17 LTS
- Maven 3.8+
- PostgreSQL database
- Tomcat 7+ servlet container
- Headless Mesquite for file parsing

**Build Artifacts:**
- `treebase-web.war` (~59MB) - Main web application
- `data_provider_web.war` - OAI-PMH interface

**Configuration Files:**
- `jdbc.properties` - Database connection
- `context.xml` - Tomcat context with database and Mesquite paths

## External Documentation

*Links to existing technical documentation*

- [API.md](../API.md) - PhyloWS API documentation
- [OAI-PMH.md](../OAI-PMH.md) - OAI-PMH harvesting interface
- [Development Documentation](../development/) - Build and deployment guides
- [Technical Notes](../technical-notes/) - Implementation details

## Pages to Account For

Complete inventory of JSP pages and controllers related to technical documentation:

| Page | URL Pattern | Controller | Status |
|------|-------------|------------|--------|
| urlAPI.jsp | /urlAPI.html | filenameController | ✅ Active |
| technology.jsp | /technology.html | filenameController | ✅ Active |
| about.jsp | /about.html | filenameController | ✅ Active |
| contact.jsp | /contact.html | filenameController | ✅ Active |
| submitTutorial.jsp | /submitTutorial.html | filenameController | ✅ Active |
| help.jsp | /help.html | helpController | ✅ Active |
| sitemap.jsp | /sitemap.xml | sitemapController | ✅ Active |

**PhyloWS Controllers:**

| Controller | URL Pattern | Description |
|------------|-------------|-------------|
| PhyloWSStudyController | /phylows/study/** | Study data API |
| PhyloWSTreeController | /phylows/tree/** | Tree data API |
| PhyloWSMatrixController | /phylows/matrix/** | Matrix data API |
| PhyloWSTaxonController | /phylows/taxon/** | Taxon data API |
| PhyloWSClassificationController | /phylows/classification/** | Classification API |

**OAI-PMH Interface:**

| Endpoint | URL Pattern | Description |
|----------|-------------|-------------|
| OAI-PMH Provider | /top/oai | Metadata harvesting service |

**JSON Output Pages (for API responses):**

| Page | Purpose |
|------|---------|
| json/studyToJSON.jsp | Study JSON serialization |
| json/treeToJSON.jsp | Tree JSON serialization |
| json/matrixToJSON.jsp | Matrix JSON serialization |
| json/taxonToJSON.jsp | Taxon JSON serialization |
| anyObjectAsRDF.jsp | RDF output generation |
| searchResultsAsRDF.jsp | Search results as RDF |

## Wireframe Notes

*To be completed in future PR*

## Open Questions

- Where should API documentation live (in-app vs external)?
- What interactive API features are needed (playground, testing)?
- How should code examples be maintained?
- What versioning strategy for API documentation?
