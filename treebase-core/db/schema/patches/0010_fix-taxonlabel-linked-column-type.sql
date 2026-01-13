-- Fix TAXONLABEL.linked column type from smallint to boolean
-- The Java entity expects a boolean type, but the database has smallint
-- This patch converts the column to the correct type

INSERT INTO versionhistory(patchnumber, patchlabel, patchdescription)
VALUES (10, 'fix-taxonlabel-linked-column-type',
        'Fix TAXONLABEL.linked column type from smallint to boolean');

-- Convert smallint to boolean using USING clause
-- Values 0 become FALSE, non-zero values become TRUE
ALTER TABLE taxonlabel
    ALTER COLUMN linked TYPE boolean
    USING (linked <> 0);
