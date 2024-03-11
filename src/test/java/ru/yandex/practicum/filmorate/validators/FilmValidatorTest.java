package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

public class FilmValidatorTest {
    @Test
    public void filmTitleValidationTest() {
        Film film = new Film(0
                , ""
                , "testDescription"
                , LocalDate.of(2000, 1, 1)
                , Duration.ofMinutes(116));
        Assertions.assertFalse(FilmValidator.filmTitleValidation(film.getName()));
    }
    @Test
    public void filmDescriptionValidationTest() {
        Film film = new Film(0
                , ""
                , "testDescription"
                , LocalDate.of(2000, 1, 1)
                , Duration.ofMinutes(116));
        film.setDescription("very looooooooooooo" +
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
                "ooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooong description");
        Assertions.assertFalse(FilmValidator.filmDescriptionValidation(film.getDescription()));
    }
    @Test
    public void filmReleaseDateValidationTest() {
        Film film = new Film(0
                , ""
                , "testDescription"
                , LocalDate.of(1670, 1, 1)
                , Duration.ofMinutes(116));
        Assertions.assertFalse(FilmValidator.filmReleaseDateValidation(film.getReleaseDate()));
    }
    @Test
    public void filmDurationValidationTest() {
        Film film = new Film(0
                , ""
                , "testDescription"
                , LocalDate.of(2000, 1, 1)
                , Duration.ofMinutes(-1));
        Assertions.assertFalse(FilmValidator.filmDurationValidation(film.getDuration()));
    }
}
