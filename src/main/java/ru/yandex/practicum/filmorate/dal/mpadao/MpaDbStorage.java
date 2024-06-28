package ru.yandex.practicum.filmorate.dal.mpadao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;


@Slf4j
@Component

public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mapper;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public MPA readById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        if (id == null) {
            throw new RuntimeException("Невозможно выполнить запрос с пустым аргументом.");
        }
        try {
            log.debug("Получен Mpa по id {}.", id);
            return jdbcTemplate.queryForObject(sqlQuery, mapper);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого Mpa");
        }
    }

    @Override
    public List<MPA> readAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY id";
        log.debug("Все Mpa получены");
        return jdbcTemplate.query(sqlQuery, mapper);
    }
}

