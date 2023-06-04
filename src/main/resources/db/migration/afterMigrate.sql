CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    table_name text;
BEGIN
    FOR table_name IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE format('CREATE TRIGGER trigger_update_updated_at_%s
                        BEFORE UPDATE ON %s
                        FOR EACH ROW
                        EXECUTE FUNCTION update_updated_at()',
                        table_name, table_name);
    END LOOP;
END $$;
