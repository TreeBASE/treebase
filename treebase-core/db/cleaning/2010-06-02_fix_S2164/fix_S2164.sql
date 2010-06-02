-- beware that the inspection tool to apply these statements needs to be 
-- utf-8 aware

BEGIN WORK;

UPDATE person SET firstname='Tímea', lastname='Balázs' WHERE person_id=3971;

DELETE FROM citation_author WHERE authors_person_id=5609;

DELETE FROM person WHERE person_id=5609;

COMMIT;

