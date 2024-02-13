BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS user_team_rankings;

CREATE TABLE users (
	user_id SERIAL,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE teams (
	team_id INT PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE matches (
    match_id SERIAL PRIMARY KEY,
    team1_id INT NOT NULL,
    team2_id INT NOT NULL,
	round INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
	team1_points_scored INTEGER,
	team2_points_scored INTEGER,
	winner_id INT,
	FOREIGN KEY (team1_id) REFERENCES teams(team_id),
	FOREIGN KEY (team2_id) REFERENCES teams(team_id),
	FOREIGN KEY (winner_id) REFERENCES teams(team_id)
);

CREATE TABLE watched_matches (
    watched_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    match_id INT NOT NULL UNIQUE,
    watched BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (match_id) REFERENCES matches(match_id)
);

CREATE TABLE ladder2023_data (
    id SERIAL PRIMARY KEY,
    round INT,
    team VARCHAR(50),
    games_played INT,
    total_points INT,
    score_differential DOUBLE PRECISION
);


COMMIT TRANSACTION;
