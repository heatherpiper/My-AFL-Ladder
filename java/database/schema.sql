BEGIN TRANSACTION;

DROP TABLE IF EXISTS watched_games;
DROP TABLE IF EXISTS user_ladder;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS teams;

CREATE TABLE users (
	user_id SERIAL,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	last_login_date TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE games (
    id INT PRIMARY KEY,
    round INT NOT NULL,
    year INT NOT NULL,
    unixtime INT NOT NULL,
    hteam VARCHAR(255),
    ateam VARCHAR(255),
    hscore INT,
    ascore INT,
    winner VARCHAR(255),
    complete INT NOT NULL
);

CREATE TABLE watched_games (
    watched_game_id SERIAL PRIMARY KEY,
    user_id INT,
    game_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);

CREATE TABLE teams (
    team_id INT PRIMARY KEY UNIQUE,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE user_ladder (
    user_id INT,
    team_id INT,
    points INT,
    percentage DOUBLE PRECISION,
    position INT,
    team_name VARCHAR(255),
    wins INT,
    losses INT,
    draws INT,
    points_for INT,
    points_against INT,
    PRIMARY KEY (user_id, team_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (team_id) REFERENCES teams(team_id),
    FOREIGN KEY (team_name) REFERENCES teams(name)
);


COMMIT TRANSACTION;
