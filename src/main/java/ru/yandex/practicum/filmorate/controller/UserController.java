package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserUnknownIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Slf4j

public class UserController {
    private int generatedId = 1;
    private final UserService userService;
    private final InMemoryUserStorage userStorage;
    @Autowired
    public UserController(UserService userService, InMemoryUserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }


    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе POST /users");
            throw new UserValidationException("Ошибка валидации пользователя, проверьте данные!");
        }
        if (user.getId() == null) {
            user.setId(generateId());
        }
        if (userService.getUserStorage().getUsers().containsKey(user.getId())) {
            log.error("Ошибка добавления пользователя при запросе POST /users");
            throw new UserAlreadyExistsException("Пользователь с таким ID уже существует!");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            userService.getUserStorage().getUsers().put(user.getId(), user);
            return user;
        }
    }

    @PutMapping("/users")
    public Optional<User> putUser(@Valid @RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе PUT /users");
            throw new UserValidationException("Ошибка валидации пользователя. Проверьте данные.");
        }
        if (!userService.getUserStorage().getUsers().containsKey(user.getId())) {
            throw new UserNotFoundException("Такой пользователь не найден!");
        } else {
            userService.getUserStorage().getUsers().replace(user.getId(), user);
            return Optional.of(userService.getUserStorage().getUsers().get(user.getId()));
        }

//        try {
//            users.replace(user.getId(), user);
//            return Optional.of(users.get(user.getId()));
//        } catch (UserNotFoundException ex) {
//            System.out.println(ex.getMessage());
//            log.error("Ошибка обновления пользователя при запросе POST /users");
//            System.out.println("Ошибка при обновлении данных пользователя");
//        }
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (!userStorage.containsId(id) || !userStorage.containsId(friendId)) {
            log.error("---Failed to add friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        log.info("PUT request to add a friend " + LocalDateTime.now() + "User1 ID = " + id + " , User2 ID = " + friendId);
        userService.addToFriend(id, friendId);
        List<User> newFriends = new ArrayList<>();
        newFriends.add(userStorage.getUser(friendId));
        newFriends.add(userStorage.getUser(id));
        return newFriends;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (!userStorage.containsId(id) || !userStorage.containsId(friendId)) {
            log.error("---Failed to delete friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        log.info("PUT request to remove a friend " + LocalDateTime.now() + "User1 ID = " + id + " , User2 ID = " + friendId);
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("users/{id}/friends")
    public Set<Long> getUserFriendsList(@PathVariable Integer id) {
        if (!userStorage.containsId(id)) {
            log.error("---Failed to add friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        return userService.getUserFriendsList(id);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")
    public List<User> getUsersSameFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (!userStorage.containsId(id) || !userStorage.containsId(otherId)) {
            log.error("---Failed to get common friends!---");
            throw new UserUnknownIdException("Пользователя/лей с таким ID не найдено");
        }
        log.info("Trying to return common friends list from User" + id + " and User" + otherId);
        return userService.getSameFriendsList(id, otherId);
    }

    private Integer generateId() {
        return generatedId++;
    }

}
