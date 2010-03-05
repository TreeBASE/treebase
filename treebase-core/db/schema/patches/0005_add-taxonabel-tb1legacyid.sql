insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (5, 'add-taxonlabel-tb1legacyid', 
       'Add tb1legacyid field to the taxonlabel table'); 

alter table taxonlabel add column tb1legacyid varchar(20); 


       