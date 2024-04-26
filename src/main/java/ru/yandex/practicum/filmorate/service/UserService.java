package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriend(Integer user1Id, Integer user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        user1.addFriend(Long.valueOf(user2.getId()));
        user2.addFriend(Long.valueOf(user1.getId()));

    }

    public void removeFromFriends(Integer user1Id, Integer user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        user1.removeFriend(Long.valueOf(user2.getId()));
        user2.removeFriend(Long.valueOf((user1.getId())));
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public Set<Long> getUserFriendsList(Integer id) {
        return userStorage.getUser(id).getFriends();
    }

    public List<User> getSameFriendsList(Integer user1Id, Integer user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        log.debug("Trying to return same friends list for User1Id = " + user1.getId() + " and User2Id = " + user2.getId());
        List<User> sameFriendsList = new ArrayList<>();
        List<Long> sameFriendsIdList = user1.getFriends().stream()
                .distinct()
                .filter(user2.getFriends()::contains)
                .collect(Collectors.toList());
        for (Long id : sameFriendsIdList) {
            sameFriendsList.add(userStorage.getUser(Math.toIntExact(id)));
        }
        return sameFriendsList;
    }
}
