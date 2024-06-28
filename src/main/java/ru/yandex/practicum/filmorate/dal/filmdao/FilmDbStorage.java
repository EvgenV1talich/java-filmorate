package ru.yandex.practicum.filmorate.dal.filmdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("filmDBStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDBStorage mpaDBStorage;
    private final GenreStorage genreStorage;
    private final LikeDBStorage likeDBStorage;


    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Long) key);
        film.setMpa(mpaDBStorage.readById(film.getMpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String query = "INSERT INTO filmgenres (filmid,genreid) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));

        updateDirector(film);
        log.debug("Film c ID {} сохранён.", film.getId());
        return film;
    }

    @Override
    public void updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Film не найден");
        }
        String sqlQuery
                = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpaid = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()) != 0) {
            log.info("Film c id {} обновлён", film.getId());
        } else {
            throw new EntityNotFoundException("Film с таким id не существует");
        }

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            String sqlDelete = "DELETE FROM filmgenres WHERE filmid = ?";
            jdbcTemplate.update(sqlDelete, film.getId());

        } else {
            String sqlDelete2 = "DELETE FROM filmgenres WHERE filmid = ?";
            jdbcTemplate.update(sqlDelete2, film.getId());
            String sqlUpdate = "INSERT INTO filmgenres (filmid, genreid) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));
        String sqlQueryDeleteDirector = "DELETE FROM filmdirectors WHERE filmid = ?";
        jdbcTemplate.update(sqlQueryDeleteDirector, film.getId());
        updateDirector(film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(query, this::mapToFilm);
        log.debug("Получены все Film.");
        return films;
    }

    @Override
    public Film getFilm(Integer id) {
        String query = "SELECT * FROM films WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, id);
        if (sqlRowSet.first()) {
            //TODO remove builder
            Film film = Film.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .description(sqlRowSet.getString("description"))
                    .releaseDate(sqlRowSet.getDate("releaseDate").toLocalDate())
                    .duration(sqlRowSet.getInt("duration"))
                    .genres(genreStorage.getGenresByFilmID(sqlRowSet.getLong("id")))
                    .likes(likeDBStorage.getLikerByFilmId(sqlRowSet.getLong("id")))
                    .mpa(mpaDBStorage.readById(sqlRowSet.getInt("mpaid")))
                    .build();
            log.debug("Получен Film с ID {}.", id);
            return film;
        } else {
            throw new EntityNotFoundException("Film не найден");
        }
    }

    public Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDBStorage.readById(rs.getInt("mpaid")))
                .likes(likeDBStorage.getLikerByFilmId(rs.getLong("id")))
                .genres(genreStorage.getGenresByFilmID(rs.getLong("id")))
                .directors(directorStorage.getDirectorsByFilmId(rs.getLong("id")))
                .build();
    }

    @Override
    public void deleteFilm(Long id) {
        String query = "DELETE FROM films WHERE id = ?";
        if (jdbcTemplate.update(query, id) != 0) {
            log.info("Film с Id {} удалён.", id);
        } else {
            log.info("Film с Id {} не найден.", id);
        }
    }

    public List<Film> getTopFilms(Long count, Long genreId, Long year) {
        return  null;
    }

    public Map<String, Object> filmToMap(Film film) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("name", film.getName());
        temp.put("description", film.getDescription());
        temp.put("releaseDate", film.getReleaseDate());
        temp.put("duration", film.getDuration());
        temp.put("mpaid", film.getMpa().getId());
        return temp;
    }
}