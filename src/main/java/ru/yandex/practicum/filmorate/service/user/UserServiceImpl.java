package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.dal.userdao.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.UserValidator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper mapper;


    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = mapper.dtoToUser(userDTO);
        if (UserValidator.validate(user)) {
            log.debug("User {} saved", userDTO.getId());
            return mapper.userToDTO(userStorage.createUser(user));
        }
        throw new UserValidationException("Invalid user" + userDTO);
    }

    @Override
    public UserDTO updateUser(UserDTO userDto) {
        User user = mapper.dtoToUser(userDto);
        if (UserValidator.validate(user)) {
            log.debug("User {} updated", userDto.getId());
            return mapper.userToDTO(userStorage.updateUser(user));
        }
        throw new UserValidationException("Invalid user" + userDto);
    }

    @Override
    public List<UserDTO> readAllUsers() {
        log.debug("Get users list");
        return mapper.listUsersToListDto(userStorage.getUsers());
    }

    @Override
    public void addFiend(Long userId, Long friendId) {
        userStorage.userAddFriend(userId, friendId);
        log.debug("User {} add to friend user {}", userId, friendId);
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("User c ID c {} получен.", id);
        return mapper.userToDTO(userStorage.getUser(id));
    }

    @Override
    public void deleteFriendById(Long idUser, Long idFriend) {
        userStorage.userDeleteFriend(idUser, idFriend);
        log.debug("Users {} and {} now not friends", idUser, idFriend);
    }

    @Override
    public List<UserDTO> readAllFriendsByUserId(Long idUser) {
        User user = userStorage.getUser(idUser);
        Set<Long> ids = user.getFriends();
        List<UserDTO> friends = new ArrayList<>();
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
    public List<UserDTO> getSameFriendsList(Long user1Id, Long user2Id) {
        Set<Long> ids = new HashSet<>(userStorage.getUser(user1Id).getFriends());
        List<UserDTO> users = new ArrayList<>();
        ids.retainAll(userStorage.getUser(user2Id).getFriends());
        log.debug("Получен список общих друзей у User с ID {} и User c ID {}.", user1Id, user2Id);
        for (Long id : ids) {
            users.add(mapper.userToDTO(userStorage.getUser(id)));
        }
        return users;
    }

}