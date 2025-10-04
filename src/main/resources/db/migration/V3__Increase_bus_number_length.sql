-- Increase bus_number column length to accommodate longer bus numbers
ALTER TABLE buses ALTER COLUMN bus_number TYPE VARCHAR(50);

