CREATE OR REPLACE FUNCTION update_status_to_expired()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.start_datetime < NOW() THEN
        NEW.status := 'expired';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_status_expired
BEFORE INSERT OR UPDATE ON driver_query
FOR EACH ROW
EXECUTE FUNCTION update_status_to_expired();