package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dal.filmdao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dal.userdao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    //TODO: переписать на userService
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryUserStorage userStorage) {
        this.filmService = filmService;
        this.userStorage = userStorage;
    }


    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе POST /films");
            throw new FilmValidationException("Ошибка валидации фильма, проверьте данные!");
        }
        filmService.createFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Optional<Film> putFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе PUT /users");
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные.");
        }
        if (!filmService.containsFilm(film.getId())) {
            throw new FilmNotFoundException("Такой фильм не найден!");
        } else {
            filmService.updateFilm(film);
            return Optional.ofNullable(filmService.getFilm(film.getId()));
        }
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Integer id, @PathVariable Long userId) {
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Integer id, @PathVariable Long userId) {
        if (!filmService.containsFilm(id)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден!");
        } else if (!userStorage.containsId(userId)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден!");
        } else {
            filmService.removeLikeToFilm(id, userId);
        }
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilmsList(@RequestParam(name = "count") Optional<Integer> count) {
        if (count.isEmpty()) {
            return filmService.getMostPopularFilms(10);
        } else if (count.get() > filmService.getFilmsCount()) {
            return filmService.getMostPopularFilms(filmService.getFilmsCount());
        } else {
            return filmService.getMostPopularFilms(count.get());
        }
    }

}
