-- V4__create_patients_table.sql

CREATE TABLE patients
(
    id            SERIAL PRIMARY KEY,
    first_name    VARCHAR(100)                                              NOT NULL,
    last_name     VARCHAR(100)                                              NOT NULL,
    date_of_birth DATE                                                      NOT NULL,
    gender        VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')) NOT NULL,
    phone         VARCHAR(20) UNIQUE                                        NOT NULL,
    email         VARCHAR(255) UNIQUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance optimization
CREATE INDEX idx_patients_phone ON patients (phone);
CREATE INDEX idx_patients_email ON patients (email);
CREATE INDEX idx_patients_name ON patients (last_name, first_name);


-- Insert initial patient data
INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email)
VALUES ('John', 'Doe', '1985-06-15', 'Male', '1234567890', 'john.doe@example.com'),
       ('Jane', 'Smith', '1992-03-22', 'Female', '9876543210', 'jane.smith@example.com');
