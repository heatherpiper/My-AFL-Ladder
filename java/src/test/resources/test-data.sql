BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');

INSERT INTO teams (team_id, name) VALUES (1, 'Team A');
INSERT INTO teams (team_id, name) VALUES (2, 'Team B');
INSERT INTO teams (team_id, name) VALUES (3, 'Team C');
INSERT INTO teams (team_id, name) VALUES (4, 'Team D');

INSERT INTO games (id, round, year, hteam, ateam, hscore, ascore, winner, complete) VALUES (1, 1, 2023, 'Team A', 'Team B', 100, 90, 'Team A', 100);
INSERT INTO games (id, round, year, hteam, ateam, hscore, ascore, winner, complete) VALUES (2, 1, 2023, 'Team C', 'Team D', 90, 100, 'Team D', 100);
INSERT INTO games (id, round, year, hteam, ateam, hscore, ascore, winner, complete) VALUES (3, 2, 2023, 'Team A', 'Team C', 100, 100, NULL, 100);

INSERT INTO watched_games (user_id, game_id) VALUES (1, 1);
INSERT INTO watched_games (user_id, game_id) VALUES (1, 2);
INSERT INTO watched_games (user_id, game_id) VALUES (2, 1);
INSERT INTO watched_games (user_id, game_id) VALUES (2, 3);

COMMIT TRANSACTION;
