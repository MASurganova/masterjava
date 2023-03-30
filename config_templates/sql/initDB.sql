DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS groups;
DROP SEQUENCE IF EXISTS project_seq;
DROP TYPE IF EXISTS group_flag;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE project_seq START 100000;

CREATE TABLE cities
(
    id   TEXT PRIMARY KEY NOT NULL,
    name TEXT             NOT NULL
);

CREATE TABLE users
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    full_name TEXT      NOT NULL,
    email     TEXT      NOT NULL,
    flag      user_flag NOT NULL,
    city_id   TEXT,
    FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
    name        TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TYPE group_flag AS ENUM ('FINISHED', 'CURRENT', 'CREATE');

CREATE TABLE groups
(
    name       TEXT PRIMARY KEY,
    flag       group_flag NOT NULL,
    project_id INTEGER    NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

TRUNCATE cities CASCADE ;

