
In the March migration, newly imported records were parked under a study_id with study_id 10215. The taxonlabelset table failed to have its study_id remapped to the newly created studies, so instead it retained the original temporary study_id. The update_taxonlabelset.sql query updates the study_id field in the taxonlabelset 

In the March migration, newly created treeblock records received NULL values for taxonlabelset_id, causing the download of trees to fail and causing a selection of trees under the Trees tab to fail to show the related selection of taxa after clicking on the Taxa tab. The map_treeblock_to_taxonlabelset.sql query addresses the problem by updating the treeblock's taxonlabelset_id using a value from one of the matrices related by way of an analysisstep. 

