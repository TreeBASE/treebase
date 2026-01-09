# API

*Restored from [archive.org](https://web.archive.org/web/20130521145240/http://sourceforge.net/apps/mediawiki/treebase/index.php?title=API)*

## Web service API

TreeBASE can be accessed programmatically through a stateless web service interface and URL architecture. This interface can deliver data in several different formats, including NEXUS, JSON, NeXML.

## PhyloWS

The site structure described here is designed to be compliant with the emerging [PhyloWS](http://evoinfo.nescent.org/PhyloWS) standard. One of the tenets of the standard is that URLs contain a **/phylows/** delimiter below which the standard recommends a [simple API](http://www.nescent.org/wg_evoinfo/PhyloWS/REST) to derefence phylogenetic data by their accession numbers. In the examples below, the url fragments come immediately below the **/phylows/** delimiter (everything between the http:// and phylows is considered subject to change, likely to be stabilized using [purl](http://purl.org/) addresses).

## Site sections

The data on the TreeBASE2 website are organized in four subsections:

- **taxon/** operational taxonomic units, taxonomic mappings and outlinks
- **matrix/** character state matrices, morphological character definitions
- **tree/** contains trees and tree nodes
- **study/** full submission records, including citation and analysis records

Within those four sections, every item in the TreeBASE2 database can be de-referenced by appending the item's full identifier to the right section name. For example, **tree/TB2:Tr2227** represents a tree (and returns a simple RDF file to describe the tree). For some classes of objects, these short addresses can be passed a **format** parameter to specify in which data format to represent the object: [study/TB2:S1787?format=html](http://purl.org/phylo/treebase/phylows/study/TB2:S1787?format=html). Identifiers that match any of the following expressions can be represented as [nexml](http://purl.org/phylo/treebase/phylows/study/TB2:S1787?format=nexml), [nexus](http://purl.org/phylo/treebase/phylows/study/TB2:S1787?format=nexus), [rdf](http://purl.org/phylo/treebase/phylows/study/TB2:S1787?format=rdf) or [html](http://purl.org/phylo/treebase/phylows/study/TB2:S1787?format=html):

- **matrix/TB2:M[0-9]+** character state matrix
- **tree/TB2:Tr[0-9]+** phylogenetic tree
- **study/TB2:S[0-9]+** study record

## Searching

The TreeBASE website can be searched using a subset of constructs from the [CQL](http://www.loc.gov/standards/sru/specs/cql.html) specification. Specifically, the [predicates listed here with an asterisk](http://spreadsheets.google.com/pub?key=rL--O7pyhR8FcnnG5-ofAlw) can be used in statements in the site section they apply to, such that, for example a taxon can be retrieved by its NCBI ID like so:

**/taxon/find?query=tb.identifier.ncbi=&lt;ncbi taxon id&gt;**

or by its name like so:

**/taxon/find?query=tb.title.taxon=&lt;name&gt;**

or using an exact match (**==**) or a case-insensitive one (**=/ignoreCase**). These statements can be combined with boolean **and**, **or** and **not**.

For example:

[/study/find?query=dcterms.contributor=Huelsenbeck or dcterms.contributor=Ronquist](http://purl.org/phylo/treebase/phylows/study/find?query=dcterms.contributor=Huelsenbeck+or+dcterms.contributor=Ronquist)

Finally, searching can be modified to project the results from one section info those of another. The effect is roughly the same as switching between tabs in the search section: if the results are a list of tree and you click on the matrix search tab, the trees are converted to the set of matrices on which the trees are based. This behaviour can be used by specifying the **recordSchema=&lt;section&gt;** argument, i.e.:

[/taxon/find?query=dcterms.title=="Homo sapiens"&format=rss1&recordSchema=tree](http://purl.org/phylo/treebase/phylows/taxon/find?query=dcterms.title==%22Homo%20sapiens%22&format=rss1&recordSchema=tree)

returns all the trees that have Homo sapiens in them. By default, all these queries return a web page, but with a format=rss1 argument the search results are listed in an RDF compatible RSS1.0 file, i.e.:

**/taxon/find?query=tb.title.taxon=&lt;name&gt;&format=rss1**

The returned results in RSS1.0 use the short urls of the form &lt;section&gt;/&lt;id&gt;, whose returned resource descriptions (like [this](http://purl.org/phylo/treebase/phylows/tree/TB2:Tr2227) one) need to be scanned to discover suitable serialization formats.

Developing the API is an on-going process, so the choice of operators is limited, with some available only in certain situations. For example, the ">" or "<" comparators are largely limited to the study creation (prism.creationDate), modification (prism.modificationDate), and publication dates (prism.publicationDate). For a list of all studies created after August 30 2011:

[/study/find?query=prism.creationDate>"2011-08-30T05:00:00Z"&format=rss1](http://purl.org/phylo/treebase/phylows/study/find?query=prism.creationDate%3E%222011-08-30T00:00:00Z%22&format=rss1)

If you're building a client to mirror TreeBASE, querying the modification date will help track older studies that have since been modified. The publication date value is stored in the database as the year of the citation, with TreeBASE located in the Eastern Standard Time zone. Consequently, finding all TreeBASE citations published in 2010 or later means searching for ">" on any date between 2010-01-01T05:00:00Z and 2011-01-01T04:59:59Z.

## Output formats

When a record of interest is located, it can be downloaded in a variety of data formats. At present, these include the commonly-used NEXUS format. In addition, the primary format for downloading data that is richer than NEXUS is [NeXML](http://nexml.org/). The website uses the NeXML annotation feature extensively to transmit metadata stored by the database. NeXML annotations are [RDFa](http://www.w3.org/TR/xhtml-rdfa-primer/) compliant element structures that use **CURIE** strings to identify metadata properties, and @content attributes to store the property value. For example, this (simplified) annotation: **&lt;meta content="uBio:2538170" property="tb:identifier.ubio"/&gt;** means that the element that encloses it has a special kind of identifier attached to it, namely one that TreeBASE recognizes as originating in uBio.

The salient part is the CURIE string predicate **tb:identifier.ubio**, which is one of a [long list](http://spreadsheets.google.com/pub?key=rL--O7pyhR8FcnnG5-ofAlw) of proposed predicates that are written in TreeBASE's NeXML output and can be used as [CQL](http://www.loc.gov/standards/sru/specs/cql.html) search predicates. The predicates proposed (and now experimentally transmitted) are intended to be subclasses of predicates from commonly used vocabularies. For example, **tb:identifier.ubio** inherits from [dcterms:identifier](http://dublincore.org/documents/dcmi-terms/#terms-identifier) and so any of the latter's semantics apply to the former, which is refined to indicate that the value is a uBio namebank ID.

Using a transformation from NeXML to [CDAO](http://www.evolutionaryontology.org/), data can also be downloaded as RDF/XML, which captures the same information. An advantage of this format is that it exposes the data to the semantic web. A disadvantage is that this is a much more verbose serialization. For a more concise output, e.g. for AJAX applications, a transformation from NeXML to JSON (using Google's XML-to-JSON mapping) is also available.
