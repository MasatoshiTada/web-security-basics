DROP TABLE IF EXISTS users;

CREATE TABLE users(
    email VARCHAR(64) PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    password VARCHAR(16) NOT NULL
);
