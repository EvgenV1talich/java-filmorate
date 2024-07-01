package ru.yandex.practicum.filmorate.dal.filmDao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer filmId);

    Film getFilm(Integer filmId);

    List<Film> getFilms();

    List<Film> getTopFilms(Long count);

    Integer getFilmsCount();

}
