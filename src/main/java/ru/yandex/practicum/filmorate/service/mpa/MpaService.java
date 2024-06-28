package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaService {

    List<MPA> readAll();
    MPA readById(Integer id);

}
