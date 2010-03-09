Various scripts used to clean the data in the database.  The primary purpose is to have a record of what has been done with the data in the NESCEent-run instances.  (That is, scripts appearing here may not be useful for any other TB instance; That's unlike the scripts in db/schema.)

Guidelines for adding code into db/tbcleanup: 

 - Each task should be in its own directory. 

 - While the task is still under development, its directory can be named with an arbitrary descriptive name. 

 - As soon as the task has been applied to the production DB, it should be renamed yyyy-mm-dd_descriptive_label. 

 - Each task directory should have README.txt describing its purpose, how to run it, and any notes about the outcome of its run on the production DB. 



