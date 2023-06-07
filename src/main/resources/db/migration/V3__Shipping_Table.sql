CREATE TABLE device_shipping_infoset (
    id bigserial PRIMARY KEY,
    driver_id bigserial  NOT NULL,
    status VARCHAR(255) NOT NULL,
    current_location varchar NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);