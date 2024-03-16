package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.User;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class UserValidator {
    public static boolean validate(User user) {
        return emailValidation(user.getEmail())
                && loginValidation(user.getLogin())
                && birthdayValidation(user.getBirthday());
    }

    public static boolean emailValidation(@NotEmpty String email) {
        return email.contains("@");
    }

    public static boolean loginValidation(String login) {
        return !login.isEmpty() && !login.contains(" ");
    }

    public static boolean birthdayValidation(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }

}
