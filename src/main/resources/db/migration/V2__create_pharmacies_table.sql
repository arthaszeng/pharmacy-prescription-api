-- V2__create_pharmacy_table.sql

CREATE TABLE pharmacies
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE, -- Unique constraint
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial pharmacy data
CREATE INDEX idx_pharmacies_name ON pharmacies (name);
CREATE INDEX idx_pharmacies_created_at ON pharmacies (created_at);


INSERT INTO pharmacies (name)
VALUES ('Central Pharmacy'),
       ('Downtown Pharmacy');
