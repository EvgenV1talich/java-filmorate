package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.userdao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dal.userdao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService(@Autowired InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriend(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        user1.addFriend(user2Id);
        user2.addFriend(user1Id);
        userStorage.updateUser(user1);
        userStorage.updateUser(user2);
    }

    public List<User> getUsersList() {
        return userStorage.getUsers();
    }

    public void removeFromFriends(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        user1.removeFriend(user2.getId());
        user2.removeFriend((user1.getId()));
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public boolean containsUser(Long userId) {
        return userStorage.containsId(userId);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public void createUser(User user) {
        userStorage.createUser(user);
    }

    public List<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        List<Long> friendsList = new ArrayList<>();
        friendsList.addAll(userStorage.getUser(id).getFriends());
        for (Long friendId : friendsList) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getSameFriendsList(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        log.debug("Trying to return same friends list for User1Id = " + user1.getId() + " and User2Id = " + user2.getId());
        List<User> sameFriendsList = new ArrayList<>();
        List<Long> sameFriendsIdList = user1.getFriends().stream()
                .distinct()
                .filter(user2.getFriends()::contains)
                .collect(Collectors.toList());
        for (Long id : sameFriendsIdList) {
            sameFriendsList.add(userStorage.getUser(id));
        }
        return sameFriendsList;
    }
}
