package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.genredao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDbStorage genreDbStorage;


    @Override
    public Genre getById(Integer id) {
        return genreDbStorage.getById(id);
    }

    @Override
    public Set<Genre> getAll() {
        return genreDbStorage.getAll();
    }
}
