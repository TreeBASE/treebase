To apply, run fix_S2164.sql

--------

Hi all,

I'm trying to fix issue 2992022, which was hypothesised to be a
character encoding problem. This is not the case, it has nothing to do
with UTF-8 handling. It's a database inconsistency:

* in the person table, there's both an entry for "Balázs Tímea",
person_id=3971 and for "Tímea Balázs", person_id=5609

* in the latter record, first and last name are in the correct order,
but the author's email address is missing, and the author order index
is 5, on a three-author paper. In the former record, the first and
last name are switched around, but everything else is in the correct
order. It looks like the nullpointer exception is thrown because there
is no author 4 (see:
http://www.mycologia.org/cgi/content/abstract/101/2/247).

* here's my suggested fix: correct the name order in record 3971,
purge record 5609 from person and from citation_author, i.e.:

update person set firstname='Tímea', lastname='Balázs' where person_id=3971;
delete from citation_author where authors_person_id=5609;
delete from person where person_id=5609;

On the test instance this fixes the exception, the citation is
serialised correctly. Can someone fix this on the production instance?
I re-assigned this bug to Bill, but whoever has access to the prod
server can run those SQL snippets and everything should be OK.

Cheers,

Rutger