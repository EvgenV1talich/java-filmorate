package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.userdao.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.userdao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userStorage;
    private final UserRowMapper mapper;

    public void addToFriend(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        user1.addFriend(user2Id);
        user2.addFriend(user1Id);
        userStorage.updateUser(user1);
        userStorage.updateUser(user2);
    }

    public List<UserDTO> getUsersList() {
        return mapper.UserListToUserDTOList(userStorage.getUsers());
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
    public UserDTO updateUser(User user) {
        return mapper.userToDTO(userStorage.updateUser(user));
    }

    public UserDTO createUser(UserDTO user) {
        return mapper.userToDTO(userStorage.createUser(mapper.DTOToUser(user)));
    }

    public List<UserDTO> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        List<Long> friendsList = new ArrayList<>();
        friendsList.addAll(userStorage.getUser(id).getFriends());
        for (Long friendId : friendsList) {
            friends.add(userStorage.getUser(friendId));
        }
        return mapper.UserListToUserDTOList(friends);
    }
    public UserDTO getUserById(Long id) {
        return mapper.userToDTO(userStorage.getUser(id));
    }
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public List<UserDTO> getSameFriendsList(Long user1Id, Long user2Id) {
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
        return mapper.UserListToUserDTOList(sameFriendsList);
    }
    public void addFriend(Long userId, Long friendId) {
        userStorage.userAddFriend(userId, friendId);
    }
    public void deleteFriend(Long userId, Long friendId) {
        userStorage.userDeleteFriend(userId, friendId);
    }

    public Set<Long> getAllFriendsByUser(Long id) {
        return userStorage.getAllFriendsByUser(id);
    }

}
