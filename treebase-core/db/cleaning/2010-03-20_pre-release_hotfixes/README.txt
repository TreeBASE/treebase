fix_taxonlabels.sql  
   detects TaxonLabelSets whose study_id still refers to a dummy study used for loading from TB1 dumps
   and updates it to the proper real study. 
   
This was applied to the production DB at treebase.nescent.org by Jon on 2010-03-20.  


=============================================================
Some relevant correspondence: 

From: 	vladimir.gapeyev@duke.edu
	Subject: 	Re: [Treebase-devel] Some results of testing with a user
	Date: 	March 19, 2010 4:00:22 PM EDT
	To: 	william.piel@yale.edu
	Cc: 	Treebase-devel@lists.sourceforge.net
	
On Mar 19, 2010, at 3:25 PM, William Piel wrote:


On Mar 19, 2010, at 12:07 PM, William Piel wrote:


On Mar 19, 2010, at 11:44 AM, Hilmar Lapp wrote:

On Mar 18, 2010, at 8:06 PM, Vladimir Gapeyev wrote:

(1) Unexpected different results on the Taxa tab -- a feature or a bug? E.g., find a single study, e.g. 10051. 
    -- Click the study (which goes to Citation tab), then to Taxa tab ==> "Nothing to display"
    -- Go to Matrices tab; click on "View Taxa" in the table ==> it goes back to the Taxa tab, showing lots of stuff
    -- Go back to Citation tab; then Taxa tab  ==> "Nothing found to display" 
   -- Go to Trees tab; click on "View Taxa" in the table ==> it goes back to the Taxa tab, showing lots of stuff.

This is for Bill to judge in terms of what kind of problem it may signal, and hence how severe it is.

This may be severe -- let's at least understand why it is happening. It is affecting a lot of studies.

I think I've figured out what's going on here. I took one taxon label that maps nicely and compared it with another taxon label that does not map nicely, and then I ran queries on tables related to these taxon labels. The main difference is that in the good case the taxonlabelset table has the correct study_id, while in the bad case the taxonlabelset has "2264" as study_id instead of "10053", which is what it should be. 

Last weekend I had solved this problem using an update query that assumed that study_id 10215 was the only "placeholder" for migrated records. Turns out that there seems to be another one: 2264. Vladimir, can you confirm that study_id 2264 is another placeholder? And if so, can you let me know if there are any other placeholder study_ids that I'm not aware of. 

If this is the problem as I think it is, the solution is the following update:

UPDATE taxonlabelset SET study_id = mx.study_id
FROM matrix mx JOIN taxonlabelset tls USING (taxonlabelset_id)
WHERE tls.study_id = 2264
AND mx.study_id <> 2264
AND taxonlabelset.taxonlabelset_id = tls.taxonlabelset_id

I followed a similar path and was reaching a similar conclusion. Here is a query that shows all TaxonLabelSets whose tls.study_id is not the same as the study reachable from the TLS via a matrix: 

select s.study_id, m.matrix_id, tls.*
from study s, taxonlabelset tls, matrix m 
where s.study_id = m.study_id and tls.taxonlabelset_id = m.taxonlabelset_id
  and s.study_id <> tls.study_id
order by s.study_id

The only Study IDs showing up as tls.study_id  in the result are  22 and 2264.   

#2264 was picked up by the migration scripts during my 1st migration -- not something I could have known ahead of time, since the scripts were creating a fresh dummy study in my testing daabase! By the time of the 2nd migration batch I realized it was going on and created the 10215 study. 

I see Bill has already run a fix on the 2264 TLSs.  Should we do it for 22-TLSs as well?  

   
   

   