package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }


    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе POST /films");
            throw new FilmValidationException("Ошибка валидации фильма, проверьте данные!");
        }
        filmStorage.createFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Optional<Film> putFilm(@RequestBody Optional<Film> film) {
        if (film.isEmpty()) {
            throw new FilmNotFoundException("Фильм не найден!");
        }
        if (!FilmValidator.validate(film.get())) {
            log.error("Ошибка валидации фильма при запросе PUT /films");
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные!");
        }
        try {
            filmStorage.updateFilm(film.get());
            return Optional.of(filmStorage.getFilm(film.get().getId()));
        } catch (FilmNotFoundException ex) {
            System.out.println(ex.getMessage());
            log.error("Ошибка обновления фильма при запросе PUT /films");
            System.out.println("Ошибка при обновлении данных фильма");
        }
        return Optional.empty();
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLikeToFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilmsList(@RequestParam(name = "count") Optional<Integer> count) {
        if (count.isEmpty()) {
            return filmService.getMostPopularFilms(10);
        } else if (count.get() > filmStorage.getFilms().size()) {
            throw new FilmNotFoundException("Некорректный запрос, такого кол-ва фильмов нет!");
        } else {
            return filmService.getMostPopularFilms(count.get());
        }
    }

}
