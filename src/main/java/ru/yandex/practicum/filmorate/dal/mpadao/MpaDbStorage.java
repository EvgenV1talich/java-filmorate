package ru.yandex.practicum.filmorate.dal.mpadao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public MPA readById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        if (id == null) {
            throw new ValidationException("Невозможно выполнить запрос с пустым аргументом.");
        }
        try {
            log.debug("Получен Mpa по id {}.", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapToMpa, id);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого Mpa");
        }
    }

    @Override
    public List<MPA> readAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY id";
        log.debug("Все Mpa получены");
        return jdbcTemplate.query(sqlQuery, this::mapToMpa);
    }

    public MPA mapToMpa(ResultSet rs, int rowNum) throws SQLException {
        return MPA.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}