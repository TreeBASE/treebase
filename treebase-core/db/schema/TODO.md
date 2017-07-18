Pending framework issues 
========================

* Would like a script for getting a current snapshot of the initial (dictionary) data, 
  to be used alongside with the schema snapshot command. 
  [A problem: simply designating some tables as "dictionaries" and others as "facts" would not work -- 
  at least some tables (Phylochar, DiscreteCharState) contains both "dictionary" and "fact" records.]
* Automation scripts for tasks, especially patch application, so that the sysadmin does not forget to wrap a transaction.  
* A better way for handling ownership and permissions in particular DB instances -- there is 
  too much manual work at the moment.  
* Less-intrusive permissions/ownership management.  Currently, ownerships and permissions are handled by pg_owner, pg_admin functions 
  that the sysadmin creates in DB after the initial installation.  Consequently, they get into the schema snapshops and have to be 
  removed by hand.

Pending schema patches: 
======================

* Maybe some indices around matrices (in addition to the indices around trees that are already there). 
