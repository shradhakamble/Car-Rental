-- Create the trigger function
CREATE OR REPLACE FUNCTION update_updated_at_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_TABLE_NAME <> 'flyway_schema_history' THEN
        NEW.updated_at = now();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop the existing trigger if it exists
DROP TRIGGER IF EXISTS update_updated_at_trigger ON public.driver_infoset;
DROP TRIGGER IF EXISTS update_updated_at_trigger ON public.driver_onboarding_journey;
DROP TRIGGER IF EXISTS update_updated_at_trigger ON public.driver_onboarding_journey_step_status_history; -- Modify with your table names

-- Create the trigger
CREATE TRIGGER update_updated_at_trigger
BEFORE UPDATE ON public.driver_infoset
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_function();

CREATE TRIGGER update_updated_at_trigger
BEFORE UPDATE ON public.driver_onboarding_journey_step_status_history
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_function();

CREATE TRIGGER update_updated_at_trigger
BEFORE UPDATE ON public.driver_onboarding_journey
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_function();
