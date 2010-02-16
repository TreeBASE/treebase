insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (2, 'drop-geospot_id_sequence', 
       'Drop geospot_id_sequence'); 

drop sequence geospot_id_sequence; 

