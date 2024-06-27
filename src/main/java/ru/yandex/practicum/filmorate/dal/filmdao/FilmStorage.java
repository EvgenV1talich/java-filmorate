package ru.yandex.practicum.filmorate.dal.filmdao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

public interface FilmStorage {
    Map<String, Object> createFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(Integer filmId);

    Film getFilm(Integer filmId);

    HashMap<Integer, Film> getFilms();

}
