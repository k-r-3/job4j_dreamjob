CREATE TABLE IF NOT EXISTS post
(
    id      SERIAL PRIMARY KEY,
    name    text,
    descr   text,
    created text
);

CREATE TABLE IF NOT EXISTS city
(
    id   SERIAL PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS candidate
(
    id    SERIAL PRIMARY KEY,
    name  text,
    photo text,
    city_id int REFERENCES city(id)
);

CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    name text,
    email text UNIQUE,
    pass text
);

INSERT INTO users(name, email, pass) VALUES ('Admin', 'root@local', 'root');

INSERT INTO city(name) VALUES('Москва');
INSERT INTO city(name) VALUES('Санкт-Петербург');
INSERT INTO city(name) VALUES('Омск');
INSERT INTO city(name) VALUES('Новосибирск');