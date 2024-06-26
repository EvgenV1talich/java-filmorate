package ru.yandex.practicum.filmorate.dal.filmDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.genreDao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.likesDao.LikesDbStorage;
import ru.yandex.practicum.filmorate.dal.mpaDao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDBStorage;
    private final GenreDbStorage genreStorage;
    private final LikesDbStorage likeDBStorage;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingColumns("name", "description", "release_date", "duration", "mpa_id")
                .usingGeneratedKeyColumns("id");
        Integer key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film)).intValue();
        film.setId(Integer.parseInt(String.valueOf(key)));
        film.setMpa(mpaDBStorage.readById(film.getMpa().getId()));
        log.info("MPA = " + film.getMpa().toString());
        if (film.getGenre() != null && !film.getGenre().isEmpty()) {
            if (genreExists(film.getGenre(), genreStorage.getAll())) {
                String query = "INSERT INTO films_genres (film_id,genre_id) VALUES (?,?)";
                for (Genre genre : film.getGenre()) {
                    jdbcTemplate.update(query, film.getId(), genre.getId());
                }
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(400));
            }
        }
        film.setGenre(genreStorage.getGenresByFilm(film.getId()));
        log.debug("Film (id={}) saved.", film.getId());
        return film;
    }

    public Integer getFilmsCount() {
        return getFilms().size();
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new FilmValidationException("Film not found");
        }
        String sqlQuery
                = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()) != 0) {
            log.info("Film (id={}) updated", film.getId());
        } else {
            throw new FilmNotFoundException("Film not exists!");
        }

        if (film.getGenre() == null || film.getGenre().isEmpty()) {
            String sqlDelete = "DELETE FROM films_genres WHERE film_id = ?";
            jdbcTemplate.update(sqlDelete, film.getId());

        } else {
            String sqlDelete2 = "DELETE FROM films_genres WHERE film_id = ?";
            jdbcTemplate.update(sqlDelete2, film.getId());
            String sqlUpdate = "INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)";
            for (Genre genre : film.getGenre()) {
                jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
            }
        }
        film.setGenre(genreStorage.getGenresByFilm(film.getId()));
        return film;
    }

    private boolean genreExists(List<Genre> filmGenres, List<Genre> dbGenres) {
        List<Integer> genresInDbIds = new ArrayList<>();
        List<Integer> filmGenresIds = new ArrayList<>();
        for (Genre genre : dbGenres) {
            genresInDbIds.add(genre.getId());
        }
        for (Genre genre : filmGenres) {
            filmGenresIds.add(genre.getId());
        }
        return new HashSet<>(genresInDbIds).containsAll(filmGenresIds);
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(query, this::mapToFilm);
        log.debug("Get films list.");
        return films;
    }

    @Override
    public Film getFilm(Integer id) {
        String query = "SELECT * FROM films WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, id);
        if (sqlRowSet.first()) {
            Film film = buildFilm(sqlRowSet, id);
            return film;
        } else {
            throw new FilmNotFoundException("Film not found!");
        }
    }

    private Film buildFilm(SqlRowSet sqlRowSet, Integer id) {
        Film film = new Film();
        film.setId(sqlRowSet.getInt("id"));
        film.setName(sqlRowSet.getString("name"));
        film.setDescription(sqlRowSet.getString("description"));
        film.setReleaseDate(sqlRowSet.getDate("release_date").toLocalDate());
        film.setDuration(sqlRowSet.getInt("duration"));
        film.setGenre(removeDublicates(genreStorage.getGenresByFilm(sqlRowSet.getInt("id"))));
        film.setLikesFromUsers(likeDBStorage.getLikerByFilmId(sqlRowSet.getInt("id")));
        film.setMpa(mpaDBStorage.readById(sqlRowSet.getInt("mpa_id")));
        log.debug("Get film {}.", id);
        return film;
    }

    public Film mapToFilm(ResultSet sqlRowSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(sqlRowSet.getInt("id"));
        film.setName(sqlRowSet.getString("name"));
        film.setDescription(sqlRowSet.getString("description"));
        film.setReleaseDate(sqlRowSet.getDate("release_date").toLocalDate());
        film.setDuration(sqlRowSet.getInt("duration"));
        film.setGenre(removeDublicates(genreStorage.getGenresByFilm(sqlRowSet.getInt("id"))));
        film.setLikesFromUsers(likeDBStorage.getLikerByFilmId(sqlRowSet.getInt("id")));
        try {
            Mpa mpa = mpaDBStorage.readById(sqlRowSet.getInt("id"));
            film.setMpa(mpa);
        } catch (Exception e) {
            log.info("Exception when create film in mapper.");
        }

        log.debug("Get film {}.", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        String query = "DELETE FROM films WHERE id = ?";
        if (jdbcTemplate.update(query, id) != 0) {
            log.info("Film {} deleted.", id);
        } else {
            log.info("Film {} not found", id);
        }
    }

    private ArrayList<Genre> removeDublicates(ArrayList<Genre> listWithDublicates) {
        List<Genre> listWithoutDublicates = listWithDublicates.stream().distinct().collect(Collectors.toList());
        return (ArrayList<Genre>) listWithoutDublicates;
    }

    public List<Film> getTopFilms(Long count) {
        String query =
                "SELECT f1.* FROM" +
                        "  (select f.id, count(f.id) AS cnt from" +
                        "  films_users fu" +
                        "  join films f on f.id = fu.film_id" +
                        "  GROUP by f.id ORDER BY 2 DESC) fff" +
                        "  JOIN films f1 ON f1.id = fff.id" +
                        "  LIMIT  ?";
        List<Film> topFilms = jdbcTemplate.query(query, this::mapToFilm, count);
        log.info("Get top {} films", count);
        return topFilms;
    }

    public Map<String, Object> filmToMap(Film film) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("name", film.getName());
        temp.put("description", film.getDescription());
        temp.put("release_date", film.getReleaseDate());
        temp.put("duration", film.getDuration());
        temp.put("mpa_id", film.getMpa().getId());
        return temp;
    }
}