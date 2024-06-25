package ru.yandex.practicum.filmorate.dal.userdao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    User getUser(Long userId);

    public List<User> getUsers();
}
