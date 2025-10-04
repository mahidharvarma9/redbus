-- Add SEMI_SLEEPER and LUXURY to bus_type enum
ALTER TABLE buses DROP CONSTRAINT IF EXISTS buses_bus_type_check;
ALTER TABLE buses ADD CONSTRAINT buses_bus_type_check CHECK (bus_type IN ('AC', 'NON_AC', 'SLEEPER', 'SEATER', 'SEMI_SLEEPER', 'LUXURY'));

