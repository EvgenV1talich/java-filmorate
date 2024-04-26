package ru.yandex.practicum.filmorate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserUnknownIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidationException(final UserValidationException ex) {
        return Map.of("error", "Ошибка валидации пользователя.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFilmValidationException(final FilmValidationException ex) {
        return Map.of("error", "Ошибка валидации пользователя.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> UserNotFoundException(final UserNotFoundException ex) {
        return Map.of("error", "Пользователь не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> FilmNotFoundException(final FilmNotFoundException ex) {
        return Map.of("error", "Фильм не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> UserUnknownIdException(final UserUnknownIdException ex) {
        return Map.of("error", "Такого ID не существует");
    }
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String, String> InternalException(final RuntimeException ex) {
//        return Map.of("error", "Ошибка валидации пользователя.");
//    }

}