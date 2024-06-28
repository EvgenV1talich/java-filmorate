package ru.yandex.practicum.filmorate.dal.likesdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikesDbStorage implements LikesDAO {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public void deleteLike(Long filmId, Long userId) {
        String query = "DELETE FROM films_users WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        log.debug("Like у Film с id {} от User с ID {} удалён", filmId, userId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String query = "DELETE FROM films_users WHERE film_id = ? AND user_id = ? ";
        jdbcTemplate.update(query, filmId, userId);
        query = "INSERT INTO films_users (film_id, user_id) VALUES(?, ?) ";
        jdbcTemplate.update(query, filmId, userId);
        log.debug("Film с ID {} добавлен Like от User с ID {}", filmId, userId);
    }

    @Override
    public Set<Long> getLikerByFilmId(Integer filmID) {
        String query = "SELECT user_id FROM films_users WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("user_id"), filmID);
        log.debug("Получен список userid Like у Film с id {}.", filmID);
        return new HashSet<>(likes);
    }

}
