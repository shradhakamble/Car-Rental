ALTER TABLE device_shipping_infoset ADD CONSTRAINT shipping_driver_id UNIQUE (driver_id);

ALTER TABLE driver_onboarding_journey ADD CONSTRAINT journey_driver_id UNIQUE (driver_id);

ALTER TABLE driver_infoset ADD CONSTRAINT driver_email UNIQUE (email);