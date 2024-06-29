package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.ArrayList;

public class FilmValidatorTest {
    @Test
    public void filmTitleValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        var v = new ArrayList<Genre>();
        v.add(genre);
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, v);
        MPA mpa = new MPA(0, "G");

        Assertions.assertFalse(FilmValidator.filmTitleValidation(film.getName()));
    }

    @Test
    public void filmDescriptionValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        var v = new ArrayList<Genre>();
        v.add(genre);
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, v);
        ;
        MPA mpa = new MPA(0, "G");
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
        var v = new ArrayList<Genre>();
        v.add(genre);
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), 111, v);
        MPA mpa = new MPA(0, "G");
        Assertions.assertFalse(FilmValidator.filmReleaseDateValidation(film.getReleaseDate()));
    }

    @Test
    public void filmDurationValidationTest() {
        Genre genre = new Genre(0, "Комедия");
        var v = new ArrayList<Genre>();
        v.add(genre);
        Film film = new Film(0, "", "testDescription", LocalDate.of(2000, 1, 1), -1, v);
        MPA mpa = new MPA(0, "G");
        Assertions.assertFalse(FilmValidator.filmDurationValidation(film.getDuration()));
    }
}
