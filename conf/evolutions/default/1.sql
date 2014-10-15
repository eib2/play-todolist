#Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
	id integer NOT NULL DEFAULT nextval('task_id_seq'),
	label varchar(255)
);

CREATE OR REPLACE TABLE usertask(
	login varchar(255) NOT NULL,
	PRIMARY KEY (login)
);

ALTER TABLE task ADD usertask_fk varchar(255) DEFAULT 'Anonymous';
ALTER TABLE task ADD FOREIGN KEY (usertask_fk) REFERENCES usertask(login);

# --- !Downs

ALTER TABLE task DROP usertask_fk; 
DELETE FROM usertask;
DROP TABLE usertask;

DROP TABLE task;
DROP SEQUENCE task_id_seq;
