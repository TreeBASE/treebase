-- Increase column lengths for CITATION table to fix DataIntegrityViolationException
-- when inserting citations with long keywords or journal names
-- See GitHub issue: value too long for type character varying(255)

INSERT INTO versionhistory(patchnumber, patchlabel, patchdescription)
VALUES (11, 'increase-citation-column-lengths',
        'Increase keywords column from 255 to 1000 and journal column from 255 to 500 to prevent data truncation errors');

-- Increase keywords column length from 255 to 1000
ALTER TABLE citation
    ALTER COLUMN keywords TYPE character varying(1000);

-- Increase journal column length from 255 to 500
ALTER TABLE citation
    ALTER COLUMN journal TYPE character varying(500);
