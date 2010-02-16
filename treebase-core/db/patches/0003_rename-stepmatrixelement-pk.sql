insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (3, 'rename-stepmatrixelement-pk', 
       'Correct PK name in stepmatrixelement and make it use its own newly created sequence.'); 

create sequence stepmatrixelement_id_sequence; 

alter table stepmatrixelement
    rename column discretecharstate_id to stepmatrixelement_id; 

alter table stepmatrixelement
  alter column stepmatrixelement_id  
    set default  nextval('stepmatrixelement_id_sequence'); 


