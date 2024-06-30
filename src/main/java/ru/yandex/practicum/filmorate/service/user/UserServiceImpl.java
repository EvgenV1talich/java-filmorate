package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.dal.userdao.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper mapper;


    @Override
    public UserDto saveUser(UserDto userDTO) {
        User user = mapper.dtoToUser(userDTO);
        if (UserValidator.validate(user)) {
            log.debug("User {} saved", userDTO.getId());
            return mapper.userToDTO(userStorage.createUser(user));
        }
        throw new UserValidationException("Invalid user" + userDTO);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = mapper.dtoToUser(userDto);
        if (UserValidator.validate(user)) {
            log.debug("User {} updated", userDto.getId());
            return mapper.userToDTO(userStorage.updateUser(user));
        }
        throw new UserValidationException("Invalid user" + userDto);
    }

    @Override
    public List<UserDto> readAllUsers() {
        log.debug("Get users list");
        return mapper.listUsersToListDto(userStorage.getUsers());
    }

    @Override
    public void addFiend(Long userId, Long friendId) {
        userStorage.userAddFriend(userId, friendId);
        log.debug("User {} add to friend user {}", userId, friendId);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.debug("User c ID c {} получен.", id);
        return mapper.userToDTO(userStorage.getUser(id));
    }

    @Override
    public void deleteFriendById(Long idUser, Long idFriend) {
        userStorage.userDeleteFriend(idUser, idFriend);
        log.debug("Users {} and {} now not friends", idUser, idFriend);
    }

    @Override
    public List<UserDto> readAllFriendsByUserId(Long idUser) {
        User user = userStorage.getUser(idUser);
        Set<Long> ids = user.getFriends();
        List<UserDto> friends = new ArrayList<>();
        for (Long id : ids) {
            friends.add(mapper.userToDTO(userStorage.getUser(id)));
        }
        log.info("Get user {} frinds list", idUser);
        return friends;
    }


    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public List<UserDto> getSameFriendsList(Long user1Id, Long user2Id) {
        Set<Long> ids = new HashSet<>(userStorage.getUser(user1Id).getFriends());
        List<UserDto> users = new ArrayList<>();
        ids.retainAll(userStorage.getUser(user2Id).getFriends());
        log.debug("Get same friends list user {} and user {}.", user1Id, user2Id);
        for (Long id : ids) {
            users.add(mapper.userToDTO(userStorage.getUser(id)));
        }
        return users;
    }

}