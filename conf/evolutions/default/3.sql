# Tasks schema

# --- !Ups

ALTER TABLE task ADD enddate date;
INSERT INTO task (usertask_fk, label, enddate) VALUES ('Edu', 'Tienes que comer', '2014-10-15');

# --- !Downs
ALTER TABLE task DROP enddate;
