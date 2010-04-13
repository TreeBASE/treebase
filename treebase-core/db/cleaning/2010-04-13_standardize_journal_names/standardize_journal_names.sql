
begin work;

UPDATE citation SET journal = 'Organisms Diversity & Evolution'
WHERE journal = 'Organisms, Diversity and Evolution'
OR journal = 'Organisms Diversity and Evolution';

-- should result in about 11 changes

commit;