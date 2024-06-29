package ru.yandex.practicum.filmorate.dal.userdao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);

    User getUser(Long userId);

    public List<User> getUsers();

    void userAddFriend(Long userId, Long friendId);

    void userDeleteFriend(Long userId, Long friendId);

}
