package ru.yandex.practicum.filmorate.dal.userdao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("userDBStorage")
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId((Long) key);
        log.debug("User создан с ID {}.", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new ValidationException("Невалидный User");
        }
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name =?, birthday = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId()) != 0) {
            log.debug(" User {} успешно обновлён", user.getId());
            return user;
        } else {
            throw new EntityNotFoundException("User с таким id не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users ";
        log.debug("Все User получены");
        return jdbcTemplate.query(sqlQuery, this::mapToUser);
    }

    @Override
    public User getUser(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, this::mapToUser, id);
            log.debug("User с ID {} получен.", id);
            return user;
        } catch (Throwable throwable) {
            throw new EntityNotFoundException("User с таким id не найден");
        }
    }

    private Set<Long> findFriendsByUserId(Long id) {
        String sqlQuery = "SELECT friendid FROM friends WHERE userid = ?";
        Set<Long> ids = new HashSet<>();
        SqlRowSet friends = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (friends.next()) {
            ids.add(friends.getLong("friendid"));
        }
        log.debug("Получены все Friends (Users) у User c ID {}.", id);
        return ids;
    }

    public User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(findFriendsByUserId(rs.getLong("id")))
                .build();
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId); // если юзер отсутствует - будет выброшено исключение
        String query = "INSERT INTO friends (userid, friendid) VALUES (?,?)";
        try {
            jdbcTemplate.update(query, userId, friendId);
            log.debug("Friend с ID {} добавлен успешно в friend к User c ID {}.", friendId, userId);
        } catch (DuplicateKeyException e) {
            log.debug("Ошибка добавления Friend с ID {} в friend к User c ID {}.", friendId, userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public void userDeleteFriend(Long userId, Long friendId) {
        getUser(userId); // для валидации
        getUser(friendId); // для валидации
        String query = "DELETE FROM friends WHERE userid = ? AND friendid = ?";
        if (jdbcTemplate.update(query, userId, friendId) != 0) {
            log.debug("У User с ID {} удалён friend (User) {}", userId, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого User");
        }

    }
    public List<User> getAllFriendsUser(Long id) {
        getUser(id); // для валидации
        String query = "SELECT * FROM users WHERE id IN (SELECT friendid FROM friends WHERE userid = ?)";
        List<User> friends = jdbcTemplate.query(query, this::mapToUser, id);
        log.debug("Получен список friendId друзей у User с id {}.", id);
        return friends;
    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        if (jdbcTemplate.update(query, id) != 0) {
            log.info("User с Id {} удалён.", id);
        } else {
            log.info("User с Id {} не найден.", id);
        }
    }

    public Map<String, Object> userToMap(User user) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("email", user.getEmail());
        temp.put("login", user.getLogin());
        temp.put("name", user.getName());
        temp.put("birthday", user.getBirthday());
        return temp;
    }
}