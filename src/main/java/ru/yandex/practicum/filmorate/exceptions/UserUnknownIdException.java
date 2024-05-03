package ru.yandex.practicum.filmorate.exceptions;

public class UserUnknownIdException extends RuntimeException {
    public UserUnknownIdException(String message) {
        super(message);
    }
}
