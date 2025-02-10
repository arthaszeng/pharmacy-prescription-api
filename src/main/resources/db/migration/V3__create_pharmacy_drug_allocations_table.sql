-- V3__create_pharmacy_drug_allocation_table.sql

CREATE TABLE pharmacy_drug_allocations
(
    id              SERIAL PRIMARY KEY,
    pharmacy_id     INT NOT NULL,
    drug_id         INT NOT NULL,
    allocated_stock INT NOT NULL CHECK (allocated_stock >= 0),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE CASCADE,
    FOREIGN KEY (drug_id) REFERENCES drugs (id) ON DELETE CASCADE
);

-- Insert initial pharmacy-drug allocations
-- Assuming pharmacy with id 1 and drug with id 1 already exist in the database
INSERT INTO pharmacy_drug_allocations (pharmacy_id, drug_id, allocated_stock)
VALUES (1, 1, 50),
       (2, 1, 30),
       (1, 2, 20);