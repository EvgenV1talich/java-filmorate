package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dal.filmdao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.userdao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.filmdao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dal.userdao.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;


    public void addLikeToFilm(Long userId, Integer filmId) {
        Film newFilm = filmStorage.getFilm(filmId);
        newFilm.addLike(userId);
        filmStorage.updateFilm(newFilm);
    }

    public void removeLikeToFilm(Integer filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.removeLike(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        Collection<Film> films = filmStorage.getFilms().values();
        return films.stream()
                .sorted((a, b) -> b.getLikes() - a.getLikes())
                .limit(count)
                .collect(Collectors.toList());
    }
    public Collection<Film> getFilms() {
        return (filmStorage.getFilms().values());
    }
    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        return film;
    }
    public boolean containsFilm(Integer id) {
        return (filmStorage.contains(id));
    }
    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }
    public Integer getFilmsCount() {
        return filmStorage.getCount();
    }

}
