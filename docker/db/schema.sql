CREATE TABLE person
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    ssn        BYTEA        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    date_of_birth DATE
);

CREATE TABLE address (
    address_id UUID PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code CHAR(5) NOT NULL CHECK (zip_code ~ '^[0-9]{5}$')
);

CREATE TABLE person_address (
    person_id UUID NOT NULL,
    address_id UUID NOT NULL,
    PRIMARY KEY (person_id, address_id),
    FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE CASCADE
);