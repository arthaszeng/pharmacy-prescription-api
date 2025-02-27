-- V1__initial_schema.sql
CREATE TABLE drugs
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255)        NOT NULL,
    manufacturer VARCHAR(255)        NOT NULL,
    batch_number VARCHAR(100) UNIQUE NOT NULL,
    expiry_date  DATE                NOT NULL,
    stock        INT                 NOT NULL CHECK (stock >= 0),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted      BOOLEAN   DEFAULT FALSE
);

CREATE INDEX idx_drugs_batch_number ON drugs (batch_number);
CREATE INDEX idx_drugs_expiry_date ON drugs (expiry_date);
CREATE INDEX idx_drugs_stock ON drugs (stock);
CREATE INDEX idx_drugs_manufacturer ON drugs (manufacturer);


INSERT INTO drugs (name, manufacturer, batch_number, expiry_date, stock)
VALUES ('Paracetamol', 'GSK', 'GSK001', '2025-12-31', 100),
       ('Ibuprofen', 'Bayer', 'BAY001', '2025-12-31', 50);