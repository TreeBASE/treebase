-- Patch 0012: Increase taxonlabel column size to handle long taxon names with embedded sequences
-- The varchar(255) limit was too restrictive for some Nexus files that contain sequence data in taxon labels

-- First drop the index on taxonlabel (it's on a varchar field)
DROP INDEX IF EXISTS taxonlabel_idx_taxonlabel;

-- Alter the column to text type which has no length limit
ALTER TABLE taxonlabel ALTER COLUMN taxonlabel TYPE text;

-- Recreate the index
CREATE INDEX taxonlabel_idx_taxonlabel ON taxonlabel(taxonlabel);

-- Record the patch
INSERT INTO versionhistory (patchnumber, patchlabel, patchdescription)
VALUES (12, '0012_increase-taxonlabel-column-size', 'Increase taxonlabel column size to text')
ON CONFLICT (patchnumber) DO NOTHING;
