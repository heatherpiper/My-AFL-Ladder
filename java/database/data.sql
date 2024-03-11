BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');

INSERT INTO teams (team_id, name) VALUES (1, 'Adelaide');
INSERT INTO teams (team_id, name) VALUES (2, 'Brisbane Lions');
INSERT INTO teams (team_id, name) VALUES (3, 'Carlton');
INSERT INTO teams (team_id, name) VALUES (4, 'Collingwood');
INSERT INTO teams (team_id, name) VALUES (5, 'Essendon');
INSERT INTO teams (team_id, name) VALUES (6, 'Fremantle');
INSERT INTO teams (team_id, name) VALUES (7, 'Geelong');
INSERT INTO teams (team_id, name) VALUES (8, 'Gold Coast');
INSERT INTO teams (team_id, name) VALUES (9, 'Greater Western Sydney');
INSERT INTO teams (team_id, name) VALUES (10, 'Hawthorn');
INSERT INTO teams (team_id, name) VALUES (11, 'Melbourne');
INSERT INTO teams (team_id, name) VALUES (12, 'North Melbourne');
INSERT INTO teams (team_id, name) VALUES (13, 'Port Adelaide');
INSERT INTO teams (team_id, name) VALUES (14, 'Richmond');
INSERT INTO teams (team_id, name) VALUES (15, 'St Kilda');
INSERT INTO teams (team_id, name) VALUES (16, 'Sydney');
INSERT INTO teams (team_id, name) VALUES (17, 'West Coast');
INSERT INTO teams (team_id, name) VALUES (18, 'Western Bulldogs');

COMMIT TRANSACTION;
