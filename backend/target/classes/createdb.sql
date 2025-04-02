DROP TABLE IF EXISTS driver_query;
CREATE TABLE driver_query (
    id SERIAL PRIMARY KEY,
    add_datetime TIMESTAMP,
    start_datetime TIMESTAMP,
    start_location TEXT,
    destination TEXT,
    driver_experience INTEGER,
    car_type TEXT,
    max_car_mileage INTEGER,
    is_satisfied BOOLEAN
);

DROP TABLE IF EXISTS dispatcher;
CREATE TABLE dispatcher (
    id UUID PRIMARY KEY NOT NULL,
    name TEXT
);

DROP TABLE IF EXISTS car;
CREATE TABLE car (
    vin TEXT PRIMARY KEY NOT NULL,
    brand TEXT,
    year_of_manufacture INTEGER,
    type TEXT,
    fuel_type TEXT,
    mileage INTEGER,
    number TEXT,
    is_serviceable BOOLEAN
);
DROP TABLE IF EXISTS driver;
CREATE TABLE driver (
    id UUID PRIMARY KEY NOT NULL,
    car_vin TEXT,
    name TEXT,
    age INTEGER,
    experience INTEGER, -- in years
    driver_licence_type TEXT,
    additional_information TEXT,
    FOREIGN KEY (car_vin) REFERENCES car(vin)
);
DROP TABLE IF EXISTS trip;
CREATE TABLE trip (
    id SERIAL PRIMARY KEY,
    dispatcher_id UUID,
    driver_id UUID,
    start_location TEXT,
    destination TEXT,
    start_datetime TIMESTAMP,
    end_datetime TIMESTAMP,
    end_car_serviceability BOOLEAN,
    mileage INTEGER,
    FOREIGN KEY (driver_id) REFERENCES driver(id),
    FOREIGN KEY (dispatcher_id) REFERENCES dispatcher(id)
);

