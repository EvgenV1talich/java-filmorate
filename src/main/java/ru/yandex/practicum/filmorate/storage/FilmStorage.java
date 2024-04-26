package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    void createFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(Integer filmId);

    Film getFilm(Integer filmId);

}