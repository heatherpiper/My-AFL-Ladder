BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');

INSERT INTO teams (name) VALUES ('Adelaide')
INSERT INTO teams (name) VALUES ('Brisbane Lions')
INSERT INTO teams (name) VALUES ('Carlton')
INSERT INTO teams (name) VALUES ('Collingwood')
INSERT INTO teams (name) VALUES ('Essendon')
INSERT INTO teams (name) VALUES ('Fremantle')
INSERT INTO teams (name) VALUES ('Geelong')
INSERT INTO teams (name) VALUES ('Gold Coast')
INSERT INTO teams (name) VALUES ('Greater Western Sydney')
INSERT INTO teams (name) VALUES ('Hawthorn')
INSERT INTO teams (name) VALUES ('Melbourne')
INSERT INTO teams (name) VALUES ('North Melbourne')
INSERT INTO teams (name) VALUES ('Port Adelaide')
INSERT INTO teams (name) VALUES ('Richmond')
INSERT INTO teams (name) VALUES ('St Kilda')
INSERT INTO teams (name) VALUES ('Sydney')
INSERT INTO teams (name) VALUES ('West Coast')
INSERT INTO teams (name) VALUES ('Western Bulldogs')

COMMIT TRANSACTION;
