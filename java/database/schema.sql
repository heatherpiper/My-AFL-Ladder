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

CREATE TABLE games (
    id INT PRIMARY KEY,
    round INT NOT NULL,
    year INT NOT NULL,
    hteam VARCHAR(255) NOT NULL,
    ateam VARCHAR(255) NOT NULL,
    hscore INT,
    ascore INT,
    winner VARCHAR(255),
    complete INT NOT NULL
);

CREATE TABLE watched_games (
    user_id INT,
    game_id INT,
    PRIMARY KEY (user_id, game_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
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
