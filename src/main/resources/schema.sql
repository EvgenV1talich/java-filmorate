DROP TABLE IF EXISTS MPA cascade;
DROP TABLE IF EXISTS films cascade;
DROP TABLE IF EXISTS films_genres cascade;
DROP TABLE IF EXISTS genres cascade;
DROP TABLE IF EXISTS films_users cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS users_friends cascade;

CREATE TABLE IF NOT EXISTS mpa
(
    id   INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR,
    CONSTRAINT MPA_PK PRIMARY KEY (ID)
);


CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    name         VARCHAR(255),
    description  VARCHAR(300),
    release_date DATE,
    duration     INTEGER,
    mpa_id       INTEGER NOT NULL REFERENCES mpa(id) ON DELETE CASCADE,
    CONSTRAINT films_pk PRIMARY KEY (ID)

);


CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER NOT NULL AUTO_INCREMENT,
    email    VARCHAR NOT NULL,
    login    VARCHAR NOT NULL,
    name     VARCHAR,
    birthday DATE,
    CONSTRAINT users_pk PRIMARY KEY (ID)
);



CREATE TABLE IF NOT EXISTS genres
(
    id   INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR,
    CONSTRAINT genres_pk PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS films_users
(
    film_id INTEGER,
    user_id INTEGER,
    CONSTRAINT films_users_films_fk FOREIGN KEY (film_id) REFERENCES films (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT films_users_users_fk FOREIGN KEY (user_id) REFERENCES users (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS films_genres
(
    film_id  INTEGER,
    genre_id INTEGER,
    CONSTRAINT films_genres_films_fk FOREIGN KEY (film_id) REFERENCES films (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT films_genres_genres_fk FOREIGN KEY (genre_id) REFERENCES genres (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS users_friends
(
    request_user_id  INTEGER,
    response_user_id INTEGER,
    status           BOOLEAN,
    CONSTRAINT users_friends_users_fk FOREIGN KEY (request_user_id) REFERENCES users (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT users_friends_users_fk_1 FOREIGN KEY (request_user_id) REFERENCES users (ID) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO mpa (id, name)
VALUES (1, 'G');
INSERT INTO mpa (id, name)
VALUES (2, 'PG');
INSERT INTO mpa (id, name)
VALUES (3, 'PG-13');
INSERT INTO mpa (id, name)
VALUES (4, 'R');
INSERT INTO mpa (id, name)
VALUES (5, 'NC-17');




INSERT INTO genres (id, name)
VALUES (1, 'Комедия');
INSERT INTO genres (id, name)
VALUES (2, 'Драма');
INSERT INTO genres (id, name)
VALUES (3, 'Мультфильм');
INSERT INTO genres (id, name)
VALUES (4, 'Триллер');
INSERT INTO genres (id, name)
VALUES (5, 'Документальный');
INSERT INTO genres (id, name)
VALUES (6, 'Боевик');


