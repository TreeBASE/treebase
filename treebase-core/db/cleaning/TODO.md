This file is to keep track of cleanup tasks that still need to be done.  

Pending data cleanups (on staging):
===================================

[Was promised to be done after migration] 

* `fixlabels_trans.zip` -- removes duplicate taxon labels - email of 2010-03-03

[Could be worthwhile to do PRIOR to Dec 2009 and Feb 2010 migrations.]

* Remove junk, particularly that created by tests
* Make sure sequences do not conflict their coresponding PKs (e.g., not smaller than the PK max)
* Known junk: Submission 22 and its related records

    Youjun's email of 2010-01-28: 
    "In table phylotree, many trees do not have a study_id value, but their phylotreenode related to study 22 via table taxonlabel.
    Their phylotree_id are: 
       1129, 1130, 1131, 1132, 2333, 2334, 2335, 2336, 2337, 2338, 2339, 2340, 2341, 2343, 2556, 2557, 2686, 2726, 2727, 2787, 
       2788, 2789, 2790, 3446, 3671, 3766, 3767, 3901, 3902, 3903, 3904, 3905, 3906, 3907, 3908, 3909, 3910, 3911, 3912, 4062,
       4063, 5705, 5706, 5707, 5708, 5720, 5721, 5921, 5941, 5981, 160000022341
       I will delete those trees because their foreign key constrain  prevent me from cleaning up about 10,000 dummy taxonlabel 
       records related to submission 22 (study 22 and submission 22 happen to be the same)." 
     VG: these IDs are from end-of-Jan treebasedev; the current treebasestage may have fewer.   

* Replace "owner" nulls (in submission.user_id?) by the special "migration" owner (see Bill's message of around 2010-02-02)

    "Also because we have reverted to an older instance of the data, the migrated records contain have NULL in the user_id of 
    the submission table. This causes null pointer errors for the unit tests. To fix the problem, I did the following:
     1. I created a user with username "migration" and email address "migration@treebase.org". This is the dummy "user" who will 
        now "own" all migrated submissions that lack an owner.
     2. I got the user_id for the "migration" user, and it happened to be 9955
     3. I ran this statement: "UPDATE submission SET user_id = 9955 WHERE user_id IS NULL"
        Now all migrated data belong to the user "migration". "
   Also, see "null->'tb1'; http://www.treebase.org/~piel/taxlabels_fix.zip; Study 22 (Jan 8)"
               
Data cleanups on production: 
============================

* Synch hibernate_sequence and {sub_matrix,sub_treebaseblock}.collection_id
  [No need if prod is restored from a dump of stage made after ~2010-02-13]
