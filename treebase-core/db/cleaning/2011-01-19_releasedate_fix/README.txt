To apply, execute the queries in fix_releasedate.sql

--------

Studies that lack a study.releasedate value fail to return a <pubDate> in the PhyloWS RSS, 
and this causes Safari's RSS reader to subsitute this value with a last-checked timestamp. 
The result is that these records appear to be new additions to the database, when in fact 
they are quite old. The NULL values in study.releasedate is an artefact of the data migration 
from TB1. Fix the releasedate and lastmodifieddate for studies that were migrated from TB1 
by using the submission.createdate.

To see how the effect, use Safari to compare the "fixed" data in treebase-dev:
http://treebase-dev.nescent.org/treebase-web/phylows/study/find?query=dcterms.contributor=Huelsenbeck&format=rss1

with the results from the same request on production here:
http://purl.org/phylo/treebase/phylows/study/find?query=dcterms.contributor=Huelsenbeck&format=rss1
