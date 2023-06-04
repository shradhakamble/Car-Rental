CREATE TABLE driver_profile (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    account_status VARCHAR(20) NOT NULL,
    vehicle_make VARCHAR(50) NOT NULL,
    vehicle_model VARCHAR(50) NOT NULL
);
