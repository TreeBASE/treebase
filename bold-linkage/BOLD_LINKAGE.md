# BOLD Linkage Feature

This document describes the BOLD Linkage feature that enables [DiSSCo](https://www.dissco.eu/) (Distributed System of Scientific Collections) to query the [BOLD](https://www.boldsystems.org/) (Barcode of Life Data Systems) API to enrich digital specimens with DNA barcode data.

## Table of Contents

- [Overview](#overview)
- [User Perspective](#user-perspective)
  - [What is BOLD?](#what-is-bold)
  - [What is DiSSCo?](#what-is-dissco)
  - [Benefits of BOLD Linkage](#benefits-of-bold-linkage)
  - [Use Cases](#use-cases)
- [Technical Implementation](#technical-implementation)
  - [Architecture Overview](#architecture-overview)
  - [BOLD API Integration](#bold-api-integration)
  - [Data Flow](#data-flow)
  - [API Endpoints](#api-endpoints)
  - [Data Mapping](#data-mapping)
  - [Error Handling](#error-handling)
- [Configuration](#configuration)
- [Examples](#examples)
- [References](#references)

---

## Overview

The BOLD Linkage feature provides a bridge between TreeBASE's phylogenetic data and BOLD's extensive DNA barcode database. This integration enables DiSSCo to automatically enrich digital specimen records with molecular identification data, providing researchers with a more complete picture of biodiversity data.

By linking TreeBASE specimens to BOLD records, users can:
- Verify taxonomic identifications using DNA barcoding
- Access sequence data associated with specimens
- Connect phylogenetic analyses to voucher specimens
- Enable cross-platform data discovery and reuse

---

## User Perspective

### What is BOLD?

The **Barcode of Life Data Systems (BOLD)** is an online workbench and database that supports the acquisition, storage, analysis, and publication of DNA barcode records. BOLD provides:

- A database of barcode sequences linked to specimen data
- Tools for quality control and taxonomic assignment
- Public APIs for programmatic data access
- Integration with GenBank and other sequence repositories

BOLD is the central repository for the International Barcode of Life project (iBOL), housing millions of barcode sequences from hundreds of thousands of species.

### What is DiSSCo?

The **Distributed System of Scientific Collections (DiSSCo)** is a pan-European research infrastructure for natural science collections. DiSSCo aims to:

- Digitize and unify access to European natural science collections
- Create Digital Specimen objects that aggregate data from multiple sources
- Enable FAIR (Findable, Accessible, Interoperable, Reusable) data principles
- Support biodiversity research through integrated specimen data

DiSSCo's Digital Specimens represent physical specimens with enriched metadata gathered from various authoritative sources, including TreeBASE and BOLD.

### Benefits of BOLD Linkage

The BOLD Linkage feature provides significant value to researchers and collections managers:

| Benefit | Description |
|---------|-------------|
| **Molecular Verification** | Confirm species identifications using DNA barcodes, reducing taxonomic uncertainty |
| **Data Enrichment** | Automatically augment specimen records with sequence data, collection details, and geographic coordinates |
| **Cross-Reference Discovery** | Find related specimens across collections by matching barcode sequences |
| **Provenance Tracking** | Link published phylogenetic trees to their underlying voucher specimens |
| **Research Acceleration** | Reduce manual data lookup time by automating the linkage process |
| **Data Quality** | Identify potential misidentifications or interesting taxonomic variants |

### Use Cases

#### 1. Specimen Identification Verification

A museum curator digitizing historical specimens can use BOLD Linkage to verify identifications:

```
1. Digital Specimen is created in DiSSCo
2. DiSSCo queries TreeBASE for related phylogenetic data
3. TreeBASE uses BOLD Linkage to find matching barcode sequences
4. Results help confirm or question the specimen's taxonomic assignment
```

#### 2. Phylogenetic Analysis Enhancement

A researcher publishing a phylogenetic tree can link their analysis to primary specimen data:

```
1. Researcher submits tree to TreeBASE with taxon labels
2. BOLD Linkage resolves taxon names to BIN (Barcode Index Numbers)
3. Linked BOLD records provide voucher specimen information
4. Published tree gains traceable provenance to physical specimens
```

#### 3. Biodiversity Assessment

A biodiversity scientist can aggregate data from multiple sources:

```
1. Query DiSSCo for specimens from a geographic region
2. Retrieve linked BOLD barcode data via TreeBASE
3. Analyze molecular diversity alongside morphological data
4. Generate comprehensive biodiversity assessments
```

---

## Technical Implementation

### Architecture Overview

The BOLD Linkage feature is implemented as a service layer within TreeBASE that mediates between DiSSCo's Digital Specimen infrastructure and the BOLD API.

```
┌─────────────────────────────────────────────────────────────────┐
│                          DiSSCo                                 │
│                  (Digital Specimen Hub)                         │
└─────────────────────────────┬───────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        TreeBASE                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                   BOLD Linkage Service                    │  │
│  │  ┌─────────────────┐  ┌──────────────┐  ┌──────────────┐  │  │
│  │  │  Query Builder  │  │  Response    │  │   Cache      │  │  │
│  │  │                 │  │  Parser      │  │   Manager    │  │  │
│  │  └─────────────────┘  └──────────────┘  └──────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              TreeBASE Core (Existing)                     │  │
│  │     Taxon Labels  │  Matrices  │  Trees  │  Studies       │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────┬───────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        BOLD Systems                             │
│              (Barcode of Life Data Systems)                     │
│         API v4: https://v4.boldsystems.org/api                  │
└─────────────────────────────────────────────────────────────────┘
```

### BOLD API Integration

The BOLD Linkage service integrates with the [BOLD API v4](https://v4.boldsystems.org/api-docs). Key API endpoints used:

#### Taxonomy Endpoint

Query taxonomic information by name:

```
GET https://v4.boldsystems.org/api/taxonomy/name/{taxon_name}
```

**Parameters:**
- `taxon_name`: Scientific name to search

**Response:** Taxonomic hierarchy, synonyms, and associated data

#### Specimen/Sequence Search

Search for specimens and sequences:

```
GET https://v4.boldsystems.org/api/occurrences/search
```

**Parameters:**
- `taxon`: Taxonomic name filter
- `geo`: Geographic region filter
- `marker`: Genetic marker (e.g., COI-5P)
- `institution`: Institution code
- `format`: Response format (json, tsv)

#### BIN (Barcode Index Number) Lookup

Retrieve information about a specific BIN:

```
GET https://v4.boldsystems.org/api/bin/{bin_id}
```

**Parameters:**
- `bin_id`: BOLD BIN identifier (e.g., BOLD:AAA0001)

### Data Flow

The data flow for enriching a digital specimen proceeds as follows:

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   DiSSCo    │       │  TreeBASE   │       │    BOLD     │       │   Result    │
│   Request   │──────▶│   Service   │──────▶│     API     │──────▶│   Cache     │
└─────────────┘       └─────────────┘       └─────────────┘       └─────────────┘
      │                     │                      │                      │
      │  1. Query with      │  2. Build BOLD      │  3. Return           │
      │     taxon name      │     API request     │     matches          │
      │                     │                      │                      │
      ▼                     ▼                      ▼                      ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   Enriched  │◀──────│   Mapped    │◀──────│   Parsed    │◀──────│   Cached    │
│   Specimen  │       │   Response  │       │   JSON      │       │   Data      │
└─────────────┘       └─────────────┘       └─────────────┘       └─────────────┘
      │                     │                      │                      │
      │  6. Return to       │  5. Map to          │  4. Parse BOLD       │
      │     DiSSCo          │     DiSSCo schema   │     response         │
```

**Step Details:**

1. **Request Reception**: DiSSCo sends a specimen enrichment request containing taxon name, geographic coordinates, or other identifiers
2. **Query Construction**: The BOLD Linkage service builds appropriate BOLD API queries based on available identifiers
3. **API Call**: Service queries BOLD API, respecting rate limits and handling pagination
4. **Response Parsing**: JSON response is parsed and validated
5. **Data Mapping**: BOLD data is mapped to DiSSCo's Digital Specimen schema
6. **Response Return**: Enriched data is returned to DiSSCo for integration

### API Endpoints

The BOLD Linkage service exposes the following endpoints within TreeBASE:

#### Taxon Enrichment

```
GET /phylows/taxon/{id}/bold
```

Retrieves BOLD data linked to a TreeBASE taxon label.

**Parameters:**
- `id`: TreeBASE taxon identifier (e.g., TB2:Tl12345)
- `format`: Response format (json, nexml) - default: json

**Response:**
```json
{
  "treebaseTaxon": {
    "id": "TB2:Tl12345",
    "name": "Homo sapiens"
  },
  "boldMatches": [
    {
      "bin": "BOLD:AAA0001",
      "processId": "ABCD001-20",
      "specimenId": "SPECIMEN123",
      "institution": "Museum X",
      "sequence": {
        "marker": "COI-5P",
        "length": 658,
        "accession": "GU123456"
      },
      "taxonomy": {
        "phylum": "Chordata",
        "class": "Mammalia",
        "order": "Primates",
        "family": "Hominidae",
        "genus": "Homo",
        "species": "Homo sapiens"
      },
      "collection": {
        "country": "Kenya",
        "coordinates": {
          "lat": -1.2921,
          "lon": 36.8219
        }
      }
    }
  ],
  "matchCount": 1,
  "queryTimestamp": "2024-01-15T10:30:00Z"
}
```

#### Batch Enrichment

```
POST /phylows/taxon/bold/batch
```

Batch enrichment for multiple taxa.

**Request Body:**
```json
{
  "taxa": [
    {"id": "TB2:Tl12345"},
    {"id": "TB2:Tl12346"},
    {"name": "Drosophila melanogaster"}
  ],
  "options": {
    "includeSequences": true,
    "maxResultsPerTaxon": 10
  }
}
```

### Data Mapping

The following table shows how BOLD fields map to DiSSCo Digital Specimen attributes:

| BOLD Field | DiSSCo Attribute | Description |
|------------|------------------|-------------|
| `processid` | `ods:specimenIdentifier` | Unique specimen identifier |
| `bin_uri` | `ods:barcode` | Barcode Index Number |
| `species_name` | `dwc:scientificName` | Scientific name |
| `institution_storing` | `dwc:institutionCode` | Holding institution |
| `lat` / `lon` | `dwc:decimalLatitude` / `dwc:decimalLongitude` | Collection coordinates |
| `country` | `dwc:country` | Collection country |
| `nucleotides` | `ods:geneticSequence` | DNA sequence |
| `markercode` | `ods:markerType` | Genetic marker used |

### Error Handling

The BOLD Linkage service implements robust error handling:

| Error Code | Description | Action |
|------------|-------------|--------|
| `BOLD_001` | BOLD API unavailable | Return cached data if available; retry with exponential backoff |
| `BOLD_002` | Rate limit exceeded | Queue request; retry after cooldown period |
| `BOLD_003` | Taxon not found | Return empty result with suggestion for alternative names |
| `BOLD_004` | Invalid response format | Log error; return partial results if possible |
| `BOLD_005` | Timeout | Return partial cached data; mark as incomplete |

**Retry Policy:**
- Maximum retries: 3
- Initial backoff: 1 second
- Backoff multiplier: 2
- Maximum backoff: 30 seconds

---

## Configuration

The BOLD Linkage service is configured via environment variables or application properties:

```properties
# BOLD API Configuration
bold.api.baseUrl=https://v4.boldsystems.org/api
bold.api.timeout=30000
bold.api.maxRetries=3

# Cache Configuration
bold.cache.enabled=true
bold.cache.ttl=86400
bold.cache.maxSize=10000

# Rate Limiting
bold.rateLimit.requestsPerSecond=10
bold.rateLimit.burstSize=20

# Logging
bold.logging.level=INFO
bold.logging.includeResponses=false
```

---

## Examples

### Example 1: Querying by Taxon Name

**Request:**
```bash
curl -X GET "https://treebase.org/treebase-web/phylows/taxon/TB2:Tl123456/bold?format=json"
```

**Response:**
```json
{
  "treebaseTaxon": {
    "id": "TB2:Tl123456",
    "name": "Drosophila melanogaster"
  },
  "boldMatches": [
    {
      "bin": "BOLD:AAB1234",
      "processId": "DROME001-21",
      "specimenId": "FLY-2021-001",
      "institution": "Natural History Museum",
      "taxonomy": {
        "order": "Diptera",
        "family": "Drosophilidae",
        "genus": "Drosophila",
        "species": "Drosophila melanogaster"
      }
    }
  ],
  "matchCount": 1
}
```

### Example 2: Batch Enrichment via DiSSCo

**Request:**
```bash
curl -X POST "https://treebase.org/treebase-web/phylows/taxon/bold/batch" \
  -H "Content-Type: application/json" \
  -d '{
    "taxa": [
      {"name": "Homo sapiens"},
      {"name": "Pan troglodytes"}
    ],
    "options": {
      "includeSequences": false,
      "maxResultsPerTaxon": 5
    }
  }'
```

---

## References

### BOLD Systems
- [BOLD Systems Website](https://www.boldsystems.org/)
- [BOLD API v4 Documentation](https://v4.boldsystems.org/api-docs)
- [BOLD Handbook](https://v3.boldsystems.org/index.php/resources/handbook)

### DiSSCo
- [DiSSCo Website](https://www.dissco.eu/)
- [DiSSCo Technical Design](https://www.dissco.eu/technical-design/)
- [Digital Specimen Specification](https://github.com/DiSSCo/openDS)

### Related TreeBASE Documentation
- [TreeBASE API](../doc/API.md)
- [OAI-PMH Interface](../doc/OAI-PMH.md)

### Standards
- [Darwin Core Terms](https://dwc.tdwg.org/terms/)
- [FAIR Principles](https://www.go-fair.org/fair-principles/)

---

## Changelog

| Version | Date | Description |
|---------|------|-------------|
| 1.0.0 | 2024-01-15 | Initial documentation release |
