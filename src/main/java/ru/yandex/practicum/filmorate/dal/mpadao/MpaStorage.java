package ru.yandex.practicum.filmorate.dal.mpadao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {
    MPA readById(Integer id);

    List<MPA> readAll();
}
