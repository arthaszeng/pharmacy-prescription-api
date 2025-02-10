-- V2__create_pharmacy_table.sql

CREATE TABLE pharmacies
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial pharmacy data
INSERT INTO pharmacies (name)
VALUES ('Central Pharmacy'),
       ('Downtown Pharmacy');