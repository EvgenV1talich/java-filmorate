package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserUnknownIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*@RestController
@Slf4j

public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsersList();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе POST /users");
            throw new UserValidationException("Ошибка валидации пользователя, проверьте данные!");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return userService.createUser(user);
    }


    @PutMapping("/users")
    public Optional<User> putUser(@Valid @RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе PUT /users");
            throw new UserValidationException("Ошибка валидации пользователя. Проверьте данные.");
        }
        *//*if (!userService.containsUser(user.getId())) {
            throw new UserNotFoundException("Такой пользователь не найден!");
        } *//*else {
            userService.updateUser(user);
            return Optional.of(userService.getUserStorage().getUser(user.getId()));
        }
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        *//*if (!userService.containsUser(id) || !userService.containsUser(friendId)) {
            throw new UserNotFoundException("Пользователя/лей с таким ID е найдено");
        }*//*
        userService.addToFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        *//*if (!userService.containsUser(id) || !userService.containsUser(friendId)) {
            log.error("---Failed to delete friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }*//*
        log.info("PUT request to remove a friend " + LocalDateTime.now() + "User1 ID = " + id + " , User2 ID = " + friendId);
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("users/{id}/friends")
    public List<User> getUserFriendsList(@PathVariable Long id) {
        *//*if (!userService.containsUser(id)) {
            log.error("---Failed to add friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }*//*
        return userService.getUserFriends(id);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")
    public List<User> getUsersSameFriends(@PathVariable Long id, @PathVariable Long otherId) {
        *//*if (!userService.containsUser(id) || !userService.containsUser(otherId)) {
            log.error("---Failed to get common friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }*//*
        log.info("Trying to return common friends list from User" + id + " and User" + otherId);
        return userService.getSameFriendsList(id, otherId);
    }

}*/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRowMapper mapper;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO newUser) {
        log.info("Получен POST запрос по эндпоинту '/users' на создание user");
        if (!UserValidator.validate(mapper.DTOToUser(newUser))){
            throw new UserValidationException("Невалидный User!");
        }
        return new ResponseEntity<>(userService.createUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO newUser) {
        log.info("Получен PUT запрос по эндпоинту '/users' на обновление user");
        /*if (!UserValidator.validate(mapper.DTOToUser(newUser))) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }*/
        return new ResponseEntity<>(userService.updateUser(newUser), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> readAllUsers() {
        log.info("Получен GET запрос по эндпоинту '/users' на получение всех users");
        return new ResponseEntity<>(userService.getUsersList(), HttpStatus.OK);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info(
                "Получен PUT запрос по эндпоинту '/users/{}/friends/{}' на добавление friend c ID {} для user c ID {}. ",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info(
                "Получен DELETE запрос по эндпоинту '/users/{}/friends/{}' " +
                        "на удаление friend c ID {} для user c ID {}.",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public Set<Long> readAllFriendsByUserId(@PathVariable Long id) {
        log.info("Получен GET запрос по эндпоинту '/users/{id}/friends' на получение всех друзей для user c ID {}.",
                id);
        return userService.getAllFriendsByUser(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDTO>> readSameFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info(
                "Получен GET запрос по эндпоинту '/users/{}/friends/common/{}' на получение всех общих друзей у Users c ID {} и {}.",
                id, otherId, id, otherId);
        return new ResponseEntity<>(userService.getSameFriendsList(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        log.info("Получен GET запрос по эндпоинту '/users/{userId}' на получение Users c ID {}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("Получен DELETE запрос по эндпоинту '/users/{}' на удаление User c ID {}. ", userId, userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
