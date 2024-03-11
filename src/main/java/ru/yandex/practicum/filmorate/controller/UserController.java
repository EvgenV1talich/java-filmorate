package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generatedId = 0;

    public String home() {
        return "Filmorate";
    }

    @PostMapping("/add-user")
    public User addUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("Пользователь с таким ID уже существует!");
        } else {
            System.out.println("Adding new user:\n" + user.toString());
            users.put(user.getId(), user);
            return user;
        }
    }

    @PatchMapping("/update-user/{id}")
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        try {
            users.replace(user.getId(), user);
            return Optional.of(user);
        } catch (UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ошибка при обновлении данных пользователя");
        }
        return Optional.empty();
    }

    @GetMapping("/users")
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("Пользователь с таким ID уже существует!");
        } else {
            System.out.println("Adding new user:\n" + user.toString());
            users.put(user.getId(), user);
            return user;
        }
    }

    private Integer generateId() {
        return generatedId++;
    }

    private void resetId() {
        generatedId = 0;
    }

    private Integer getLastGeneratedId() {
        return generatedId;
    }

}
