CREATE TABLE person
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    ssn        BYTEA        NOT NULL
);