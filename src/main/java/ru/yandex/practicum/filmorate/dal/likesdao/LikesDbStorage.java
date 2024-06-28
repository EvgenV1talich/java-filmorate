package ru.yandex.practicum.filmorate.dal.likesdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.likesdao.LikesDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikesDbStorage implements LikesDAO {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void deleteLike(Integer filmId, Long userId) {
        String query = "DELETE FROM likes WHERE filmid = ? AND userid = ?";
        jdbcTemplate.update(query, filmId, userId); // в том же порядке что и в скрипте
        log.debug("Like у Film с id {} от User с ID {} удалён", filmId, userId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String query = "DELETE FROM likes WHERE filmId = ? AND userId = ? ";
        jdbcTemplate.update(query, filmId, userId);
        query = "INSERT INTO likes (filmid, userid) VALUES(?, ?) ";
        jdbcTemplate.update(query, filmId, userId);
        log.debug("Film с ID {} добавлен Like от User с ID {}", filmId, userId);
    }

    @Override
    public Set<Long> getLikerByFilmId(Integer filmID) {
        String query = "SELECT userid FROM likes WHERE filmid = ?";
        List<Long> likes = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("userid"), filmID);
        log.debug("Получен список userid Like у Film с id {}.", filmID);
        return new HashSet<>(likes);
    }

    private static Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("FILMID");
    }

}