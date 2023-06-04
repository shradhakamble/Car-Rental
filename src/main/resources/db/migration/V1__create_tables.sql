CREATE TABLE driver_infoset (
    id bigserial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    dob varchar(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    address JSONB,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);


CREATE TABLE driver_vehicle_infoset (
    id bigserial PRIMARY KEY,
    vehicle_number VARCHAR(255) NOT NULL,
    driver_id bigserial NOT NULL,
    registered_name varchar(20) NOT NULL,
    model varchar(20) NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE driver_onboarding_journey (
    id bigserial PRIMARY KEY,
    driver_id bigserial  NOT NULL,
    current_step VARCHAR(255) NOT NULL,
    current_step_status varchar NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE driver_onboarding_journey_step_status_history (
    id bigserial PRIMARY KEY,
    driver_id bigserial NOT NULL,
    step varchar NOT NULL,
    step_status varchar NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);


