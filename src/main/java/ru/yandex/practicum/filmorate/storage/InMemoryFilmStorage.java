package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer lastGeneratedId = 1;
    private final Set<Integer> filmIds = new TreeSet<>();

    @Override
    public void createFilm(Film film) {
        if (film.getId() == null) {
            film.setId(generateId());
        }
        try {
            StringBuilder builder = new StringBuilder();
            log.info(builder
                    .append("Trying to create film (id = ")
                    .append(film.getId())
                    .append(")")
                    .toString());
            films.put(film.getId(), film);
            builder = new StringBuilder();
            log.info(builder
                    .append("Film created successfully (id = ")
                    .append(film.getId())
                    .append(")")
                    .toString());

        } catch (FilmAlreadyExistsException ex) {
            StringBuilder builder = new StringBuilder();
            log.error(builder
                    .append("Error when creating new film (filmId = ")
                    .append(film.getId())
                    .append(")")
                    .toString());
            throw new FilmAlreadyExistsException("Ошибка добавления фильма");
        }
    }

    @Override
    public void updateFilm(Film film) {
        try {
            StringBuilder builder = new StringBuilder();
            log.info(builder
                    .append("Trying to update film {id = ")
                    .append(film.getId())
                    .append(")")
                    .toString());
            films.replace(film.getId(), film);
        } catch (FilmNotFoundException ex) {
            log.error("Ошибка обновления фильма");
            throw new FilmNotFoundException("Ошибка обновления фильма");
        }
    }

    @Override
    public void deleteFilm(Integer filmId) {
        try {
            filmIds.remove(filmId);
            films.remove(filmId);
        } catch (FilmNotFoundException ex) {
            StringBuilder builder = new StringBuilder();
            log.error(builder
                    .append("Error when delete film (id = ")
                    .append(filmId)
                    .append(")")
                    .toString());
            throw new FilmNotFoundException("Ошибка при удалении фильма");
        }
    }

    @Override
    public Film getFilm(Integer filmId) {
        return films.get(filmId);
    }

    public boolean containsId(Integer id) {
        return films.containsKey(id);
    }

    public boolean containsFilm(Integer filmId) {
        return films.containsKey(filmId);
    }

    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    private Integer generateId() {
        return lastGeneratedId++;
    }
}
