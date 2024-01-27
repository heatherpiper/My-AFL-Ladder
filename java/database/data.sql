BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');

INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Australian Eastern Standard Time', 'AEST', 10.0);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Australian Central Standard Time', 'ACST', 9.5);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Australian Western Standard Time', 'AWST', 8.0);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Eastern Standard Time', 'EST', -5.0);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Mountain Standard Time', 'MST', -6.0);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Central Standard Time', 'CST', -7.0);
INSERT INTO timezones (name,abbreviation,utc_offset) VALUES ('Pacific Standard Time', 'PST', -8.0);

INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Melbourne Cricket Ground','Melbourne','Victoria', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Optus Stadium','Perth','Western Australia', (SELECT id FROM timezones WHERE abbreviation = 'AWST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Adelaide Oval','Adelaide','South Australia', (SELECT id FROM timezones WHERE abbreviation = 'ACST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Marvel Stadium','Melbourne','Victoria',(SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Sydney Cricket Ground','Sydney','New South Wales', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('The Gabba','Brisbane','Queensland', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('GMHBA Stadium','Geelong','Victoria', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Heritage Bank Stadium','Gold Coast','Queensland', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Giants Stadium','Sydney','New South Wales', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('University of Tasmania Stadium','Launceston','Tasmania', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Blundstone Arena','Hobart','Tasmania', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('UNSW Canberra Oval','Canberra','ACT', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('TIO Stadium','Darwin','Northern Territory', (SELECT id FROM timezones WHERE abbreviation = 'ACST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('Mars Stadium','Ballarat','Victoria', (SELECT id FROM timezones WHERE abbreviation = 'AEST'));
INSERT INTO venues (stadium,city,state,abbreviation) VALUES ('TIO Traeger Park','Alice Springs','Northern Territory', (SELECT id FROM timezones WHERE abbreviation = 'ACST'));

INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Sydney Swans', 'Melbourne', 0, 2024-03-07 8:30:00, (SELECT id FROM venues WHERE stadium = 'Sydney Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Brisbane Lions', 'Carlton', 0, 2024-03-08 8:40:00, (SELECT id FROM venues WHERE stadium = 'The Gabba'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Gold Coast Suns', 'Richmond', 0, 2024-03-09 5:20:00, (SELECT id FROM venues WHERE stadium = 'Heritage Bank Stadium'))
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('GWS Giants', 'Collingwood', 0, 2024-03-09 8:30:00, (SELECT id FROM venues WHERE stadium ='Giants Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Carlton', 'Richmond', 1, 2024-03-14 8:30:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Collingwood', 'Sydney Swans', 1, 2024-03-15 8:40:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Essendon', 'Hawthorn', 1, 2024-03-16 2:45:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('GWS Giants', 'North Melbourne', 1, 2024-03-16 5:35:00, (SELECT id FROM venues WHERE stadium ='Giants Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Geelong Cats', 'St Kilda', 1, 2024-03-16 8:30:00, (SELECT id FROM venues WHERE stadium ='GMHBA Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Gold Coast Suns', 'Adelaide Crows', 1, 2024-03-16 9:10:00, (SELECT id FROM venues WHERE stadium ='Heritage Bank Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Melbourne', 'Western Bulldogs', 1, 2024-03-17 2:00:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Port Adelaide', 'West Coast Eagles', 1, 2024-03-17 5:00:00, (SELECT id FROM venues WHERE stadium ='Adelaide Oval'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Fremantle', 'Brisbane Lions', 1, 2024-03-17 7:50:00, (SELECT id FROM venues WHERE stadium ='Optus Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('St Kilda', 'Collingwood', 2, 2024-03-21 8:30:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Adelaide Crows', 'Geelong Cats', 2, 2024-03-22 8:40:00, (SELECT id FROM venues WHERE stadium ='Adelaide Oval'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('North Melbourne', 'Fremantle', 2, 2024-03-23 2:45:00, (SELECT id FROM venues WHERE stadium ='Marvel Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Hawthorn', 'Melbourne', 2, 2024-03-23 5:35:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Sydney', 'Essendon', 2, 2024-03-23 8:30:00, (SELECT id FROM venues WHERE stadium ='Sydney Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Western Bulldogs', 'Gold Coast Suns', 2, 2024-03-24 2:00:00, (SELECT id FROM venues WHERE stadium ='Mars Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Richmond', 'Port Adelaide', 2, 2024-03-24 5:00:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('West Coast Eagles', 'GWS Giants', 2, 2024-03-24 7:50:00, (SELECT id FROM venues WHERE stadium ='Optus Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Brisbane Lions', 'Collingwood', 3, 2024-03-28 8:30:00, (SELECT id FROM venues WHERE stadium ='The Gabba'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('North Melbourne', 'Carlton', 3, 2024-03-29 5:20:00, (SELECT id FROM venues WHERE stadium ='Marvel Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Fremantle', 'Adelaide Crows', 3, 2024-03-29 8:20:00, (SELECT id FROM venues WHERE stadium ='Optus Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Essendon', 'St Kilda', 3, 2024-03-30 5:20:00, (SELECT id FROM venues WHERE stadium ='Marvel Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Port Adelaide', 'Melbourne', 3, 2024-03-30 8:30:00, (SELECT id FROM venues WHERE stadium ='Adelaide Oval'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Western Bulldogs', 'West Coast Eagles', 3, 2024-03-31 3:00:00, (SELECT id FROM venues WHERE stadium ='Marvel Stadium'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Richmond', 'Sydney Swans', 3, 2024-03-31 6:00:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));
INSERT INTO matches (team1, team2, round, start_time, venue_id) VALUES ('Hawthorn', 'Geelong Cats', 3, 2024-04-01 5:20:00, (SELECT id FROM venues WHERE stadium ='Melbourne Cricket Ground'));

COMMIT TRANSACTION;
