package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class FilmValidatorTest {
    @Test
    public void filmTitleValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, new ArrayList<>((Collection) genre));
        MPA mpa = new MPA(0, "G", film.getId());

        Assertions.assertFalse(FilmValidator.filmTitleValidation(film.getName()));
    }

    @Test
    public void filmDescriptionValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, new ArrayList<>((Collection) genre));;
        MPA mpa = new MPA(0, "G", film.getId());
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
        Genre genre = new Genre(0, "Комедия");
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, new ArrayList<>((Collection) genre));
        MPA mpa = new MPA(0, "G", film.getId());
        Assertions.assertFalse(FilmValidator.filmReleaseDateValidation(film.getReleaseDate()));
    }

    @Test
    public void filmDurationValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), -1, new ArrayList<>((Collection) genre));
        MPA mpa = new MPA(0, "G", film.getId());
        Assertions.assertFalse(FilmValidator.filmDurationValidation(film.getDuration()));
    }
}
