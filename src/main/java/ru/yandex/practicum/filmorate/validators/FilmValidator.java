package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public abstract class FilmValidator {

    public static boolean validate(Film film) {
        return filmTitleValidation(film.getName())
                && filmDescriptionValidation(film.getDescription())
                && filmReleaseDateValidation(film.getReleaseDate())
                && filmDurationValidation(film.getDuration());
    }

    public static boolean filmTitleValidation(String title) {
        return !title.isEmpty();
    }

    public static boolean filmDescriptionValidation(String description) {
        return description.length() <= 200;
    }

    public static boolean filmReleaseDateValidation(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !date.isBefore(minDate);
    }

    public static boolean filmDurationValidation(Integer duration) {
        return (duration >= 0);
    }


}
