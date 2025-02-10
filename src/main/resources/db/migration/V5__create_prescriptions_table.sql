CREATE TABLE prescriptions
(
    id          SERIAL PRIMARY KEY,
    patient_id  INT         NOT NULL,
    pharmacy_id INT         NOT NULL,
    status      VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE CASCADE
);

-- Insert initial prescription data
INSERT INTO prescriptions (patient_id, pharmacy_id, status)
VALUES (1, 1, 'PENDING');
