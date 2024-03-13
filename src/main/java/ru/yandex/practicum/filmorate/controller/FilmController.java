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

    @PatchMapping("/update-film/{id}")
    public Optional<Film> updateFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные!");
        }
        try {
            films.replace(film.getId(), film);
            return Optional.of(film);
        } catch (FilmNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ошибка при обновлении данных фильма");
        }
        return Optional.empty();
    }

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(generateId());
        }
        //Validation
        if (!FilmValidator.validate(film)) {
            throw new FilmValidationException("Ошибка валидации, проверьте данные!");
        }
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistsException("Фильм с таким ID уже существует!");
        } else {
            System.out.println("Adding new film:\n" + film.toString());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PutMapping("/films")
    public Optional<Film> putFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные!");
        }
        try {
            films.replace(film.getId(), film);
            return Optional.of(film);
        } catch (FilmNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ошибка при обновлении данных фильма");
        }
        return Optional.empty();
    }

    private Integer generateId() {
        return generatedId++;
    }

    private void resetId() {
        generatedId = 0;
    }

    private Integer getLastGeneratedId() {
        return generatedId;
    }
}
