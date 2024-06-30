package ru.yandex.practicum.filmorate.dal.genredao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

public interface GenreDao {

    Genre getById(Integer id);

    List<Genre> getAll();

    ArrayList<Genre> getGenresByFilm(Integer filmId);

}
