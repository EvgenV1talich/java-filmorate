package ru.yandex.practicum.filmorate.dal.genredao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

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
    public ArrayList<Genre> getGenresByFilm(Integer filmId) {
        String sqlQuery
                = "SELECT genre_id, name FROM films_genres INNER JOIN genres ON genre_id = id WHERE film_id = ? ORDER BY genre_id ASC ";
        List<Genre> genres = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")), filmId);
        log.debug("Получен список Genres  для Film с id {}", filmId);
        return new ArrayList<>(genres);
    }
}
