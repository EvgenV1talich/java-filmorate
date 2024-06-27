package ru.yandex.practicum.filmorate.dal.filmdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmMapper;

    @Override
    public Map<String, Object> createFilm(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        Number id = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        log.info("Create new film: generated id = " + id);
        return filmToMap(film);
    }

    @Override
    public void updateFilm(Film film) {
        String query = "UPDATE FILMS\n" +
                "SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?\n" +
                "WHERE ID=?;";
        int rowsUpdated = jdbcTemplate.update(query,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }
    }

    @Override
    public void deleteFilm(Integer filmId) {
        String query = "DELETE FROM FILMS\n" +
                "WHERE ID = :filmId;";
        int rowsDeleted = jdbcTemplate.update(query);
        if (rowsDeleted == 0) {
            throw new RuntimeException("Не удалось удалить данные");
        }
    }

    @Override
    public Film getFilm(Integer filmId) {
        String query = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION\n" +
                "FROM FILMS\n" +
                "WHERE ID = :filmId";
        return (jdbcTemplate.queryForObject(query, filmMapper));

    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        String query = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION\n" +
                "FROM FILMS;";

        HashMap<Integer, Film> filmHashMap = new HashMap<>();
        List<Film> filmsList = jdbcTemplate.query(query, filmMapper);
        for (Film film : filmsList) {
            filmHashMap.put(film.getId(), film);
        }
        return filmHashMap;

    }
    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("NAME", film.getName());
        filmMap.put("DESCRIPTION", film.getDescription());
        filmMap.put("RELEASE_DATE", film.getReleaseDate());
        filmMap.put("DURATION",film.getDuration());
        return filmMap;
    }

    public boolean contains(Integer id) {
        String query = "EXISTS(SELECT ID FROM FILMS WHERE ID = :id);";
        return (jdbcTemplate.update(query)) > 0;
    }

    public Integer getCount() {
        return jdbcTemplate.update("SELECT COUNT(*) FROM films;");
    }

    public Collection<Film> getMostPopularFilms(Integer topCount) {
        //TODO: написать нормальный запрос
        return null;
    }
}
