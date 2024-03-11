package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidatorTest {
    @Test
    public void emailValidationTest() {
        User user = new User(0
                , ""
                , "testLogin"
                , "testName"
                , LocalDate.of(2000, 1, 1));
        Assertions.assertFalse(UserValidator.emailValidation(user.getEmail()));
        user.setEmail("aboba-aboba");
        Assertions.assertFalse(UserValidator.emailValidation(user.getEmail()));
        user.setEmail("aboba@aboba.ru");
        Assertions.assertTrue(UserValidator.emailValidation(user.getEmail()));
    }
    @Test
    public void loginValidationTest() {
        User user = new User(0
                , ""
                , ""
                , "testName"
                , LocalDate.of(2000, 1, 1));
        Assertions.assertFalse(UserValidator.loginValidation(user.getLogin()));
        user.setLogin("abo ba");
        Assertions.assertFalse(UserValidator.loginValidation(user.getLogin()));
    }
    @Test
    public void birthdayValidationTest() {
        User user = new User(0
                , ""
                , "testLogin"
                , "testName"
                , LocalDate.of(2103, 1, 1));
        Assertions.assertFalse(UserValidator.birthdayValidation(user.getBirthday()));
    }
}
