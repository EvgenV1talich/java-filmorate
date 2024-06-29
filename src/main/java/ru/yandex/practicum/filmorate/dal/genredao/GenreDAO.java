package ru.yandex.practicum.filmorate.dal.genredao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreDAO {

    Genre getById(Integer id);
    Set<Genre> getAll();
    Set<Genre> getGenresByFilm(Integer filmId);

}
