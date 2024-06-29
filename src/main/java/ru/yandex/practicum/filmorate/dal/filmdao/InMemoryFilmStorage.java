package ru.yandex.practicum.filmorate.dal.filmdao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
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
    public Film createFilm(Film film) {
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
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
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
        return film;
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

    public List<Film> getFilms() {
        return null;
    }

    @Override
    public List<Film> getTopFilms(Long count) {
        return null;
    }

    @Override
    public Integer getFilmsCount() {
        return null;
    }

    private Integer generateId() {
        return lastGeneratedId++;
    }
}
