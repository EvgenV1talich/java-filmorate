package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/filmorate")
@Slf4j
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();

    private int generatedId = 0;

    public String home() {
        return "Filmorate";
    }

    @PostMapping("/add-film")
    public Film addFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistsException("Фильм с таким ID уже существует!");
        } else {
            System.out.println("Adding new film:\n" + film.toString());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PatchMapping("/update-film/{id}")
    public Optional<Film> updateFilm(@RequestBody Film film) {
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
    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistsException("Фильм с таким ID уже существует!");
        } else {
            System.out.println("Adding new film:\n" + film.toString());
            films.put(film.getId(), film);
            return film;
        }
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
