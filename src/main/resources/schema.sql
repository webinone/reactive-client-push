DROP TABLE IF EXISTS worker_location;
CREATE TABLE worker_location (id SERIAL PRIMARY KEY, longitude VARCHAR(255), altitude VARCHAR(255), area VARCHAR(4096));