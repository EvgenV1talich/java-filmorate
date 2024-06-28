package ru.yandex.practicum.filmorate.dal.userdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper mapper;

    @Override
    public User createUser(User user) {
        /*String query = "INSERT INTO USERS\n" +
                "(ID, EMAIL, LOGIN, NAME, BIRTHDAY)\n" +
                "VALUES(?, ?, ?, ?, ?);";
        jdbcTemplate.update(query, user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());*/
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId(key.longValue());
        log.debug("User создан с ID {}.", user.getId());
        return user;
        //return user;
    }

    @Override
    public User updateUser(User user) {

        if (user == null || user.getId() == null) {
            throw new UserNotFoundException("User не найден");
        }
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name =?, birthday = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId()) != 0) {
            log.debug(" User {} успешно обновлён", user.getId());
            return user;
        } else {
            throw new UserNotFoundException("User с таким id не найден " + user.getId());
        }

    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM USERS\n" +
                "WHERE ID=?;";
        jdbcTemplate.update(query, id);
    }

    @Override
    public User getUser(Long id) {
        String sqlQuery = String.format("SELECT * FROM users WHERE id = %d", id);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery);
        User user = new User();
        if (userRows.first()) {
            user.setId(userRows.getLong("id"));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
            user.setFriends(getAllFriendsByUser(user.getId()));
        } else {
            throw new EntityNotFoundException("Юзер с таким id не найден " + id);
        }
        return user;
    }

    /*public boolean contains(Long userId) {
        String query = "EXISTS(SELECT id FROM \"users\" WHERE id = :userId);";
        return (jdbcTemplate.update(query)) > 0;
    }*/

    @Override
    public List<User> getUsers() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {
        if (!UserValidator.validate(getUser(userId)) || !UserValidator.validate(getUser(friendId))) {
            throw new UserValidationException("Невалидный юзер!");
        }
        String query = "INSERT INTO users_friends (request_user_id, response_user_id) VALUES (?,?)";
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
        if (UserValidator.validate(getUser(userId)) || UserValidator.validate(getUser(friendId))) {
            throw new UserValidationException("Невалидный User!");
        }
        getUser(userId); // для валидации
        getUser(friendId); // для валидации
        String query = "DELETE FROM users_friends WHERE request_user_id = ? AND response_user_id = ?";
        if (jdbcTemplate.update(query, userId, friendId) != 0) {
            log.debug("У User с ID {} удалён friend (User) {}", userId, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого User");
        }

    }

    public Set<Long> getAllFriendsByUser(Long id) {
        /*getUser(id);
        String query = "SELECT * FROM users WHERE id IN (SELECT friendid FROM friends WHERE userid = ?)";
        List<User> friends = jdbcTemplate.query(query, mapper);
        log.debug("Получен список friendId друзей у User с id {}.", id);
        return friends;*/
        String sqlQuery = "SELECT response_user_id FROM users_friends WHERE request_user_id = ?";
        Set<Long> ids = new HashSet<>();
        SqlRowSet friends = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (friends.next()) {
            ids.add(friends.getLong("response_user_id"));
        }
        log.debug("Получены все Friends (Users) у User c ID {}.", id);
        return ids;
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
