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