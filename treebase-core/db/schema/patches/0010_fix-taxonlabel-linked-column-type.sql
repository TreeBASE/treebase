-- Fix TAXONLABEL.linked column type from smallint to boolean
-- The Java entity expects a boolean type, but the database has smallint
-- This patch converts the column to the correct type
-- Made idempotent: only runs if column is not already boolean

INSERT INTO versionhistory(patchnumber, patchlabel, patchdescription)
VALUES (10, 'fix-taxonlabel-linked-column-type',
        'Fix TAXONLABEL.linked column type from smallint to boolean');

-- Convert smallint to boolean using USING clause
-- Values 0 become FALSE, non-zero values become TRUE
-- Only apply if column is not already boolean
DO $$
BEGIN
    -- Check if the column type is not already boolean
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'public'
        AND table_name = 'taxonlabel' 
        AND column_name = 'linked' 
        AND data_type != 'boolean'
    ) THEN
        ALTER TABLE taxonlabel
            ALTER COLUMN linked TYPE boolean
            USING (linked <> 0);
    END IF;
END $$;
