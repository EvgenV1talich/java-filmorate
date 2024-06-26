package ru.yandex.practicum.filmorate.dal.likesDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class LikesDbStorage implements LikesDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void deleteLike(Integer filmId, Long userId) {
        String query = "DELETE FROM films_users WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        log.debug("Like in film id={} from user id={} removed", filmId, userId);
    }

    @Override
    public void addLike(Integer filmId, Long userId) {
        String query = "DELETE FROM films_users WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        query = "INSERT INTO films_users (film_id, user_id) VALUES(?, ?) ";
        jdbcTemplate.update(query, filmId, userId);
        log.debug("Add like to film id={} from user id={}", filmId, userId);
    }

    @Override
    public Set<Long> getLikerByFilmId(Integer filmID) {
        String query = "SELECT user_id FROM films_users WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("user_id"), filmID);
        log.debug("Get users who liked film id={}.", filmID);
        return new HashSet<>(likes);
    }

    private static Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("film_id");
    }

}