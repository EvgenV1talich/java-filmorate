package ru.yandex.practicum.filmorate.dal.genredao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDAO {

    Genre getById(Integer id);
    List<Genre> getAll();
    List<Genre> getGenresByFilm(Integer filmId);

}
