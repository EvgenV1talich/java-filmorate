package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class InMemoryUserStorage  implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final Set<Integer> userIds = new TreeSet<>();
    private Integer lastGeneratedId;
    @Override
    public void createUser(User user) {
        generateId(user);
        try {
            log.info("Trying to create user (id = " + user.getId());
            users.put(user.getId(), user);
        } catch (UserAlreadyExistsException ex) {
            log.error("Error when creating new user (userId = " + user.getId() + ")");
            throw new UserAlreadyExistsException("Ошибка добавления пользователя");
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            users.replace(user.getId(), user);
        } catch (UserNotFoundException ex) {
            log.error("Error when updating user");
            throw new UserNotFoundException("Error when updating user");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            log.info("Trying to delete user (userId = " + userId + ")");
            users.remove(userId);
        } catch (RuntimeException ex) {
            log.info("Error when delete user");
            throw new RuntimeException("Error when delete user");
        }
        log.info("User " + userId + " successfully created!");
    }

    @Override
    public User getUser(Integer userId) {
                return users.get(userId);
    }
    public boolean containsId(Integer id) {
        return users.containsKey(id);
    }
    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    private void generateId(User user) {
        if (!(user.getId() == null)) {
            throw new UserAlreadyExistsException("Ошибка генерации id (id уже существует)");
        }
        user.setId(lastGeneratedId);
        log.debug("new userId = " + lastGeneratedId + " was generated!");
        userIds.add(lastGeneratedId++);
    }
}
