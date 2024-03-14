package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();

    private int generatedId = 1;

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(generateId());
        }
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе POST /films");
            throw new FilmValidationException("Ошибка валидации, проверьте данные!");
        }
        if (films.containsKey(film.getId())) {
            log.error("Ошибка добавления фильма при запросе POST /films");
            throw new FilmAlreadyExistsException("Фильм с таким ID уже существует!");
        } else {
            System.out.println(film.getDuration());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PutMapping("/films")
    public Optional<Film> putFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе PUT /films");
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные!");
        }
        try {
            films.replace(film.getId(), film);
            return Optional.of(films.get(film.getId()));
        } catch (FilmNotFoundException ex) {
            System.out.println(ex.getMessage());
            log.error("Ошибка обновления фильма при запросе PUT /films");
            System.out.println("Ошибка при обновлении данных фильма");
        }
        return Optional.empty();
    }

    private Integer generateId() {
        return generatedId++;
    }

}
