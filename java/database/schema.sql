BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS venues;
DROP TABLE IF EXISTS timezones;

CREATE TABLE users (
	user_id SERIAL,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE timezones (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100),
	abbreviation VARCHAR(4),
	utc_offset DECIMAL (4, 2) NOT NULL
);

CREATE TABLE venues (
	id SERIAL PRIMARY KEY,
    stadium VARCHAR (50) NOT NULL,
    city VARCHAR(50),
    state VARCHAR(50),
    timezone_id INT,
	FOREIGN KEY (timezone_id) REFERENCES timezones(id)
);

CREATE TABLE matches (
    match_id SERIAL PRIMARY KEY,
    team1 VARCHAR(50) NOT NULL,
    team2 VARCHAR(50) NOT NULL,
	round INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
	venue_id INTEGER,
	FOREIGN KEY (venue_id) REFERENCES venues(id)
);

COMMIT TRANSACTION;
