package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user);

    List<UserDto> readAllUsers();

    void addFiend(Long userId, Long friendId);

    UserDto getUserById(Long id);

    void deleteFriendById(Long idUser, Long idFriend);

    List<UserDto> readAllFriendsByUserId(Long idUser);

    void deleteUser(Long id);

    List<UserDto> getSameFriendsList(Long user1Id, Long user2Id);

}