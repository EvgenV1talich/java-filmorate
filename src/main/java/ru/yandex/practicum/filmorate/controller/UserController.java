package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

@RestController
@Slf4j

public class UserController {
    private Long generatedId = 1L;
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
        userService.createUser(user);
        return userService.getUserStorage().getUser(user.getId());
    }


    @PutMapping("/users")
    public Optional<User> putUser(@Valid @RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе PUT /users");
            throw new UserValidationException("Ошибка валидации пользователя. Проверьте данные.");
        }
        if (!userService.containsUser(user.getId())) {
            throw new UserNotFoundException("Такой пользователь не найден!");
        } else {
            userService.updateUser(user);
            return Optional.of(userService.getUserStorage().getUser(user.getId()));
        }
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (!userService.containsUser(id) || !userService.containsUser(friendId)) {
            throw new UserNotFoundException("Пользователя/лей с таким ID е найдено");
        }
        userService.addToFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (!userService.containsUser(id) || !userService.containsUser(friendId)) {
            log.error("---Failed to delete friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        log.info("PUT request to remove a friend " + LocalDateTime.now() + "User1 ID = " + id + " , User2 ID = " + friendId);
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("users/{id}/friends")
    public List<User> getUserFriendsList(@PathVariable Long id) {
        if (!userService.containsUser(id)) {
            log.error("---Failed to add friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        return userService.getUserFriends(id);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")
    public List<User> getUsersSameFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (!userService.containsUser(id) || !userService.containsUser(otherId)) {
            log.error("---Failed to get common friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        log.info("Trying to return common friends list from User" + id + " and User" + otherId);
        return userService.getSameFriendsList(id, otherId);
    }

    private Long generateId() {
        return generatedId++;
    }

}
