To apply, run set_study_in_phylotrees.sql

--------

From: 	william.piel@yale.edu
	Subject: 	Re: [help.nescent.org #7817] Access to TreeBASE data
	Date: 	April 13, 2010 1:03:36 AM EDT
	To: 	help@nescent.org
	Cc: 	vgapeyev@nescent.org, hlapp@nescent.org
	

So indeed, the study_id value is missing from the phylotree table. This is quite weird because it's very sporadic -- only 10 trees 
(among 433 trees that are new to TreeBASE since we went live) have this problem. It will be hard to replicate the bug, wherever it is. 
	
Bill ran this on treebase-stage 2010-04-13, and after that he was able to go here: 
http://treebase-stage.nescent.org/treebase-web/phylows/study/TB2:S10349?x-access-code=d5039fb25843a2c8a19d7ec93cbe1541&format=html
... and view and download tree Tr7238 without a problem. 


---------

Jon will run this on production 2010-04-13 