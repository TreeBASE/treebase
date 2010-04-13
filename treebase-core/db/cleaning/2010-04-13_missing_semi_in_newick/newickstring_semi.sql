

begin work;

UPDATE phylotree 
SET newickstring = newickstring || ';'
WHERE newickstring NOT LIKE '%;'
AND newickstring NOT LIKE '% '
AND newickstring IS NOT NULL;

-- results in 3 changes

commit;

