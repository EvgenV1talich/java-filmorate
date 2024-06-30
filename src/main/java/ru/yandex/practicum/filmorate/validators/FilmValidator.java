package ru.yandex.practicum.filmorate.validators;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.dal.genredao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mpadao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilmValidator {


    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    public static boolean validate(Film film) {
        return filmTitleValidation(film.getName())
                && filmDescriptionValidation(film.getDescription())
                && filmReleaseDateValidation(film.getReleaseDate())
                && filmDurationValidation(film.getDuration());
    }

    public static boolean filmTitleValidation(String title) {
        if (title.isEmpty() || title == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean filmDescriptionValidation(String description) {
        return description.length() <= 200;
    }

    public static boolean filmReleaseDateValidation(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !date.isBefore(minDate);
    }

    public static boolean filmMpaValidation(Mpa mpa, List<Mpa> mpas) {
        ArrayList<Integer> mpaIds = new ArrayList<>();
        for (Mpa mpaTemp : mpas) {
            mpaIds.add(mpaTemp.getId());
        }
        return mpaIds.contains(mpa.getId());
    }

    public static boolean filmGenreValidation(ArrayList<Genre> filmGenres, ArrayList<Genre> dbGenres) {
        ArrayList<Integer> filmGenresIds = new ArrayList<>();
        ArrayList<Integer> dbGenresIds = new ArrayList<>();
        for (Genre filmGenre : filmGenres) {
            filmGenresIds.add(filmGenre.getId());
        }
        for (Genre dbGenre : dbGenres) {
            dbGenresIds.add(dbGenre.getId());
        }
        return dbGenresIds.containsAll(filmGenresIds);
    }

    public static boolean filmDurationValidation(Integer duration) {
        return (duration >= 0);
    }


}
