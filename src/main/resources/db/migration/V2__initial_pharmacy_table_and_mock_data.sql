-- V2__create_pharmacy_table.sql

CREATE TABLE Pharmacy
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial pharmacy data
INSERT INTO Pharmacy (name)
VALUES ('Central Pharmacy'),
       ('Downtown Pharmacy');