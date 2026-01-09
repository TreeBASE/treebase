# OAI-PMH Interface

TreeBASE implements the [OAI-PMH](http://www.openarchives.org/pmh/) (Open Archives Initiative Protocol for Metadata Harvesting) protocol for metadata harvesting. This interface allows third-party services to discover and harvest metadata about studies in TreeBASE.

## Base URL

The OAI-PMH base URL for TreeBASE is:

```
https://www.treebase.org/treebase-web/top/oai
```

## Protocol Version

TreeBASE implements OAI-PMH version 2.0.

## Supported Verbs

TreeBASE supports the following OAI-PMH verbs:

### Identify

Returns information about the repository.

```
https://www.treebase.org/treebase-web/top/oai?verb=Identify
```

### ListMetadataFormats

Lists the metadata formats available from the repository.

```
https://www.treebase.org/treebase-web/top/oai?verb=ListMetadataFormats
```

TreeBASE supports the following metadata formats:

| Prefix | Schema | Namespace |
|--------|--------|-----------|
| `oai_dc` | `http://www.openarchives.org/OAI/2.0/oai_dc.xsd` | `http://www.openarchives.org/OAI/2.0/oai_dc/` |
| `dryad` | `http://ils.unc.edu/mrc/dryad/version1_0/dryad_1_0.xsd` | `http://ils.unc.edu/mrc/dryad/` |

### ListRecords

Lists records (metadata plus identifier) from the repository.

```
https://www.treebase.org/treebase-web/top/oai?verb=ListRecords&metadataPrefix=oai_dc
```

Optional parameters:
- `from` - Start date for selective harvesting (format: `YYYY-MM-DDThh:mm:ssZ`)
- `until` - End date for selective harvesting (format: `YYYY-MM-DDThh:mm:ssZ`)

Example with date range:
```
https://www.treebase.org/treebase-web/top/oai?verb=ListRecords&metadataPrefix=oai_dc&from=2010-01-01T00:00:00Z&until=2011-01-01T00:00:00Z
```

### ListIdentifiers

Lists identifiers (headers only) from the repository.

```
https://www.treebase.org/treebase-web/top/oai?verb=ListIdentifiers&metadataPrefix=oai_dc
```

Optional parameters:
- `from` - Start date for selective harvesting
- `until` - End date for selective harvesting

### GetRecord

Retrieves a single record by identifier.

```
https://www.treebase.org/treebase-web/top/oai?verb=GetRecord&metadataPrefix=oai_dc&identifier=TreeBASE.org/study/TB2:S1787
```

Required parameters:
- `identifier` - The unique identifier of the record
- `metadataPrefix` - The metadata format

### ListSets

TreeBASE does not support set-based harvesting. This verb returns a `noSetHierarchy` error.

## Identifier Format

Records in TreeBASE use identifiers in the format:

```
TreeBASE.org/study/TB2:S<study_id>
```

For example: `TreeBASE.org/study/TB2:S1787`

## Dublin Core Metadata

When using the `oai_dc` metadata prefix, records contain Dublin Core elements including:

- `dc:title` - The title of the publication
- `dc:creator` - Authors of the study
- `dc:subject` - Keywords associated with the study
- `dc:description` - Study name and notes
- `dc:publisher` - Journal or publisher name
- `dc:date` - Publication year
- `dc:identifier` - PURL identifier for the study

## Example Usage

### Harvesting all records modified since a date

```bash
curl "https://www.treebase.org/treebase-web/top/oai?verb=ListRecords&metadataPrefix=oai_dc&from=2020-01-01T00:00:00Z"
```

### Getting repository information

```bash
curl "https://www.treebase.org/treebase-web/top/oai?verb=Identify"
```

### Retrieving a specific record

```bash
curl "https://www.treebase.org/treebase-web/top/oai?verb=GetRecord&metadataPrefix=oai_dc&identifier=TreeBASE.org/study/TB2:S1787"
```

## Error Handling

The OAI-PMH interface returns standard OAI-PMH error codes:

- `badVerb` - Invalid or missing verb
- `badArgument` - Invalid argument format (e.g., invalid date format)
- `cannotDisseminateFormat` - Invalid metadataPrefix
- `idDoesNotExist` - Invalid record identifier
- `noSetHierarchy` - Sets are not supported (returned by ListSets)

## Related Documentation

- [TreeBASE PhyloWS API](https://github.com/TreeBASE/treebase/wiki/API)
- [OAI-PMH Specification](http://www.openarchives.org/OAI/openarchivesprotocol.html)
- [Dublin Core Metadata Initiative](http://dublincore.org/)
