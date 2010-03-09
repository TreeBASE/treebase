A perl script from Bill to remove duplicate taxon labels. 

Before running, adjust passwords in fixlabels_trans.pl

Was run 2010-03-03, after Dec'2009 matrices and trees were loaded, but prior to loading Dec'2009 Taxon intelligence.  

output.txt contains the output from this script. 

The stdout was: 
The database starts with 101401 redundant records.
The database ends with 0 redundant records.



This query checks whether there are duplicate taxon labels. 

SELECT COUNT(*) FROM 
(SELECT study_id, taxonlabel, COUNT(taxonlabel_id) 
FROM taxonlabel WHERE study_id IS NOT NULL 
GROUP BY study_id, taxonlabel HAVING COUNT(taxonlabel_id) > 1 
ORDER BY COUNT(taxonlabel_id) DESC) AS countrecs;

