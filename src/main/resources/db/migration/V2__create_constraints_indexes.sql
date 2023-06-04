ALTER TABLE driver_vehicle_infoset
ADD CONSTRAINT fk_vehicles_driver_id
FOREIGN KEY (driver_id)
REFERENCES driver_infoset (id);


ALTER TABLE driver_onboarding_journey
ADD CONSTRAINT fk_journey_driver_id
FOREIGN KEY (driver_id)
REFERENCES driver_infoset (id);

ALTER TABLE driver_onboarding_journey_step_status_history
ADD CONSTRAINT fk_journey_status_driver_id
FOREIGN KEY (driver_id)
REFERENCES driver_infoset (id);

---


CREATE INDEX idx_driver_contact
ON driver_infoset (contact_number);

CREATE INDEX idx_driver_email
ON driver_infoset (email);

CREATE INDEX idx_driver_status
ON driver_infoset (status);


CREATE INDEX idx_vehicle_number
ON driver_vehicle_infoset (vehicle_number);

CREATE INDEX idx_vehicle_driver
ON driver_vehicle_infoset (driver_id);

CREATE INDEX idx_journey_driver
ON driver_onboarding_journey (driver_id);

CREATE INDEX idx_journey_status_driver
ON driver_onboarding_journey_step_status_history (driver_id);