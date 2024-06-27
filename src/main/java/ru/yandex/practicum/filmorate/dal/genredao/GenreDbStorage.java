package ru.yandex.practicum.filmorate.dal.genredao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreMapper;

    @Override
    public Genre getById(Integer id) {
        String query = "SELECT * FROM genres WHERE id = :id;";
        if (id == null) {
            throw new RuntimeException("Ошибка - передан запрос с неверным ID жанра");
        }
        try {
            return jdbcTemplate.queryForObject(query, genreMapper);
        } catch (Throwable ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Жанр не существует!");
        }
    }

    @Override
    public List<Genre> getAll() {
        String query = "SELECT * FROM genres;";
        return jdbcTemplate.query(query, genreMapper);
    }

    @Override
    public List<Genre> getGenresByFilm(Integer filmId) {
        return null;
    }
}
