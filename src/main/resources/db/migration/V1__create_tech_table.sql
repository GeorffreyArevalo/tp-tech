CREATE TABLE IF NOT EXISTS technology (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(90)  NOT NULL
);
