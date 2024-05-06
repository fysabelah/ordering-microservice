ALTER TABLE shipments
    ADD COLUMN state varchar(255),
    ADD COLUMN city varchar(255);

UPDATE shipments
SET state = ' ', city = ' '
WHERE state IS NULL AND city IS NULL;

ALTER TABLE shipments
ALTER COLUMN state SET NOT NULL;

ALTER TABLE shipments
ALTER COLUMN city SET NOT NULL;

ALTER TABLE shipments
RENAME COLUMN block to neighborhood;