BEGIN transaction; 

update study as s
  set user_id = u.user_id
from "user" as u  
where s.user_id is null
  and u.username = 'tb1import'; 

update submission as s
  set user_id = u.user_id
from "user" as u  
where s.user_id is null
  and u.username = 'tb1import'; 

COMMIT; 
