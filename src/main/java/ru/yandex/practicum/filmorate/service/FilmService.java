package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLikeToFilm(Long userId, Integer filmId) {
        if (!userStorage.containsId(userId) || !filmStorage.containsFilm(filmId)) {
            throw new FilmNotFoundException("Фильм или пользователь с таким ID не найден!");
        }
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
        /*return (List<Film>) films.stream()
                .sorted(Film.compareByLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());*/
        return films.stream()
                .sorted((a, b) -> b.getLikes() - a.getLikes())
                .limit(count)
                .collect(Collectors.toList());
    }

}
