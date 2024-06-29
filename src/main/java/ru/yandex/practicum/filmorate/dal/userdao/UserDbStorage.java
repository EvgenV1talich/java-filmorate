package ru.yandex.practicum.filmorate.dal.userdao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.persistence.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository("userDBStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("ID");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId(Long.parseLong(String.valueOf(key)));
        log.debug("Create user id={}.", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new UserValidationException("Invalid user!");
        }
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name =?, birthday = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId()) != 0) {
            log.debug("Update user {} success", user.getId());
            return user;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users ";
        log.info("Get user list success");
        return jdbcTemplate.query(sqlQuery, this::mapToUser);
    }

    @Override
    public User getUser(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, this::mapToUser, id);
            log.info("Get user id={}.", id);
            return user;
        } catch (Throwable throwable) {
            throw new UserNotFoundException("User not found");
        }
    }

    private Set<Long> findFriendsByUserId(Long id) {
        String sqlQuery = "SELECT response_user_id FROM users_friends WHERE request_user_id = ?";
        Set<Long> ids = new HashSet<>();
        SqlRowSet friends = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (friends.next()) {
            ids.add(friends.getLong("response_user_id"));
        }
        log.debug("Get friends list from user id={}.", id);
        return ids;
    }

    public User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        user.setFriends(findFriendsByUserId(user.getId()));
        return user;
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId); // если юзер отсутствует - будет выброшено исключение
        String query = "INSERT INTO users_friends (request_user_id, response_user_id) VALUES (?,?)";
        try {
            jdbcTemplate.update(query, userId, friendId);
            log.debug("Add to friends between {} and {} success", friendId, userId);
        } catch (DuplicateKeyException e) {
            log.debug("Error when add friends {} and {}.", friendId, userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public void userDeleteFriend(Long userId, Long friendId) {
        //TODO: доделать валидацию
        getUser(userId); // для валидации
        getUser(friendId); // для валидации
        String query = "DELETE FROM users_friends WHERE request_user_id = ? AND response_user_id = ?";
        if (jdbcTemplate.update(query, userId, friendId) != 0) {
            log.debug("Remove friendship between {} and {}", userId, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }
//    public List<User> getAllFriendsUser(Long id) {
//        getUser(id); // для валидации
//        String query = "SELECT * FROM users WHERE id IN (SELECT friendid FROM friends WHERE userid = ?)";
//        List<User> friends = jdbcTemplate.query(query, this::mapToUser, id);
//        log.debug("Получен список friendId друзей у User с id {}.", id);
//        return friends;
//    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        if (jdbcTemplate.update(query, id) != 0) {
            log.info("User {} deleted.", id);
        } else {
            log.info("User {} not found", id);
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