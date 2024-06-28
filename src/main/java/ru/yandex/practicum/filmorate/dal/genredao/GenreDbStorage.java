package ru.yandex.practicum.filmorate.dal.genredao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Genre getById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";

        if (id == null) {
            throw new ValidationException("Невозможно выполнить запрос с пустым аргументом.");
        }
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapToGenre, id);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Нет такого Genre");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY id";
        log.debug("Все Genres получены.");
        return jdbcTemplate.query(sqlQuery, this::mapToGenre);
    }

    public Genre mapToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Genre> getGenresByFilm(Integer filmId) {
        String sqlQuery
                = "SELECT genreid, name FROM filmgenres INNER JOIN genres ON genreid = id WHERE filmid = ? ORDER BY genreid ASC ";
        List<Genre> genres = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("genreid"), rs.getString("name")), filmId);
        log.debug("Получен список Genres  для Film с id {}", filmId);
        return null;
    }
}