package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generatedId = 1;

    public String home() {
        return "Filmorate";
    }

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }
        //Validation
        if (!UserValidator.validate(user)) {
            throw new UserValidationException("Ошибка валидации пользователя, проверьте данные!");
        }
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("Пользователь с таким ID уже существует!");
        } else {
            System.out.println("Adding new user:\n" + user.toString());
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        }
    }

    @PutMapping("/users")
    public Optional<User> putUser(@Valid @RequestBody User user) {
        if (!UserValidator.validate(user)) {
            throw new UserValidationException("Ошибка валидации пользователя. Проверьте данные.");
        }
        try {
            System.out.println("Updating user:\n" + user.toString());
            users.replace(user.getId(), user);
            return Optional.of(user);
        } catch (UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ошибка при обновлении данных пользователя");
        }
        return Optional.empty();
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
