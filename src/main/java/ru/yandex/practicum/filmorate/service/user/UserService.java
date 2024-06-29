package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO saveUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    List<UserDTO> readAllUsers();

    void addFiend(Long userId, Long friendId);

    UserDTO getUserById(Long id);

    void deleteFriendById(Long idUser, Long idFriend);

    List<UserDTO> readAllFriendsByUserId(Long idUser);

    void deleteUser(Long id);

}