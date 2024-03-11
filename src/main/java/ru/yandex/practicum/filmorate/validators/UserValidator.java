package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public abstract class UserValidator {
    public static boolean validate(User user) {
        if (emailValidation(user.getEmail())
        && loginValidation(user.getLogin())
        && birthdayValidation(user.getBirthday())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean emailValidation(String email) {
        if (email.isEmpty() || !email.contains("@")) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean loginValidation(String login) {
        if (login.isEmpty() || login.contains(" ")) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean birthdayValidation(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }

}
