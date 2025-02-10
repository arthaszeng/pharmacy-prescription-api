-- V5__create_prescriptions_table.sql
CREATE TABLE prescriptions
(
    id          SERIAL PRIMARY KEY,
    patient_id  INT NOT NULL,
    pharmacy_id INT NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'FULFILLED', 'CANCELLED')),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE CASCADE
);

-- Indexes for performance optimization
CREATE INDEX idx_prescriptions_patient ON prescriptions (patient_id);
CREATE INDEX idx_prescriptions_pharmacy ON prescriptions (pharmacy_id);
CREATE INDEX idx_prescriptions_status ON prescriptions (status);


-- Insert initial prescription data
INSERT INTO prescriptions (patient_id, pharmacy_id, status)
VALUES (1, 1, 'PENDING');