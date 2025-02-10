CREATE TABLE prescription_drugs
(
    id              SERIAL PRIMARY KEY,
    prescription_id INT NOT NULL,
    drug_id         INT NOT NULL,
    dosage          INT NOT NULL CHECK (dosage > 0),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES prescriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (drug_id) REFERENCES drugs (id) ON DELETE CASCADE
);

-- Insert initial prescription-drug data
INSERT INTO prescription_drugs (prescription_id, drug_id, dosage)
VALUES (1, 1, 2),
       (1, 2, 3);