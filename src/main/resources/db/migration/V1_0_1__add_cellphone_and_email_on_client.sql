ALTER TABLE clients
    ADD COLUMN email varchar(255),
    ADD COLUMN cellphone varchar(255);

UPDATE clients
SET email = ' ', cellphone = ' '
WHERE email IS NULL AND cellphone IS NULL;

ALTER TABLE clients
ALTER COLUMN email SET NOT NULL;

ALTER TABLE clients
ALTER COLUMN cellphone SET NOT NULL;