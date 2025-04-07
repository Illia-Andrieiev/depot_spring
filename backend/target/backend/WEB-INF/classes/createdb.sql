DROP TABLE IF EXISTS profile;
CREATE TABLE profile (
    email TEXT PRIMARY KEY NOT NULL,
    address TEXT,
    phone TEXT,
    birth_day INTEGER,
    birth_month INTEGER,
    birth_year INTEGER,
    first_name TEXT,
    last_name TEXT
);
DROP TABLE IF EXISTS driver_query;
CREATE TABLE driver_query (
    id SERIAL PRIMARY KEY,
    add_datetime TIMESTAMP,
    start_datetime TIMESTAMP,
    email TEXT,
    start_location TEXT,
    destination TEXT,
    driver_experience INTEGER,
    car_type TEXT,
    max_car_mileage INTEGER,
    status TEXT
);

DROP TABLE IF EXISTS dispatcher;
CREATE TABLE dispatcher (
    id UUID PRIMARY KEY NOT NULL,
    email TEXT UNIQUE NOT NULL,
	FOREIGN KEY (email) REFERENCES profile(email)
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
    id SERIAL PRIMARY KEY NOT NULL,
    email TEXT UNIQUE NOT NULL,
    car_vin TEXT,
    age INTEGER,
    experience INTEGER, -- in years
    driver_licence_type TEXT,
    additional_information TEXT,
    FOREIGN KEY (car_vin) REFERENCES car(vin),
	FOREIGN KEY (email) REFERENCES profile(email)
);

DROP TABLE IF EXISTS trip;
CREATE TABLE trip (
    id SERIAL PRIMARY KEY,
    query_id INTEGER,
    dispatcher_id UUID,
    driver_id INTEGER,
    end_datetime TIMESTAMP,
    end_car_serviceability BOOLEAN,
    mileage INTEGER,
    FOREIGN KEY (driver_id) REFERENCES driver(id),
    FOREIGN KEY (dispatcher_id) REFERENCES dispatcher(id),
    FOREIGN KEY (quey_id) REFERENCES driver_query(id)
);
