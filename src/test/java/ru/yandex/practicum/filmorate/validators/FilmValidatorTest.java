package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

public class FilmValidatorTest {
    @Test
    public void filmTitleValidationTest() {
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111);
        Assertions.assertFalse(FilmValidator.filmTitleValidation(film.getName()));
    }

    @Test
    public void filmDescriptionValidationTest() {
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111);
        film.setDescription("looooooooooooo" +
                "ooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooong description");
        Assertions.assertFalse(FilmValidator.filmDescriptionValidation(film.getDescription()));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        Film film = new Film(0, "", "testDescription", LocalDate.of(1670, 1, 1), 111);
        Assertions.assertFalse(FilmValidator.filmReleaseDateValidation(film.getReleaseDate()));
    }

    @Test
    public void filmDurationValidationTest() {
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), -1);
        Assertions.assertFalse(FilmValidator.filmDurationValidation(film.getDuration()));
    }
}
