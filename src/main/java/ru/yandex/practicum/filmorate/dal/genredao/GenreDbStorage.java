package ru.yandex.practicum.filmorate.dal.genredao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Genre getById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre id is null!");
        }
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapToGenre, id);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Genre not found");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY id";
        log.debug("Все Genres получены.");

        var v = jdbcTemplate.query(sqlQuery, this::mapToGenre);

        return new ArrayList<>(v);

    }

    public Genre mapToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public ArrayList<Genre> getGenresByFilm(Integer filmId) {
        String sqlQuery
                = "SELECT genre_id, name FROM films_genres INNER JOIN genres ON genre_id = id WHERE film_id = ? ORDER BY genre_id ASC ";
        var genres = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")), filmId);
        log.debug("Get genres list for film (id {})", filmId);

        return new ArrayList<>(genres);
    }
}