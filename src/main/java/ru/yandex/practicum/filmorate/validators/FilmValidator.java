package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public abstract class FilmValidator {

    public static boolean validate(Film film) {
        if (filmTitleValidation(film.getName())
                && filmDescriptionValidation(film.getDescription())
                && filmReleaseDateValidation(film.getReleaseDate())
                && filmDurationValidation(film.getDuration())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean filmTitleValidation(String title) {
        if (title.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean filmDescriptionValidation(String description) {
        if (description.length() > 200) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean filmReleaseDateValidation(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !date.isBefore(minDate);
    }

    public static boolean filmDurationValidation(Integer duration) {
        return (duration > 0);
    }


}
