ALTER TABLE capability_technology
    DROP CONSTRAINT IF EXISTS fk_capability_technology_technology;

ALTER TABLE capability_technology
    ADD CONSTRAINT fk_capability_technology_technology
        FOREIGN KEY (technology_id)
            REFERENCES technology (id)
            ON DELETE CASCADE;

