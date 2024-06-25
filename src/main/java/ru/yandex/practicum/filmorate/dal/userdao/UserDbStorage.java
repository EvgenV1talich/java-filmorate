package ru.yandex.practicum.filmorate.dal.userdao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;
    @Override
    public void createUser(User user) {
        String query = "INSERT INTO USERS\n" +
                "(ID, EMAIL, LOGIN, NAME, BIRTHDAY)\n" +
                "VALUES(?, ?, ?, ?, ?);";
        jdbcTemplate.update(query, user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE USERS\n" +
                "SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=?\n" +
                "WHERE ID=?;";
        jdbcTemplate.query(query, mapper);

    }

    @Override
    public void deleteUser(Long userId) {
        String query = "DELETE FROM USERS\n" +
                "WHERE ID=?;";
        jdbcTemplate.update(query, userId);
    }

    @Override
    public User getUser(Long userId) {
        String query = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY\n" +
                "WHERE ID=?" +
                "FROM USERS;";
        return(jdbcTemplate.queryForObject(query, mapper));
    }

    @Override
    public List<User> getUsers() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, mapper);
    }
}
