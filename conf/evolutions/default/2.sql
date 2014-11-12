# Tasks schema

# --- !Ups

CREATE TABLE usertask(
	login VARCHAR(255) NOT NULL,
	PRIMARY KEY (login)
);

ALTER TABLE task ADD usertask_fk VARCHAR(255);

INSERT INTO usertask (login) VALUES ('Anonymous');
INSERT INTO usertask (login) VALUES ('Edu');

# --- !Downs

ALTER TABLE task DROP usertask_fk; 
DELETE FROM usertask;
DROP TABLE usertask;
