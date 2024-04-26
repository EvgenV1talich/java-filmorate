package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

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

    public void addLikeToFilm(Integer filmId, Integer userId) {
        if (!userStorage.containsId(userId) || !filmStorage.containsFilm(filmId)) {
            throw new FilmNotFoundException("Фильм или пользователь с таким ID не найден!");
        }
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLikeToFilm(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        film.removeLike(user.getId());
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> films = (List<Film>) filmStorage.getFilms();
        return (List<Film>) films.stream()
                .sorted(Film.compareByLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}
