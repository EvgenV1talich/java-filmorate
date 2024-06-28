DROP TABLE IF EXISTS MPA cascade;
DROP TABLE IF EXISTS films cascade;
DROP TABLE IF EXISTS films_genres cascade;
DROP TABLE IF EXISTS genres cascade;
DROP TABLE IF EXISTS films_users cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS users_friends cascade;

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(255),
	DESCRIPTION VARCHAR(300),
	RELEASE_DATE DATE,
	DURATION INTEGER,
	CONSTRAINT FILMS_PK PRIMARY KEY (ID)
);


CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	EMAIL VARCHAR NOT NULL,
	LOGIN VARCHAR NOT NULL,
	NAME VARCHAR,
	BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.MPA (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	RATE VARCHAR,
    films_id INTEGER NOT NULL,
	CONSTRAINT MPA_PK PRIMARY KEY (ID),
	CONSTRAINT MPA_FILMS_FK FOREIGN KEY (films_id) REFERENCES PUBLIC.FILMS(ID)
);


CREATE TABLE IF NOT EXISTS PUBLIC.GENRES (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR,
	CONSTRAINT GENRES_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS_USERS (
	FILM_ID INTEGER,
	USER_ID INTEGER,
	CONSTRAINT FILMS_USERS_FILMS_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILMS_USERS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS_GENRES (
	FILM_ID INTEGER,
	GENRE_ID INTEGER,
	CONSTRAINT FILMS_GENRES_FILMS_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILMS_GENRES_GENRES_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS_FRIENDS (
	REQUEST_USER_ID INTEGER,
	RESPONSE_USER_ID INTEGER,
	STATUS BOOLEAN,
	CONSTRAINT USERS_FRIENDS_USERS_FK FOREIGN KEY (REQUEST_USER_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT USERS_FRIENDS_USERS_FK_1 FOREIGN KEY (REQUEST_USER_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE IF EXISTS FILMS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE IF EXISTS USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE IF EXISTS MPA ALTER COLUMN ID RESTART WITH 1;

INSERT INTO FILMS (id, DESCRIPTION, release_date, duration) VALUES (1, 'xxxxx', '2022-03-12', 2);
INSERT INTO FILMS (id, DESCRIPTION, release_date, duration) VALUES (2, 'xxxxx', '2021-04-11', 1);
INSERT INTO FILMS (id, DESCRIPTION, release_date, duration) VALUES (3, 'xxxxx', '2023-08-22', 3);
INSERT INTO FILMS (id, DESCRIPTION, release_date, duration) VALUES (4, 'xxxxx', '2024-10-26', 1);
INSERT INTO FILMS (id, DESCRIPTION, release_date, duration) VALUES (5, 'xxxxx', '2020-11-14', 2);

INSERT INTO mpa (id, rate, films_id) VALUES (1, 'G', 1);
INSERT INTO mpa (id, rate, films_id) VALUES (2, 'PG', 2);
INSERT INTO mpa (id, rate, films_id) VALUES (3, 'PG-13', 3);
INSERT INTO mpa (id, rate, films_id) VALUES (4, 'R', 4);
INSERT INTO mpa (id, rate, films_id) VALUES (5, 'NC-17', 5);

INSERT INTO genres (id, name) VALUES (1, 'Комедия');
INSERT INTO genres (id, name) VALUES (2, 'Драма');
INSERT INTO genres (id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres (id, name) VALUES (4, 'Триллер');
INSERT INTO genres (id, name) VALUES (5, 'Документальный');
INSERT INTO genres (id, name) VALUES (6, 'Боевик');