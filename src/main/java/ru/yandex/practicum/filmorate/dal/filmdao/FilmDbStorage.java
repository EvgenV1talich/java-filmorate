package ru.yandex.practicum.filmorate.dal.filmdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.genredao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.likesdao.LikesDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mpadao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmMapper;
    private final MpaDbStorage mpaDBStorage;
    private final GenreDbStorage genreStorage;
    private final LikesDbStorage likeDbStorage;


    @Override
    public Film createFilm(Film film) {
        //TODO: проверить sql
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);
        film.setMpa(mpaDBStorage.readById(film.getMpa().getId()));

        if (film.getGenre() != null && !film.getGenre().isEmpty()) {
            String query = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
            for (Genre genre : film.getGenre()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        film.addGenre(genreStorage.getById(film.getId()));
        log.info("Film c ID {} сохранён.", film.getId());
        return film;
    }

    @Override
    public void updateFilm(Film film) {
        String query = "UPDATE FILMS " +
                "SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=? WHERE ID=?;";
        int rowsUpdated = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
                );
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
        String query = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION " +
                " FROM FILMS " +
                " WHERE ID = ?";
        SqlRowSet sqlRowSet  =null;
        try {
            sqlRowSet = jdbcTemplate.queryForRowSet(query, filmId);
        }catch(Exception e){
            int a = 0;
        }
        if (sqlRowSet.first()) {
            Film film = new Film();
            film.setId(sqlRowSet.getInt("ID"));
            film.setName(sqlRowSet.getString("NAME"));
            film.setDescription(sqlRowSet.getString("DESCRIPTION"));
            film.setReleaseDate(sqlRowSet.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(sqlRowSet.getInt("DURATION"));
            film.setGenre(genreStorage.getGenresByFilm(sqlRowSet.getInt("ID")));
            film.setLikesFromUsers(likeDbStorage.getLikerByFilmId(sqlRowSet.getInt("id")));
            /*Film film = Film.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .description(sqlRowSet.getString("description"))
                    .releaseDate(sqlRowSet.getDate("releaseDate").toLocalDate())
                    .duration(sqlRowSet.getInt("duration"))
                    .genres(genreStorage.getGenresByFilmID(sqlRowSet.getLong("id")))
                    .likes(likeDBStorage.getLikerByFilmId(sqlRowSet.getLong("id")))
                    .directors(directorStorage.getDirectorsByFilmId(sqlRowSet.getLong("id")))
                    .mpa(mpaDBStorage.readById(sqlRowSet.getInt("mpaid")))
                    .build();*/
            log.debug("Получен Film с ID {}.", filmId);
            return film;
        } else {
            throw new EntityNotFoundException("Film не найден");
        }



        //return (jdbcTemplate.queryForObject(query, filmMapper));

    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        String query = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION FROM FILMS;";

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
        String query = "SELECT count(*) FROM FILMS WHERE ID = ?";
        return (jdbcTemplate.queryForObject(query, new Object[]{id}, Integer.class).intValue()) > 0;
    }

    /*public Integer getCount() {
        //return jdbcTemplate.execute("SELECT COUNT(*) FROM films");
    }*/

    public List<Map<String, Object>> getMostPopularFilms(Integer topCount) {
        //TODO: написать нормальный запрос
        String query = "SELECT id, film_id FROM films INNER JOIN films_users ON films.id = films_users.film_id";
        return jdbcTemplate.queryForList(query);
    }
    public Integer getFilmsCount() {
        String query = "SELECT COUNT(*) FROM films";
        return jdbcTemplate.update(query);
    }
}
