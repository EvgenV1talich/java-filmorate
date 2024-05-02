package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private final Set<Long> userIds = new TreeSet<>();
    private Long lastGeneratedId = 1L;

    @Override
    public void createUser(User user) {
        generateId(user);
        try {
            log.info(String.format("Trying to create user (id = %d)"), user.getId());
            users.put(user.getId(), user);
        } catch (UserAlreadyExistsException ex) {
            log.error(String.format("Error when creating new user (userId = %d)"), user.getId());
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
    public void deleteUser(Long userId) {
        try {
            log.info(String.format("Trying to delete user (userId = %d"), userId);
            users.remove(userId);
        } catch (RuntimeException ex) {
            log.info("Error when delete user");
            throw new RuntimeException("Error when delete user");
        }
        log.info(String.format("User %d successfully created!"), userId);
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    public boolean containsId(Long id) {
        return users.containsKey(id);
    }

    public HashMap<Long, User> getUsersMap() {
        return users;
    }

    @Override
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<>();
        usersList.addAll(users.values());
        return usersList;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    private void generateId(User user) {
        if (user.getId() != null && userIds.contains(user.getId())) {
            throw new UserAlreadyExistsException("Ошибка генерации id");
        }
        user.setId(lastGeneratedId++);
        log.debug(String.format("new userId = %d was generated!"), user.getId());
        userIds.add(user.getId());
    }
}
