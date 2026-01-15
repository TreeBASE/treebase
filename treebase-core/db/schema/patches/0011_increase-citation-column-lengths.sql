-- Increase column lengths for CITATION table to fix DataIntegrityViolationException
-- when inserting citations with long keywords or journal names
-- See GitHub issue: value too long for type character varying(255)
-- Made idempotent: only runs if column lengths are smaller than required

INSERT INTO versionhistory(patchnumber, patchlabel, patchdescription)
VALUES (11, 'increase-citation-column-lengths',
        'Increase keywords column from 255 to 1000 and journal column from 255 to 500 to prevent data truncation errors');

-- Increase keywords column length from 255 to 1000 if needed
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'citation' 
        AND column_name = 'keywords' 
        AND character_maximum_length < 1000
    ) THEN
        ALTER TABLE citation
            ALTER COLUMN keywords TYPE character varying(1000);
    END IF;
END $$;

-- Increase journal column length from 255 to 500 if needed
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'citation' 
        AND column_name = 'journal' 
        AND character_maximum_length < 500
    ) THEN
        ALTER TABLE citation
            ALTER COLUMN journal TYPE character varying(500);
    END IF;
END $$;
