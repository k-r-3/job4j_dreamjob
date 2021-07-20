CREATE TABLE IF NOT EXISTS post(
    id   SERIAL PRIMARY KEY,
    name text,
    descr text,
    created text
);
CREATE TABLE IF NOT EXISTS candidate(
    id SERIAL PRIMARY KEY,
    name text,
    photo text
);