CREATE TABLE IF NOT EXISTS capability_technology (
    capability_technology_id BIGSERIAL PRIMARY KEY,
    capability_id            BIGINT NOT NULL,
    technology_id            BIGINT NOT NULL,
    CONSTRAINT fk_capability_technology_technology
        FOREIGN KEY (technology_id)
            REFERENCES technology (id)
);

