# Tasks schema

# --- !Ups

INSERT INTO usertask (login) VALUES ('Anonymous');
INSERT INTO usertask (login) VALUES ('Edu');

# --- !Downs
DELETE FROM task;
DELETE FROM usertask;
