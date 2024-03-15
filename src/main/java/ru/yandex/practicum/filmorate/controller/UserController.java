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

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return users.values();
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
        if (users.containsKey(user.getId())) {
            log.error("Ошибка добавления пользователя при запросе POST /users");
            throw new UserAlreadyExistsException("Пользователь с таким ID уже существует!");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        }
    }

    @PutMapping("/users")
    public Optional<User> putUser(@Valid @RequestBody User user) {
        if (!UserValidator.validate(user)) {
            log.error("Ошибка валидации пользователя при запросе PUT /users");
            throw new UserValidationException("Ошибка валидации пользователя. Проверьте данные.");
        }
        try {
            users.replace(user.getId(), user);
            return Optional.of(users.get(user.getId()));
        } catch (UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            log.error("Ошибка обновления пользователя при запросе POST /users");
            System.out.println("Ошибка при обновлении данных пользователя");
        }
        return Optional.empty();
    }

    private Integer generateId() {
        return generatedId++;
    }

}
