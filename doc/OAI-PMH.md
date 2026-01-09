# OAI-PMH

*Restored from [archive.org](https://web.archive.org/web/20130306042254/http://sourceforge.net/apps/mediawiki/treebase/index.php?title=OAI-PMH)*

## Metadata Harvesting Service

Metadata on TreeBASE records can be acquired programmatically through an OAI-PMH service.

## OAI-PMH

The behavior and extent of metadata served is under development and subject to change.

## Verbs

The service responds to the following verbs:

- http://treebase.org/treebase-web/top/oai?verb=Identify
- http://treebase.org/treebase-web/top/oai?verb=ListMetadataFormats
- http://treebase.org/treebase-web/top/oai?verb=ListSets
- http://treebase.org/treebase-web/top/oai?verb=ListRecords&metadataPrefix=oai_dc&until=1996-11-04T00:00:00Z
- http://treebase.org/treebase-web/top/oai?verb=ListRecords&metadataPrefix=oai_dc&from=2011-04-30T00:00:00Z
- http://treebase.org/treebase-web/top/oai?verb=GetRecord&metadataPrefix=oai_dc&identifier=TB:s1234
- http://treebase.org/treebase-web/top/oai?verb=ListIdentifiers&metadataPrefix=oai_dc&until=1996-11-04T00:00:00Z
