package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {
    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Integer userId);

    User getUser(Integer userId);

    public HashMap<Integer, User> getUsers();
}
