-- V3__create_pharmacy_drug_allocation_table.sql

CREATE TABLE pharmacy_drug_allocations
(
    id              SERIAL PRIMARY KEY,
    pharmacy_id     INT NOT NULL,
    drug_id         INT NOT NULL,
    allocated_stock INT NOT NULL CHECK (allocated_stock >= 0),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign keys with cascade delete
    CONSTRAINT fk_pharmacy_drug_allocations_pharmacy FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE CASCADE,
    CONSTRAINT fk_pharmacy_drug_allocations_drug FOREIGN KEY (drug_id) REFERENCES drugs (id) ON DELETE CASCADE,

    -- Ensure unique allocation per pharmacy & drug
    CONSTRAINT uq_pharmacy_drug_allocations UNIQUE (pharmacy_id, drug_id)
);

-- Indexes for performance optimization
CREATE INDEX idx_pharmacy_drug_allocations_pharmacy ON pharmacy_drug_allocations (pharmacy_id);
CREATE INDEX idx_pharmacy_drug_allocations_drug ON pharmacy_drug_allocations (drug_id);
CREATE INDEX idx_pharmacy_drug_allocations_stock ON pharmacy_drug_allocations (allocated_stock);

-- Insert initial pharmacy-drug allocations
INSERT INTO pharmacy_drug_allocations (pharmacy_id, drug_id, allocated_stock)
VALUES (1, 1, 10),
       (1, 2, 20),
       (2, 1, 30);